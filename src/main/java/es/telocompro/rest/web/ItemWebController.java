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
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.rest.controller.exception.ItemNotFoundException;
import es.telocompro.service.item.ItemService;
import es.telocompro.service.item.category.CategoryService;
import es.telocompro.util.Constant;
import es.telocompro.util.PageWrapper;

@Controller
@RequestMapping(value = "/bazaar/")
public class ItemWebController {
	
	Logger logger = Logger.getLogger(ItemWebController.class);
	
	@Autowired
	private ItemService itemService;
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
	
	/**
	 * FIND BY TITLE
	 */
    @RequestMapping(value="search", method = RequestMethod.GET)
    public String itemListByTitleSearchPage(Model model,
    		@RequestParam(value="subcategory", required=false, defaultValue="") String subCategory,
    		@RequestParam("title") String title,
    		@RequestParam(value = "place", required = false, defaultValue="") String place,
    		@RequestParam(value = "min", required = false, defaultValue="0" ) int prizeMin,
    		@RequestParam(value = "max", required = false, defaultValue="0" ) int prizeMax,
    		@RequestParam(value = "p", required = false, defaultValue="0") int page, 
    		@RequestParam(value = "s", required = false, defaultValue="5" ) int size) {
    	
//    	// Populate selectors
//    	@SuppressWarnings("unchecked")
//		List<Province> provinces = IteratorUtils.toList(provinceService.findAllProvinces().iterator());
//    	model.addAttribute("provinces", provinces);
//    	@SuppressWarnings("unchecked")
//		List<SubCategory> subCategories = IteratorUtils.toList(categoryService
//				.findAllSubCategoriesFromSubCategoryName(subCategory).iterator());
//    	model.addAttribute("subCategories", subCategories);
    	
    	model.addAttribute("featuredItems", itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, subCategory));
    	
//    	Page<Item> p = itemService.getItemByTitleAndSubCategory(title, subCategory, page, size);
//    	Page<Item> p = itemService.getItemByParams(title, subCategory, place, prizeMin, prizeMax, page, size);
    	
    	// FIXME: Broken search
    	Page<Item> p = itemService.getItemByParams2(title, subCategory, 0, null, prizeMin, prizeMax, page, size);

    	// Quick workaround for manage pagination with searches
    	PageWrapper<Item> pageWrapper;
    	if (subCategory != "") 
    		pageWrapper= new PageWrapper<Item>(p, "search?title=" + title + "&subcategory=" + subCategory);
    	else 
    		pageWrapper = new PageWrapper<Item>(p, "search?title=" + title);
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
    		@RequestParam(value = "dis", required = false, defaultValue="0") String distance,
    		@RequestParam(value = "lat", required = false, defaultValue="0") String latitude,
    		@RequestParam(value = "lng", required = false, defaultValue="0") String longitude,
    		@RequestParam(value = "min", required = false, defaultValue="0" ) int prizeMin,
    		@RequestParam(value = "max", required = false, defaultValue="0" ) int prizeMax,
    		@RequestParam(value = "p", required = false, defaultValue="0") int page, 
    		@RequestParam(value = "s", required = false, defaultValue="5" ) int size) {
		
//		double searchLat, searchLng;
//    	
//		// Search for a specific location
//		if (!place.equals("")) {
//			String googleUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
//			String googleAPI = "AIzaSyBselexTzagOc9K-E3UkdHQzHElRfDNo5w";
//			String paramAPI = "key=".concat(googleAPI);
//			String paramSensor = "sensor=false";
//			String paramQuery = "query=".concat(place);
//			String formedURL = googleUrl+paramAPI+"&"+paramSensor+"&"+paramQuery;
//			URL url;
//			try {
//				url = new URL(formedURL);
//				HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
//				// dump all the content
//				//	print_content(con);
//				if (con != null) {
//					try {
//						BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//						StringBuilder builder = new StringBuilder();
//						String input = "";
//						
//						while ((input = br.readLine()) != null) 
//							builder.append(input);
//						
//						JSONObject jsonResponse = (JSONObject) JSONValue.parse(builder.toString());
//						
//						System.out.println(jsonResponse.toJSONString());
//						
//						JSONArray results = (JSONArray) jsonResponse.get("results");
//						JSONObject resultsObject = (JSONObject) results.get(0);
//						JSONObject geometry = (JSONObject) resultsObject.get("geometry");
//						JSONObject location = (JSONObject) geometry.get("location");
//						searchLat = (double) location.get("lat");
//						searchLng = (double) location.get("lng");
//						
//						System.out.println("lat " + searchLat + " - lng " + searchLng);
//					} catch (IOException e) { e.printStackTrace(); }
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		
		model.addAttribute("featuredItems", itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, categoryName));
		
		String urlLocation = "category/" + categoryName;
		model.addAttribute("url", urlLocation);
		
		if (prizeMin != 0)
			model.addAttribute("prizeMin", prizeMin);
		if (prizeMax != 0)
			model.addAttribute("prizeMax", prizeMax);
    	
//    	Page<Item> p = itemService.getAllItemsByCategory(categoryName, prizeMin, prizeMax, page, size);
//		Page<Item> p = itemService.getItemByParams(title, categoryName, province, prizeMin, prizeMax, page, size);
		double dis = Double.parseDouble(distance);
    	float lat = Float.valueOf(latitude);
    	float lng = Float.valueOf(longitude);
    	float[] latLng = {lat , lng};
		Page<Item> p = itemService.getItemByParams2(title, categoryName, dis, latLng, prizeMin, prizeMax, page, size);
    	PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, categoryName);
    	List<Item> items = p.getContent();
    	
    	model.addAttribute("page", pageWrapper);
    	model.addAttribute("itemsList", items);
    	
        return "elovendo/item/list/item_list";
    }
	
	/**
	 * FIND BY SUBCATEGORY
	 */
    
//    @RequestMapping(value="{subcategoryname}", params = {"p","s"}, method = RequestMethod.GET)
//    public String itemListPage(Model model, 
//    		@PathVariable("subcategoryname") String subCategoryName,
//    		@RequestParam("p") int page, @RequestParam( "s" ) int size) {
//    	
////    	String pageString = request.getParameter("page");
////    	if ("previous".equals(pageString)) System.out.println("previous equaled");
////    	else System.out.println("previous not equaled at all cause is " + pageString);
//    	
//    	model.addAttribute("page", page);
//    	model.addAttribute("size", size);
//    	
//    	Page<Item> p = itemService.getAllItemsBySubCategory(subCategoryName, 0, 0, page, size);
//    	List<Item> items = p.getContent();
//    	
//    	model.addAttribute("pagePag", p);
//    	model.addAttribute("items", items);
//    	
//        return "elovendo/item/list/item_list";
//    }
	
    @RequestMapping(value="sub/{subcategoryname}", method = RequestMethod.GET)
    public String itemListPage(Model model, 
    		@PathVariable("subcategoryname") String subCategoryName,
    		@RequestParam(value = "title", required = false, defaultValue="") String title,
    		@RequestParam(value = "province", required = false, defaultValue="") String province,
    		@RequestParam(value = "dis", required = false, defaultValue="0") String distance,
    		@RequestParam(value = "lat", required = false, defaultValue="0") String latitude,
    		@RequestParam(value = "lng", required = false, defaultValue="0") String longitude,
    		@RequestParam(value = "min", required = false, defaultValue="0" ) int prizeMin,
    		@RequestParam(value = "max", required = false, defaultValue="0" ) int prizeMax,
    		@RequestParam(value = "p", required = false, defaultValue="0") int page, 
    		@RequestParam(value = "s", required = false, defaultValue="5" ) int size) {
    	
//    	String pageString = request.getParameter("page");
//    	if ("previous".equals(pageString)) System.out.println("previous equaled");
//    	else System.out.println("previous not equaled at all cause is " + pageString);
    	
//    	model.addAttribute("page", page);
//    	model.addAttribute("size", size);
    	
    	model.addAttribute("featuredItems", 
    			itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, subCategoryName));
    	
//    	Page<Item> p = itemService.getAllItemsBySubCategory(subCategoryName, 0, 0, page, size);
//    	Page<Item> p = itemService.getAllItemsBySubCategory(subCategoryName, province, prizeMin, prizeMax, page, size);
    	int dis = Integer.valueOf(distance);
    	float lat = Float.valueOf(latitude);
    	float lng = Float.valueOf(longitude);
    	float[] latLng = {lat , lng};
    	Page<Item> p = 
    			itemService.getItemByParams2(title, subCategoryName, dis, latLng, prizeMin, prizeMax, page, size);
    	PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, subCategoryName);
    	List<Item> items = p.getContent();
    	
    	@SuppressWarnings("unchecked")
		List<SubCategory> subCategories = IteratorUtils.toList(categoryService
				.findAllSubCategoriesFromSubCategoryName(subCategoryName).iterator());
    	model.addAttribute("subCategories", subCategories);
    	
//    	@SuppressWarnings("unchecked")
//		List<SubCategory> subCategories = IteratorUtils.toList(categoryService
//				.getAllSubCatByCategoryIdOrderBySubCatName(subCategoryName).iterator());
//    	model.addAttribute("subCategories", subCategories);
    	
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
    	
    	return "elovendo/item/itemView";
    }

}
