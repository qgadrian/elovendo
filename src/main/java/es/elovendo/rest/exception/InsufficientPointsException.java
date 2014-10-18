package es.elovendo.rest.exception;

@SuppressWarnings("serial")
public class InsufficientPointsException extends Exception {
	
	public InsufficientPointsException() {
        super("Not enought points ");
    }
	
}
