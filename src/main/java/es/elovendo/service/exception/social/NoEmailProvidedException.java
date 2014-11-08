package es.elovendo.service.exception.social;

@SuppressWarnings("serial")
public class NoEmailProvidedException extends Exception {

	public NoEmailProvidedException(String msg) {
		super(msg);
	}
	
	public NoEmailProvidedException() {
		
	}
	
}
