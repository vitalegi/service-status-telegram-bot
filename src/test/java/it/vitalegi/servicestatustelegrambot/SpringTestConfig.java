package it.vitalegi.servicestatustelegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import it.vitalegi.servicestatustelegrambot.telegram.TelegramHandlers;

@Configuration
@Profile("!prod")
@ComponentScan("it.vitalegi.servicestatustelegrambot")
public class SpringTestConfig {

	Logger log = LoggerFactory.getLogger(SpringConfig.class);

	@Bean
	public TelegramHandlers telegramHandlers() {
		log.info("init telegramHandlers");
		TelegramHandlers telegramHandlers = new TelegramHandlers("username", "token");
		return telegramHandlers;
	}
}
