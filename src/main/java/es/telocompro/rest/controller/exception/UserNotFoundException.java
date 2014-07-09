package es.telocompro.rest.controller.exception;

@SuppressWarnings("serial")
public class UserNotFoundException extends Exception {
	
	public UserNotFoundException(long userId) {
        super("user " + userId + " not found");
    }
	
	public UserNotFoundException(String login) {
		super("user " + login + " not found");
	}
	
}
