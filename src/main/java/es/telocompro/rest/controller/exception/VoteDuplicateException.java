package es.telocompro.rest.controller.exception;

@SuppressWarnings("serial")
public class VoteDuplicateException extends Exception {
	
	public VoteDuplicateException(long user1, long user2, long itemId) {
        super("user " + user1 + " already vote for " + user2 + " for item " + itemId);
    }
	
}
