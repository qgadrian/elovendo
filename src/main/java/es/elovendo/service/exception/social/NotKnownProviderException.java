package es.elovendo.service.exception.social;

@SuppressWarnings("serial")
public class NotKnownProviderException extends Exception {

	public NotKnownProviderException(String msg) {
		super(msg);
	}
	
	public NotKnownProviderException() { }
}
