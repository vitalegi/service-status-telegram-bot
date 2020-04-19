package it.vitalegi.servicestatustelegrambot.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramHandlers extends TelegramLongPollingBot {

	Logger log = LoggerFactory.getLogger(TelegramHandlers.class);

	String botUsername;

	String botToken;

	@Autowired
	TelegramProcessor telegramProcessor;

	public TelegramHandlers(String botUsername, String botToken) {
		super();
		this.botUsername = botUsername;
		this.botToken = botToken;
	}

	@Override
	public String getBotToken() {
		log.info("getBotToken");
		return botToken;
	}

	@Override
	public String getBotUsername() {
		log.info("getBotUsername");
		return botUsername;
	}

	@Override
	public void onUpdateReceived(Update update) {
		log.info("onUpdateReceived {}", update);
		telegramProcessor.process(update);
	}
}
