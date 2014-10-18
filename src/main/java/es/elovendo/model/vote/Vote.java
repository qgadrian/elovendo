package es.elovendo.model.vote;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Immutable;

import es.elovendo.model.item.Item;
import es.elovendo.model.user.User;

@Entity
@Table(name = "vote")
@Immutable
public class Vote {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long voteId;
	
	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="userId")
	@JoinColumn(name="userIdVote",referencedColumnName="userid")
	private User userVote;
	
	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="userId")
	@JoinColumn(name="userIdReceive", referencedColumnName="userid")
	private User userReceive;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="itemId")
	private Item item;
	
	private int voteType;
	private boolean voteState;
    
	private int voteValue;
	
	@Size(max=50)
    private String voteMessage;
	
	protected Vote() {}

	public Vote(User userVote, User userReceive, Item item, int voteType,
			boolean voteState, int voteValue, String voteMessage) {
		super();
		this.userVote = userVote;
		this.userReceive = userReceive;
		this.item = item;
		this.voteType = voteType;
		this.voteState = voteState;
		this.voteValue = voteValue;
		this.voteMessage = voteMessage;
	}

	public boolean getVoteValid() {
		return voteState;
	}

	public void setVoteValid(boolean voteValid) {
		this.voteState = voteValid;
	}

	public Long getVoteId() {
		return voteId;
	}

	public User getUserVote() {
		return userVote;
	}

	public User getUserReceive() {
		return userReceive;
	}

	public int getVoteType() {
		return voteType;
	}

	public int getVoteValue() {
		return voteValue;
	}

	public String getVoteMessage() {
		return voteMessage;
	}
	
}
