package es.elovendo.model.vote;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import es.elovendo.model.user.User;


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
	
	@Query("SELECT COUNT(v) FROM Vote v WHERE voteType = 1 AND v.userReceive.userId = :userId")
	int findNumberVotesPositive(@Param("userId") Long userId);

	@Query("SELECT COUNT(v) FROM Vote v WHERE voteType = 0 AND v.userReceive.userId = :userId)")
	int findNumberVotesNegative(@Param("userId") Long userId);
	
	@Query("SELECT COUNT(v) FROM Vote v WHERE v.userReceive.userId = :userId AND "
			+ "(voteState = FALSE OR voteId NOT IN "
			+ "(SELECT vo.voteId FROM Vote vo WHERE voteState = TRUE AND vo.userVote.userId = :userId))")
	int findNumberVotesQueued(@Param("userId") Long userId);
	
	@Query("SELECT v FROM Vote v WHERE voteType = 1 AND v.userReceive.userId = :userId")
	Page<Vote> findVotesPositive(@Param("userId") Long userId, Pageable pageable);

	@Query("SELECT v FROM Vote v WHERE voteType = 0 AND v.userReceive.userId = :userId)")
	Page<Vote> findVotesNegative(@Param("userId") Long userId, Pageable pageable);
	
	@Query("SELECT v FROM Vote v WHERE v.userReceive.userId = :userId AND "
			+ "(voteState = FALSE OR voteId NOT IN "
			+ "(SELECT vo.voteId FROM Vote vo WHERE voteState = TRUE AND vo.userVote.userId = :userId))")
	Page<Vote> findVotesQueued(@Param("userId") Long userId, Pageable pageable);
}
