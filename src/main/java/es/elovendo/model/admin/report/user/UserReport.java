package es.elovendo.model.admin.report.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.elovendo.model.user.User;

@Entity
@Table(name = "UserReport")
public class UserReport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userReportId")
	private Long userReportId;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "reporterId")
	private User reporter;
	
	private String reportMessage;

	public UserReport() { }

	public UserReport(User user, User reporter, String reportMessage) {
		this.user = user;
		this.reporter = reporter;
		this.reportMessage = reportMessage;
	}

	public Long getUserReportId() {
		return userReportId;
	}

	public User getUser() {
		return user;
	}

	public User getReporter() {
		return reporter;
	}

	public String getReportMessage() {
		return reportMessage;
	}
}
