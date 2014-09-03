package es.telocompro.rest.controller.exception;

@SuppressWarnings("serial")
public class InsufficientPointsException extends Exception {
	
	public InsufficientPointsException() {
        super("Not enought points ");
    }
	
}
