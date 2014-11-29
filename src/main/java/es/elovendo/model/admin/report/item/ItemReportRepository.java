package es.elovendo.model.admin.report.item;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ItemReportRepository extends PagingAndSortingRepository<ItemReport, Long> {
	
	@Modifying
	@Transactional
	@Query("DELETE FROM ItemReport ir WHERE ir.item.itemId = :itemId")
	void deleteAllItemReports(@Param("itemId") Long itemId);

}
