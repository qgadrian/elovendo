package es.telocompro.model.item.category;

import es.telocompro.model.item.category.subcategory.SubCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */

@Repository("categoryRepository")
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    // FAQ: El nombre de la tabla con mayúscula, si no da error
    @Query("SELECT c FROM Category c ORDER BY categoryid")
    Iterable<Category> findAllOrderByCategoryId();

    @Query("SELECT s FROM SubCategory s WHERE s.category.categoryName = :categoryName ORDER BY subcategoryid")
    Iterable<SubCategory> findAllSubCategoryByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT c FROM Category c WHERE categoryname = :categoryName")
    Category findCategoryByName(@Param("categoryName") String categoryName);

    @Query("SELECT s FROM SubCategory s WHERE subcategoryname = :subCategoryName")
    SubCategory findSubCategoryByName(@Param("subCategoryName") String subCategoryName);

}
