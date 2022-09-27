package fr.univ.lr.mpi.services.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.exchanges.impl.ExchangeAttributeNames;
import fr.univ.lr.mpi.services.IService;

/**
 * Billing Service
 * 
 * @author FAUCHER Tony
 */
public class BillingService extends Thread implements IService {

	/**
	 * List for the bill, it's contains BillingEntry (caller phone number,
	 * recipient phone number, duration and date)
	 */
	private List<BillingEntry> entries;

	/**
	 * Event stack
	 */
	private Stack<IEvent> eventStack;

	/**
	 * Constructor
	 */
	public BillingService() {
		this.entries = new ArrayList<BillingEntry>();
		this.eventStack = new Stack<IEvent>();
	}

	/**
	 * Class runner
	 */
	public void run() {
		while (true) {
			if (this.eventStack.isEmpty()) {
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			/**
			 * TODO export the following code into a dedicated method
			 */
			IEvent event = this.eventStack.pop();
			switch (event.getEventType()) {
			case CONNECTION_CLOSED:
				// Get the caller phone Number and the recipient phone number
				String callerPhoneNumber = event.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
				String recipientPhoneNumber = event.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);

				// Get the duration
				Double duration = Double.parseDouble(event.getAttributeValue(ExchangeAttributeNames.CONNECTION_DURATION));

				// Get the date
				String dateString = event.getAttributeValue(ExchangeAttributeNames.DATE);
				DateFormat df = DateFormat.getDateTimeInstance();
				Date date;
				try {
					date = df.parse(dateString);
					// Add a billing entry in the array
					this.addEntry(callerPhoneNumber, recipientPhoneNumber, date, duration);
					
					System.out.println("---------BillingServive added call entry for ended communication between "
						+ callerPhoneNumber
						+ " and "
						+ recipientPhoneNumber
						+ " on "
						+ date
						+ " which lates "
						+ duration
						+ " seconds");

				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			}
		}
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
		this.entries.add(new BillingEntry(callerPhoneNumber, recipientPhoneNumber, date, duration));
	}
	
	/**
	 * Event listener
	 */
	@Override
	public synchronized void receiveEvent(IEvent event) {
		this.eventStack.add(event);
		this.notify();
	}
}
