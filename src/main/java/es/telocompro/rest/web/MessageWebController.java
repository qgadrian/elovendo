package es.telocompro.rest.web;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.telocompro.model.item.Item;
import es.telocompro.model.message.Message;
import es.telocompro.model.message.MessageState;
import es.telocompro.model.message.MessageThread;
import es.telocompro.model.user.User;
import es.telocompro.rest.controller.exception.InvalidMessageThreadException;
import es.telocompro.rest.controller.exception.MessageThreadAlreadyExistsException;
import es.telocompro.rest.controller.exception.MessageThreadNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;
import es.telocompro.service.message.MessageService;
import es.telocompro.util.IOUtil;
import es.telocompro.util.PageWrapper;

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
		try {
			conversations = IteratorUtils.toList(messageService.getMessageThreads(user).iterator());
		} catch (NullPointerException e) {}

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
		List<Message> conversation = pageWrapper.getContent();		

		model.addAttribute("conversation", conversation);
		
		model.addAttribute("messageThreadId", messageThreadId);
		
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
			messageObject.put("date", message.getMessageDate());
			
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
					MessageThreadNotFoundException {
		
		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
		long remoteIpAddress = IOUtil.Dot2LongIP(request.getRemoteAddr());
		
		//  message thread has priority over pointing to a receiver username 
		if (!messageThreadId.equals("")) {
			long mThreadId = Long.valueOf(messageThreadId);
			messageService.sendMessage(user, mThreadId, messageText, remoteIpAddress);
		}
		else
			messageService.sendMessage(user, receiver, messageText, remoteIpAddress);

//		return "elovendo/pricing/points";
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
