package it.vitalegi.servicestatustelegrambot.telegram.request;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import it.vitalegi.servicestatustelegrambot.util.StringUtil;

public class TelegramRequestWrapperImpl implements RequestWrapper {

	Logger log = LoggerFactory.getLogger(TelegramRequestWrapperImpl.class);

	DefaultAbsSender absSender;
	Message message;

	public TelegramRequestWrapperImpl(DefaultAbsSender absSender, Message message) {
		super();
		this.absSender = absSender;
		this.message = message;
	}

	protected java.io.File downloadFile(String filePath) {
		try {
			return absSender.downloadFile(filePath);
		} catch (TelegramApiException e) {
			throw new RuntimeException("Error downloading document " + filePath);
		}
	}

	@Override
	public byte[] getDocumentContent() {
		Document document = message.getDocument();

		log.info("File name: {}", document.getFileName());
		log.info("File id:   {}", document.getFileId());

		String filePath = getFilePath(document);
		log.info("FilePath:  {}", filePath);

		java.io.File file = downloadFile(filePath);
		try {
			return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
		} catch (IOException e) {
			throw new RuntimeException("Error reading " + document.getFileId() + " " + document.getFileName(), e);
		}
	}

	@Override
	public String getDocumentName() {
		Document document = message.getDocument();

		return document.getFileName();
	}

	protected String getFilePath(Document document) {
		GetFile getFileMethod = new GetFile();
		getFileMethod.setFileId(document.getFileId());
		try {
			File file = absSender.execute(getFileMethod);
			return file.getFilePath();
		} catch (TelegramApiException e) {
			throw new RuntimeException(
					"Error downloading document " + document.getFileName() + " " + document.getFileId());
		}
	}

	@Override
	public String getText() {
		return message.getText();
	}

	@Override
	public boolean hasDocument() {
		return message.hasDocument();
	}

	@Override
	public boolean hasText() {
		return message.hasText();
	}

	@Override
	public List<String> tokenizeText() {
		return StringUtil.tokenize(getText());
	}

	@Override
	public Long getChatId() {
		return message.getChatId();
	}
}
