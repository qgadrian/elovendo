package es.elovendo.rest.exception;

@SuppressWarnings("serial")
public class UserSelfishOperationException extends Exception {
	
	public UserSelfishOperationException() {
		super();
	}
	
	public UserSelfishOperationException(String msg) {
		super(msg);
	}

}
