package es.telocompro.service.item.category;

import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.CategoryRepository;
import es.telocompro.model.item.category.subcategory.SubCategory;

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

    @Override
    public Iterable<Category> findAllCategoriesOrderByCategoryId() {
        return categoryRepository.findAllOrderByCategoryId();
    }

    @Override
    public Iterable<SubCategory> getAllSubCatByCategoryIdOrderBySubCatId(String categoryName) {
        return categoryRepository.findAllSubCategoryByCategoryName(categoryName);
    }

	@Override
	public SubCategory getSubCategoryByName(String subCategoryName) {
		return categoryRepository.findSubCategoryByName(subCategoryName);
	}
}
