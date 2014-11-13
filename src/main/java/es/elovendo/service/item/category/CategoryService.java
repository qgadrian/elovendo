package es.elovendo.service.item.category;

import java.util.List;

import es.elovendo.model.item.category.Category;
import es.elovendo.model.item.category.subcategory.SubCategory;
import es.elovendo.rest.exception.CategoryNotFoundException;
import es.elovendo.rest.exception.SubCategoryNotFoundException;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */
public interface CategoryService {
	
	public Category getCategoryBySubCategoryName(String subCategoryName) throws CategoryNotFoundException;
	
	public Category getCategoryBySubCategoryId(long subCategoryId) throws CategoryNotFoundException;
	
	public Category getCategoryByCategoryName(String categoryName) throws CategoryNotFoundException;
	
	public Category getCategoryByCategoryId(long categoryId) throws CategoryNotFoundException;

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
    
    public List<SubCategory> getAllSubCategoriesFromSubCategoryId(long subCategoryId);
    
    public SubCategory getSubCategoryBySubCategoryId(long subCategoryId) throws SubCategoryNotFoundException;
    
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
