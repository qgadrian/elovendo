package es.elovendo.rest.exception;

@SuppressWarnings("serial")
public class InvalidVoteUsersException extends Exception {

	public InvalidVoteUsersException(long user1, long user2, long voteId) {
		super("user " + user1 + " and " + user2 + " cannot interact with " + voteId);
	}

}
