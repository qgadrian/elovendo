package es.telocompro.rest.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.rest.exception.CategoriesNotFoundException;
import es.telocompro.service.item.category.CategoryService;
import es.telocompro.util.Constant;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */

@Controller
@RequestMapping(value = Constant.MOBILE_API_URL_PREFIX_V1 + "/bazaar/")  
public class MobileCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Get category list
     */
    @RequestMapping(value="categories", method = RequestMethod.GET)
    public @ResponseBody Iterable<Category> getCategories() {
        return categoryService.getAllCategoriesOrderByCategoryId();
    }

    /**
     * Get all subCategories from a category
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "{category}", method = RequestMethod.GET)
    public @ResponseBody JSONArray getSubCategories(@PathVariable("category") String categoryName) 
    throws CategoriesNotFoundException {
        Iterable<SubCategory> subCategories =
        		categoryService.getAllSubCatByCategoryIdOrderBySubCatId(categoryName);
        
        if (subCategories == null) {
        	throw new CategoriesNotFoundException(categoryName);
        }
        
    	JSONArray jsonArray = new JSONArray();
    	for (SubCategory subCategory : subCategories) {
    		JSONObject object = new JSONObject();
    		object.put("subCategoryName", subCategory.getSubCategoryName());
    		object.put("subCategoryId", subCategory.getId());
    		object.put("categoryName", subCategory.getCategory().getCategoryName());
    		object.put("categoryId", subCategory.getCategory().getCategoryId());
    		jsonArray.add(object);
    	}
        
        return jsonArray;
    }
    
//    @ExceptionHandler(CategoriesNotFoundException.class)
//    protected @ResponseBody ResponseEntity<RestApiError> handleNoteNotFoundException(
//    		CategoriesNotFoundException categoriesNotFoundException, HttpServletRequest request, 
//    		HttpServletResponse response) {  
//        RestApiError restApiError = new RestApiError(HttpStatus.NOT_FOUND, 
//        		categoriesNotFoundException.getMessage(), categoriesNotFoundException.getMessage(), 
//        		this.getInfoUrl());  
//        return CategoryControllerHelper.createAndSendResponse(restApiError);
//    }
//    
//    private String getInfoUrl(){  
//        return "http://localhost.com/api/support/"; //+ code.ordinal();  
//    } 

}
