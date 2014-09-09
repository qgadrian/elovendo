package es.telocompro.model.item.favorite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends PagingAndSortingRepository<Favorite, FavoriteKey>{
	
	@Query("SELECT f FROM Favorite f WHERE f.userId = :userId")
	Page<Favorite> findLastFav(@Param("userId") long userId, Pageable pageable);

}
