package fr.univ.lr.mpi.services.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.exchanges.impl.ExchangeAttributeNames;
import fr.univ.lr.mpi.services.IService;

/**
 * Billing Service
 * 
 * @author FAUCHER Tony <faucher.tony85@gmail.com>
 * 
 */
public class BillingService implements IService {

	/**
	 * List for the bill, it's contains BillingEntry (caller phone number, recipient phone number, duration and date)
	 */
	private List<BillingEntry> entries;

	/**
	 * Constructor
	 * 
	 */
	public BillingService() {
		this.entries = new ArrayList<BillingEntry>();

	}

	/**
	 * Create and Add an entry in the list
	 * 
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

	@Override
	public void receiveEvent(IEvent event) {
		switch (event.getEventType()) {
		case CONNECTION_CLOSED:
			// Get the caller phone Number and the recipient phone number
			String callerPhoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
			String recipientPhoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);

			// Get the duration
			Double duration = Double
					.parseDouble(event
							.getAttributeValue(ExchangeAttributeNames.CONNECTION_DURATION));

			// Get the date
			String dateString = event
					.getAttributeValue(ExchangeAttributeNames.DATE);
			DateFormat df = DateFormat.getDateInstance();
			Date date;
			try {
				date = df.parse(dateString);

				// Add a billing entry in the array
				this.entries.add(new BillingEntry(callerPhoneNumber,
						recipientPhoneNumber, date, duration));

			} catch (ParseException e) {

				e.printStackTrace();
			}

			break;

		}

	}
}
