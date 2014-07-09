package es.telocompro.model.vote;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import es.telocompro.model.user.User;


public interface VoteRepository extends PagingAndSortingRepository<Vote, Long> {

	Vote findByUserVote(User userVote);
	
	Vote findByUserReceive(User userReceive);
	
	@Query("SELECT v FROM Vote v WHERE v.userVote.userId = :userIdVote"
			+ " AND v.userReceive.userId = :userIdReceive"
			+ " AND v.item.itemId = :itemId AND voteState = false")
	Vote findByPendingState(@Param(value="userIdVote") Long userIdVote, 
			@Param(value="userIdReceive") Long userIdReceive, 
			@Param(value="itemId") Long itemId);
	
	@Query("SELECT v FROM Vote v WHERE v.userVote.userId = :userIdVote"
			+ " AND v.userReceive.userId = :userIdReceive"
			+ " AND v.item.itemId = :itemId")
	Vote findVote(@Param(value="userIdVote") Long userIdVote, 
			@Param(value="userIdReceive") Long userIdReceive, 
			@Param(value="itemId") Long itemId);

	@Query("SELECT v FROM Vote v WHERE v.item.itemId = :itemId")
	List<Vote> findByItem(@Param(value="itemId") Long itemId);

}
