package es.telocompro.service.vote;


import java.util.List;

import org.springframework.data.domain.Page;

import es.telocompro.model.item.Item;
import es.telocompro.model.user.User;
import es.telocompro.model.vote.Vote;
import es.telocompro.rest.exception.InvalidSelfVoteException;
import es.telocompro.rest.exception.InvalidVoteUsersException;
import es.telocompro.rest.exception.ItemNotFoundException;
import es.telocompro.rest.exception.UserNotFoundException;

public interface VoteService {
	
	/**
	 * Returns a vote for the given arguments if matched.
	 * @param userVote
	 * @param userReceive
	 * @param item
	 * @return
	 */
	public Vote getVote(User userVote, User userReceive, Item item);
	
	/**
	 * Returns a list vote for a given item
	 * @param item
	 * @return
	 */
	public List<Vote> getVotesForItem(Item item);
	
	/**
	 * Adds a vote between the given users. Saves it as "pending" both users didn't vote each other yet.
	 * If the first user who voted first emits a NEGATIVE VOTE and the other users emits a POSITIVE VOTE,
	 * for BOTH VOTES reability will be the one BEFORE that votes (others votes could affect).
	 * @param userIdVote
	 * @param userIdReceive
	 * @param itemId
	 * @param reability
	 * @throws UserNotFoundException
	 * @throws ItemNotFoundException 
	 * @throws InvalidVoteUsersException 
	 * @throws InvalidSelfVoteException 
	 */
	public Vote addVote(Long userIdVote, Long userIdReceive, Long itemId, int voteType, 
			float reability, String voteMessage) throws UserNotFoundException, ItemNotFoundException, InvalidVoteUsersException, InvalidSelfVoteException;
	
	public int getNumberVotesPositive(Long userId);
	
	public int getNumberVotesNegative(Long userId);
	
	public int getNumberVotesQueued(Long userId);
	
	public Page<Vote> getVotesPositive(Long userId, int page, int size);
	
	public Page<Vote> getVotesNegative(Long userId, int page, int size);
	
	public Page<Vote> getVotesQueued(Long userId, int page, int size);
}
