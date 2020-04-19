package it.vitalegi.servicestatustelegrambot.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BytesUtil {
	public final static long KB = 1024;
	public final static long MB = 1048576;
	public final static long GB = 1073741824;

	public static String bytes(long bytes) {
		return bytes(Double.valueOf(bytes));
	}

	public static String bytes(double bytes) {
		BigDecimal b = BigDecimal.valueOf(bytes);
		if (bytes < KB) {
			return b.setScale(0, RoundingMode.HALF_UP) + "B";
		}
		if (bytes < MB) {
			return b.divide(BigDecimal.valueOf(KB), 2, RoundingMode.HALF_UP).toPlainString() + "KB";
		}
		if (bytes < GB) {
			return b.divide(BigDecimal.valueOf(MB), 2, RoundingMode.HALF_UP).toPlainString() + "MB";
		}
		return b.divide(BigDecimal.valueOf(GB), 2, RoundingMode.HALF_UP).toPlainString() + "GB";
	}

	public static String speed(double bytes) {
		return bytes(bytes) + "s";
	}

	public static String ratio(long missing, long size) {
		BigDecimal left = BigDecimal.valueOf(missing);
		BigDecimal tot = BigDecimal.valueOf(size);

		BigDecimal nom = tot.subtract(left).multiply(BigDecimal.valueOf(100));
		BigDecimal ratio = nom.divide(tot, 2, RoundingMode.HALF_UP);
		return ratio(ratio);
	}

	public static String ratio(BigDecimal ratio) {
		ratio = ratio.setScale(2, RoundingMode.HALF_UP);
		return ratio.toPlainString();
	}
}
