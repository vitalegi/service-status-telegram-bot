package it.vitalegi.servicestatustelegrambot.store;

import java.util.ArrayList;

public class UserDataFactory {

	public static UserData newInstance(long userId) {

		UserData userData = new UserData();
		userData.setUserId(userId);
		userData.setMonitors(new ArrayList<HttpMonitor>());
		return userData;
	}
}
