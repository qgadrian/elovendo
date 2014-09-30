package es.telocompro.model.message;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.annotations.Immutable;

import es.telocompro.model.user.User;

@Entity
@Immutable
@Table(name = "message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "messageId")
	private Long messageId;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "messageThreadId")
	private MessageThread messageThread;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="senderId", referencedColumnName = "userId")
	private User sender;
	
	@Column(name="text")
	private String messageText;
	
	private long ipAddress;
	
	@Column(columnDefinition="DATETIME", name = "messageDate")
    @Temporal(TemporalType.TIMESTAMP)
	private Calendar messageDate;
	
	public Message() {}

	public Message(MessageThread messageThread, User sender,
			String messageText, long ipAddress) {
		this.messageThread = messageThread;
		this.sender = sender;
		this.messageText = messageText;
		this.ipAddress = ipAddress;
		this.messageDate = Calendar.getInstance();
	}

	public Long getMessageId() {
		return messageId;
	}

	public MessageThread getMessageThread() {
		return messageThread;
	}

	public User getSender() {
		return sender;
	}

	public String getMessageText() {
		return messageText;
	}

	public Calendar getMessageDate() {
		return messageDate;
	}

	public long getIpAddress() {
		return ipAddress;
	}
	
	@Transient
	public boolean isToday() {
		return DateUtils.isSameDay(Calendar.getInstance(), this.messageDate);
	}

}
