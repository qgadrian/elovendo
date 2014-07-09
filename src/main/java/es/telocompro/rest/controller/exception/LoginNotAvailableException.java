package es.telocompro.rest.controller.exception;

@SuppressWarnings("serial")
public class LoginNotAvailableException extends Exception {

	private String login;
	
	public LoginNotAvailableException(String login) {
        
        super(login + " is in use");
        this.login = login;
    }

	public String getLogin() {
		return login;
	}
	
}
