package es.elovendo.service.offer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.elovendo.model.message.Message;
import es.elovendo.model.message.MessageRepository;
import es.elovendo.model.message.MessageThread;
import es.elovendo.model.message.MessageThreadRepository;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.MessageThreadAlreadyExistsException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.service.message.MessageService;
import es.elovendo.service.user.UserService;

@Service("offerService")
public class OfferServiceImpl implements OfferService {

	@Autowired
	private UserService userService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private MessageThreadRepository messageThreadRepository;

	@Override
	public Message sendOffer(User sender, Long receiverId, int offer, long ipAddress) throws UserNotFoundException,
			MessageThreadAlreadyExistsException {

		User receiver = userService.findUserById(receiverId);
		if (sender.equals(receiver))
			throw new MessageThreadAlreadyExistsException(0);
		
	System.out.println("#################################");
	System.out.println(sender.toString());
	System.out.println(receiver.toString());
	System.out.println("Offer: " + offer + " from IP: " + ipAddress);
	System.out.println("#################################");

		MessageThread messageThread = messageThreadRepository.findMessageThreadByUsers(sender.getUserId(),
				receiver.getUserId());

		if (messageThread == null)
			messageThread = messageService.createMessageThread(sender, receiver);

		Message message = messageRepository.save(new Message(messageThread, sender, null, offer, ipAddress));

		messageService.createMessageState(sender, receiver, message, messageThread);

		return message;
	}

}
