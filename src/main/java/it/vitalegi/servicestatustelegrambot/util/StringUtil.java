package it.vitalegi.servicestatustelegrambot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StringUtil {

	public static boolean isNotNull(String value) {
		return !isNullOrEmpty(value);
	}

	public static boolean isNullOrEmpty(String value) {
		return value == null || value.equals("");
	}

	public static boolean isNotNullOrEmpty(String value) {
		return !isNullOrEmpty(value);
	}

	public static List<String> tokenize(String text) {
		List<String> tokenized = new ArrayList<>();
		if (isNullOrEmpty(text)) {
			return tokenized;
		}
		try (Scanner scan = new Scanner(text)) {
			scan.forEachRemaining(tokenized::add);
		}
		return tokenized;
	}
}
