package es.elovendo.model.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("messageThreadRepository")
public interface MessageThreadRepository extends PagingAndSortingRepository<MessageThread, Long>{
	
	// FAQ: Acordarse del lenguaje HQL ¬¬'....
	@Query("SELECT m FROM MessageThread m WHERE (m.participant1.userId = :user1 AND m.participant2.userId = :user2) "
			+ "OR (m.participant1.userId = :user2 AND m.participant2.userId = :user1)")
	MessageThread findMessageThreadByUsers(@Param("user1") Long user1, @Param("user2") Long user2);
	
	@Query("SELECT m FROM MessageThread m WHERE m.participant1.userId = :user OR m.participant2.userId = :user")
	Page<MessageThread> findMessageThreadsByUser(@Param("user") Long user, Pageable pageable);

}
