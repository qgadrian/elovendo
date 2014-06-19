package es.telocompro.model.item;

import java.util.List;

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

    @Query("SELECT i FROM Item i WHERE i.title LIKE %:title%")
    Iterable<Item> findByTitle(@Param("title") String title);

    @Query("SELECT i FROM Item i WHERE i.user.userId = :userid")
    List<Item> findByUserId(@Param("userid") Long userId);

    @Query("SELECT i FROM Item i WHERE i.subCategory.subCategoryName = :subCategoryName ORDER BY startdate")
    Iterable<Item> findItemsBySubCategoryName(@Param("subCategoryName") String subCategoryName);

}
