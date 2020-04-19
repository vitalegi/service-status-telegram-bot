package it.vitalegi.servicestatustelegrambot.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.vitalegi.servicestatustelegrambot.SpringTestConfig;
import it.vitalegi.servicestatustelegrambot.util.logging.LoggingAspect;

@SpringBootTest(classes = SpringTestConfig.class)
public class JsonComparatorTest {

	Logger logger = LoggerFactory.getLogger(JsonComparatorTest.class);

	@Autowired
	JsonComparator jsonComparator;

	@Autowired
	LoggingAspect loggingAspect;

	@Test
	void testEmptyObjectsAreEquals() throws Exception {
		loggingAspect.logMethodStart();
		JSONObject obj1 = Json.init().build();
		JSONObject obj2 = Json.init().build();
		jsonComparator.assertEquals(obj1, obj2);
	}

	@Test
	void testSimplePropertiesAreEquals() throws Exception {
		loggingAspect.logMethodStart();
		JSONObject obj1 = Json.init().put("a", "1").put("b", "test").build();
		JSONObject obj2 = Json.init().put("a", "1").put("b", "test").build();
		jsonComparator.assertEquals(obj1, obj2);
	}

	@Test
	void testSwappedPropertiesAreEquals() throws Exception {
		loggingAspect.logMethodStart();
		JSONObject obj1 = Json.init().put("a", "1").put("b", "test").build();
		JSONObject obj2 = Json.init().put("b", "test").put("a", "1").build();
		jsonComparator.assertEquals(obj1, obj2);
	}

	@Test
	void testActualJsonWithAdditionalPropertiesAreDifferent() throws Exception {
		loggingAspect.logMethodStart();
		AssertionFailedError assertion = Assertions.assertThrows(AssertionFailedError.class, () -> {
			JSONObject obj1 = Json.init().put("a", "1").build();
			JSONObject obj2 = Json.init().put("a", "1").put("b", "2").build();
			jsonComparator.assertEquals(obj1, obj2);
		});
		assertEquals("On key: b, expected: null, but was: 2", assertion.getMessage());
	}

	@Test
	void testDifferentSimplePropsThrowsException() throws Exception {
		loggingAspect.logMethodStart();
		AssertionFailedError assertion = Assertions.assertThrows(AssertionFailedError.class, () -> {
			JSONObject obj1 = Json.init().put("a", "1").build();
			JSONObject obj2 = Json.init().put("a", "2").build();
			jsonComparator.assertEquals(obj1, obj2);
		});
		assertEquals("On key: a, expected: 1, but was: 2", assertion.getMessage());
	}

	@Test
	void testComplexObjectAreEquals() throws Exception {
		loggingAspect.logMethodStart();
		JSONObject obj1 = Json.init().put("a", "1").put("b", Json.init().put("c", "2").build()).build();
		JSONObject obj2 = Json.init().put("a", "1").put("b", Json.init().put("c", "2").build()).build();
		jsonComparator.assertEquals(obj1, obj2);
	}

	@Test
	void testDifferentComplexObjectThrowsException() throws Exception {
		loggingAspect.logMethodStart();
		AssertionFailedError assertion = Assertions.assertThrows(AssertionFailedError.class, () -> {
			JSONObject obj1 = Json.init().put("a", "1").put("b", Json.init().put("c", "2").build()).build();
			JSONObject obj2 = Json.init().put("a", "1").put("b", Json.init().put("c", "3").build()).build();
			jsonComparator.assertEquals(obj1, obj2);
		});
		assertEquals("On key: b.c, expected: 2, but was: 3", assertion.getMessage());
	}

	@Test
	void testMissingComplexObjectPropertiesThrowsException() throws Exception {
		loggingAspect.logMethodStart();
		AssertionFailedError assertion = Assertions.assertThrows(AssertionFailedError.class, () -> {
			JSONObject obj1 = Json.init().put("a", "1").put("b", Json.init().put("c", "2").build()).build();
			JSONObject obj2 = Json.init().put("a", "1").put("b", Json.init().put("d", "3").build()).build();
			jsonComparator.assertEquals(obj1, obj2);
		});
		assertEquals("On key: b.c, expected: 2, but was: null", assertion.getMessage());
	}
}
