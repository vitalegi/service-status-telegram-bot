package it.vitalegi.servicestatustelegrambot.util;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.vitalegi.servicestatustelegrambot.util.logging.LogExecutionTime;
import org.opentest4j.AssertionFailedError;

@Service
public class JsonComparator {
	Logger logger = LoggerFactory.getLogger(JsonComparator.class);

	@Autowired
	JsonComparator comparator;

	@LogExecutionTime
	public void assertEquals(JSONObject expected, JSONObject actual) {
		Deque<String> keyDeque = new LinkedList<>();
		comparator.doAssertEquals(expected, actual, keyDeque);
	}

	@LogExecutionTime
	protected void doAssertEquals(Object expected, Object actual, Deque<String> keys) {
		if (expected == null && actual == null) {
			return;
		}
		if (expected != null && actual == null) {
			fail(notEqual(expected, actual, keys));
		}
		if (expected == null && actual != null) {
			fail(notEqual(expected, actual, keys));
		}
		if (expected instanceof JSONObject) {
			if (actual instanceof JSONObject) {
				comparator.doAssertEqualsJSONObject((JSONObject) expected, (JSONObject) actual, keys);
			} else {
				throw new AssertionFailedError(
						"Expected " + expected.getClass().getName() + ", found " + actual.getClass().getName());
			}
		} else if (expected instanceof JSONArray) {
			if (actual instanceof JSONArray) {
				comparator.doAssertEqualsJSONArray((JSONArray) expected, (JSONArray) actual, keys);
			} else {
				throw new AssertionFailedError(
						"Expected " + expected.getClass().getName() + ", found " + actual.getClass().getName());
			}
		} else {
			comparator.doAssertEqualsPrimitiveType(expected, actual, keys);
		}
	}

	@LogExecutionTime
	protected void doAssertEqualsJSONObject(JSONObject expected, JSONObject actual, Deque<String> keys) {
		Iterator<String> keysIt = (Iterator<String>) expected.keys();
		keysIt.forEachRemaining(key -> {
			keys.addLast(key);
			comparator.doAssertEquals(expected.opt(key), actual.opt(key), keys);
			keys.removeLast();
		});

		keysIt = (Iterator<String>) actual.keys();
		keysIt.forEachRemaining(key -> {
			keys.addLast(key);
			comparator.doAssertEquals(expected.opt(key), actual.opt(key), keys);
			keys.removeLast();
		});
	}

	@LogExecutionTime
	protected void doAssertEqualsJSONArray(JSONArray expected, JSONArray actual, Deque<String> keys) {

		if (expected.length() != actual.length()) {
			fail(notEqualLength(expected, actual, keys));
		}
		for (int i = 0; i < expected.length(); i++) {
			comparator.doAssertEquals(expected.opt(i), actual.opt(i), keys);
		}
	}

	@LogExecutionTime
	protected void doAssertEqualsPrimitiveType(Object expected, Object actual, Deque<String> keys) {
		String v1 = expected.toString();
		String v2 = actual.toString();
		if (v1.equals(v2)) {
			return;
		}
		fail(notEqual(expected, actual, keys));
	}

	protected void fail(String message) {
		throw new AssertionFailedError(message);
	}

	protected String notEqualLength(JSONArray expected, JSONArray actual, Deque<String> keys) {
		return buildMessage("Length don't match. ", expected.length(), actual.length(), keys);
	}

	protected String notEqual(Object expected, Object actual, Deque<String> keys) {
		return buildMessage("", expected, actual, keys);
	}

	protected String buildMessage(String prefix, Object expected, Object actual, Deque<String> keys) {
		return "On key: " + formatKeys(keys) + ", expected: " + expected + ", but was: " + actual;
	}

	protected String formatKeys(Deque<String> keys) {
		return keys.stream().collect(Collectors.joining("."));
	}
}
