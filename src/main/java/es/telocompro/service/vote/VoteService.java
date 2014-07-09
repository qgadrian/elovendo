package es.telocompro.service.vote;


import es.telocompro.model.item.Item;
import es.telocompro.model.user.User;
import es.telocompro.model.vote.Vote;
import es.telocompro.rest.controller.exception.ItemNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;

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
	 * Adds a vote between the given users. Saves it as "pending" both users didn't vote each other yet.
	 * @param userIdVote
	 * @param userIdReceive
	 * @param itemId
	 * @param reability
	 * @throws UserNotFoundException
	 * @throws ItemNotFoundException 
	 */
	public Vote addVote(Long userIdVote, Long userIdReceive, Long itemId, int voteType, 
			float reability, String voteMessage) throws UserNotFoundException, ItemNotFoundException;

}
