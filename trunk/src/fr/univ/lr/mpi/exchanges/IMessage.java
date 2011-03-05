package fr.univ.lr.mpi.exchanges;

import fr.univ.lr.mpi.exchanges.impl.MessageType;

/**
 * IMessage.java
 * 
 * @author Tony FAUCHER
 */
public interface IMessage {
	
	/**
	 * Gets the message type
	 * 
	 * @author Tony FAUCHER
	 * @return
	 */
	public MessageType getMessageType();
	
	/**
	 * Gets the caller phone number
	 * 
	 * @author Tony FAUCHER
	 * @return
	 */
	public String getCallerPhoneNumber();
	
	/**
	 * Gets the recipient phone number
	 * 
	 * @author Tony FAUCHER
	 * @return
	 */
	public String getRecipientPhoneNumber();

	/**
	 * Message content getter
	 * 
	 * @return
	 */
	public String getContent();
	
}
