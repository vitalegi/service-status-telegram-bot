package it.vitalegi.servicestatustelegrambot.telegram;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface SendMessageWrapper {

	Message sendHtmlMessage(Long toChatId, String text);

	Message sendTextMessage(Long toChatId, String text);

}
