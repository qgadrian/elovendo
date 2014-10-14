package es.elovendo.model.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository("messageRepository")
public interface MessageRepository extends PagingAndSortingRepository<Message, Long>{

	@Query("SELECT m FROM Message m WHERE messageThreadId = :messageThread ORDER BY messageDate")
	Page<Message> findMessagesByMessageThread(@Param("messageThread") Long messageThread, Pageable pageable);
	
	@Query("SELECT messageText FROM Message m WHERE messageThreadId = :messageThread ORDER BY messageDate DESC")
	Page<String> findLastMessageFromMessageThread(@Param("messageThread") Long messageThread, Pageable pageable);
	
}
