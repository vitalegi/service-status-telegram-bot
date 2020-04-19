package it.vitalegi.servicestatustelegrambot.store;

import java.util.Map;

public interface UserDataRepository {

	UserData getUserData(long userId);

	public Map<Long, UserData> getUsersData();

	void setUserData(UserData userData);

}