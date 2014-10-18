package es.elovendo.service.item.category;

import java.util.List;

import es.elovendo.model.item.category.Category;
import es.elovendo.model.item.category.CategoryRepository;
import es.elovendo.model.item.category.subcategory.SubCategory;
import es.elovendo.model.item.category.subcategory.SubCategoryRepository;
import es.elovendo.rest.exception.CategoryNotFoundException;
import es.elovendo.rest.exception.SubCategoryNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Override
	public Category getCategoryBySubCategoryName(String subCategoryName) throws CategoryNotFoundException {
		Category category = categoryRepository.findCategoryBySubCategoryName(subCategoryName);
		if (category == null) throw new CategoryNotFoundException(subCategoryName);
		return category;
	}

	@Override
	public Category getCategoryByCategoryName(String categoryName) throws CategoryNotFoundException {
		Category category = categoryRepository.findCategoryByName(categoryName);
		if (category == null) throw new CategoryNotFoundException(categoryName);
		return category;
	}

	@Override
    public Iterable<Category> getAllCategoriesOrderByCategoryId() {
        return categoryRepository.findAllOrderByCategoryId();
    }

    @Override
    public Iterable<SubCategory> getAllSubCatByCategoryIdOrderBySubCatId(String categoryName) {
        return categoryRepository.findAllSubCategoryByCategoryName(categoryName);
    }
    
    @Override
    public Iterable<SubCategory> getAllSubCatByCategoryId(long categoryId) {
    	return subCategoryRepository.findAllSubCategoriesByCategoryId(categoryId);
    }

	@Override
	public SubCategory getSubCategoryByName(String subCategoryName) {
		return categoryRepository.findSubCategoryByName(subCategoryName);
	}

	@Override
	public Iterable<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Iterable<SubCategory> getAllSubCategories() {
		return subCategoryRepository.findAll();
	}
	
	@Override
	public List<SubCategory> getAllSubCategoriesFromSubCategoryName(String subCategoryName) {
		return categoryRepository.findAllSubCategoriesFromSubCategoryName(subCategoryName);
	}

	@Override
	public SubCategory getSubCategoryBySubCategoryId(long subCategoryId) throws SubCategoryNotFoundException {
		SubCategory subCategory = subCategoryRepository.findOne(subCategoryId);
		if (subCategory == null) throw new SubCategoryNotFoundException(subCategoryId);
		return subCategory;
	}

	@Override
	public Iterable<SubCategory> getAllSubCategoriesFromCategoryName(
			String categoryName) {
		return subCategoryRepository.findAllSubCategoriesByCategoryName(categoryName);
	}
}
