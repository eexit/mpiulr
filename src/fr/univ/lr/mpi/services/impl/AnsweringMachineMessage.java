package fr.univ.lr.mpi.services.impl;

import java.util.Date;

public class AnsweringMachineMessage {

	
	//attributes
	private Date postDate;
	private String posterPhoneNumber, ownerPhoneNumber;
	private Object message;

	
	//Constructor
	/**
	 * 
	 * @param postDate
	 * @param posterPhoneNumber
	 * @param message
	 */
	public AnsweringMachineMessage(Date postDate, String ownerPhoneNumber, String posterPhoneNumber,
			Object message) {
		this.postDate = postDate;
		this.ownerPhoneNumber = ownerPhoneNumber;
		this.posterPhoneNumber = posterPhoneNumber;
		this.message = message;
	}


	//méthodes
	
	/**
	 * @return Post Date
	 */
	public Date getPostDate() {
		return postDate;
	}

	/**
	 * 
	 * @return Phone number poster
	 */
	public String getPosterPhoneNumber() {
		return posterPhoneNumber;
	}

	/**
	 * 
	 * @return Phone number owner
	 */
	public String getOwnerPhoneNumber() {
		return this.ownerPhoneNumber;
	}
	
	/**
	 * 
	 * @return The message
	 */
	public Object getMessage() {
		return message;
	}
	
	
	
	
}
