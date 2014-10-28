package es.elovendo.util.currency;

@SuppressWarnings("serial")
public class NoCurrencyLocaleException extends Exception {
	public NoCurrencyLocaleException() {
		super();
	}

	public NoCurrencyLocaleException(String message) {
		super(message);
	}

	public NoCurrencyLocaleException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoCurrencyLocaleException(Throwable cause) {
		super(cause);
	}
}
