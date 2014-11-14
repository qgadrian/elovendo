package es.elovendo.model.admin.report.item;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ItemReportRepository extends PagingAndSortingRepository<ItemReport, Long> {
	
//	@Query("SELECT ir FROM ItemReport ir")
//	Page<ItemReport> findAll(Pageable pageable);

}
