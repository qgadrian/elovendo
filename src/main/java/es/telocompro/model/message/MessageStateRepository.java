package es.telocompro.model.message;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface MessageStateRepository extends PagingAndSortingRepository<MessageState, MessageStateKey> {

	@Query("SELECT COUNT(m) FROM MessageState m WHERE readState = 0 AND userId = :userId")
	int getNumberOfMessagesUnread(@Param("userId") Long userId);
	
//	@Query("SELECT messageId FROM MessageState m WHERE readState = 0 AND userId = :userId AND "
//			+ "messageThreadId = :messageThreadId")
//	Iterable<Long> getMessagesUnread(@Param("userId") Long userId, 
//			@Param("messageThreadId") Long messageThreadId);
	
	@Query("SELECT m FROM MessageState m WHERE readState = 0 AND userId = :userId AND "
			+ "messageThreadId = :messageThreadId")
	Iterable<MessageState> getMessagesUnread(@Param("userId") Long userId, 
			@Param("messageThreadId") Long messageThreadId);
	
}
