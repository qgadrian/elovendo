package es.telocompro.rest.controller;

import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.service.item.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Get category list
     */
    @RequestMapping(value="/categories", method = RequestMethod.GET)
    public Iterable<Category> getCategories() {
        return categoryService.findAllCategoriesOrderByCategoryId();
    }

    /**
     * Get all subcategories from a category
     */
    @RequestMapping(value = "/{category}", method = RequestMethod.GET)
    public Iterable<SubCategory> getSubCategories(@PathVariable("category") String categoryName) {
        return categoryService.findAllSubCatByCategoryIdOrdBySubCatId(categoryName);
    }

}
