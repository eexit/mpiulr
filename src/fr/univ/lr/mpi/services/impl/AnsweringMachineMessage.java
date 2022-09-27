package fr.univ.lr.mpi.services.impl;

import java.util.Date;

/**
 * AnsweringService.java
 * 
 * @author FAUCHER Tony
 */
public class AnsweringMachineMessage {

	/**
	 * The date of te message
	 */
	private Date postDate;

	/**
	 * The caller phone number
	 */
	private String posterPhoneNumber;

	/**
	 * The recipient phone number
	 */
	private String ownerPhoneNumber;

	/**
	 * The Message
	 */
	private Object message;

	/**
	 * Class constructor
	 * 
	 * @param postDate
	 * @param posterPhoneNumber
	 * @param message
	 */
	public AnsweringMachineMessage(Date postDate, String ownerPhoneNumber,
			String posterPhoneNumber, Object message) {
		this.postDate = postDate;
		this.ownerPhoneNumber = ownerPhoneNumber;
		this.posterPhoneNumber = posterPhoneNumber;
		this.message = message;
	}

	/**
	 * Post date getter
	 * 
	 * @return Post Date
	 */
	public Date getPostDate() {
		return postDate;
	}

	/**
	 * Poster phone number getter
	 * 
	 * @return Phone number poster
	 */
	public String getPosterPhoneNumber() {
		return posterPhoneNumber;
	}

	/**
	 * Answering machine owner phone number getter
	 * 
	 * @return Phone number owner
	 */
	public String getOwnerPhoneNumber() {
		return this.ownerPhoneNumber;
	}

	/**
	 * Message content getter
	 * 
	 * @return The message
	 */
	public Object getMessage() {
		return message;
	}
}
