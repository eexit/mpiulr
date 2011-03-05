package fr.univ.lr.mpi.exchanges.impl;

import fr.univ.lr.mpi.exchanges.IMessage;

/**
 * Message.java
 * 
 * @author FAUCHER Tony
 */
public class Message implements IMessage {

	/**
	 * Message type
	 */
	private MessageType type;
	
	/**
	 * Caller phone number
	 */
	private String callerPhoneNumber;
	
	/**
	 * Recipient phone number
	 */
	private String recipientPhoneNumber;
	
	/**
	 * Message content
	 */
	private String content;

	/**
	 * Class constructor
	 * 
	 * @param type
	 * @param callerPhoneNumber
	 */
	public Message(MessageType type, String callerPhoneNumber) {
		this(type, callerPhoneNumber, null, null);
	}
	
	/**
	 * Class constructor
	 * 
	 * @param type
	 * @param callerPhoneNumber
	 * @param recipientPhoneNumber
	 */
	public Message(MessageType type, String callerPhoneNumber, String recipientPhoneNumber) {
		this(type, callerPhoneNumber, recipientPhoneNumber, null);
	}

	/**
	 * Class constructor
	 * 
	 * @param type
	 * @param callerPhoneNumber
	 * @param recipientPhoneNumber
	 * @param content
	 */
	public Message(MessageType type, String callerPhoneNumber,
			String recipientPhoneNumber, String content) {
		this.type = type;
		this.callerPhoneNumber = callerPhoneNumber;
		this.recipientPhoneNumber = recipientPhoneNumber;
		this.content = content;
	}

	/**
	 * Message type getter
	 * 
	 * @return
	 */
	public MessageType getMessageType() {
		return this.type;
	}

	/**
	 * Message content getter
	 * 
	 * @return
	 */
	public String getContent()
	{
		return this.content;
	}
	
	/**
	 * Caller phone number getter
	 * 
	 * @return
	 */
	public String getCallerPhoneNumber() {
		return this.callerPhoneNumber;
	}

	/**
	 * Recipient phone number getter
	 * 
	 * @return
	 */
	public String getRecipientPhoneNumber() {
		return recipientPhoneNumber;
	}

	@Override
	public String toString() {
		return "Message [type=" + type + ", callerPhoneNumber=" + callerPhoneNumber
			+ ", recipientPhoneNumber=" + recipientPhoneNumber
			+ ", content=" + content + "]";
	}
}
