package it.vitalegi.servicestatustelegrambot.util;

import java.util.List;

public class ListUtil {

	public static <E> E getOrNull(List<E> list, int index) {
		if (list == null || index >= list.size()) {
			return null;
		}
		return list.get(index);
	}

	public static boolean isEmpty(List<?> list) {
		return list == null || list.isEmpty();
	}

	public static boolean isNotEmpty(List<?> list) {
		return list != null && !list.isEmpty();
	}
}
