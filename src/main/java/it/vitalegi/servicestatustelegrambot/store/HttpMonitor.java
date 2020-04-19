package it.vitalegi.servicestatustelegrambot.store;

import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class HttpMonitor {

	String uri;
	String method;
	Map<String, String> headers;
	String body;

	public String toPlainString() {
		StringBuilder sb = new StringBuilder();
		sb.append(method + " " + uri + "\n");
		headers.entrySet().forEach(entry -> {
			sb.append(entry.getKey() + ":" + entry.getValue() + "\n");
		});
		sb.append("\n");
		sb.append(body);
		return sb.toString();
	}
}
