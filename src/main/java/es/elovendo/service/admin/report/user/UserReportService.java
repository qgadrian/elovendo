package es.elovendo.service.admin.report.user;

import org.springframework.data.domain.Page;

import es.elovendo.model.admin.report.user.UserReport;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.UserNotFoundException;

public interface UserReportService {
	
	/**
	 * Creates a new report
	 * @param userId User which will be reported
	 * @param user Reporter user (clarifying, "not the item owner")
	 * @param reportMessage Report message from user
	 * @return
	 * @throws UserNotFoundException 
	 */
	public UserReport createUserReport(Long userId, User user, String reportMessage) throws UserNotFoundException;
	
	/**
	 * Deletes an item report
	 * @param itemReportId
	 */
	public void deleteUserReport(Long userReportId);
	
	/**
	 * Get all item reports
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<UserReport> getAllUserReport(int page, int size);

}
