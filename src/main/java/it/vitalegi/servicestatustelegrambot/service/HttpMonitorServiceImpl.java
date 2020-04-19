package it.vitalegi.servicestatustelegrambot.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.vitalegi.servicestatustelegrambot.http.HttpClientCaller;
import it.vitalegi.servicestatustelegrambot.http.HttpResponseWrapper;
import it.vitalegi.servicestatustelegrambot.store.HttpMonitor;
import it.vitalegi.servicestatustelegrambot.store.UserData;
import it.vitalegi.servicestatustelegrambot.store.UserDataRepository;
import it.vitalegi.servicestatustelegrambot.telegram.SendMessageWrapper;
import it.vitalegi.servicestatustelegrambot.util.logging.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HttpMonitorServiceImpl {

	@Autowired
	SendMessageWrapper sendMessage;

	@Autowired
	UserDataRepository userDataRepository;

	@Autowired
	HttpClientCaller httpClientCaller;

	@Autowired
	MonitorStatus monitorStatus;

	public void process() {
		log.info("Scheduled");
		Map<Long, UserData> usersData = userDataRepository.getUsersData();
		Iterator<UserData> entries = usersData.values().iterator();
		while (entries.hasNext()) {
			process(entries.next());
		}
	}

	@LogExecutionTime
	protected void process(UserData userData) {
		for (HttpMonitor monitor : userData.getMonitors()) {
			process(userData, monitor);
		}
	}

	protected void process(UserData userData, HttpMonitor monitor) {
		HttpResponseWrapper response = call(monitor);
		int status = checkMonitor(response);
		if (monitorStatus.updateAndCheckIfChanged(monitor, status)) {
			sendMessage.sendTextMessage(userData.getUserId(), getMessage(userData, monitor, response));
		}
	}

	protected String getMessage(UserData userData, HttpMonitor monitor, HttpResponseWrapper response) {

		StringBuilder sb = new StringBuilder();
		sb.append(monitor.toPlainString()).append("\n");
		sb.append("Status: " + checkMonitor(response));
		return sb.toString();
	}

	protected int checkMonitor(HttpResponseWrapper response) {
		return response.getStatus();
	}

	protected HttpResponseWrapper call(HttpMonitor monitor) {
		try {
			HttpRequestBase httpRequest = createRequest(monitor);
			return httpClientCaller.doCall(httpRequest);
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	protected HttpRequestBase createRequest(HttpMonitor monitor)
			throws URISyntaxException, UnsupportedEncodingException {
		HttpRequestBase request;
		if ("POST".equals(monitor.getMethod())) {
			request = new HttpPost();
			((HttpPost) request).setEntity(new StringEntity(monitor.getBody()));
		} else if ("GET".equals(monitor.getMethod())) {
			request = new HttpGet();
		} else {
			throw new IllegalArgumentException("Unrecognized method. " + monitor);
		}
		request.setURI(new URI(monitor.getUri()));
		monitor.getHeaders().entrySet().forEach(entry -> request.addHeader(entry.getKey(), entry.getValue()));
		return request;
	}
}
