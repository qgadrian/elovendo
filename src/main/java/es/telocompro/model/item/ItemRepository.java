package es.telocompro.model.item;

import java.math.BigDecimal;

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

    @Query("SELECT i, i.user.login AS username, i.subCategory.subCategoryName AS subCategory,"
    		+ " i.subCategory.category.categoryName FROM Item i WHERE i.title LIKE %:title%")
    Page<Item> findByTitle(@Param("title") String title, Pageable pageable);

    @Query("SELECT i, i.user.login AS username, i.subCategory.subCategoryName AS subCategory,"
    		+ " i.subCategory.category.categoryName FROM Item i WHERE i.user.login = :username")
    Page<Item> findByUserName(@Param("username") String userName, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName ORDER BY startdate")
    Page<Item> findItemsBySubCategoryName(@Param("subCategoryName") String subCategoryName, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND prize >= :prizeMin "
    		+ "ORDER BY startdate")
    Page<Item> findItemsBySubCategoryName(@Param("subCategoryName") String subCategoryName, 
    		@Param("prizeMin") BigDecimal prizeMin, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName AND prize >= :prizeMin "
    		+ "AND prize <= :prizeMax ORDER BY startdate")
    Page<Item> findItemsBySubCategoryName(@Param("subCategoryName") String subCategoryName, 
    		@Param("prizeMin") BigDecimal prizeMin, @Param("prizeMax") BigDecimal prizeMax, Pageable pageable);

}
