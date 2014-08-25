package es.telocompro.model.item;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */

@Repository("itemRepository")
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {

	// TODO: Not using end date i this two down here
	//   @Query("SELECT i, i.user.login AS username, i.subCategory.subCategoryName AS subCategory,"
    //		+ " i.subCategory.category.categoryName FROM Item i WHERE i.title LIKE %:title%")
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title%")
    Page<Item> findByTitle(@Param("title") String title, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.subCategory.subCategoryName = :subCategoryName")
    Page<Item> findByTitleAndSubCategory(@Param("title") String title, 
    		@Param("subCategoryName") String subCategory, Pageable pageable);

    @Query("SELECT i, i.user.login AS username, i.subCategory.subCategoryName AS subCategory,"
    		+ " i.subCategory.category.categoryName FROM Item i WHERE i.user.login = :username")
    Page<Item> findByUserName(@Param("username") String userName, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.featured = 1 ORDER BY RAND()")
    List<Item> findRandomFeaturedItems(Pageable pageable);
    
    /**
     * Finds an item which has featured option enabled, searching for filter in category or subCategory.
     * Explicitly set a *number* of maximum random items to retrieve.  
     * @param pageable
     * @param filter Category or SubCategory name for query
     * @return *number* of random items.
     */
    @Query("SELECT DISTINCT i FROM Item i WHERE i.featured = 1 AND (i.subCategory.subCategoryName = :name "
    		+ "OR i.subCategory.category.categoryName = :name) ORDER BY RAND()")
    List<Item> findRandomFeaturedItemsByFilter(Pageable pageable, @Param("name") String filter);
    
    //TODO Find items caducated

    /* CATEGORY */
    
    /**
     * Find all items from a subCategory
     * @param subCategoryName SubCategory name
     * @param pageable
     * @return Items
     */
    @Query("SELECT i FROM Item i WHERE i.subCategory.category.categoryName = :categoryName AND "
    		+ "endDate > NOW() ORDER BY startdate")
    Page<Item> findItemsByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);
    
    /**
     * Find all items from a subCategory filtering by a minimum prize
     * @param subCategoryName SubCategory name
     * @param prizeMin Minimum prize
     * @param pageable
     * @return Items
     */
    @Query("SELECT i FROM Item i WHERE i.subCategory.category.categoryName = :categoryName AND "
    		+ "prize >= :prizeMin AND endDate > NOW() ORDER BY startdate")
    Page<Item> findItemsByCategoryName(@Param("categoryName") String categoryName, 
    		@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
    
    /**
     * Find all items from a subCategory filtering by a minimum and maximum prize
     * @param subCategoryName
     * @param prizeMin Minimum prize
     * @param prizeMax Maximum prize
     * @param pageable
     * @return
     */
    @Query("SELECT i FROM Item i WHERE i.subCategory.category.categoryName = :categoryName AND "
    		+ "prize >= :prizeMin AND prize <= :prizeMax AND endDate > NOW() ORDER BY startdate")
    Page<Item> findItemsByCategoryName(@Param("categoryName") String categoryName, 
    		@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);

    /* SUBCATEGORY */
    
    /**
     * Find all items from a subCategory
     * @param subCategoryName SubCategory name
     * @param pageable
     * @return Items
     */
    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND endDate > NOW() ORDER BY startdate")
    Page<Item> findItemsBySubCategoryName(@Param("subCategoryName") String subCategoryName, Pageable pageable);
    
    /**
     * Find all items from a subCategory filtering by a minimum prize
     * @param subCategoryName SubCategory name
     * @param prizeMin Minimum prize
     * @param pageable
     * @return Items
     */
    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND prize >= :prizeMin "
    		+ "AND endDate > NOW() ORDER BY startdate")
    Page<Item> findItemsBySubCategoryName(@Param("subCategoryName") String subCategoryName, 
    		@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
    
    /**
     * Find all items from a subCategory filtering by a minimum and maximum prize
     * @param subCategoryName
     * @param prizeMin Minimum prize
     * @param prizeMax Maximum prize
     * @param pageable
     * @return
     */
    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND prize >= :prizeMin "
    		+ "AND prize <= :prizeMax AND endDate > NOW() ORDER BY startdate")
    Page<Item> findItemsBySubCategoryName(@Param("subCategoryName") String subCategoryName, 
    		@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);

}
