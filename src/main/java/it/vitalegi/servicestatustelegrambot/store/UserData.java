package it.vitalegi.servicestatustelegrambot.store;

import java.util.List;

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
public class UserData {

	protected long userId;
	protected List<HttpMonitor> monitors;
}
