package es.elovendo.rest.web;

import java.security.Principal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.elovendo.model.user.User;
import es.elovendo.rest.exception.ItemNotFoundException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.service.admin.report.item.ItemReportService;
import es.elovendo.service.admin.report.user.UserReportService;
import es.elovendo.service.user.UserService;
import es.elovendo.util.SessionUserObtainer;

@Controller
@RequestMapping(value = "/site/police")
public class PoliceWebController {
	
	@Autowired
	private ItemReportService itemReportService;
	@Autowired
	private UserReportService userReportService;
	@Autowired
	private UserService userService;
	
	Logger logger = Logger.getLogger(PoliceWebController.class);

	@RequestMapping(value = "/denounce/item/{itemId}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public @ResponseBody void receiveItemDenounce(Principal principal,
			@PathVariable long itemId,
			@RequestParam(value="reason", required=true) String reason) throws UserNotFoundException, ItemNotFoundException {
		
		logger.warn("Received a denounce for item " + itemId + ", reason: " + reason);
		
		SessionUserObtainer obtainer = SessionUserObtainer.getInstance();
		User user = obtainer.getSessionUser();
		
		itemReportService.createItemReport(itemId, user, reason);
		
	}
	
	@RequestMapping(value = "/denounce/user/{userId}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public @ResponseBody void receiveUserDenounce(@PathVariable long userId,
			@RequestParam(value="reason", required=true) String reason) throws UserNotFoundException {
		
		logger.warn("Received a denounce for user " + userId + ", reason: " + reason);
				
		SessionUserObtainer obtainer = SessionUserObtainer.getInstance();
		User user = obtainer.getSessionUser();
		
		userReportService.createUserReport(userId, user, reason);
	}
}
