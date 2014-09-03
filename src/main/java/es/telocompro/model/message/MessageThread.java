package es.telocompro.model.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import es.telocompro.model.user.User;

@Entity
@Immutable
@Table(name = "messageThread")
public class MessageThread {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "messageThreadId")
	private Long messageThreadId;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name="participant1", referencedColumnName="userId")
	private User participant1;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name="participant2", referencedColumnName="userId")
	private User participant2;
	
	public MessageThread() {}

	public MessageThread(User participant1, User participant2) {
		this.participant1 = participant1;
		this.participant2 = participant2;
	}

	public Long getMessageThreadId() {
		return messageThreadId;
	}

	public User getParticipant1() {
		return participant1;
	}

	public User getParticipant2() {
		return participant2;
	}
}
