package it.vitalegi.servicestatustelegrambot.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import it.vitalegi.servicestatustelegrambot.exception.TelegramException;

@Service
public class SendMessageWrapperImpl implements SendMessageWrapper {

	@Autowired
	TelegramHandlers telegramHandlers;

	@Override
	public Message sendTextMessage(Long toChatId, String text) {
		SendMessage reply = new SendMessage();
		reply.setChatId(toChatId);
		reply.setText(text);
		try {
			return telegramHandlers.execute(reply);
		} catch (TelegramApiException e) {
			throw new TelegramException(e);
		}
	}

	@Override
	public Message sendHtmlMessage(Long toChatId, String text) {
		SendMessage reply = new SendMessage();
		reply.setChatId(toChatId);
		reply.setText(text);
		reply.enableHtml(true);
		try {
			return telegramHandlers.execute(reply);
		} catch (TelegramApiException e) {
			throw new TelegramException(e);
		}
	}
}
