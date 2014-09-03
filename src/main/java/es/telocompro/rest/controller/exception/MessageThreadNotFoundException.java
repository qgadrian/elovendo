package es.telocompro.rest.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="SubCategories Not Found")
public class MessageThreadNotFoundException extends Exception {

	private long messageThreadId;
	
	public MessageThreadNotFoundException(long messageThreadId) {
        
        super("Message thread not found => " + messageThreadId);
        this.messageThreadId = messageThreadId;
    }

	public long getMessageThreadId() {
		return messageThreadId;
	}
	
}
