package it.vitalegi.servicestatustelegrambot.telegram.request;

import java.util.List;

public interface RequestWrapper {

	public byte[] getDocumentContent();

	public String getDocumentName();

	public String getText();

	public boolean hasDocument();

	public boolean hasText();

	public List<String> tokenizeText();

	Long getChatId();
}
