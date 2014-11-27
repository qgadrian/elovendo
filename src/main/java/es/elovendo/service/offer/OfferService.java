package es.elovendo.service.offer;

import es.elovendo.model.message.Message;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.MessageThreadAlreadyExistsException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.rest.exception.UserSelfishOperationException;

public interface OfferService {

	public Message sendOffer(User sender, Long receiverId, int offer, long ipAddress) throws UserNotFoundException,
			MessageThreadAlreadyExistsException, UserSelfishOperationException;

}
