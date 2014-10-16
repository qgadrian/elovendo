package es.elovendo.rest.exception;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class MessageTextTooLongException extends Exception {
	
	Logger logger = Logger.getLogger(MessageTextTooLongException.class);

	public long userId;

	public MessageTextTooLongException(long userId) {
		super("Message too long by user " + userId);
		logger.warn("Client side validation bypass by user with id " + userId);
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

}
