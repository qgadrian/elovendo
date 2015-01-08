package es.elovendo.model.item;

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

// AUTO UPDATE QUERY:
/** UPDATE item SET startDate = NOW(), endDate = (NOW() + INTERVAL 30 DAY) WHERE autoRenew = 1; **/
// SET SCHEDULER ON
/** SET GLOBAL event_scheduler = ON; **/
// SEE Scheduler events
/** SELECT * FROM INFORMATION_SCHEMA.EVENTS WHERE EVENT_NAME = "reset"; ---- PROCESSES: SHOW PROCESSLIST\G **/ 
// EVENT MYSQL
/** CREATE EVENT autoRenew ON SCHEDULE EVERY 15 MINUTE DO UPDATE item SET startDate = NOW(), endDate = DATE_ADD(NOW(), INTERVAL 30 DAY) WHERE endDate < NOW() AND autoRenew = TRUE; **/
// See scheduler
/** SELECT * FROM INFORMATION_SCHEMA.EVENTS WHERE EVENT_NAME = "reset"; **/
// DROP scheduler
/** DROP EVENT reset_test; **/

@Repository("itemRepository")
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {
	
	/* USER */

    @Query("SELECT i, i.user.login AS username, i.subCategory.subCategoryName AS subCategory,"
    		+ " i.subCategory.category.categoryName FROM Item i WHERE i.user.login = :username AND endDate > NOW() ORDER BY startDate")
    Page<Item> findByUserName(@Param("username") String userName, Pageable pageable);
    
    @Query("SELECT COUNT(i) FROM Item i WHERE i.user.userId = :userId AND endDate > NOW() ORDER BY startDate")
    int findNumberUserItems(@Param("userId") Long userId);
    
    @Query("SELECT i FROM Item i WHERE i.user.login = :userName AND endDate > NOW() ORDER BY startDate")
	List<Item> findByUserName(@Param("userName") String userName);

    @Query("SELECT i FROM Item i WHERE i.user.userId = :userId ORDER BY startDate")
	List<Item> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT i FROM Item i WHERE i.user.userId = :userId AND endDate > NOW() ORDER BY startDate")
    List<Item> findLastItems(@Param("userId") long userId, Pageable pageable);
    
    /*************************************************************************************************************/
    
    /* RANDOM */
    
    @Query("SELECT i FROM Item i WHERE featured = 1 AND endDate > NOW() ORDER BY RAND()")
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
    
    @Query("SELECT DISTINCT i FROM Item i WHERE i.featured = 1 AND i.subCategory.category.categoryId = :categoryId "
    		+ "AND endDate > NOW() ORDER BY RAND()")
	List<Item> findRandomFeaturedItemsByCategoryId(@Param("categoryId") long categoryId, Pageable pageable);
    
    /*************************************************************************************************************/
    
    /* ALL */
    
    @Query("SELECT i FROM Item i WHERE prize >= :prizeMin AND endDate > NOW() ORDER BY startdate")
    Page<Item> findAllItemsMin(@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE prize <= :prizeMax AND endDate > NOW() ORDER BY startdate")
    Page<Item> findAllItemsMax(@Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE prize >= :prizeMin AND prize <= :prizeMax AND "
    		+ "endDate > NOW() ORDER BY startdate DESC")
    Page<Item> findAllItemsMinMax(@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax,
    		Pageable pageable);
    
    /* TITLE */
    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findByTitle(@Param("title") String title, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND prize >= :prizeMin AND endDate > NOW()")
//    Page<Item> findByTitleMin(@Param("title") String title, @Param("prizeMin") String prizeMin, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND prize <= :prizeMax AND endDate > NOW()")
//    Page<Item> findByTitleMax(@Param("title") String title, @Param("prizeMax") String prizeMax, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND prize >= :prizeMin AND prize <= :prizeMax "
//    		+ "AND endDate > NOW()")
//    Page<Item> findByTitleMinMax(@Param("title") String title, @Param("prizeMin") String prizeMin,
//    		@Param("prizeMax") String prizeMax, Pageable pageable);
    
    /*************************************************************************************************************/

//    /* CATEGORY */
//    
//    /**
//     * Find all items from a subCategory
//     * @param subCategoryName SubCategory name
//     * @param pageable
//     * @return Items
//     */
//    @Query("SELECT i FROM Item i WHERE i.subCategory.category.categoryName = :categoryName AND "
//    		+ "endDate > NOW() ORDER BY startdate")
//    Page<Item> findItemsByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);
//    
//    /**
//     * Find all items from a subCategory filtering by a minimum prize
//     * @param subCategoryName SubCategory name
//     * @param prizeMin Minimum prize
//     * @param pageable
//     * @return Items
//     */
//    @Query("SELECT i FROM Item i WHERE i.subCategory.category.categoryName = :categoryName AND "
//    		+ "prize >= :prizeMin AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findItemsByCategoryNameMin(@Param("categoryName") String categoryName, 
//    		@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.subCategory.category.categoryName = :categoryName AND "
//    		+ "prize <= :prizeMax AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findItemsByCategoryNameMax(@Param("categoryName") String categoryName, 
//    		@Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
//    
//    /**
//     * Find all items from a subCategory filtering by a minimum and maximum prize
//     * @param subCategoryName
//     * @param prizeMin Minimum prize
//     * @param prizeMax Maximum prize
//     * @param pageable
//     * @return
//     */
//    @Query("SELECT i FROM Item i WHERE i.subCategory.category.categoryName = :categoryName AND "
//    		+ "prize >= :prizeMin AND prize <= :prizeMax AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findItemsByCategoryNameMinMax(@Param("categoryName") String categoryName, 
//    		@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
    
    /*************************************************************************************************************/

//    /* SUBCATEGORY */
//    
//    /**
//     * Find all items from a subCategory
//     * @param subCategoryName SubCategory name
//     * @param pageable
//     * @return Items
//     */
//    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findBySubCategoryName(@Param("subCategoryName") String subCategoryName, Pageable pageable);
//    
//    /**
//     * Find all items from a subCategory filtering by a minimum prize
//     * @param subCategoryName SubCategory name
//     * @param prizeMin Minimum prize
//     * @param pageable
//     * @return Items
//     */
//    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND prize >= :prizeMin "
//    		+ "AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findBySubCategoryNameMin(@Param("subCategoryName") String subCategoryName, 
//    		@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
//    
//    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND prize >= :prizeMax "
//    		+ "AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findBySubCategoryNameMax(@Param("subCategoryName") String subCategoryName, 
//    		@Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
//    
//    /**
//     * Find all items from a subCategory filtering by a minimum and maximum prize
//     * @param subCategoryName
//     * @param prizeMin Minimum prize
//     * @param prizeMax Maximum prize
//     * @param pageable
//     * @return
//     */
//    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND prize >= :prizeMin "
//    		+ "AND prize <= :prizeMax AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findBySubCategoryNameMinMax(@Param("subCategoryName") String subCategoryName, 
//    		@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);
    
    /*************************************************************************************************************/
    
    /* PARAMS */
//    
//    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title% AND (i.subCategory.subCategoryName = :subCategoryName "
//    		+ "OR i.subCategory.category.categoryName = :subCategoryName) "
//    		+ "AND endDate > NOW() ORDER BY startdate")
//    Page<Item> findByParams(@Param("title") String title, 
//    		@Param("subCategoryName") String subCategory, Pageable pageable);

    
    /*************************************************************************************************************/
    
    /** SECOND SHOT **/
    
    @Query("SELECT new Item(i, (6371*acos(cos(radians(:lat))*cosRadLat*cos(radLng-radians(:lng))+sin(radians(:lat))*sinRadLat))) "
    		+ "FROM Item i WHERE endDate > NOW() AND title LIKE %:title% "
    		+ "AND (:pMin <= 0.0 OR prize > :pMin) AND (:pMax <= 0.0 OR prize < :pMax) AND "
    		+ "(:name = '' OR i.subCategory.subCategoryName = :name OR i.subCategory.category.categoryName = :name) "
    		+ "AND (:dis >= 5000.0 OR "
    		+ "(6371*acos(cos(radians(:lat))*cosRadLat*cos(radLng-radians(:lng))+sin(radians(:lat))*sinRadLat)) < :dis) ORDER BY startDate DESC")
//    @Query("SELECT new Item(i, SQRT(POW(:lat - latitude , 2) + POW(:lng - longitude, 2)) * 100) "
//    		+ "FROM Item i WHERE endDate > NOW() AND title LIKE %:title% "
//    		+ "AND (:pMin <= 0.0 OR prize > :pMin) AND (:pMax <= 0.0 OR prize < :pMax) AND "
//    		+ "(:cat = '' OR i.subCategory.subCategoryName = :cat OR i.subCategory.category.categoryName = :cat) "
//    		+ "AND (:dis >= 5000.0 OR "
//    		+ "SQRT(POW(:lat - latitude , 2) + POW(:lng - longitude, 2)) * 100 < :dis)")
    Page<Item> findByParams(@Param("title") String title, 
    		@Param("name") String name, @Param("lat") double latitude, 
    		@Param("lng") double longitude, @Param("dis") double distance,
    		@Param("pMin") double prizemin, @Param("pMax") double prizemax, Pageable pageable);
    

    // Use 0L to comparison a long value
    @Query("SELECT new Item(i, (6371*acos(cos(radians(:lat))*cosRadLat*cos(radLng-radians(:lng))+sin(radians(:lat))*sinRadLat))) "
    		+ "FROM Item i WHERE endDate > NOW() AND title LIKE %:title% "
    		+ "AND (:pMin <= 0.0 OR prize > :pMin) AND (:pMax <= 0.0 OR prize < :pMax) AND "
    		+ "((:isCategory = TRUE AND :cat > 0L AND i.subCategory.category.categoryId = :cat) OR "
    		+ "(:cat > 0L AND i.subCategory.id = :cat) OR :cat = 0L) "
    		+ "AND (:dis >= 5000.0 OR "
    		+ "(6371*acos(cos(radians(:lat))*cosRadLat*cos(radLng-radians(:lng))+sin(radians(:lat))*sinRadLat)) < :dis) ORDER BY startDate DESC")
    Page<Item> findByParams(@Param("title") String title, 
    		@Param("cat") long cat, @Param("isCategory") boolean isCategory, 
    		@Param("lat") double latitude, @Param("lng") double longitude, 
    		@Param("dis") double distance, @Param("pMin") double prizemin, 
    		@Param("pMax") double prizemax, Pageable pageable);
}