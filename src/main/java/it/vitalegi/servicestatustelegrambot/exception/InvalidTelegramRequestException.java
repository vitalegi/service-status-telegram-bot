package it.vitalegi.servicestatustelegrambot.exception;

public class InvalidTelegramRequestException extends RuntimeException {

	public InvalidTelegramRequestException() {
		super();
	}

	public InvalidTelegramRequestException(String msg) {
		super(msg);
	}
}
