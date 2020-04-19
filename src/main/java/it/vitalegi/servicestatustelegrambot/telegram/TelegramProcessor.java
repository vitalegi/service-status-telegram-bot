package it.vitalegi.servicestatustelegrambot.telegram;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import it.vitalegi.servicestatustelegrambot.exception.InvalidTelegramRequestException;
import it.vitalegi.servicestatustelegrambot.store.UserData;
import it.vitalegi.servicestatustelegrambot.store.UserDataRepository;
import it.vitalegi.servicestatustelegrambot.telegram.request.RequestWrapper;
import it.vitalegi.servicestatustelegrambot.telegram.request.TelegramRequestWrapperImpl;
import it.vitalegi.servicestatustelegrambot.util.DateUtil;

@Service
public class TelegramProcessor {

	Logger log = LoggerFactory.getLogger(TelegramProcessor.class);

	@Autowired
	TelegramHandlers telegramHandlers;

	@Value("#{'${telegram.authorized-ids}'.split(',')}")
	List<Integer> authorizedIds;

	@Autowired
	SendMessageWrapper sendMessage;

	@Autowired
	UserDataRepository userDataRepository;

	public void process(Update update) {

		isAuthorized(update);

		Message message = update.getMessage();

		RequestWrapper messageWrapper = new TelegramRequestWrapperImpl(telegramHandlers, message);

		if (messageWrapper.hasText()) {
			log.info("received message from {}: {}", messageWrapper.getChatId(), messageWrapper.getText());
		}

		UserData userData = userDataRepository.getUserData(messageWrapper.getChatId());
		log.info("User status: {}", userData);
		String msg = messageWrapper.getText();
		List<String> values = parseMessage(msg);
		// userDataRepository.setUserData(userData);
	}

	protected void isAuthorized(Update update) {
		Integer userId = update.getMessage().getFrom().getId();
		for (Integer id : authorizedIds) {
			log.info("Match {} vs {}", userId, id);
			if (id.equals(userId)) {
				return;
			}
		}
		User sender = update.getMessage().getFrom();

		sendMessage.sendTextMessage((long) sender.getId(), "Unauthorized.");
		throw new InvalidTelegramRequestException(
				"User not authorized: " + sender.getUserName() + " " + sender.getId());
	}

	protected List<String> parseMessage(String msg) {
		if (msg == null) {
			return new ArrayList<>();
		}
		try (Scanner scan = new Scanner(msg)) {
			List<String> values = new ArrayList<>();
			while (scan.hasNext()) {
				values.add(scan.next());
			}
			return values;
		}
	}
}
