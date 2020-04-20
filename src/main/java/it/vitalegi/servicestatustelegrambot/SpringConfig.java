package it.vitalegi.servicestatustelegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import it.vitalegi.servicestatustelegrambot.service.HttpMonitorServiceImpl;
import it.vitalegi.servicestatustelegrambot.service.MonitorStatus;
import it.vitalegi.servicestatustelegrambot.telegram.TelegramHandlers;

@Configuration
@Profile("prod")
@EnableScheduling
public class SpringConfig {

	Logger log = LoggerFactory.getLogger(SpringConfig.class);

	@Value("${telegram.bot-username}")
	String botUsername;

	@Value("${telegram.bot-token}")
	String botToken;

	@Autowired
	HttpMonitorServiceImpl httpMonitorService;

	@Autowired
	MonitorStatus monitorStatus;
	
	@Bean
	public TelegramBotsApi telegramBot() {
		log.info("init telegramBot");
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		return telegramBotsApi;
	}

	@Bean
	public TelegramHandlers telegramHandlers(TelegramBotsApi telegramBotsApi) {
		log.info("init telegramHandlers");
		TelegramHandlers telegramHandlers = new TelegramHandlers(botUsername, botToken);
		try {
			telegramBotsApi.registerBot(telegramHandlers);
		} catch (TelegramApiException e) {
			throw new RuntimeException("Error configuring telegramHandlers", e);
		}
		return telegramHandlers;
	}

	@Scheduled(cron = "${scheduler.check.cron.expression}")
	public void scheduleHttpMonitorCheckTask() {
		httpMonitorService.process();
	}

	@Scheduled(cron = "${scheduler.reset.cron.expression}")
	public void scheduleHttpMonitorResetTask() {
		monitorStatus.reset();
	}
}
