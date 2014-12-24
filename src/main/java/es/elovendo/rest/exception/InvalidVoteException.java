package es.elovendo.rest.exception;

@SuppressWarnings("serial")
public class InvalidVoteException extends Exception {

	public InvalidVoteException(long user1, String msg) {
		super("INVALID VOTE for user " + user1 + ",\nREASON: " + msg);
	}

}
