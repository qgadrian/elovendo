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
	
	/* USER */

    @Query("SELECT i, i.user.login AS username, i.subCategory.subCategoryName AS subCategory,"
    		+ " i.subCategory.category.categoryName FROM Item i WHERE i.user.login = :username")
    Page<Item> findByUserName(@Param("username") String userName, Pageable pageable);
    
    /*************************************************************************************************************/
    
    /* RANDOM */
    
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
    		+ "OR i.subCategory.category.categoryName = :name) AND endDate > NOW() ORDER BY RAND()")
    List<Item> findRandomFeaturedItemsByFilter(Pageable pageable, @Param("name") String filter);
    
    /*************************************************************************************************************/
    
    /* ALL */
    
    @Query("SELECT i FROM Item i WHERE prize >= :prizeMin AND endDate > NOW() ORDER BY startdate")
    Page<Item> findAllItemsMin(@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE prize <= :prizeMax AND endDate > NOW() ORDER BY startdate")
    Page<Item> findAllItemsMax(@Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE prize >= :prizeMin AND prize <= :prizeMax AND "
    		+ "endDate > NOW() ORDER BY startdate")
    Page<Item> findAllItemsMinMax(@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax,
    		Pageable pageable);
    
    //TODO Find items caducated
    
    /* TITLE */
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND endDate > NOW() ORDER BY startdate")
    Page<Item> findByTitle(@Param("title") String title, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND prize >= :prizeMin AND endDate > NOW()")
    Page<Item> findByTitleMin(@Param("title") String title, @Param("prizeMin") String prizeMin, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND prize <= :prizeMax AND endDate > NOW()")
    Page<Item> findByTitleMax(@Param("title") String title, @Param("prizeMax") String prizeMax, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND prize >= :prizeMin AND prize <= :prizeMax "
    		+ "AND endDate > NOW()")
    Page<Item> findByTitleMinMax(@Param("title") String title, @Param("prizeMin") String prizeMin,
    		@Param("prizeMax") String prizeMax, Pageable pageable);
    
    /*************************************************************************************************************/

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
    Page<Item> findItemsByCategoryNameMin(@Param("categoryName") String categoryName, 
    		@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.subCategory.category.categoryName = :categoryName AND "
    		+ "prize <= :prizeMax AND endDate > NOW() ORDER BY startdate")
    Page<Item> findItemsByCategoryNameMax(@Param("categoryName") String categoryName, 
    		@Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
    
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
    Page<Item> findItemsByCategoryNameMinMax(@Param("categoryName") String categoryName, 
    		@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
    
    /*************************************************************************************************************/

    /* SUBCATEGORY */
    
    /**
     * Find all items from a subCategory
     * @param subCategoryName SubCategory name
     * @param pageable
     * @return Items
     */
    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND endDate > NOW() ORDER BY startdate")
    Page<Item> findBySubCategoryName(@Param("subCategoryName") String subCategoryName, Pageable pageable);
    
    /**
     * Find all items from a subCategory filtering by a minimum prize
     * @param subCategoryName SubCategory name
     * @param prizeMin Minimum prize
     * @param pageable
     * @return Items
     */
    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND prize >= :prizeMin "
    		+ "AND endDate > NOW() ORDER BY startdate")
    Page<Item> findBySubCategoryNameMin(@Param("subCategoryName") String subCategoryName, 
    		@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND prize >= :prizeMax "
    		+ "AND endDate > NOW() ORDER BY startdate")
    Page<Item> findBySubCategoryNameMax(@Param("subCategoryName") String subCategoryName, 
    		@Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
    
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
    Page<Item> findBySubCategoryNameMinMax(@Param("subCategoryName") String subCategoryName, 
    		@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
    
    /*************************************************************************************************************/
    
    /* PARAMS */
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND (i.subCategory.subCategoryName = :subCategoryName "
    		+ "OR i.subCategory.category.categoryName = :subCategoryName) "
    		+ "AND i.province.provinceName = :province AND endDate > NOW() ORDER BY startdate")
    Page<Item> findByParams(@Param("title") String title, 
    		@Param("subCategoryName") String subCategory, @Param("province") String province, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND (i.subCategory.subCategoryName = :subCategoryName "
    		+ "OR i.subCategory.category.categoryName = :subCategoryName) "
    		+ "AND endDate > NOW() "
    		+ "AND (6371*acos(cos(radians(:lat))*cosRadLat*cos(radLng-radians(:lng))+sin(radians(:lat))*sinRadLat)) < :d "
    		+ "ORDER BY startdate")
    Page<Item> findByParams(@Param("title") String title, @Param("subCategoryName") String subCategory, 
    		@Param("lat") double latitude, @Param("lng") double longitude,
    		@Param("d") double distance, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.province.provinceName = :province "
    		+ "AND endDate > NOW() ORDER BY startdate")
    Page<Item> findByParamsWithProvince(@Param("title") String title, 
    		@Param("province") String province, Pageable pageable);

    /**/
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND endDate > NOW() "
    		+ "AND (6371*acos(cos(radians(:lat))*cosRadLat*cos(radLng-radians(:lng))+sin(radians(:lat))*sinRadLat)) < :d "
    		+ "ORDER BY startdate")
    Page<Item> findByParamsWithDistance(@Param("title") String title, 
    		@Param("lat") double latitude, @Param("lng") double longitude,
    		@Param("d") double distance, Pageable pageable);
    /**/
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND (i.subCategory.subCategoryName = :subCategoryName "
    		+ "OR i.subCategory.category.categoryName = :subCategoryName) "
    		+ "AND endDate > NOW() ORDER BY startdate")
    Page<Item> findByParamsWithSubCat(@Param("title") String title, @Param("subCategoryName") String subCategory, 
    		Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND (i.subCategory.subCategoryName = :subCategoryName "
    		+ "OR i.subCategory.category.categoryName = :subCategoryName) "
    		+ "AND i.province.provinceName = :province AND prize >= :prizeMin AND endDate > NOW() ORDER BY startdate")
    Page<Item> findByParamsMin(@Param("title") String title, 
    		@Param("subCategoryName") String subCategory, @Param("province") String province,
    		@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND (i.subCategory.subCategoryName = :subCategoryName "
    		+ "OR i.subCategory.category.categoryName = :subCategoryName) AND prize >= :prizeMin AND endDate > NOW() "
    		+ "AND (6371*acos(cos(radians(:lat))*cosRadLat*cos(radLng-radians(:lng))+sin(radians(:lat))*sinRadLat)) < :d "
    		+ "ORDER BY startdate")
    Page<Item> findByParamsMin(@Param("title") String title, 
    		@Param("subCategoryName") String subCategory, @Param("lat") double latitude, 
    		@Param("lng") double longitude, @Param("d") double distance,
    		@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.province.provinceName = :province "
    		+ "AND prize >= :prizeMin AND endDate > NOW() ORDER BY startdate")
    Page<Item> findByParamsWithProvinceMin(@Param("title") String title, @Param("province") String province,
    		@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND prize >= :prizeMin AND endDate > NOW() "
    		+ "AND (6371*acos(cos(radians(:lat))*cosRadLat*cos(radLng-radians(:lng))+sin(radians(:lat))*sinRadLat)) < :d "
    		+ "ORDER BY startdate")
    Page<Item> findByParamsWithDistanceMin(@Param("title") String title, @Param("lat") double latitude, 
    		@Param("lng") double longitude, @Param("d") double distance,
    		@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND (i.subCategory.subCategoryName = :subCategoryName "
    		+ "OR i.subCategory.category.categoryName = :subCategoryName) "
    		+ "AND prize >= :prizeMin AND endDate > NOW() ORDER BY startdate")
    Page<Item> findByParamsWithSubCatMin(@Param("title") String title, @Param("subCategoryName") String subCategory,
    		@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND (i.subCategory.subCategoryName = :subCategoryName "
    		+ "OR i.subCategory.category.categoryName = :subCategoryName) "
    		+ "AND i.province.provinceName = :province AND prize <= :prize AND endDate > NOW() ORDER BY startdate")
    Page<Item> findByParamsMax(@Param("title") String title, 
    		@Param("subCategoryName") String subCategory, @Param("province") String province,
    		@Param("prize") BigDecimal prize, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% "
    		+ "AND i.province.provinceName = :province AND prize <= :prize AND endDate > NOW() ORDER BY startdate")
    Page<Item> findByParamsWithProvinceMax(@Param("title") String title, @Param("province") String province,
    		@Param("prize") BigDecimal prize, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND (i.subCategory.subCategoryName = :subCategoryName "
    		+ "OR i.subCategory.category.categoryName = :subCategoryName) "
    		+ "AND prize <= :prize AND endDate > NOW() ORDER BY startdate")
    Page<Item> findByParamsWithSubCatMax(@Param("title") String title, @Param("subCategoryName") String subCategory, 
    		@Param("prize") BigDecimal prize, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND (i.subCategory.subCategoryName = :subCategoryName "
    		+ "OR i.subCategory.category.categoryName = :subCategoryName) "
    		+ "AND i.province.provinceName = :province AND prize >= :prizeMin AND prize <= :prizeMax "
    		+ "AND endDate > NOW() ORDER BY startdate")
    Page<Item> findByParamsMinMax(@Param("title") String title, 
    		@Param("subCategoryName") String subCategory, @Param("province") String province,
    		@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND (i.subCategory.subCategoryName = :subCategoryName "
    		+ "OR i.subCategory.category.categoryName = :subCategoryName) "
    		+ "AND prize >= :prizeMin AND prize <= :prizeMax AND endDate > NOW() "
    		+ "AND (6371*acos(cos(radians(:lat))*cosRadLat*cos(radLng-radians(:lng))+sin(radians(:lat))*sinRadLat)) < :d "
    		+ "ORDER BY startdate")
    Page<Item> findByParamsMinMax(@Param("title") String title, 
    		@Param("subCategoryName") String subCategory, @Param("lat") double latitude, 
    		@Param("lng") double longitude, @Param("d") double distance,
    		@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% "
    		+ "AND i.province.provinceName = :province AND prize >= :prizeMin AND prize <= :prizeMax "
    		+ "AND endDate > NOW() ORDER BY startdate")
    Page<Item> findByParamsWithProvinceMinMax(@Param("title") String title, @Param("province") String province,
    		@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% "
    		+ "AND prize >= :prizeMin AND prize <= :prizeMax AND endDate > NOW() "
    		+ "AND (6371*acos(cos(radians(:lat))*cosRadLat*cos(radLng-radians(:lng))+sin(radians(:lat))*sinRadLat)) < :d "
    		+ "ORDER BY startdate")
    Page<Item> findByParamsWithDistanceMinMax(@Param("title") String title, @Param("lat") double latitude, 
    		@Param("lng") double longitude, @Param("d") double distance,
    		@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND (i.subCategory.subCategoryName = :subCategoryName "
    		+ "OR i.subCategory.category.categoryName = :subCategoryName) "
    		+ "AND prize >= :prizeMin AND prize <= :prizeMax "
    		+ "AND endDate > NOW() ORDER BY startdate")
    Page<Item> findByParamsWithSubCatMinMax(@Param("title") String title, @Param("subCategoryName") String subCategory,
    		@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
    
    /*************************************************************************************************************/
    
    /* PARAMS */

//    // title subCategory and province
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.subCategory.subCategoryName = :subCategoryName")
//    Page<Item> findByTitleAndSubCategory(@Param("title") String title, 
//    		@Param("subCategoryName") String subCategory, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.province.provinceName = :province")
//    Page<Item> findByTitleAndProvince(@Param("title") String title, 
//    		@Param("province") String province, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND "
//    		+ "i.province.provinceName = :province AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findBySubCategoryAndProvince(@Param("subCategoryName") String subCategoryName,
//    		@Param("province") String province, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.subCategory.subCategoryName = :subCategoryName "
//    		+ "AND i.province.provinceName = :province AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findByTitleAndSubCategoryAndProvince(@Param("title") String title, 
//    		@Param("subCategoryName") String subCategory, @Param("province") String province, Pageable pageable);
//    // end
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.subCategory.subCategoryName = :subCategoryName "
//    		+ "AND prize >= :prizeMin AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findByTitleAndSubCategoryMin(@Param("title") String title, 
//    		@Param("subCategoryName") String subCategory, @Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.subCategory.subCategoryName = :subCategoryName "
//    		+ "AND prize <= :prizeMax AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findByTitleAndSubCategoryMax(@Param("title") String title, 
//    		@Param("subCategoryName") String subCategory, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.subCategory.subCategoryName = :subCategoryName "
//    		+ "AND prize >= :prizeMin AND prize <= :prizeMax AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findByTitleAndSubCategoryMinMax(@Param("title") String title, 
//    		@Param("subCategoryName") String subCategory, @Param("prizeMin") BigDecimal prizeMin,
//    		@Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.province.provinceName = :province "
//    		+ "AND prize >= :prizeMin AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findByTitleAndProvinceMin(@Param("title") String title, @Param("prizeMin") BigDecimal prizeMin, 
//    		@Param("province") String province, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.province.provinceName = :province "
//    		+ "AND prize <= :prizeMax AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findByTitleAndProvinceMax(@Param("title") String title, @Param("prizeMax") BigDecimal prizeMax, 
//    		@Param("province") String province, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.subCategory.subCategoryName = :subCategoryName "
//    		+ "AND i.province.provinceName = :province AND prize >= :prizeMin AND prize <= :prizeMax "
//    		+ "AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findByTitleAndProvinceMinMax(@Param("title") String title, 
//    		@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax, 
//    		@Param("province") String province, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName "
//    		+ "AND i.province.provinceName = :province AND prize >= :prizeMin AND endDate > NOW() "
//    		+ "ORDER BY startdate")
//    Page<Item> findBySubCategoryAndProvinceMin(@Param("subCategoryName") String subCategory, 
//    		@Param("province") String province, @Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName "
//    		+ "AND i.province.provinceName = :province AND prize <= :prizeMax AND endDate > NOW() "
//    		+ "ORDER BY startdate")
//    Page<Item> findBySubCategoryAndProvinceMax(@Param("subCategoryName") String subCategory, 
//    		@Param("province") String province, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.subCategory.subCategoryName = :subCategoryName "
//    		+ "AND i.province.provinceName = :province AND prize >= :prizeMin AND prize <= :prizeMax "
//    		+ "AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findBySubCategoryAndProvinceMinMax(@Param("subCategoryName") String subCategory,
//    		@Param("province") String province, @Param("prizeMin") BigDecimal prizeMin, 
//    		@Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.subCategory.subCategoryName = :subCategoryName "
//    		+ "AND i.province.provinceName = :province AND prize >= :prizeMin AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findByTitleAndSubCategoryAndProvinceMin(@Param("title") String title, 
//    		@Param("subCategoryName") String subCategory, @Param("province") String province, 
//    		@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.subCategory.subCategoryName = :subCategoryName "
//    		+ "AND i.province.provinceName = :province AND prize <= :prizeMax AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findByTitleAndSubCategoryAndProvinceMax(@Param("title") String title, 
//    		@Param("subCategoryName") String subCategory, @Param("province") String province, 
//    		@Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND i.subCategory.subCategoryName = :subCategoryName "
//    		+ "AND i.province.provinceName = :province AND prize >= :prizeMin AND prize <= :prizeMax "
//    		+ "AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findByTitleAndSubCategoryAndProvinceMinMax(@Param("title") String title, 
//    		@Param("subCategoryName") String subCategory, @Param("province") String province,
//    		@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
    
    
    
    
//    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND "
//    		+ "i.province.provinceName = :province AND prize >= :prizeMin AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findBySubCategoryNameAndProvinceMin(@Param("subCategoryName") String subCategoryName,
//    		@Param("province") String province, @Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND "
//    		+ "i.province.provinceName = :province AND prize <= :prizeMax AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findBySubCategoryAndProvinceMax(@Param("subCategoryName") String subCategoryName,
//    		@Param("province") String province, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND "
//    		+ "i.province.provinceName = :province AND prize >= :prizeMin AND prize <= :prizeMax AND "
//    		+ "endDate > NOW() ORDER BY startdate")
//    Page<Item> findBySubCategoryAndProvinceMinMax(@Param("subCategoryName") String subCategoryName,
//    		@Param("province") String province, @Param("prizeMin") BigDecimal prizeMin,
//    		@Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
}