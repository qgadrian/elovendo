package es.elovendo.service.message;

import org.springframework.data.domain.Page;

import es.elovendo.model.message.Message;
import es.elovendo.model.message.MessageState;
import es.elovendo.model.message.MessageThread;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.InvalidMessageThreadException;
import es.elovendo.rest.exception.MessageTextTooLongException;
import es.elovendo.rest.exception.MessageThreadAlreadyExistsException;
import es.elovendo.rest.exception.MessageThreadNotFoundException;
import es.elovendo.rest.exception.UserNotFoundException;

public interface MessageService {
	
	public MessageThread createMessageThread(User sender, User receiver) 
			throws MessageThreadAlreadyExistsException, UserNotFoundException;
	
	/**
	 * Create a message state for a send message. Sender will be set as "read", and receiver as "not read" 
	 * @param sender
	 * @param receiver
	 * @param message
	 * @param messageThread
	 */
	public void createMessageState(User sender, User receiver, Message message, MessageThread messageThread);
	
	public Message sendMessage(User sender, User receiver, MessageThread messageThread,
			String messageText, long ipAddress) throws MessageThreadAlreadyExistsException, UserNotFoundException;
	
	public Message sendMessage(User sender, User receiver, String messageText, long ipAddress) 
			throws MessageThreadAlreadyExistsException, UserNotFoundException;

	@Deprecated
	public Message sendMessage(User sender, String receiver, String messageText, long ipAddress) 
			throws MessageThreadAlreadyExistsException, UserNotFoundException, MessageTextTooLongException;
	
	public Message sendMessage(User sender, Long receiverId, String messageText, long ipAddress) 
			throws MessageThreadAlreadyExistsException, UserNotFoundException, MessageTextTooLongException;
	
	public Message sendMessage(User sender, long messageThreadId, String messageText, long ipAddress) 
			throws MessageThreadAlreadyExistsException, UserNotFoundException, 
			InvalidMessageThreadException, MessageThreadNotFoundException, MessageTextTooLongException;
	
	public Page<MessageThread> getMessageThreads(User user);
	
	public Page<Message> getMessageThreadMessages(long messageThreadId, User user);
	
	public Iterable<Message> getMessageThreadUnreadMessages(User user, long messageThreadId);
	
	public void deleteMessageThread(long messageThread);
	
	public int getUnreadMessages(User user);
	
	public int getUnreadMessages(long userId);
	
	public MessageState getMessageState(Long messageId, User user);

	public Message getLastMessage(long messageThreadId);
}
