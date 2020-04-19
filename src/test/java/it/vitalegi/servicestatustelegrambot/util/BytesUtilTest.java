package it.vitalegi.servicestatustelegrambot.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static it.vitalegi.servicestatustelegrambot.util.BytesUtil.KB;
import static it.vitalegi.servicestatustelegrambot.util.BytesUtil.MB;
import static it.vitalegi.servicestatustelegrambot.util.BytesUtil.GB;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import it.vitalegi.servicestatustelegrambot.SpringTestConfig;

@SpringBootTest(classes = SpringTestConfig.class)
public class BytesUtilTest {

	@Test
	void testbytes1B() {
		assertEquals("1B", BytesUtil.bytes(1));
	}

	@Test
	void testbytes1023B() {
		assertEquals("1023B", BytesUtil.bytes(1023));
	}

	@Test
	void testbytes1KB() {
		assertEquals("1.00KB", BytesUtil.bytes(KB));
	}

	@Test
	void testbytes500_1KB() {
		assertEquals("500.10KB", BytesUtil.bytes(500 * KB + 100));
	}

	@Test
	void testbytes1MB() {
		assertEquals("1.00MB", BytesUtil.bytes(MB));
	}

	@Test
	void testbytes500_1MB() {
		assertEquals("500.10MB", BytesUtil.bytes(500 * MB + 100 * KB));
	}

	@Test
	void testbytes1GB() {
		assertEquals("1.00GB", BytesUtil.bytes(GB));
	}

	@Test
	void testbytes5_012GB() {
		assertEquals("5.01GB", BytesUtil.bytes(5 * GB + 12 * MB));
	}

	@Test
	void testbytes5_016GB() {
		assertEquals("5.02GB", BytesUtil.bytes(5 * GB + 16 * MB));
	}

	@Test
	void testbytes100000GB() {
		assertEquals("100000.00GB", BytesUtil.bytes(100000 * GB));
	}
}
