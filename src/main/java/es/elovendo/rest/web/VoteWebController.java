package es.elovendo.rest.web;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.elovendo.model.user.User;
import es.elovendo.model.vote.Vote;
import es.elovendo.service.item.ItemService;
import es.elovendo.service.item.category.CategoryService;
import es.elovendo.service.item.favorite.FavoriteService;
import es.elovendo.service.purchase.PurchaseService;
import es.elovendo.service.user.UserService;
import es.elovendo.service.vote.VoteService;
import es.elovendo.util.Constant;
import es.elovendo.util.PageWrapper;
import es.elovendo.util.SessionUserObtainer;

@Controller
@RequestMapping("/site/")
public class VoteWebController {
	
	Logger logger = Logger.getLogger(VoteWebController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private VoteService voteService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private PurchaseService purchaseService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private FavoriteService favoriteService;
	@Autowired
	private MessageSource messageSource;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "{userId}/vote/{type}", method = RequestMethod.GET)
	public @ResponseBody JSONObject getVotesPositive (@PathVariable long userId,
			@PathVariable String type,
			@RequestParam(value="p", defaultValue="0") int page, 
			@RequestParam(value="s", defaultValue="5") int size) {
		
		Page<Vote> votes = null;
		if (type.equalsIgnoreCase("positive")) votes = voteService.getVotesPositive(userId, page, size);
		else if (type.equalsIgnoreCase("negative")) votes = voteService.getVotesNegative(userId, page, size);
		else if (type.equalsIgnoreCase("pend")) votes = voteService.getVotesQueued(userId, page, size);
		else return null;
		
		String url = "/site/" + userId + "/vote/" + type;
		PageWrapper<Vote> pageWrapper = new PageWrapper<>(votes, url);
		
		JSONObject output = new JSONObject();
		JSONArray outputArray = new JSONArray();
		
		for (Vote vote : votes.getContent()) {
			JSONObject element = new JSONObject();
			element.put("type", vote.getVoteType());
			element.put("message", vote.getVoteMessage());
			element.put("user", vote.getUserVote().getLogin());
			element.put("avatar", vote.getUserVote().getAvatar200h());
			outputArray.add(element);
		}
		
		output.put("output", outputArray);
		
		return output;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "current/vote/{type}", method = RequestMethod.GET)
	public @ResponseBody JSONObject getSessionVotesPositive (@PathVariable String type,
			@RequestParam(value="p", defaultValue="0") int page, 
			@RequestParam(value="s", defaultValue="5") int size) {
		
		User user = SessionUserObtainer.getInstance().getSessionUser();
		
		Page<Vote> votes = null;
		if (type.equalsIgnoreCase(Constant.VOTE_POSITIVE_STRING)) 
			votes = voteService.getVotesPositive(user.getUserId(), page, size);
		else if (type.equalsIgnoreCase(Constant.VOTE_NEGATIVE_STRING)) 
			votes = voteService.getVotesNegative(user.getUserId(), page, size);
		else if (type.equalsIgnoreCase(Constant.VOTE_PENDING_STRING)) 
			votes = voteService.getVotesQueued(user.getUserId(), page, size);
		else return null;
		
		JSONObject output = new JSONObject();
		JSONArray outputArray = new JSONArray();
		JSONArray pageArray = new JSONArray();
		
		for (Vote vote : votes.getContent()) {
			JSONObject element = new JSONObject();
			element.put("type", vote.getVoteType());
			element.put("message", vote.getVoteMessage());
			element.put("user", vote.getUserVote().getLogin());
			element.put("avatar", vote.getUserVote().getAvatar200h());
			outputArray.add(element);
		}
		
		// Page
		JSONObject pageJSONObject = new JSONObject();
    	pageJSONObject.put("pageNumber", votes.getNumber());
    	pageJSONObject.put("pageElements", votes.getNumberOfElements());
    	pageJSONObject.put("totalPages", votes.getTotalPages());
    	pageJSONObject.put("totalElements",votes.getTotalElements());
    	pageArray.add(pageJSONObject);
		
		output.put("output", outputArray);
		output.put("page", pageArray);
		
		return output;
	}

}
