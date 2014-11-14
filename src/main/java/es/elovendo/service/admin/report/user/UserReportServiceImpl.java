package es.elovendo.service.admin.report.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import es.elovendo.model.admin.report.user.UserReport;
import es.elovendo.model.admin.report.user.UserReportRepository;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.service.user.UserService;

@Service("userReportService")
public class UserReportServiceImpl implements UserReportService {
	
	@Autowired
	private UserReportRepository userReportRepository;
	@Autowired
	private UserService userService;

	@Override
	public UserReport createUserReport(Long userId, User user, String reportMessage) throws UserNotFoundException {
		User reportedUser = userService.findUserById(userId);
		UserReport report = new UserReport(reportedUser, user, reportMessage);
		return userReportRepository.save(report);
	}

	@Override
	public void deleteUserReport(Long userReportId) {
		userReportRepository.delete(userReportId);
	}
	
	@Override
	public Page<UserReport> getAllUserReport(int page, int size) {
		PageRequest request = new PageRequest(page, size);
		return userReportRepository.findAll(request);
	}

}
