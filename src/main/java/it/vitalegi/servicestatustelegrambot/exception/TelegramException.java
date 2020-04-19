package it.vitalegi.servicestatustelegrambot.exception;

public class TelegramException extends RuntimeException {

	public TelegramException(Throwable cause) {
		super(cause);
	}

	public TelegramException(String message, Throwable cause) {
		super(message, cause);
	}
}
