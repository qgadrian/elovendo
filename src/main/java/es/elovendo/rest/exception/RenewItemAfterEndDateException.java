package es.elovendo.rest.exception;

@SuppressWarnings("serial")
public class RenewItemAfterEndDateException extends Exception {

	public RenewItemAfterEndDateException() {
		super();
	}
	
	public RenewItemAfterEndDateException(String msg) {
		super(msg);
	}
	
}
