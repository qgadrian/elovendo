package es.elovendo.model.item.category;

import java.util.List;

import es.elovendo.model.item.category.subcategory.SubCategory;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by @adrian on 18/06/14. All rights reserved.
 */

@Repository("categoryRepository")
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

	@Query("SELECT c FROM Category c WHERE categoryId = "
			+ "(SELECT s.category.categoryId FROM SubCategory s WHERE subCategoryName = :subCategoryName)")
	Category findCategoryBySubCategoryName(@Param("subCategoryName") String subCategoryName);
	
	@Query("SELECT c FROM Category c WHERE categoryId = "
			+ "(SELECT s.category.categoryId FROM SubCategory s WHERE subCategoryId = :subCategoryId)")
	Category findCategoryBySubCategoryId(@Param("subCategoryId") long subCategoryId);

	// FAQ: El nombre de la tabla con mayúscula, si no da error
	@Query("SELECT c FROM Category c ORDER BY categoryid")
	Iterable<Category> findAllOrderByCategoryId();

	@Query("SELECT s FROM SubCategory s WHERE s.category.categoryName = :categoryName ORDER BY subcategoryid")
	Iterable<SubCategory> findAllSubCategoryByCategoryName(@Param("categoryName") String categoryName);

	@Query("SELECT c FROM Category c WHERE categoryname = :categoryName")
	Category findCategoryByName(@Param("categoryName") String categoryName);

	@Query("SELECT c FROM Category c WHERE categoryid = :categoryId")
	Category findCategoryByCategoryId(@Param("categoryId") long categoryId);

	@Query("SELECT s FROM SubCategory s WHERE subcategoryname = :subCategoryName")
	SubCategory findSubCategoryByName(@Param("subCategoryName") String subCategoryName);

	@Query("SELECT s FROM SubCategory s WHERE s.category.categoryName IN (SELECT u.category.categoryName "
			+ "FROM SubCategory u WHERE u.subCategoryName = :subCategoryName)")
	List<SubCategory> findAllSubCategoriesFromSubCategoryName(@Param("subCategoryName") String subCategoryName);
	
	@Query("SELECT s FROM SubCategory s WHERE s.category.categoryId IN (SELECT u.category.categoryId "
			+ "FROM SubCategory u WHERE u.id = :subCategoryId)")
	List<SubCategory> findAllSubCategoriesFromSubCategoryId(@Param("subCategoryId") long subCategoryId);

}
