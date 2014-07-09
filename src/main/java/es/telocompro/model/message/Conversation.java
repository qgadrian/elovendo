//package es.telocompro.model.message;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//import org.hibernate.annotations.Immutable;
//
//import es.telocompro.model.user.User;
//
//@Entity
//@Immutable
//@Table(name = "conversation")
//public class Conversation {
//	
//	@Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "conversationid")
//	private Long conversationId;
//	
//	@ManyToOne(optional=false, fetch=FetchType.LAZY)
//    @JoinColumn(name = "userid")
//	private User user1;
//	
//	@ManyToOne(optional=false, fetch=FetchType.LAZY)
//    @JoinColumn(name = "userid")
//	private User user2;
//
//	public Conversation(User user1, User user2) {
//		super();
//		this.user1 = user1;
//		this.user2 = user2;
//	}
//
//	public Long getConversationId() {
//		return conversationId;
//	}
//
//	public User getUser1() {
//		return user1;
//	}
//
//	public User getUser2() {
//		return user2;
//	}
//
//}
