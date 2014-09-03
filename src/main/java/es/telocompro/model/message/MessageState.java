package es.telocompro.model.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "messageState")
@IdClass(MessageStateKey.class)
public class MessageState {

	@Id
	private Long messageId;
	@Id
	private Long userId;
	@Id
	private Long messageThreadId;
	
//	@ManyToOne(optional=false, fetch=FetchType.LAZY)
//    @JoinColumn(name="userId", referencedColumnName = "userId")
//	private User user;
	
	@Column(name="readState")
	private boolean readState;
	
	public MessageState() {}

	public MessageState(Long messageId, Long userId, Long messageThreadId, boolean readState) {
		this.messageId = messageId;
//		this.user = user;
		this.messageThreadId = messageThreadId;
		this.userId = userId;
		this.readState = readState;
	}

	public boolean isReadState() {
		return readState;
	}

	public void setReadState(boolean readState) {
		this.readState = readState;
	}

	public Long getMessageId() {
		return messageId;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getMessageThreadId() {
		return messageThreadId;
	}

}
