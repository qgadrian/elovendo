package es.telocompro.service.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.telocompro.model.item.Item;
import es.telocompro.model.user.User;
import es.telocompro.model.vote.Vote;
import es.telocompro.model.vote.VoteRepository;
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
	public Vote addVote(Long userIdVote, Long userIdReceive, Long itemId, int voteType,
			float reability, String voteMessage) throws UserNotFoundException, ItemNotFoundException {
		
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
		
		Vote votePending = getVote(userReceive, userVote, item);
		Vote vote;
		
		if (votePending == null) { // If there is no other vote, remain this vote as "inactive"
			vote = new Vote(userVote, userReceive, item, voteType, VOTE_INACTIVE, voteValue, voteMessage);
		} else {
			// TODO Not sure if the state of the vote is necessary...
			
			// Increase value for the user who receives the vote
			vote = new Vote(userVote, userReceive, item, voteType, VOTE_ACTIVE, voteValue, voteMessage);
			int userReceiveValue = userReceive.getUserValue() + voteValue;
			if (userReceiveValue > 100) userReceiveValue = 100;
			userReceive.setUserValue(userReceiveValue);
			
			// Increase value for the user who get voted earlier
			int userVoteValue = userVote.getUserValue() + votePending.getVoteValue();
			if (userVoteValue > 100) userVoteValue = 100;
			userVote.setUserValue(userVoteValue);
			
			// update users
			userService.updateUser(userReceive);
			userService.updateUser(userVote);
		}
		
		return voteRepository.save(vote);
	}
	
}
