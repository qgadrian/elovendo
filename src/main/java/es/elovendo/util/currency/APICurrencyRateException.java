package es.elovendo.util.currency;

@SuppressWarnings("serial")
public class APICurrencyRateException extends Exception {
	public APICurrencyRateException() {
		super();
	}

	public APICurrencyRateException(String message) {
		super(message);
	}

	public APICurrencyRateException(String message, Throwable cause) {
		super(message, cause);
	}

	public APICurrencyRateException(Throwable cause) {
		super(cause);
	}
}
