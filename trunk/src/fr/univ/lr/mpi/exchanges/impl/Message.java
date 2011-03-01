package fr.univ.lr.mpi.exchanges.impl;

import fr.univ.lr.mpi.exchanges.IMessage;

/**
 * 
 * @author FAUCHER Tony
 * 
 */
public class Message implements IMessage {

	// attributs
	private MessageType type;
	private String callerPhoneNumber;
	private String recipientPhoneNumber;

	// constructor
	public Message(MessageType type, String callerPhoneNumber,
			String recipientPhoneNumber) {
		this.type = type;
		this.callerPhoneNumber = callerPhoneNumber;
		this.recipientPhoneNumber = recipientPhoneNumber;
	}

	// getter and setter

	/**
	 * @return the message type
	 */
	public MessageType getMessageType() {
		return this.type;
	}

	// called phone number
	/**
	 * @return caller phone number
	 */
	public String getCallerPhoneNumber() {
		return this.callerPhoneNumber;
	}

	// recipient phone number
	/**
	 * @return the recipient phone number
	 */
	public String getRecipientPhoneNumber() {
		return recipientPhoneNumber;
	}

	@Override
	public String toString() {
		return "Message [callerPhoneNumber=" + callerPhoneNumber
				+ ", recipientPhoneNumber=" + recipientPhoneNumber + ", type="
				+ type + "]";
	}

}
