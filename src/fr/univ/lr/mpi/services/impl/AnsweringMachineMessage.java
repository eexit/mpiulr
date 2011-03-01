package fr.univ.lr.mpi.services.impl;

import java.util.Date;

public class AnsweringMachineMessage {

	
	//attributes
	private Date postDate;
	private String posterPhoneNumber;
	private Object message;
	
	
	//Constructor
	/**
	 * 
	 * @param postDate
	 * @param posterPhoneNumber
	 * @param message
	 */
	public AnsweringMachineMessage(Date postDate, String posterPhoneNumber,
			Object message) {
		this.postDate = postDate;
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
	 * @return The message
	 */
	public Object getMessage() {
		return message;
	}
	
	
	
	
}
