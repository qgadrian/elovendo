package es.elovendo.rest.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.elovendo.model.user.User;
import es.elovendo.rest.exception.InvalidMessageThreadException;
import es.elovendo.rest.exception.MessageTextTooLongException;
import es.elovendo.rest.exception.MessageThreadAlreadyExistsException;
import es.elovendo.rest.exception.MessageThreadNotFoundException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.service.exception.EmailNotFoundException;
import es.elovendo.service.message.MessageService;
import es.elovendo.service.offer.OfferService;
import es.elovendo.service.user.UserService;
import es.elovendo.util.IOUtil;
import es.elovendo.util.sessionHelper.SessionUserObtainer;
import es.elovendo.util.sessionHelper.exception.AnonymousUserAuthenticationException;

@Controller
@RequestMapping(value = "/elovendo/offer")
public class OfferWebController {
	
	Logger logger = Logger.getLogger(MessageWebController.class);

	@Autowired
	private MessageService messageService;
	@Autowired
	private OfferService offerService;
	@Autowired
	private UserService userService;

	/**
	 * Send a intern message to the user. ReceiverId or MessageThreadId can be
	 * null, but not both of them.
	 * 
	 * @param receiver
	 *            Can be empty. Receivers user Id.
	 * @param messageThreadId
	 *            Message thread, if the conversation previously exists. Can be
	 *            empty.
	 * @param messageText
	 *            Message text, required.
	 * @param model
	 * @param request
	 * @throws MessageThreadAlreadyExistsException
	 * @throws UserNotFoundException
	 * @throws InvalidMessageThreadException
	 * @throws MessageThreadNotFoundException
	 * @throws MessageTextTooLongException
	 * @throws AnonymousUserAuthenticationException
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public @ResponseBody void sendMessage(
			@RequestParam(value = "receiver", required = true, defaultValue = "") Long receiver,
			@RequestParam(value = "offer", required = true) int offer, Model model, HttpServletRequest request)
			throws AnonymousUserAuthenticationException, MessageThreadAlreadyExistsException, UserNotFoundException,
			MessageTextTooLongException {

		User user = SessionUserObtainer.getInstance().getSessionUser();

		long remoteIpAddress = IOUtil.Dot2LongIP(request.getRemoteAddr());

		offerService.sendOffer(user, receiver, offer, remoteIpAddress);
	}

	@RequestMapping(value = "/public/send", method = RequestMethod.POST)
	public @ResponseBody void sendPublicMessage(@RequestParam(value = "receiver", required = true) Long receiverId,
			@RequestParam(value = "sender", required = true) String senderName,
			@RequestParam(value = "email", required = true) String senderEmail,
			@RequestParam(value = "offer", required = true) int offer, 
			Model model, HttpServletRequest request) throws EmailNotFoundException {

		// long remoteIpAddress = IOUtil.Dot2LongIP(request.getRemoteAddr());

		String receiverEmail = userService.getUserEmailFromUserId(receiverId);
		
		logger.error("Received an public offer of " + offer );

//		MailSender mailSender = MailSender.getInstance();
//		mailSender.sendMail(senderName, senderEmail, receiverEmail, "Testing", offer);
	}

}
