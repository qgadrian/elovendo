package es.telocompro.model.item.category.subcategory;

import es.telocompro.model.item.category.Category;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */
@Repository("subCategoryRepository")
public interface SubCategoryRepository extends PagingAndSortingRepository<SubCategory, Long>  {
}
