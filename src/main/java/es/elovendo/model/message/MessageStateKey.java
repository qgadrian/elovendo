package es.elovendo.model.message;

import java.io.Serializable;

public class MessageStateKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -365841837558403385L;
	
	private Long messageId;
	private Long userId;
	
	public MessageStateKey() {}

	public MessageStateKey(Long messageId, Long userId) {
		this.messageId = messageId;
		this.userId = userId;
	}

	public Long getMessageId() {
		return messageId;
	}

	public Long getUserId() {
		return userId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MessageStateKey) {
			MessageStateKey key = (MessageStateKey) obj;
			return key.getUserId().equals(this.userId) && key.getMessageId().equals(this.messageId);
		}
		return false;
	}

}
