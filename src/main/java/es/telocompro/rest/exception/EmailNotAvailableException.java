package es.telocompro.rest.exception;

@SuppressWarnings("serial")
public class EmailNotAvailableException extends Exception {

	private String email;
	
	public EmailNotAvailableException(String email) {
        
        super("email " + email + " is in use");
        this.email = email;
    }

	public String getLogin() {
		return email;
	}
	
}
