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
    public Iterable<Category> getAllCategoriesOrderByCategoryId();

    /**
     * Return all subCategories from a given category
     * @param categoryName
     * @return Categories
     */
    public Iterable<SubCategory> getAllSubCatByCategoryIdOrderBySubCatId(String categoryName);
    
    /**
     * Return all subCategories from a given category
     * @param categoryName
     * @return Categories
     */
    public Iterable<SubCategory> getAllSubCatByCategoryId(long categoryId);
    
    public List<SubCategory> getAllSubCategoriesFromSubCategoryName(String subCategoryName);
    
    public SubCategory getSubCategoryBySubCategoryId(long subCategoryId);
    
    /**
     * Returns subCategory searching by its name
     * @param subCategoryName
     * @return SubCategory
     */
    public SubCategory getSubCategoryByName(String subCategoryName);
    
    public Iterable<Category> getAllCategories();
    public Iterable<SubCategory> getAllSubCategories();
    
    public Iterable<SubCategory> getAllSubCategoriesFromCategoryName(String categoryName);
}
