package es.elovendo.model.admin.report.item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.elovendo.model.item.Item;
import es.elovendo.model.user.User;

@Entity
@Table(name = "ItemReport")
public class ItemReport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "itemReportId")
	private Long itemReportId;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "itemId")
	private Item item;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "reporterId")
	private User user;
	
	private String reportMessage;
	
	public ItemReport() { }

	public ItemReport(Item item, User user, String reportMessage) {
		this.item = item;
		this.user = user;
		this.reportMessage = reportMessage;
	}

	public Long getItemReportId() {
		return itemReportId;
	}

	public Item getItem() {
		return item;
	}

	public User getUser() {
		return user;
	}

	public String getReportMessage() {
		return reportMessage;
	}

}
