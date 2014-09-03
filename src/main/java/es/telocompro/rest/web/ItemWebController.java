package es.telocompro.rest.web;

import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.province.Province;
import es.telocompro.rest.controller.exception.ItemNotFoundException;
import es.telocompro.service.item.ItemService;
import es.telocompro.service.item.category.CategoryService;
import es.telocompro.service.province.ProvinceService;
import es.telocompro.util.Constant;
import es.telocompro.util.PageWrapper;

@Controller
@RequestMapping(value = "/bazaar/")
public class ItemWebController {
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private ProvinceService provinceService;
	@Autowired
	private CategoryService categoryService;
	
	/**
	 * FIND BY TITLE
	 */
    @RequestMapping(value="search", method = RequestMethod.GET)
    public String itemListByTitleSearchPage(Model model,
    		@RequestParam(value="subcategory", required=false, defaultValue="") String subCategory,
    		@RequestParam("title") String title,
    		@RequestParam(value = "province", required = false, defaultValue="") String province,
    		@RequestParam(value = "min", required = false, defaultValue="0" ) int prizeMin,
    		@RequestParam(value = "max", required = false, defaultValue="0" ) int prizeMax,
    		@RequestParam(value = "p", required = false, defaultValue="0") int page, 
    		@RequestParam(value = "s", required = false, defaultValue="5" ) int size) {
    	
    	// Populate selectors
    	@SuppressWarnings("unchecked")
		List<Province> provinces = IteratorUtils.toList(provinceService.findAllProvinces().iterator());
    	model.addAttribute("provinces", provinces);
    	@SuppressWarnings("unchecked")
		List<SubCategory> subCategories = IteratorUtils.toList(categoryService
				.findAllSubCategoriesFromSubCategoryName(subCategory).iterator());
    	model.addAttribute("subCategories", subCategories);
    	
    	model.addAttribute("featuredItems", itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, subCategory));
    	
//    	Page<Item> p = itemService.getItemByTitleAndSubCategory(title, subCategory, page, size);
    	Page<Item> p = itemService.getItemByParams(title, subCategory, province, prizeMin, prizeMax, page, size);

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
    	
    	@SuppressWarnings("unchecked")
		List<Province> provinces = IteratorUtils.toList(provinceService.findAllProvinces().iterator());
    	model.addAttribute("provinces", provinces);
    	@SuppressWarnings("unchecked")
		List<SubCategory> subCategories = IteratorUtils.toList(categoryService
				.getAllSubCatByCategoryIdOrderBySubCatId(categoryName).iterator());
    	model.addAttribute("subCategories", subCategories);
    	
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
		List<Province> provinces = IteratorUtils.toList(provinceService.findAllProvinces().iterator());
    	model.addAttribute("provinces", provinces);
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
