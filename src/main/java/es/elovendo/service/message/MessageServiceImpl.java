package es.elovendo.service.message;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import es.elovendo.model.message.Message;
import es.elovendo.model.message.MessageRepository;
import es.elovendo.model.message.MessageState;
import es.elovendo.model.message.MessageStateKey;
import es.elovendo.model.message.MessageStateRepository;
import es.elovendo.model.message.MessageThread;
import es.elovendo.model.message.MessageThreadRepository;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.InvalidMessageThreadException;
import es.elovendo.rest.exception.MessageTextTooLongException;
import es.elovendo.rest.exception.MessageThreadAlreadyExistsException;
import es.elovendo.rest.exception.MessageThreadNotFoundException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.service.user.UserService;

@Service("messageService")
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	MessageThreadRepository messageThreadRepository;
	@Autowired
	MessageRepository messageRepository;
	@Autowired
	MessageStateRepository messageStateRepository;
	@Autowired
	UserService userService;

	@Override
	public MessageThread createMessageThread(User sender, User receiver) 
			throws MessageThreadAlreadyExistsException, UserNotFoundException {
//		User _receiver = userService.findUserByLogin(receiver);
		MessageThread messageThread =
				messageThreadRepository.findMessageThreadByUsers(sender.getUserId(), receiver.getUserId());
		
		if ( messageThread != null)
			throw new MessageThreadAlreadyExistsException(messageThread.getMessageThreadId());
		
		messageThread = new MessageThread(sender, receiver);
		return messageThreadRepository.save(messageThread);
	}
	
	@Override
	public Message sendMessage(User sender, User receiver,
			String messageText, long ipAddress) throws MessageThreadAlreadyExistsException, UserNotFoundException {
		
		if (sender.getUserId().equals(receiver.getUserId())) // Check if message its to itself
			throw new MessageThreadAlreadyExistsException(0);
		
		MessageThread messageThread = 
				messageThreadRepository.findMessageThreadByUsers(sender.getUserId(), receiver.getUserId());
		
		if (messageThread == null)
			messageThread = createMessageThread(sender, receiver);
		
		Message message = messageRepository.save(
				new Message(messageThread, sender, messageText, ipAddress));
		
		createMessageState(sender, receiver, message, messageThread);
		
		return message;
	}
	
	@Override
	public Message sendMessage(User sender, User receiver, MessageThread messageThread,
			String messageText, long ipAddress) throws MessageThreadAlreadyExistsException, UserNotFoundException {
		
		Message message = messageRepository.save(
				new Message(messageThread, sender, messageText, ipAddress));
		
		createMessageState(sender, receiver, message, messageThread);
		
		return message;
	}

	@Override
	@Deprecated
	public Message sendMessage(User sender, String _receiver, String messageText, long ipAddress)
			throws MessageThreadAlreadyExistsException, UserNotFoundException, MessageTextTooLongException {
		if (messageText.length() > 500)
			throw new MessageTextTooLongException(sender.getUserId());
		
		User receiver = userService.findUserByLogin(_receiver);

		return sendMessage(sender, receiver, messageText, ipAddress);
	}
	
	@Override
	public Message sendMessage(User sender, Long receiverId, String messageText, long ipAddress)
			throws MessageThreadAlreadyExistsException, UserNotFoundException, MessageTextTooLongException {
		if (messageText.length() > 500)
			throw new MessageTextTooLongException(sender.getUserId());
		
		User receiver = userService.findUserById(receiverId);

		return sendMessage(sender, receiver, messageText, ipAddress);
	}
	
	@Override
	public Message sendMessage(User sender, long messageThreadId,
			String messageText, long ipAddress)
			throws MessageThreadAlreadyExistsException, UserNotFoundException, 
			InvalidMessageThreadException, MessageThreadNotFoundException, MessageTextTooLongException {
		
		if (messageText.length() > 500)
			throw new MessageTextTooLongException(sender.getUserId());
		
		MessageThread messageThread = messageThreadRepository.findOne(messageThreadId);
		
		if (messageThread == null) throw new MessageThreadNotFoundException(messageThreadId);
		
		if (!messageThread.getParticipant1().getUserId().equals(sender.getUserId())
				&& !messageThread.getParticipant2().getUserId().equals(sender.getUserId()))
			throw new InvalidMessageThreadException(sender.getUserId());
		
		User receiver = sender.getUserId().equals(messageThread.getParticipant1().getUserId())? 
				messageThread.getParticipant2() : messageThread.getParticipant1();
				
		return sendMessage(sender, receiver, messageThread, messageText, ipAddress);
	} 

	@Override
	public Page<MessageThread> getMessageThreads(User user) {
		return messageThreadRepository.findMessageThreadsByUser(user.getUserId(), new PageRequest(0, 100));
	}

	@Override
	public Page<Message> getMessageThreadMessages(long messageThreadId, User user) {
		
		Page<Message> messages =
				messageRepository.findMessagesByMessageThread(messageThreadId, new PageRequest(0, 100));
		
		// Updating message states
		for (Message m : messages) {
			MessageState messageState = getMessageState(m.getMessageId(), user);
			if (!messageState.isReadState()) updateMessageState(messageState, true);
		}
		
		return messages;
	}
	
	@Override
	public Iterable<Message> getMessageThreadUnreadMessages(User user, long messageThreadId) {
		
		Iterable<MessageState> messageStates = 
				messageStateRepository.getMessagesUnread(user.getUserId(), messageThreadId);
		
		List<Long> messageIds = new ArrayList<Long>();
		for (MessageState state : messageStates) {
			messageIds.add(state.getMessageId());
			updateMessageState(state, true);
		}
		
		return messageRepository.findAll(messageIds);
	}

	@Override
	public void deleteMessageThread(long messageThread) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getUnreadMessages(User user) {
		return messageStateRepository.getNumberOfMessagesUnread(user.getUserId());
	}
	
	@Override
	public int getUnreadMessages(long userId) {
		return messageStateRepository.getNumberOfMessagesUnread(userId);
	}

	@Override
	public MessageState getMessageState(Long messageId, User user) {
		MessageStateKey key = new MessageStateKey(messageId, user.getUserId());
		return messageStateRepository.findOne(key);
	}

	private void createMessageState(User sender, User receiver, Message message, MessageThread messageThread) {
		
		MessageState messageState = new MessageState(message.getMessageId(), sender.getUserId(),
				messageThread.getMessageThreadId(), true);
		messageStateRepository.save(messageState);
		messageState = new MessageState(message.getMessageId(), receiver.getUserId(), 
				messageThread.getMessageThreadId(), false);
		messageStateRepository.save(messageState);
		
		messageStateRepository.save(messageState);
	}
	
	private void updateMessageState(MessageState messageState, boolean read) {
		messageState.setReadState(read);
		messageStateRepository.save(messageState);
	}

	@Override
	public String getLastMessage(long messageThreadId) {
		List<String> lastMessage =
				messageRepository.findLastMessageFromMessageThread(messageThreadId, new PageRequest(0, 1)).getContent();
		
		if (!lastMessage.isEmpty())
			return lastMessage.get(0);
		return "";
	}

}
