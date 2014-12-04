package es.elovendo.service.offer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.elovendo.model.item.Item;
import es.elovendo.model.message.Message;
import es.elovendo.model.message.MessageRepository;
import es.elovendo.model.message.MessageThread;
import es.elovendo.model.message.MessageThreadRepository;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.ItemNotFoundException;
import es.elovendo.rest.exception.MessageThreadAlreadyExistsException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.rest.exception.UserSelfishOperationException;
import es.elovendo.service.item.ItemService;
import es.elovendo.service.message.MessageService;
import es.elovendo.service.user.UserService;

@Service("offerService")
public class OfferServiceImpl implements OfferService {

	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private MessageThreadRepository messageThreadRepository;

	@Override
	public Message sendOffer(User sender, Long receiverId, Long itemId, int offer, long ipAddress)
			throws UserNotFoundException, MessageThreadAlreadyExistsException, UserSelfishOperationException,
			ItemNotFoundException {

		User receiver = userService.findUserById(receiverId);
		if (sender.equals(receiver))
			throw new MessageThreadAlreadyExistsException(0);

		Item item = itemService.getItemById(itemId);

		System.out.println("#################################");
		System.out.println(sender.toString());
		System.out.println(receiver.toString());
		System.out.println("Offer: " + offer + " from IP: " + ipAddress);
		System.out.println("#################################");

		if (sender.equals(receiver))
			throw new UserSelfishOperationException("Users " + sender.getUserId() + " tried to offer himself");

		MessageThread messageThread = messageThreadRepository.findItemMessageThreadByUsers(sender.getUserId(),
				receiverId, itemId);

		// If there is no thread between users, create a new one
		if (messageThread == null)
			messageThread = messageService.createMessageThread(sender, receiver, item);

		Message message = messageRepository.save(new Message(messageThread, sender, null, offer, ipAddress));

		messageService.createMessageState(sender, receiver, message, messageThread);

		return message;
	}

}
