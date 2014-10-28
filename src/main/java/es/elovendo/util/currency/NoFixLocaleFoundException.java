package es.elovendo.util.currency;

@SuppressWarnings("serial")
public class NoFixLocaleFoundException extends Exception {
	public NoFixLocaleFoundException() {
		super();
	}

	public NoFixLocaleFoundException(String message) {
		super(message);
	}

	public NoFixLocaleFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoFixLocaleFoundException(Throwable cause) {
		super(cause);
	}
}
