package es.elovendo.service.admin.report.item;

import org.springframework.data.domain.Page;

import es.elovendo.model.admin.report.item.ItemReport;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.ItemNotFoundException;

public interface ItemReportService {
	
	/**
	 * Creates a new report
	 * @param itemId Item which will be reported
	 * @param user Reporter user (clarifying, "not the item owner")
	 * @param reportMessage Report message from user
	 * @return
	 * @throws ItemNotFoundException 
	 */
	public ItemReport createItemReport(Long itemId, User user, String reportMessage) throws ItemNotFoundException;
	
	/**
	 * Deletes an item report
	 * @param itemReportId
	 */
	public void deleteItemReport(Long itemReportId);
	
	/**
	 * Get all item reports
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<ItemReport> getAllItemReport(int page, int size);
	
	/**
	 * Deletes all report for a given Item
	 * @param itemId
	 */
	public void deleteAllItemReports(Long itemId);

}