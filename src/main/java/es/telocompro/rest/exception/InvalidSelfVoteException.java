package es.telocompro.rest.exception;

@SuppressWarnings("serial")
public class InvalidSelfVoteException extends Exception {
	
	public InvalidSelfVoteException(long userId) {
        super("user " + userId + "tried to vote himself");
    }
	
}
