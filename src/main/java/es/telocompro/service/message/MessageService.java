package es.telocompro.service.message;

import org.springframework.data.domain.Page;

import es.telocompro.model.message.Message;
import es.telocompro.model.message.MessageState;
import es.telocompro.model.message.MessageThread;
import es.telocompro.model.user.User;
import es.telocompro.rest.controller.exception.InvalidMessageThreadException;
import es.telocompro.rest.controller.exception.MessageThreadAlreadyExistsException;
import es.telocompro.rest.controller.exception.MessageThreadNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;

public interface MessageService {
	
	public MessageThread createMessageThread(User sender, User receiver) 
			throws MessageThreadAlreadyExistsException, UserNotFoundException;
	
	public Message sendMessage(User sender, User receiver, MessageThread messageThread,
			String messageText, long ipAddress) throws MessageThreadAlreadyExistsException, UserNotFoundException;
	
	public Message sendMessage(User sender, User receiver, String messageText, long ipAddress) 
			throws MessageThreadAlreadyExistsException, UserNotFoundException;
	
	public Message sendMessage(User sender, String receiver, String messageText, long ipAddress) 
			throws MessageThreadAlreadyExistsException, UserNotFoundException;
	
	public Message sendMessage(User sender, long messageThreadId, String messageText, long ipAddress) 
			throws MessageThreadAlreadyExistsException, UserNotFoundException, 
			InvalidMessageThreadException, MessageThreadNotFoundException;
	
	public Page<MessageThread> getMessageThreads(User user);
	
	public Page<Message> getMessageThreadMessages(long messageThreadId, User user);
	
	public Iterable<Message> getMessageThreadUnreadMessages(User user, long messageThreadId);
	
	public void deleteMessageThread(long messageThread);
	
	public int getUnreadMessages(User user);
	
	public int getUnreadMessages(String userName) throws UserNotFoundException;
	
	public MessageState getMessageState(Long messageId, User user);

}
