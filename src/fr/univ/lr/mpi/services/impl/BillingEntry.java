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
	 * @author FAUCHER Tony
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
	 * @author FAUCHER Tony
	 * @return phone number called
	 */
	public String getCallerPhoneNumber() {
		return this.callerPhoneNumber;
	}

	/**
	 * @author FAUCHER Tony
	 * @return phone number recipient
	 */
	public String getRecipientPhoneNumber() {
		return this.recipientPhoneNumber;
	}

	/**
	 * @author FAUCHER Tony
	 * @return call duration
	 */
	public double getDuration() {
		return this.duration;
	}

	/**
	 * @author FAUCHER Tony
	 * @return call date
	 */
	public Date getDate() {
		return this.date;
	}

}
