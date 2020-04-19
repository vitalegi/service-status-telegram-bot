package it.vitalegi.servicestatustelegrambot.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static it.vitalegi.servicestatustelegrambot.util.TimeUtil.MINUTE;
import static it.vitalegi.servicestatustelegrambot.util.TimeUtil.HOUR;
import static it.vitalegi.servicestatustelegrambot.util.TimeUtil.DAY;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import it.vitalegi.servicestatustelegrambot.SpringTestConfig;

@SpringBootTest(classes = SpringTestConfig.class)
public class TimeUtilTest {

	@Test
	void test1s() {
		assertEquals("1s", TimeUtil.eta(1));
	}

	@Test
	void testUnknown() {
		assertEquals("Unknown", TimeUtil.eta(-1));
	}

	@Test
	void testDone() {
		assertEquals("---", TimeUtil.eta(-2));
	}

	@Test
	void test30s() {
		assertEquals("30s", TimeUtil.eta(30));
	}

	@Test
	void test1m() {
		assertEquals("1m00s", TimeUtil.eta(MINUTE));
	}

	@Test
	void test5m30s() {
		assertEquals("5m30s", TimeUtil.eta(5 * MINUTE + 30));
	}

	@Test
	void test1h() {
		assertEquals("1h00m", TimeUtil.eta(HOUR));
	}

	@Test
	void test1h30m59s() {
		assertEquals("1h30m", TimeUtil.eta(1 * HOUR + 30 * MINUTE + 59));
	}

	@Test
	void test1d() {
		assertEquals("1d00h", TimeUtil.eta(DAY));
	}

	@Test
	void test1d1h30m59s() {
		assertEquals("1d01h", TimeUtil.eta(1 * DAY + 1 * HOUR + 30 * MINUTE + 59));
	}

	@Test
	void test10d5h59m() {
		assertEquals("10d05h", TimeUtil.eta(10 * DAY + 5 * HOUR + 59 * MINUTE));
	}

}
