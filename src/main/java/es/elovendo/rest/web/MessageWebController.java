package es.elovendo.rest.web;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import es.elovendo.model.message.Message;
import es.elovendo.model.message.MessageThread;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.InvalidMessageThreadException;
import es.elovendo.rest.exception.ItemNotFoundException;
import es.elovendo.rest.exception.MessageTextTooLongException;
import es.elovendo.rest.exception.MessageThreadAlreadyExistsException;
import es.elovendo.rest.exception.MessageThreadNotFoundException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.service.exception.EmailNotFoundException;
import es.elovendo.service.message.MessageService;
import es.elovendo.service.user.UserService;
import es.elovendo.util.IOUtil;
import es.elovendo.util.PageWrapper;
import es.elovendo.util.mail.MailSender;
import es.elovendo.util.sessionHelper.SessionUserObtainer;
import es.elovendo.util.sessionHelper.exception.AnonymousUserAuthenticationException;

@Controller
@RequestMapping(value = "/elovendo/messages")
public class MessageWebController {

	Logger logger = Logger.getLogger(MessageWebController.class);

	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;

	/* CONVERSATION LIST */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/inbox", method = RequestMethod.GET)
	public String getConversationList(Model model, HttpSession session) throws AnonymousUserAuthenticationException {

		User user = SessionUserObtainer.getInstance().getSessionUser();

		List<MessageThread> conversations = new ArrayList<>();

		String partner;

		try {
			conversations = IteratorUtils.toList(messageService.getMessageThreads(user).iterator());

			// Set for each conversation which user is speaking with current user
			// (avoid show conversation with himself)
			for (MessageThread thread : conversations) {
				// To set the "non-user" name in the conversations view
				String p1 = thread.getParticipant1().getLogin();
				String p2 = thread.getParticipant2().getLogin();
				partner = p1.equals(user.getLogin()) ? p2 : p1;
				
				Message lastMessage = messageService.getLastMessage(thread.getMessageThreadId());
				int unreadMessages = messageService.getUnreadMessages(user);

				thread.setPartner(partner);
				thread.setLastMessage(lastMessage);
				thread.setUnreadMessages(unreadMessages);
			}

		} catch (NullPointerException | NoSuchElementException e) {
			logger.debug("Exception at messages inbox");
		}

		model.addAttribute("conversations", conversations);

		return "elovendo/message/conversationsList";

	}

	/* SINGLE CONVERSATION VIEW */

	@RequestMapping(value = "/conversation/{messageThreadId}", method = RequestMethod.GET)
	public String getConversationView(Model model, @PathVariable("messageThreadId") String _messageThreadId)
			throws AnonymousUserAuthenticationException {

		long messageThreadId = Long.valueOf(_messageThreadId);

		User user = SessionUserObtainer.getInstance().getSessionUser();

		Page<Message> conversationPage = messageService.getMessageThreadMessages(messageThreadId, user);
		PageWrapper<Message> pageWrapper = new PageWrapper<Message>(conversationPage, "inbox?messageThreadId="
				+ messageThreadId);

		List<Message> conversation = Lists.reverse(pageWrapper.getContent());

		model.addAttribute("conversation", conversation);

		model.addAttribute("messageThreadId", messageThreadId);

		model.addAttribute("user", user);
		if (user.isSocialUser())
			model.addAttribute("userAvatar", user.getLargeSocialAvatar());
		else
			model.addAttribute("userAvatar", "http://www.elovendo.com/" + user.getAvatar200h());

		return "elovendo/message/conversationView";

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/conversation/{messageThreadId}/update", method = RequestMethod.GET)
	public @ResponseBody JSONObject getJSONUnreadMessages(@PathVariable("messageThreadId") String _messageThreadId,
			HttpServletResponse response) throws AnonymousUserAuthenticationException {

		long messageThreadId = Long.valueOf(_messageThreadId);

		User user = SessionUserObtainer.getInstance().getSessionUser();

		Iterable<Message> unreadMessages = messageService.getMessageThreadUnreadMessages(user, messageThreadId);

		JSONObject jsonResponse = new JSONObject();
		JSONArray messageArray = new JSONArray();

		for (Message message : unreadMessages) {
			JSONObject messageObject = new JSONObject();
			messageObject.put("userName", message.getSender().getLogin());
			messageObject.put("message", message.getMessageText());
			messageObject.put("date", message.getMessageDate().getTimeInMillis());
			
			if (message.getSender().isSocialUser())
				messageObject.put("pic", message.getSender().getLargeSocialAvatar());
			else 
				messageObject.put("pic", message.getSender().getAvatar200h());

			messageArray.add(messageObject);
		}

		jsonResponse.put("content", messageArray);

		response.setStatus(HttpServletResponse.SC_OK);

		return jsonResponse;

	}

	/**
	 * Send a intern message to the user. ReceiverId or MessageThreadId can be
	 * null, but not both of them.
	 * 
	 * @param receiverId
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
	 * @throws ItemNotFoundException 
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public @ResponseBody void sendMessage(
			@RequestParam(value = "receiver", required = false, defaultValue = "") Long receiverId,
			@RequestParam(value = "m", required = false, defaultValue = "") String messageThreadId,
			@RequestParam(value = "i", required = false, defaultValue = "0") long itemId,
			@RequestParam(value = "messageText", required = true) String messageText, 
			Model model, HttpServletRequest request) throws MessageThreadAlreadyExistsException, UserNotFoundException,
			InvalidMessageThreadException, MessageThreadNotFoundException, MessageTextTooLongException,
			AnonymousUserAuthenticationException, ItemNotFoundException {

		User user = SessionUserObtainer.getInstance().getSessionUser();

		long remoteIpAddress = IOUtil.Dot2LongIP(request.getRemoteAddr());

		// message thread has priority over pointing to a receiver userName
		if (!messageThreadId.equals("")) {
			long mThreadId = Long.valueOf(messageThreadId);
			messageService.sendMessage(user, mThreadId, messageText, remoteIpAddress);
		} else
			messageService.sendMessage(user, receiverId, itemId, messageText, remoteIpAddress);
		// messageService.sendMessage(user, receiver, messageText,
		// remoteIpAddress);
	}

	@RequestMapping(value = "/public/send", method = RequestMethod.POST)
	public @ResponseBody void sendPublicMessage(@RequestParam(value = "receiver", required = true) Long receiverId,
			@RequestParam(value = "sender", required = true) String senderName,
			@RequestParam(value = "email", required = true) String senderEmail,
			@RequestParam(value = "messageText", required = true) String messageText, Model model,
			HttpServletRequest request) throws EmailNotFoundException {

		// long remoteIpAddress = IOUtil.Dot2LongIP(request.getRemoteAddr());

		String receiverEmail = userService.getUserEmailFromUserId(receiverId);

		MailSender mailSender = MailSender.getInstance();
		mailSender.sendMail(senderName, senderEmail, receiverEmail, "Testing", messageText);
	}

	@RequestMapping(value = "/getUnread", method = RequestMethod.GET)
	public @ResponseBody int numberOfMessagesUnread() throws UserNotFoundException,
			AnonymousUserAuthenticationException {

		User user = SessionUserObtainer.getInstance().getSessionUser();

		if (user == null)
			return 0;

		return messageService.getUnreadMessages(user);
	}

}
