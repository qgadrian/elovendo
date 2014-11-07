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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
import es.elovendo.rest.exception.MessageTextTooLongException;
import es.elovendo.rest.exception.MessageThreadAlreadyExistsException;
import es.elovendo.rest.exception.MessageThreadNotFoundException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.service.message.MessageService;
import es.elovendo.util.IOUtil;
import es.elovendo.util.PageWrapper;

@Controller
@RequestMapping(value = "/elovendo/messages")
public class MessageWebController {
	
	Logger logger = Logger.getLogger(MessageWebController.class);
	
	@Autowired
	private MessageService messageService;

	/* CONVERSATION LIST */
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/inbox", method = RequestMethod.GET) 
	public String getConversationList(Model model, HttpSession session) {
		
		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<MessageThread> conversations = new ArrayList<>();
		
		String partner;
		
		try {
			conversations = IteratorUtils.toList(messageService.getMessageThreads(user).iterator());
			
			// Set for each conversation which user is speaking with curent user (avoid show conversation as with himself)
			MessageThread messageThread = conversations.iterator().next();
			String p1 = messageThread.getParticipant1().getLogin();
			String p2 = messageThread.getParticipant2().getLogin();
			
			String lastMessage = messageService.getLastMessage(messageThread.getMessageThreadId());
			int unreadMessages = messageService.getUnreadMessages(user.getLogin());
			
			partner = p1.equals(user.getLogin()) ? p2 : p1;
			
			for (MessageThread thread : conversations) {
				thread.setPartner(partner);
				thread.setLastMessage(lastMessage);
				thread.setUnreadMessages(unreadMessages);
			}
			
		} catch (NullPointerException | UserNotFoundException | NoSuchElementException e) {
			logger.debug("Exception at messages inbox");
		}

		model.addAttribute("conversations", conversations);
		
		return "elovendo/message/messageList";
		
	}
	
	/* SINGLE CONVERSATION VIEW */
	
	@RequestMapping(value = "/conversation/{messageThreadId}", method = RequestMethod.GET) 
	public String getConversationView(Model model,
			@PathVariable("messageThreadId") String _messageThreadId) {
		
		long messageThreadId = Long.valueOf(_messageThreadId); 
		
		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Page<Message> conversationPage = messageService.getMessageThreadMessages(messageThreadId, user);
		PageWrapper<Message> pageWrapper = 
				new PageWrapper<Message>(conversationPage, "inbox?messageThreadId=" + messageThreadId);
		
		List<Message> conversation = Lists.reverse(pageWrapper.getContent());

		model.addAttribute("conversation", conversation);
		
		model.addAttribute("messageThreadId", messageThreadId);
		
		model.addAttribute("user", user);
		model.addAttribute("userAvatar", user.getAvatar200h());
		
		return "elovendo/message/conversationView";
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/conversation/{messageThreadId}/update", method = RequestMethod.GET) 
	public @ResponseBody JSONObject getJSONUnreadMessages(
			@PathVariable("messageThreadId") String _messageThreadId,
			HttpServletResponse response) {
		
		long messageThreadId = Long.valueOf(_messageThreadId); 
		
		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken)) {
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		
		Iterable<Message> unreadMessages = messageService.getMessageThreadUnreadMessages(user, messageThreadId);
		
		JSONObject jsonResponse = new JSONObject();
		JSONArray messageArray = new JSONArray();
		
		for (Message message : unreadMessages) {
			JSONObject messageObject = new JSONObject();
			messageObject.put("pic", message.getSender().getAvatar200h());
			messageObject.put("userName", message.getSender().getLogin());
			messageObject.put("message", message.getMessageText());
			messageObject.put("date", message.getMessageDate().getTimeInMillis());
			
			messageArray.add(messageObject);
		}

		jsonResponse.put("content", messageArray);
		
		response.setStatus(HttpServletResponse.SC_OK);
		
		return jsonResponse;
		
	}
	
	@RequestMapping(value = "/send", method = RequestMethod.POST)
//	public String itemListPage(@ModelAttribute(value = "receiver") String receiver,
	public @ResponseBody void itemListPage(
			@RequestParam(value = "receiver", required=false, defaultValue="") String receiver,
			@RequestParam(value = "m", required=false, defaultValue="") String messageThreadId,
			@RequestParam(value = "messageText", required=true) String messageText,
			Model model, HttpServletRequest request) 
					throws MessageThreadAlreadyExistsException, UserNotFoundException, InvalidMessageThreadException, 
					MessageThreadNotFoundException, MessageTextTooLongException {
		
		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
		long remoteIpAddress = IOUtil.Dot2LongIP(request.getRemoteAddr());
		
		//  message thread has priority over pointing to a receiver userName 
		if (!messageThreadId.equals("")) {
			long mThreadId = Long.valueOf(messageThreadId);
			messageService.sendMessage(user, mThreadId, messageText, remoteIpAddress);
		}
		else
			messageService.sendMessage(user, receiver, messageText, remoteIpAddress);
	}
	
	@RequestMapping(value="/getUnread", method = RequestMethod.GET)
	public @ResponseBody int numberOfMessagesUnread() throws UserNotFoundException {
		
		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (user == null) return 0;
		
		return messageService.getUnreadMessages(user);
	}

}
