package it.vitalegi.servicestatustelegrambot.util;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {

	public static final String DATE = "yyyy-MM-dd";
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DATE, Locale.ITALIAN);
}
