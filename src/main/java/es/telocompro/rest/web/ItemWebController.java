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
import es.telocompro.service.item.ItemService;
import es.telocompro.service.item.category.CategoryService;
import es.telocompro.service.province.ProvinceService;
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
    		@RequestParam("title") String title,
    		@RequestParam(value = "p", required = false, defaultValue="0") int page, 
    		@RequestParam(value = "s", required = false, defaultValue="5" ) int size) {
    	
    	Page<Item> p = itemService.getItemByTitle(title, page, size);
    	PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, title);
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
    		@RequestParam(value = "p", required = false, defaultValue="0") int page, 
    		@RequestParam(value = "s", required = false, defaultValue="5" ) int size,
    		@RequestParam(value = "min", required = false, defaultValue="0" ) int prizeMin,
    		@RequestParam(value = "max", required = false, defaultValue="0" ) int prizeMax) {
    	
//    	String pageString = request.getParameter("page");
//    	if ("previous".equals(pageString)) System.out.println("previous equaled");
//    	else System.out.println("previous not equaled at all cause is " + pageString);
    	
//    	model.addAttribute("page", page);
//    	model.addAttribute("size", size);
		
		String urlLocation = "category/" + categoryName;
		model.addAttribute("url", urlLocation);
		
		if (prizeMin != 0)
			model.addAttribute("prizeMin", prizeMin);
		if (prizeMax != 0)
			model.addAttribute("prizeMax", prizeMax);
    	
    	Page<Item> p = itemService.getAllItemsByCategory(categoryName, prizeMin, prizeMax, page, size);
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
    		@RequestParam(value = "p", required = false, defaultValue="0") int page, 
    		@RequestParam(value = "s", required = false, defaultValue="5" ) int size) {
    	
//    	String pageString = request.getParameter("page");
//    	if ("previous".equals(pageString)) System.out.println("previous equaled");
//    	else System.out.println("previous not equaled at all cause is " + pageString);
    	
//    	model.addAttribute("page", page);
//    	model.addAttribute("size", size);
    	
    	Page<Item> p = itemService.getAllItemsBySubCategory(subCategoryName, 0, 0, page, size);
    	PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, subCategoryName);
    	List<Item> items = p.getContent();
    	
    	@SuppressWarnings("unchecked")
		List<Province> provinces = IteratorUtils.toList(provinceService.findAllProvinces().iterator());
    	model.addAttribute("provinces", provinces);
    	
//    	@SuppressWarnings("unchecked")
//		List<SubCategory> subCategories = IteratorUtils.toList(categoryService
//				.getAllSubCatByCategoryIdOrderBySubCatName(subCategoryName).iterator());
//    	model.addAttribute("subCategories", subCategories);
    	
    	model.addAttribute("page", pageWrapper);
    	model.addAttribute("itemsList", items);
    	
        return "elovendo/item/list/item_list";
    }

}
