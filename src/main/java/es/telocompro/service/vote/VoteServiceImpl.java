package es.telocompro.service.vote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.telocompro.model.item.Item;
import es.telocompro.model.user.User;
import es.telocompro.model.vote.Vote;
import es.telocompro.model.vote.VoteRepository;
import es.telocompro.rest.controller.exception.InvalidVoteUsersException;
import es.telocompro.rest.controller.exception.ItemNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;
import es.telocompro.service.item.ItemService;
import es.telocompro.service.user.UserService;
import static es.telocompro.util.Constant.*;

@Service(value="voteService")
public class VoteServiceImpl implements VoteService {

	@Autowired
	VoteRepository voteRepository;
	@Autowired
	UserService userService;
	@Autowired
	ItemService itemService;
	
	@Override
	public Vote getVote(User userVote, User userReceive, Item item) {
		return voteRepository.findVote(userVote.getUserId(), 
				userReceive.getUserId(), item.getItemId());
	}
	
	@Override
	public List<Vote> getVotesForItem(Item item) {
		return voteRepository.findByItem(item.getItemId());
	}

	@Override
	public Vote addVote(Long userIdVote, Long userIdReceive, Long itemId, int voteType,
			float reability, String voteMessage) throws UserNotFoundException, ItemNotFoundException, 
			InvalidVoteUsersException {
		
		User userVote = userService.findUserById(userIdVote);
		User userReceive = userService.findUserById(userIdReceive);
		Item item = itemService.getItemById(itemId);
		
		if (userVote == null) throw new UserNotFoundException(userIdVote);
		if (userReceive == null) throw new UserNotFoundException(userIdReceive);
		if (item == null) throw new ItemNotFoundException(itemId);
		
		// Vote value
		int voteValue;
		if (voteType == VOTE_POSITIVE) voteValue = Math.round(VOTE_BASE * reability);
		else voteValue = Math.round(-VOTE_BASE * reability);
		
		// Check if user who receives the vote already voted to the other one
		Vote firstVote = getVote(userReceive, userVote, item);
		Vote vote; 
		
		// Check fake new votes with different users for one item
		for (Vote v : getVotesForItem(item)) validateUsersForVote(userVote, userReceive, v);
		
		// TODO contemplar que el voto negativo cuente siempre
		// TODO Not sure if the state of the vote is necessary...

		if (firstVote == null) {
			// Users with >75 value can give bad vote without "response"
			if (voteType == VOTE_NEGATIVE && userVote.getUserValue() > 75) {
				vote = new Vote(userVote, userReceive, item, voteType, VOTE_ACTIVE, voteValue, voteMessage);
				updateUserWithVote(userReceive, voteValue);
			}
			else // If vote is positive or user doesn't have "value", mark it as inactive
				vote = new Vote(userVote, userReceive, item, voteType, VOTE_INACTIVE, voteValue, voteMessage);
		} else {
			// Check if the existing vote is between the current users, otherwise throw an error
			validateUsersForVote(userVote, userReceive, firstVote);
			
			// Increase value for the user who receives the vote
			vote = new Vote(userVote, userReceive, item, voteType, VOTE_ACTIVE, voteValue, voteMessage);
			updateUserWithVote(userReceive, voteValue);
			
			// Increase value for the user who get voted earlier
			updateUserWithVote(userVote, firstVote.getVoteValue());
			
			// Active the first vote and update it if it is inactive
			if (!firstVote.getVoteValid()) {
				firstVote.setVoteValid(VOTE_ACTIVE);
				voteRepository.save(firstVote);
			}
		}
		
		return voteRepository.save(vote);
	}
	
	private void updateUserWithVote(User user, int voteValue) {
		int userValue = user.getUserValue() + voteValue;
		if (userValue > 100) userValue = 100;
		else if (userValue < 0) userValue = 0;
		user.setUserValue(userValue);
		userService.updateUser(user);
	}
	
	private void validateUsersForVote(User user1, User user2, Vote vote) 
			throws InvalidVoteUsersException {
		
		if ((!vote.getUserVote().equals(user1) && vote.getUserReceive().equals(user2))
			|| (!vote.getUserVote().equals(user2) && vote.getUserReceive().equals(user1)))
			throw new InvalidVoteUsersException(user1.getUserId(), user2.getUserId(), vote.getVoteId());
		
//		if (vote.getUserVote().equals(user1)) {
//			if (!vote.getUserReceive().equals(user2)) 
//				throw new InvalidVoteUsersException(user1.getUserId(), user2.getUserId(), vote.getVoteId());
//		} else if (vote.getUserVote().equals(user2)) {
//			if (!vote.getUserReceive().equals(user1)) 
//				throw new InvalidVoteUsersException(user2.getUserId(), user1.getUserId(), vote.getVoteId());
//		} else 
//			throw new InvalidVoteUsersException(user2.getUserId(), user1.getUserId(), vote.getVoteId());
		
	}
	
}
