package es.elovendo.model.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import es.elovendo.model.item.Item;
import es.elovendo.model.user.User;

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
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name="itemId", referencedColumnName="itemId")
	private Item item;
	
	@Transient
	private String partner; // Helper to save the "non-user" person in the conversation
	@Transient
	private int unreadMessages;
	@Transient
	private Message lastMessage;
	
	public MessageThread() {}

	public MessageThread(User participant1, User participant2, Item item) {
		this.participant1 = participant1;
		this.participant2 = participant2;
		this.item = item;
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

	public Item getItem() {
		return item;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public int getUnreadMessages() {
		return unreadMessages;
	}

	public void setUnreadMessages(int unreadMessages) {
		this.unreadMessages = unreadMessages;
	}

	public Message getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(Message lastMessage) {
		this.lastMessage = lastMessage;
	}
}
