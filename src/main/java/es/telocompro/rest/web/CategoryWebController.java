package es.telocompro.rest.web;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.service.item.category.CategoryService;

@Controller
@RequestMapping(value = "/bazaar/")
public class CategoryWebController {
	
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

}
