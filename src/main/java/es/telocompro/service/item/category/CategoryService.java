package es.telocompro.service.item.category;

import java.util.List;

import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.subcategory.SubCategory;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */
public interface CategoryService {

    /**
     * Finds all categories ordering them by its id
     * @return Categories
     */
    public Iterable<Category> findAllCategoriesOrderByCategoryId();

    /**
     * Return all subcategories from a given category
     * @param categoryName
     * @return Categories
     */
    public Iterable<SubCategory> getAllSubCatByCategoryIdOrderBySubCatId(String categoryName);
    
    public List<SubCategory> findAllSubCategoriesFromSubCategoryName(String subCategoryName);
    
    /**
     * Returns subCategory searching by its name
     * @param subCategoryName
     * @return SubCategory
     */
    public SubCategory getSubCategoryByName(String subCategoryName);
    
    public Iterable<Category> findAllCategories();
    public Iterable<SubCategory> findAllSubCategories();
}
