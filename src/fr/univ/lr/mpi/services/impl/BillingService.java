package fr.univ.lr.mpi.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.univ.lr.mpi.services.IService;

/**
 * Billing Service
 * 
 * @author FAUCHER Tony
 * 
 */
public class BillingService implements IService {

	// attributes
	private List<BillingEntry> entries;


	/**
	 * Constructor
	 * 
	 * @author FAUCHER Tony
	 */
	public BillingService() {
		this.entries = new ArrayList<BillingEntry>();

	}

	/**
	 * @author FAUCHER Tony Create and Add an entry in the list
	 * @param callerPhoneNumber
	 * @param recipientPhoneNumber
	 * @param date
	 * @param duration
	 */
	public void addEntry(String callerPhoneNumber, String recipientPhoneNumber,
			Date date, double duration) {
		this.entries.add(new BillingEntry(callerPhoneNumber,
				recipientPhoneNumber, date, duration));
	}
}
