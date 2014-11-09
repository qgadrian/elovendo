package es.elovendo.rest.web;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/site/police")
public class PoliceWebController {
	
	Logger logger = Logger.getLogger(PoliceWebController.class);

	@RequestMapping(value = "/denounce/item/{itemId}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public @ResponseBody void receiveItemDenounce(@PathVariable long itemId,
			@RequestParam(value="reason", required=true) String reason) {
		
		logger.warn("Received a denounce for item " + itemId + ", reason: " + reason);
		
	}
	
	@RequestMapping(value = "/denounce/user/{userId}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public @ResponseBody void receiveUserDenounce(@PathVariable long userId,
			@RequestParam(value="reason", required=true) String reason) {
		
		logger.warn("Received a denounce for user " + userId + ", reason: " + reason);
		
	}
}
