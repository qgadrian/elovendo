package es.elovendo.util.sessionHelper.exception;


@SuppressWarnings("serial")
public class AnonymousUserAuthenticationException extends Exception {

	public AnonymousUserAuthenticationException() {
		super();
	}
	
	public AnonymousUserAuthenticationException(String message) {
		super(message);
	}
}
