package it.vitalegi.servicestatustelegrambot.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HttpClientCallerImpl implements HttpClientCaller {

	Logger log = LoggerFactory.getLogger(HttpClientCallerImpl.class);

	@Override
	public HttpResponseWrapper call(HttpPost httpRequest) throws IOException {
		return doCall(httpRequest);
	}

	@Override
	public HttpResponseWrapper call(HttpGet httpRequest) throws IOException {
		return doCall(httpRequest);
	}

	@Override
	public HttpResponseWrapper doCall(HttpRequestBase request) throws IOException {

		String id = UUID.randomUUID().toString();

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			logRequest(id, request);

			HttpClientContext context = new HttpClientContext();
			context.setCookieStore(new BasicCookieStore());

			CloseableHttpResponse response = httpClient.execute(request, context);
			HttpResponseWrapperImpl responseWrapper = process(response);
			logResponse(id, responseWrapper);
			return responseWrapper;
		}
	}

	protected byte[] getPayload(HttpEntity entity) {
		try {
			byte[] bytes = EntityUtils.toByteArray(entity);
			return bytes;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void logRequest(String id, HttpRequest request) {
		log.trace("{} REQUEST -------------------", id);
		log.info("{} URL: {}", id, request.getRequestLine().getUri());
		for (Header header : request.getAllHeaders()) {
			log.trace("{} Header: {}={}", id, header.getName(), header.getValue());
		}
		if (request instanceof HttpEntityEnclosingRequest) {
			HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
			String payload = toString(entity);

			try {
				JSONObject obj = new JSONObject(payload);
				log.trace("{} Body: {}", id, obj.toString());
			} catch (Exception e) {
				log.trace("{} Body: not json, skip logging", id);
			}

		}
		log.trace("{} ---------------------------", id);
	}

	protected void logResponse(String id, HttpResponseWrapper responseWrapper) {
		log.trace("{} RESPONSE-------------------", id);
		log.info("{} Status: {}, {}", id, responseWrapper.getStatus(), responseWrapper.getReasonPhrase());
		responseWrapper.getHeaders().entrySet().forEach(h -> {
			log.trace("{} Header: {}={}", id, h.getKey(), h.getValue());
		});

		byte[] payload = responseWrapper.getPayload();
		String payloadAsString = new String(payload);
		if (payloadAsString.length() > 100) {
			payloadAsString = payloadAsString.substring(0, 100) + " ...";
		}
		payloadAsString = payloadAsString.replace('\n', '_');

		log.trace("{} Body: {}", id, payloadAsString);
		log.trace("{} ---------------------------", id);
	}

	protected HttpResponseWrapperImpl process(CloseableHttpResponse response) {
		int status = response.getStatusLine().getStatusCode();
		String reasonPhrase = response.getStatusLine().getReasonPhrase();

		Map<String, List<String>> headers = new HashMap<>();
		for (Header header : response.getAllHeaders()) {
			if (!headers.containsKey(header.getName())) {
				headers.put(header.getName(), new ArrayList<>());
			}
			headers.get(header.getName()).add(header.getValue());
		}
		byte[] payload = getPayload(response.getEntity());
		return new HttpResponseWrapperImpl(status, reasonPhrase, headers, payload);
	}

	protected String toString(HttpEntity entity) {
		ContentType contentType = ContentType.getOrDefault(entity);
		try {
			EntityUtils.toString(entity, contentType.getCharset());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		OutputStream os = new ByteArrayOutputStream();
		try {
			entity.writeTo(os);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return os.toString();
	}
}