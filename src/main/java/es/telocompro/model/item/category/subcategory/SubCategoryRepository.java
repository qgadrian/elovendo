package es.telocompro.model.item.category.subcategory;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */
@Repository("subCategoryRepository")
public interface SubCategoryRepository extends PagingAndSortingRepository<SubCategory, Long>  {
	
	@Query("SELECT s FROM SubCategory s WHERE s.category.categoryId = :categoryId")
	Iterable<SubCategory> findAllSubCategoriesByCategoryId(@Param("categoryId") long categoryId);
	
	@Query("SELECT s FROM SubCategory s WHERE s.category.categoryName = :categoryName")
	Iterable<SubCategory> findAllSubCategoriesByCategoryName(@Param("categoryName") String categoryName);
}