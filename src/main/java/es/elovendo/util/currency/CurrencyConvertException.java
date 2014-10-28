package es.elovendo.util.currency;

@SuppressWarnings("serial")
public class CurrencyConvertException extends Exception {

	public CurrencyConvertException() {
		super();
	}

	public CurrencyConvertException(String message) {
		super(message);
	}

	public CurrencyConvertException(String message, Throwable cause) {
		super(message, cause);
	}

	public CurrencyConvertException(Throwable cause) {
		super(cause);
	}
	
}
