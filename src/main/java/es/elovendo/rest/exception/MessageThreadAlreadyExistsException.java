package es.elovendo.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="SubCategories Not Found")
public class MessageThreadAlreadyExistsException extends Exception {

	private long messageThreadId;
	
	public MessageThreadAlreadyExistsException(long messageThreadId) {
        
        super("Message thread duplicate for => "
        		+ messageThreadId);
        this.messageThreadId = messageThreadId;
    }

	public long getMessageThreadId() {
		return messageThreadId;
	}
	
}
