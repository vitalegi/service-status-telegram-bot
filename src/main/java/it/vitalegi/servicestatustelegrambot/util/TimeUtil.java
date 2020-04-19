package it.vitalegi.servicestatustelegrambot.util;

public class TimeUtil {
	public static final int MINUTE = 60;
	public static final int HOUR = 60 * MINUTE;
	public static final int DAY = 24 * HOUR;

	public static String eta(long eta) {
		if (eta == -1) {
			return "Unknown";
		}
		if (eta == -2) {
			return "---";
		}
		if (eta < MINUTE) {
			return eta + "s";
		}
		if (eta < HOUR) {
			long minutes = eta / MINUTE;
			long seconds = eta % MINUTE;
			return format(0, 0, minutes, seconds);
		}
		if (eta < DAY) {
			long hours = eta / HOUR;
			long minutes = (eta % HOUR) / MINUTE;
			return format(0, hours, minutes, 0);
		}
		long days = eta / DAY;
		long hours = (eta % DAY) / HOUR;
		return format(days, hours, 0, 0);
	}

	protected static String format(long days, long hours, long minutes, long seconds) {
		if (days > 0) {
			return days + "d" + padding(hours, 2) + "h";
		}
		if (hours > 0) {
			return hours + "h" + padding(minutes, 2) + "m";
		}
		if (minutes > 0) {
			return minutes + "m" + padding(seconds, 2) + "s";
		}
		return seconds + "s";
	}

	protected static String padding(long value, int size) {
		String str = value + "";
		while (str.length() < size) {
			str = "0" + str;
		}
		return str;
	}
}
