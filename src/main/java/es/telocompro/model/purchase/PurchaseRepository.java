package es.telocompro.model.purchase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("purchaseRepository")
public interface PurchaseRepository extends PagingAndSortingRepository<Purchase, String> {
	
    @Query("SELECT p FROM Purchase p WHERE p.user.login = :username")
    Page<Purchase> findAllPurchasesByUserName(@Param("username") String userName, Pageable pageable);
    
    @Query("SELECT p FROM Purchase p WHERE p.user.userId = :userId")
    Page<Purchase> findAllPurchasesByUserId(@Param("userId") Long userId, Pageable pageable);

}
