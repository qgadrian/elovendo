package es.telocompro.rest.exception;

@SuppressWarnings("serial")
public class UserDuplicateException extends Exception {
	
	public UserDuplicateException(long userId) {
        super("user " + userId + " already exists");
    }
	
	public UserDuplicateException(String login) {
		super("user " + login + " already exists");
	}
	
}
