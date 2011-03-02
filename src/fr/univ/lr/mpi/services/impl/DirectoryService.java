package fr.univ.lr.mpi.services.impl;

import java.util.ArrayList;
import java.util.List;

import fr.univ.lr.mpi.commutator.impl.AutoCommutator;
import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.exchanges.impl.Event;
import fr.univ.lr.mpi.exchanges.impl.EventType;
import fr.univ.lr.mpi.exchanges.impl.ExchangeAttributeNames;
import fr.univ.lr.mpi.services.IService;

/**
 * The directory service
 * 
 * @author FAUCHER Tony <faucher.tony85@gmail.com>
 * 
 */
public class DirectoryService implements IService {

	/**
	 * List of the differents lines
	 */
	private List<String> directory;

	/**
	 * Constructor
	 * 
	 */
	public DirectoryService() {
		this.directory = new ArrayList<String>();
	}

	/**
	 * Add a phone number in the directory
	 * 
	 * @param phoneNumber
	 */
	public void addPhoneNumber(String phoneNumber) {
		this.directory.add(phoneNumber);
	}

	/**
	 * @param phoneNumber
	 * @return if the phone number is in the directory or not
	 */
	public boolean exist(String phoneNumber) {
		return this.directory.contains(phoneNumber);
	}

	@Override
	public void receiveEvent(IEvent event) {
		String phoneNumber;
		switch (event.getEventType()) {
		
		/**
		 * Case of line creation
		 */
		case LINE_CREATION:
			// Get the phone number
			phoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
			this.directory.add(phoneNumber);
			break;

		/**
		 * Case of line deletion
		 */
		case LINE_DELETION:
			// Get the phone number
			phoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
			this.directory.remove(phoneNumber);
			break;

		/**
		 * We need to know if the number is in the directory
		 */
		case PHONE_NUMBER_REQUEST:
			// Get the phone number
			phoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);

			// if the phone number is in the directory, we return true, else we
			// return false
			
			// Create an event
			Event e = new Event(EventType.PHONE_NUMBER_RESPONSE);
			e.addAttributes(ExchangeAttributeNames.EXISTS, Boolean
					.toString(this.exist(phoneNumber)));

			// Send to the autocommutator the event
			AutoCommutator.getInstance().receiveEvent(e);

			break;
		}

	}
}
