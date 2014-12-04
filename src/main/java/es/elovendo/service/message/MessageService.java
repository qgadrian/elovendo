package es.elovendo.service.message;

import org.springframework.data.domain.Page;

import es.elovendo.model.item.Item;
import es.elovendo.model.message.Message;
import es.elovendo.model.message.MessageState;
import es.elovendo.model.message.MessageThread;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.InvalidMessageThreadException;
import es.elovendo.rest.exception.ItemNotFoundException;
import es.elovendo.rest.exception.MessageTextTooLongException;
import es.elovendo.rest.exception.MessageThreadAlreadyExistsException;
import es.elovendo.rest.exception.MessageThreadNotFoundException;
import es.elovendo.rest.exception.UserNotFoundException;

public interface MessageService {

	public MessageThread createMessageThread(User sender, User receiver, Item item)
			throws MessageThreadAlreadyExistsException, UserNotFoundException;

	/**
	 * Create a message state for a send message. Sender will be set as "read",
	 * and receiver as "not read"
	 * 
	 * @param sender
	 * @param receiver
	 * @param message
	 * @param messageThread
	 */
	public void createMessageState(User sender, User receiver, Message message, MessageThread messageThread);

	/**
	 * Send a new message for an existing message thread
	 * @param sender
	 * @param receiver
	 * @param messageThread
	 * @param messageText
	 * @param ipAddress
	 * @return
	 * @throws MessageThreadAlreadyExistsException
	 * @throws UserNotFoundException
	 */
	public Message sendMessage(User sender, User receiver, MessageThread messageThread, String messageText,
			long ipAddress) throws MessageThreadAlreadyExistsException, UserNotFoundException;

	/**
	 * Sends a message from one user to another for the given item. Calls
	 * createMessageThread(User sender, User receiver, Item item) method if the
	 * message thread doesn't exists for the users and item
	 * @param sender
	 * @param receiver
	 * @param item
	 * @param messageText
	 * @param ipAddress
	 * @return
	 * @throws MessageThreadAlreadyExistsException
	 * @throws UserNotFoundException
	 */
	public Message sendMessage(User sender, User receiver, Item item, String messageText, long ipAddress)
			throws MessageThreadAlreadyExistsException, UserNotFoundException;

	// @Deprecated
	// public Message sendMessage(User sender, String receiver, String
	// messageText, long ipAddress)
	// throws MessageThreadAlreadyExistsException, UserNotFoundException,
	// MessageTextTooLongException;

	/**
	 * Sends a message from one user to another for the given item. Calls
	 * createMessageThread(User sender, User receiver, Item item) method if the
	 * message thread doesn't exists for the users and item
	 * 
	 * @param sender
	 * @param receiverId
	 * @param itemId
	 * @param messageText
	 * @param ipAddress
	 * @return
	 * @throws MessageThreadAlreadyExistsException
	 * @throws UserNotFoundException
	 * @throws MessageTextTooLongException
	 * @throws ItemNotFoundException
	 */
	public Message sendMessage(User sender, Long receiverId, Long itemId, String messageText, long ipAddress)
			throws MessageThreadAlreadyExistsException, UserNotFoundException, MessageTextTooLongException,
			ItemNotFoundException;

	/**
	 * Sends a message from the user to a already existing Message thread
	 * 
	 * @param sender
	 *            User who will send the message
	 * @param messageThreadId
	 *            Destination message thread for the message
	 * @param messageText
	 * @param ipAddress
	 * @return
	 * @throws MessageThreadAlreadyExistsException
	 * @throws UserNotFoundException
	 * @throws InvalidMessageThreadException
	 * @throws MessageThreadNotFoundException
	 * @throws MessageTextTooLongException
	 */
	public Message sendMessage(User sender, long messageThreadId, String messageText, long ipAddress)
			throws MessageThreadAlreadyExistsException, UserNotFoundException, InvalidMessageThreadException,
			MessageThreadNotFoundException, MessageTextTooLongException;

	public Page<MessageThread> getMessageThreads(User user);

	public Page<Message> getMessageThreadMessages(long messageThreadId, User user);

	public Iterable<Message> getMessageThreadUnreadMessages(User user, long messageThreadId);

	public void deleteMessageThread(long messageThread);

	public int getUnreadMessages(User user);

	public int getUnreadMessages(long userId);

	public MessageState getMessageState(Long messageId, User user);

	public Message getLastMessage(long messageThreadId);
}
