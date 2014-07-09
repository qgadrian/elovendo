//package es.telocompro.model.message;
//
//import java.util.Calendar;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.Lob;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//
//import org.hibernate.annotations.Immutable;
//
//import es.telocompro.model.user.User;
//
//@Entity
//@Immutable
//@Table(name = "conversationreply")
//public class ConversationReply {
//
//	@Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "conversationreplyid")
//	private Long convReplyId;
//	
//    @Lob
//	private String reply;
//
//    @ManyToOne(optional=false, fetch=FetchType.LAZY)
//    @JoinColumn(name = "userid")
//    private User userId;
//
//	private String ip;
//	
//	@Column(columnDefinition="DATETIME", name = "msgdate")
//    @Temporal(TemporalType.TIMESTAMP)
//	private Calendar msgDate;
//	
//	@ManyToOne(optional=false, fetch=FetchType.LAZY)
//    @JoinColumn(name = "conversationid")
//	private Conversation conversationId;
//
//	public ConversationReply(String reply, User userId, String ip,
//			Calendar msgDate, Conversation conversationId) {
//		super();
//		this.reply = reply;
//		this.userId = userId;
//		this.ip = ip;
//		this.msgDate = msgDate;
//		this.conversationId = conversationId;
//	}
//
//	public Long getConvReplyId() {
//		return convReplyId;
//	}
//
//	public String getReply() {
//		return reply;
//	}
//
//	public User getUserId() {
//		return userId;
//	}
//
//	public String getIp() {
//		return ip;
//	}
//
//	public Calendar getMsgDate() {
//		return msgDate;
//	}
//
//	public Conversation getConversationId() {
//		return conversationId;
//	}
//	
//}
