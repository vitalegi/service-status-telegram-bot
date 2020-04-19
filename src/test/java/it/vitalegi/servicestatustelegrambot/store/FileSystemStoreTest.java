package it.vitalegi.servicestatustelegrambot.store;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import it.vitalegi.servicestatustelegrambot.SpringTestConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = SpringTestConfig.class)
class FileSystemStoreTest {

	FileSystemStoreImpl service;

	@BeforeEach
	void init() throws IOException {
		log.info("Init, delete test folder");

		deleteDirectoryRecursive("./test/");
	}

	@AfterEach
	void cleanup() throws IOException {
		log.info("Cleanup, delete test folder");

		deleteDirectoryRecursive("./test/");
	}

	@Test
	void notExistingStoreShouldInitialize() throws IOException {

		String storePath = getStorageFilePath(getCurrentMethodName());

		service = new FileSystemStoreImpl();
		service.storeLocation = storePath;

		deleteDirectoryRecursive(storePath);

		assertEquals(UserDataFactory.newInstance(5), service.getUserData(5));
	}

	@Test
	void setUserDataShouldUpdateFile() throws IOException {

		String storePath = getStorageFilePath(getCurrentMethodName());

		service = new FileSystemStoreImpl();
		service.storeLocation = storePath;

		Files.deleteIfExists(Paths.get(storePath));

		UserData user = UserDataFactory.newInstance(5);
		user.getMonitors().add(new HttpMonitor());

		service.setUserData(user);

		UserData actual = service.getUserData(5);
		assertEquals(user, actual);

		assertTrue(Files.exists(Paths.get(storePath)));
		Files.delete(Paths.get(storePath));
	}

	@Test
	void setUserDataShouldReleaseResources() throws IOException {

		String storePath = getStorageFilePath(getCurrentMethodName());

		service = new FileSystemStoreImpl();
		service.storeLocation = storePath;

		Files.deleteIfExists(Paths.get(storePath));

		for (int i = 0; i < 10; i++) {
			UserData user = UserDataFactory.newInstance(5);
			service.setUserData(user);
			service.getUserData(5);
		}
	}

	protected String getStorageFilePath(String methodName) {
		return "./test/" + FileSystemStoreTest.class.getName() + "/" + methodName + ".json";
	}

	protected String getCurrentMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	protected void deleteDirectoryRecursive(String dir) throws IOException {
		Path start = Paths.get(dir);
		if (!Files.exists(start)) {
			return;
		}
		Files.walk(start)//
				.sorted(Comparator.reverseOrder())//
				.map(Path::toFile)//
				.forEach(f -> {
					log.info("Delete: {}", f.getAbsolutePath());
					f.delete();
				});
	}
}
