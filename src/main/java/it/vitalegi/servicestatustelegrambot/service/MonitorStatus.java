package it.vitalegi.servicestatustelegrambot.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import it.vitalegi.servicestatustelegrambot.store.HttpMonitor;

@Service
public class MonitorStatus {

	Map<HttpMonitor, Integer> lastValue;

	@PostConstruct
	protected void init() {
		lastValue = new HashMap<>();
	}

	public boolean updateAndCheckIfChanged(HttpMonitor monitor, Integer newValue) {
		Integer oldValue = lastValue.get(monitor);
		lastValue.put(monitor, newValue);
		boolean changed = oldValue == null || !oldValue.equals(newValue);
		return changed;
	}
}
