package it.vitalegi.servicestatustelegrambot.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpMonitorFactory {

	HttpMonitor monitor;

	public static HttpMonitorFactory newInstance() {
		HttpMonitorFactory factory = new HttpMonitorFactory();
		factory.monitor = new HttpMonitor();
		factory.monitor.setHeaders(new HashMap<>());
		return factory;
	}

	public HttpMonitor build() {
		return monitor;
	}

	public HttpMonitorFactory uri(String uri) {
		monitor.setUri(uri);
		return this;
	}

	public HttpMonitorFactory method(String method) {
		monitor.setMethod(method);
		return this;
	}

	public HttpMonitorFactory headers(Map<String, String> headers) {
		monitor.setHeaders(headers);
		return this;
	}

	public HttpMonitorFactory body(String body) {
		monitor.setBody(body);
		return this;
	}
}
