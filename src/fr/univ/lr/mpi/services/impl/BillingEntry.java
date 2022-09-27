package fr.univ.lr.mpi.services.impl;

import java.util.Date;

/**
 * Billing Entry
 * 
 * @author FAUCHER Tony
 */
public class BillingEntry {

	/**
	 * The caller and recipient phone number
	 */
	private String callerPhoneNumber, recipientPhoneNumber;

	/**
	 * The duration
	 */
	private double duration;

	/**
	 * The date
	 */
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
	 * Caller phone number getter
	 * 
	 * @return phone number called
	 */
	public String getCallerPhoneNumber() {
		return this.callerPhoneNumber;
	}

	/**
	 * Recipient phone number getter
	 * 
	 * @return phone number recipient
	 */
	public String getRecipientPhoneNumber() {
		return this.recipientPhoneNumber;
	}

	/**
	 * Call duration getter
	 * 
	 * @return call duration
	 */
	public double getDuration() {
		return this.duration;
	}

	/**
	 * Call date getter
	 * 
	 * @return call date
	 */
	public Date getDate() {
		return this.date;
	}

	@Override
	public String toString() {
		return "BillingEntry [callerPhoneNumber=" + callerPhoneNumber
				+ ", date=" + date + ", duration=" + duration
				+ ", recipientPhoneNumber=" + recipientPhoneNumber + "]";
	}
}
