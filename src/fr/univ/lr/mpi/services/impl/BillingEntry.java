package fr.univ.lr.mpi.services.impl;

import java.util.Date;

/**
 * 
 * @author FAUCHER Tony
 * 
 */
public class BillingEntry {

	// attributes
	private String callerPhoneNumber, recipientPhoneNumber;
	private double duration;
	private Date date;

	/**
	 * Constructor
	 * 
	 * @param callerPhoneNumber
	 * @param recipientPhoneNumber
	 * @param date
	 * @param duration
	 */
	public BillingEntry(String callerPhoneNumber, String recipientPhoneNumber,
			Date date, double duration) {
		this.callerPhoneNumber = callerPhoneNumber;
		this.recipientPhoneNumber = recipientPhoneNumber;
		this.duration = duration;
		this.date = date;
	}

	/**
	 * @return phone number called
	 */
	public String getCallerPhoneNumber() {
		return this.callerPhoneNumber;
	}

	/**
	 * @return phone number recipient
	 */
	public String getRecipientPhoneNumber() {
		return this.recipientPhoneNumber;
	}

	/**
	 * @return call duration
	 */
	public double getDuration() {
		return this.duration;
	}

	/**
	 * @return call date
	 */
	public Date getDate() {
		return this.date;
	}

}
