package es.telocompro.rest.web;

import java.util.List;

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

import es.telocompro.model.item.Item;
import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.rest.controller.exception.ItemNotFoundException;
import es.telocompro.service.item.ItemService;
import es.telocompro.service.item.category.CategoryService;
import es.telocompro.service.user.UserService;
import es.telocompro.service.vote.VoteService;
import es.telocompro.util.Constant;
import es.telocompro.util.PageWrapper;

import static es.telocompro.util.Constant.S_ITEMS_PER_PAGE;

@Controller
@RequestMapping(value = "/bazaar/")
public class ItemWebController {
	
	Logger logger = Logger.getLogger(ItemWebController.class);
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private VoteService voteService;
	@Autowired
	private UserService userService;
	@Autowired
	private CategoryService categoryService;
	
	/**
	 * GET CATEGORIES
	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value="getcats", method=RequestMethod.GET)
//	public @ResponseBody JSONObject getCategoriesData() {
//		
//		Iterable<Category> categories = 
//				categoryService.findAllCategories();
//		JSONObject output = new JSONObject();
//		JSONArray outputArray = new JSONArray();
//		
//		for (Category cat : categories) {
//			JSONObject element = new JSONObject();
//			element.put("id", cat.getCategoryId());
//			element.put("name", cat.getCategoryName());
//			outputArray.add(element);
//		}
//		
//		output.put("output", outputArray);
//		
//		return output;
//	}
	
	/**
	 * GET SUBCATEGORIES
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="getsubs/{categoryId}", method=RequestMethod.GET)
	public @ResponseBody JSONObject getSubCategoriesFromCategory(@PathVariable long categoryId) {
		
		Iterable<SubCategory> subCategories = 
				categoryService.getAllSubCatByCategoryId(categoryId);
		JSONObject output = new JSONObject();
		JSONArray outputArray = new JSONArray();
		
		for (SubCategory sub : subCategories) {
			JSONObject element = new JSONObject();
			element.put("id", sub.getId());
			element.put("name", sub.getSubCategoryName());
			outputArray.add(element);
		}
		
		output.put("output", outputArray);
		
		return output;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="getsubs/s/{categoryName}", method=RequestMethod.GET)
	public @ResponseBody JSONObject getSubCategoriesFromCategoryName(@PathVariable String categoryName) {
		
		Iterable<SubCategory> subCategories = 
				categoryService.getAllSubCategoriesFromCategoryName(categoryName);
		JSONObject output = new JSONObject();
		JSONArray outputArray = new JSONArray();
		
		// Main "all" select option
		JSONObject element = new JSONObject();
		element.put("id", 0);
		// TODO: i18n
		element.put("name", "Todas las subcategorías");
		outputArray.add(element);
		
		for (SubCategory sub : subCategories) {
			element = new JSONObject();
			element.put("id", sub.getId());
			element.put("name", sub.getSubCategoryName());
			outputArray.add(element);
		}
		
		output.put("output", outputArray);
		
		return output;
	}
	
	/**
	 * FIND BY TITLE
	 */
    @RequestMapping(value="search", method = RequestMethod.GET)
    public String itemListByTitleSearchPage(Model model,
    		@RequestParam(value="subcategory", required=false, defaultValue="") String subCategory,
    		@RequestParam("title") String title,
    		@RequestParam(value = "dis", required = false, defaultValue="0") double dis,
    		@RequestParam(value = "lat", required = false, defaultValue="0") double lat,
    		@RequestParam(value = "lng", required = false, defaultValue="0") double lng,
    		@RequestParam(value = "min", required = false, defaultValue="0" ) int prizeMin,
    		@RequestParam(value = "max", required = false, defaultValue="0" ) int prizeMax,
    		@RequestParam(value = "p", required = false, defaultValue="0") int page, 
    		@RequestParam(value = "s", required = false, defaultValue=S_ITEMS_PER_PAGE ) int size) {
    	
    	model.addAttribute("featuredItems", itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, subCategory));
    	
    	@SuppressWarnings("unchecked")
		List<Category> categories = IteratorUtils.toList(
				categoryService.findAllCategories().iterator());
		model.addAttribute("categories", categories);
    	
    	// FIXME: Broken search
    	Page<Item> p = itemService.getItemsByParams(title, subCategory, dis, lat, lng, prizeMin, prizeMax, page, size);

    	// Quick workaround for manage pagination with searches
    	PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, 
    			fixPaginationUrl(subCategory, title, dis, lat, lng, prizeMin, prizeMax));
    	
    	List<Item> items = p.getContent();
    	
    	model.addAttribute("page", pageWrapper);
    	model.addAttribute("itemsList", items);
    	
        return "elovendo/item/list/item_list";
    }
	
	
	/**
	 * FIND BY CATEGORY
	 */
	
	@RequestMapping(value="category/{categoryName}", method = RequestMethod.GET)
    public String itemListByCategoryPage(Model model, 
    		@PathVariable("categoryName") String categoryName,
    		@RequestParam(value = "title", required = false, defaultValue="") String title,
    		@RequestParam(value = "dis", required = false, defaultValue="0") double dis,
    		@RequestParam(value = "lat", required = false, defaultValue="0") double lat,
    		@RequestParam(value = "lng", required = false, defaultValue="0") double lng,
    		@RequestParam(value = "min", required = false, defaultValue="0" ) int prizeMin,
    		@RequestParam(value = "max", required = false, defaultValue="0" ) int prizeMax,
    		@RequestParam(value = "p", required = false, defaultValue="0") int page, 
    		@RequestParam(value = "s", required = false, defaultValue=S_ITEMS_PER_PAGE ) int size) {
		
		model.addAttribute("featuredItems", 
				itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, categoryName));
		
		String urlLocation = "category/" + categoryName;
		model.addAttribute("url", urlLocation);
		
		@SuppressWarnings("unchecked")
		List<Category> categories = IteratorUtils.toList(
				categoryService.findAllCategories().iterator());
		model.addAttribute("categories", categories);
		
		if (prizeMin != 0)
			model.addAttribute("prizeMin", prizeMin);
		if (prizeMax != 0)
			model.addAttribute("prizeMax", prizeMax);

		Page<Item> p = 
				itemService.getItemsByParams(title, categoryName, dis, lat, lng, prizeMin, prizeMax, page, size);
		
		String fixedUrl = fixPaginationUrl(categoryName, title, dis, lat, lng, prizeMin, prizeMax);
		
		PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, fixedUrl);
		List<Item> items = p.getContent();

		model.addAttribute("page", pageWrapper);
		model.addAttribute("itemsList", items);

		return "elovendo/item/list/item_list";
	}

	/**
	 * FIND BY SUBCATEGORY
	 */
	
	@RequestMapping(value = "sub/{subcategoryname}", method = RequestMethod.GET)
	public String itemListPage(
			Model model,
			@PathVariable("subcategoryname") String subCategoryName,
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "dis", required = false, defaultValue = "0") double dis,
			@RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
			@RequestParam(value = "lng", required = false, defaultValue = "0") double lng,
			@RequestParam(value = "min", required = false, defaultValue = "0") int prizeMin,
			@RequestParam(value = "max", required = false, defaultValue = "0") int prizeMax,
			@RequestParam(value = "p", required = false, defaultValue = "0") int page,
			@RequestParam(value = "s", required = false, defaultValue = S_ITEMS_PER_PAGE) int size) {

		model.addAttribute("featuredItems", itemService.getRandomFeaturedItems(
				Constant.MAX_RANDOM_ITEMS, subCategoryName));

		Page<Item> p = itemService.getItemsByParams(title, subCategoryName,
				dis, lat, lng, prizeMin, prizeMax, page, size);
		PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p,
				fixPaginationUrl(subCategoryName, title, dis, lat, lng, prizeMin, prizeMax));
		List<Item> items = p.getContent();

		@SuppressWarnings("unchecked")
		List<Category> categories = IteratorUtils.toList(categoryService
				.findAllCategories().iterator());
		model.addAttribute("categories", categories);

		model.addAttribute("page", pageWrapper);
		model.addAttribute("itemsList", items);

		return "elovendo/item/list/item_list";
	}
    
    @RequestMapping(value=Constant.ALL_PATH, method = RequestMethod.GET)
    public String allItemListPage(Model model,
    		@RequestParam(value = "title", required = false, defaultValue="") String title,
    		@RequestParam(value = "dis", required = false, defaultValue="0") double dis,
    		@RequestParam(value = "lat", required = false, defaultValue="0") double lat,
    		@RequestParam(value = "lng", required = false, defaultValue="0") double lng,
    		@RequestParam(value = "min", required = false, defaultValue="0" ) int prizeMin,
    		@RequestParam(value = "max", required = false, defaultValue="0" ) int prizeMax,
    		@RequestParam(value = "p", required = false, defaultValue="0") int page, 
    		@RequestParam(value = "s", required = false, defaultValue=S_ITEMS_PER_PAGE ) int size) {
    	
    	
    	model.addAttribute("featuredItems", 
    			itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, null));
    	
    	Page<Item> p = 
    			itemService.getItemsByParams(title, null, dis, lat, lng, prizeMin, prizeMax, page, size);
    		
    	PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, 
    			fixPaginationUrl(Constant.ALL_PATH, title, dis, lat, lng, prizeMin, prizeMax));
    	
    	List<Item> items = p.getContent();
    	
    	@SuppressWarnings("unchecked")
		List<Category> categories = IteratorUtils.toList(
				categoryService.findAllCategories().iterator());
		model.addAttribute("categories", categories);

    	
    	model.addAttribute("page", pageWrapper);
    	model.addAttribute("itemsList", items);
    	
        return "elovendo/item/list/item_list";
    }
    
    /**
	 * FIND RANDOM
	 */
    private List<Item> getRandomItems(int maxItems, String subCategory) {
    	return itemService.getRandomFeaturedItems(maxItems, subCategory);
    }
    
    
    /**
	 * VIEW ITEM
	 */
    @RequestMapping(value="item/{itemId}", method = RequestMethod.GET)
    public String itemViewPage(Model model,
    		@PathVariable("itemId") long itemId) {
    	
    	Item item;
		try { item = itemService.getItemById(itemId); } 
		catch (ItemNotFoundException e) { return "elovendo/error/error"; }
		
    	model.addAttribute("item", item);
    	model.addAttribute("votesPositive", voteService.getVotesPositive(item.getUser().getUserId()));
    	model.addAttribute("votesNegative", voteService.getVotesNegative(item.getUser().getUserId()));
    	model.addAttribute("totalItems", itemService.getNumberUserItems(item.getUser().getUserId()));
    	
    	return "elovendo/item/itemView";
    }
    
    /** STUFF **/
    
	private String fixPaginationUrl(String categoryName, String title,
			double dis, double lat, double lng, int prizeMin, int prizeMax) {
		
		String tmp = categoryName+"?lat="+lat+"&lng="+lng;
		
		if (!title.equals("")) tmp = tmp.concat("&title="+title);
		if(dis > 0) tmp = tmp.concat("&dis="+dis);
		if(prizeMin > 0) tmp = tmp.concat("&min="+prizeMin);
		if(prizeMax > 0) tmp = tmp.concat("&max="+prizeMax);
		
		return tmp;
	}

}
