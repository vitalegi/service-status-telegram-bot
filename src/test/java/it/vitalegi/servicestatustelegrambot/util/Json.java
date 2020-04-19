package it.vitalegi.servicestatustelegrambot.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json {
	public static Json init() {
		return new Json();
	}

	JSONObject json;

	public Json() {
		json = new JSONObject();
	}

	public JSONObject build() {
		return json;
	}

	public String buildString() {
		return json.toString();
	}

	public Json put(String name, JSONObject value) {
		try {
			json.put(name, value);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public Json putJSONArray(String name, Object... values) {
		JSONArray array = new JSONArray();
		for (Object value : values) {
			array.put(value);
		}
		return put(name, array);
	}

	public Json put(String name, JSONArray value) {
		try {
			json.put(name, value);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public Json put(String name, Integer value) {
		try {
			json.put(name, value);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public Json put(String name, Long value) {
		try {
			json.put(name, value);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public Json put(String name, Double value) {
		try {
			json.put(name, value);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public Json put(String name, boolean value) {
		try {
			json.put(name, value);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public Json put(String name, String value) {
		try {
			json.put(name, value);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

}
