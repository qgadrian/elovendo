package es.telocompro.rest.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="SubCategories Not Found")
public class InvalidMessageThreadException extends Exception {

	private long userId;
	
	public InvalidMessageThreadException(long userId) {
        
        super("Message thread doesn't belong to => " + userId);
        this.userId = userId;
    }

	public long getUserId() {
		return userId;
	}
	
}
