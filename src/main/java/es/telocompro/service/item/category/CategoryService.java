package es.telocompro.service.item.category;

import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.subcategory.SubCategory;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */
public interface CategoryService {

    /**
     * Finds all categories ordering them by its id
     * @return
     */
    public Iterable<Category> findAllCategoriesOrderByCategoryId();

    /**
     * Return all subcategories from a given category
     * @param categoryName
     * @return
     */
    public Iterable<SubCategory> findAllSubCatByCategoryIdOrdBySubCatId(String categoryName);
}
