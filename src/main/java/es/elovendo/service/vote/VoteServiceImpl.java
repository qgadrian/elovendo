package es.elovendo.service.vote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.elovendo.model.item.Item;
import es.elovendo.model.user.User;
import es.elovendo.model.vote.Vote;
import es.elovendo.model.vote.VoteRepository;
import es.elovendo.rest.exception.InvalidSelfVoteException;
import es.elovendo.rest.exception.InvalidVoteException;
import es.elovendo.rest.exception.InvalidVoteUsersException;
import es.elovendo.rest.exception.ItemNotFoundException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.rest.exception.VoteDuplicateException;
import es.elovendo.service.item.ItemService;
import es.elovendo.service.user.UserService;
import static es.elovendo.util.Constant.*;

@Service(value = "voteService")
public class VoteServiceImpl implements VoteService {

	@Autowired
	VoteRepository voteRepository;
	@Autowired
	UserService userService;
	@Autowired
	ItemService itemService;

	@Override
	public Vote getVote(User userVote, User userReceive, Item item) {
		return voteRepository.findVote(userVote.getUserId(), userReceive.getUserId(), item.getItemId());
	}

	@Override
	public List<Vote> getVotesForItem(Item item) {
		return voteRepository.findByItem(item.getItemId());
	}

	// @Override
	// public Vote addVote(Long userIdVote, Long userIdReceive, Long itemId, int
	// voteType,
	// float reability, String voteMessage) throws UserNotFoundException,
	// ItemNotFoundException,
	// InvalidVoteUsersException, InvalidSelfVoteException {
	//
	// User userVote = userService.findUserById(userIdVote);
	// User userReceive = userService.findUserById(userIdReceive);
	// Item item = itemService.getItemById(itemId);
	//
	// if (userVote == null) throw new UserNotFoundException(userIdVote);
	// if (userReceive == null) throw new UserNotFoundException(userIdReceive);
	// if (userVote.equals(userReceive)) throw new
	// InvalidSelfVoteException(userVote.getUserId());
	// if (item == null) throw new ItemNotFoundException(itemId);
	//
	// // Vote value
	// int voteValue;
	// if (voteType == VOTE_POSITIVE) voteValue = Math.round(VOTE_BASE *
	// reability);
	// else voteValue = Math.round(-VOTE_BASE * reability);
	//
	// // Check if user who receives the vote already voted to the other one
	// Vote firstVote = getVote(userReceive, userVote, item);
	// Vote vote;
	//
	// // Check fake new votes with different users for one item
	// for (Vote v : getVotesForItem(item)) validateUsersForVote(userVote,
	// userReceive, v);
	//
	// // TODO contemplar que el voto negativo cuente siempre
	// // TODO Not sure if the state of the vote is necessary...
	//
	// if (firstVote == null) {
	// // Users with >75 value can give bad vote without "response"
	// if (voteType == VOTE_NEGATIVE && userVote.getUserValue() > 75) {
	// vote = new Vote(userVote, userReceive, item, voteType, VOTE_ACTIVE,
	// voteValue, voteMessage);
	// updateUserWithVote(userReceive, voteValue);
	// }
	// else // If vote is positive or user doesn't have "value", mark it as
	// inactive
	// vote = new Vote(userVote, userReceive, item, voteType, VOTE_INACTIVE,
	// voteValue, voteMessage);
	// } else {
	// // Check if the existing vote is between the current users, otherwise
	// throw an error
	// validateUsersForVote(userVote, userReceive, firstVote);
	//
	// // Increase value for the user who receives the vote
	// vote = new Vote(userVote, userReceive, item, voteType, VOTE_ACTIVE,
	// voteValue, voteMessage);
	// updateUserWithVote(userReceive, voteValue);
	//
	// // Active the first vote and update it if it is inactive
	// if (!firstVote.getVoteValid()) {
	// firstVote.setVoteValid(VOTE_ACTIVE);
	// voteRepository.save(firstVote);
	//
	// // Increase value for the user who get voted earlier
	// updateUserWithVote(userVote, firstVote.getVoteValue()); // this was
	// outside, but don't sure where should be...
	// }
	// }
	//
	// return voteRepository.save(vote);
	// }

	@Override
	public Vote addVote(Long userIdVote, Long userIdReceive, Long itemId, int voteType, float reability,
			String voteMessage) throws UserNotFoundException, ItemNotFoundException, InvalidVoteUsersException,
			InvalidSelfVoteException, InvalidVoteException, VoteDuplicateException {

		User userVote = userService.findUserById(userIdVote);
		User userReceive = userService.findUserById(userIdReceive);
		Item item = itemService.getItemById(itemId);
		return addVote(userVote, userReceive, item, voteType, reability, voteMessage);

	}

	@Override
	public Vote addVote(User userVote, Long userIdReceive, Long itemId, int voteType, float reability,
			String voteMessage) throws UserNotFoundException, ItemNotFoundException, InvalidVoteUsersException,
			InvalidSelfVoteException, InvalidVoteException, VoteDuplicateException {

		User userReceive = userService.findUserById(userIdReceive);
		Item item = itemService.getItemById(itemId);
		return addVote(userVote, userReceive, item, voteType, reability, voteMessage);

	}

	@Override
	@Transactional
	public Vote addVote(User userVote, User userReceive, Item item, int voteType, float reability, String voteMessage)
			throws UserNotFoundException, ItemNotFoundException, InvalidVoteUsersException, InvalidSelfVoteException,
			InvalidVoteException, VoteDuplicateException {

		// Check if user tries to vote himself
		if (userVote.equals(userReceive))
			throw new InvalidSelfVoteException(userVote.getUserId());

		// Check if already voted
		Vote voteCheck = getVote(userVote, userReceive, item);
		if (voteCheck != null)
			throw new VoteDuplicateException(userVote.getUserId(), userReceive.getUserId(), item.getItemId());

		// Set vote value
		int voteValue;
		if (voteType == VOTE_POSITIVE)
			voteValue = Math.round(VOTE_BASE * reability);
		else if (voteType == VOTE_NEGATIVE)
			voteValue = Math.round(-VOTE_BASE * reability);
		else
			throw new InvalidVoteException(userVote.getUserId(), "Invalid vote type: " + voteType);

		// Check if user who receives the vote already voted to the other one
		Vote firstVote = getVote(userReceive, userVote, item);
		// This vote
		Vote vote;

		if (firstVote != null) {
			// Check if this vote it's legitimate from the first one (avoid "infinite first vote" problem)
			validateUsersForVote(userVote, userReceive, firstVote);
			
			if (firstVote.getVoteState()) {
				// Set new vote
				vote = new Vote(userVote, userReceive, item, voteType, VOTE_STATE_ACTIVE, VOTE_NOT_QUEUED, voteValue,
						voteMessage);
				firstVote.setVoteQueue(VOTE_NOT_QUEUED);
				firstVote = voteRepository.save(firstVote);
				updateUserWithVote(userReceive, voteValue);
			} else {
				// Set new vote
				vote = new Vote(userVote, userReceive, item, voteType, VOTE_STATE_ACTIVE, VOTE_NOT_QUEUED, voteValue,
						voteMessage);
				updateUserWithVote(userReceive, voteValue);
				// Update previous vote
				firstVote.setVoteState(VOTE_STATE_ACTIVE);
				firstVote.setVoteQueue(VOTE_NOT_QUEUED);
				firstVote = voteRepository.save(firstVote);
				updateUserWithVote(firstVote.getUserReceive(), firstVote.getVoteValue());
			}
		} else {
			if (userVote.getUserValue() > REP_LIMIT_USER_VALUE) {
				// Set new vote
				vote = new Vote(userVote, userReceive, item, voteType, VOTE_STATE_ACTIVE, VOTE_QUEUED, voteValue,
						voteMessage);
				updateUserWithVote(userReceive, voteValue);
			} else {
				vote = new Vote(userVote, userReceive, item, voteType, VOTE_STATE_INACTIVE, VOTE_QUEUED, voteValue,
						voteMessage);
			}
		}

		return voteRepository.save(vote);

	}

	private void updateUserWithVote(User user, int voteValue) {
		int userValue = user.getUserValue() + voteValue;
		if (userValue > 100)
			userValue = 100;
		else if (userValue < 0)
			userValue = 0;
		user.setUserValue(userValue);
		userService.updateUser(user);
	}

	/**
	 * Check if for an existing vote, another user is trying to vote to another
	 * 
	 * @param user1
	 * @param user2
	 * @param vote
	 * @throws InvalidVoteUsersException
	 */
	private void validateUsersForVote(User user1, User user2, Vote vote) throws InvalidVoteUsersException {

		if ((!vote.getUserVote().equals(user1) && vote.getUserReceive().equals(user2))
				|| (!vote.getUserVote().equals(user2) && vote.getUserReceive().equals(user1)))
			throw new InvalidVoteUsersException(user1.getUserId(), user2.getUserId(), vote.getVoteId());

	}

	@Override
	public int getNumberVotesPositive(Long userId) {
		return voteRepository.findNumberVotesPositive(userId);
	}

	@Override
	public int getNumberVotesNegative(Long userId) {
		return voteRepository.findNumberVotesNegative(userId);
	}

	@Override
	public int getNumberVotesQueued(Long userId) {
		return voteRepository.findNumberVotesQueued(userId);
	}

	@Override
	public Page<Vote> getVotesPositive(Long userId, int page, int size) {
		PageRequest request = new PageRequest(page, size);
		return voteRepository.findVotesPositive(userId, request);
	}

	@Override
	public Page<Vote> getVotesNegative(Long userId, int page, int size) {
		PageRequest request = new PageRequest(page, size);
		return voteRepository.findVotesNegative(userId, request);
	}

	@Override
	public Page<Vote> getVotesQueued(Long userId, int page, int size) {
		PageRequest request = new PageRequest(page, size);
		return voteRepository.findVotesQueued(userId, request);
	}
}
