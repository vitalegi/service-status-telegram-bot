package it.vitalegi.servicestatustelegrambot.store;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class FileSystemStoreImpl implements UserDataRepository {

	@Value("${store.data.location}")
	protected String storeLocation;

	@Override
	public UserData getUserData(long userId) {

		Map<Long, UserData> users = readUsersDataFromJson();

		if (users.containsKey(userId)) {
			return users.get(userId);
		} else {
			return UserDataFactory.newInstance(userId);
		}

	}

	@Override
	public Map<Long, UserData> getUsersData() {

		return readUsersDataFromJson();
	}

	@Override
	public synchronized void setUserData(UserData userData) {

		Map<Long, UserData> usersData = readUsersDataFromJson();
		usersData.put(userData.getUserId(), userData);
		writeUsersDataToJson(usersData);
	}

	protected Map<Long, UserData> readUsersDataFromJson() {

		try {
			@Cleanup
			InputStream is = openInputStream();

			return getMapper().readValue(is, new TypeReference<Map<Long, UserData>>() {
			});
		} catch (NoSuchFileException e) {
			log.error("File doesn't exist {}: {}", storeLocation, e.getMessage());
			return new HashMap<>();
		} catch (IOException e) {
			log.error("Error reading stream. {}", e.getMessage(), e);
			return new HashMap<>();
		}
	}

	protected InputStream openInputStream() throws IOException {
		return Files.newInputStream(Paths.get(storeLocation), StandardOpenOption.READ);
	}

	protected void writeUsersDataToJson(Map<Long, UserData> usersData) {

		try (OutputStream os = openOutputStream()) {
			getMapper().writeValue(os, usersData);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected OutputStream openOutputStream() throws IOException {
		try {
			Path storePath = Paths.get(storeLocation);
			Path storeDir = Paths.get(storeLocation).getParent();
			Files.createDirectories(storeDir);
			Files.deleteIfExists(storePath);
			Files.createFile(storePath);
			return Files.newOutputStream(Paths.get(storeLocation), StandardOpenOption.WRITE);
		} catch (IOException e) {
			log.error("Error writing file {}: {}", storeLocation, e.getMessage());
			throw e;
		}

	}

	protected ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return mapper;
	}
}
