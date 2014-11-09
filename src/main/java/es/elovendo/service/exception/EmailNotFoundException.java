package es.elovendo.service.exception;

@SuppressWarnings("serial")
public class EmailNotFoundException extends Exception {

	public EmailNotFoundException(String msg) {
		super(msg);
	}
	
	public EmailNotFoundException() {
		
	}
	
}
