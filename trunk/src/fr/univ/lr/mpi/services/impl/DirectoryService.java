package fr.univ.lr.mpi.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
public class DirectoryService extends Thread implements IService {

	/**
	 * List of the different lines
	 */
	private List<String> directory;

	private Stack<IEvent> eventStack;

	/**
	 * Constructor
	 * 
	 */
	public DirectoryService() {
		this.directory = new ArrayList<String>();
		this.eventStack = new Stack<IEvent>();
	}

	@Override
	public void run() {
		while (true) {
			String phoneNumber;
			if (eventStack.isEmpty()) {
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			IEvent event = eventStack.pop();
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

				// if the phone number is in the directory, we return true, else
				// we return false

				// Create a new event
				Event newEvent = new Event(EventType.PHONE_NUMBER_RESPONSE);
				// add caller phone number
				newEvent
						.addAttribute(
								ExchangeAttributeNames.CALLER_PHONE_NUMBER,
								event
										.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER));
				// add recipient phone number
				newEvent.addAttribute(
						ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
						phoneNumber);

				// add boolean

				newEvent.addAttribute(ExchangeAttributeNames.EXISTS, Boolean
						.toString(this.exist(phoneNumber)));

				// Send to the autocommutator the event
				AutoCommutator.getInstance().receiveEvent(newEvent);

				break;
			}
		}
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
	public synchronized void receiveEvent(IEvent event) {
		this.eventStack.add(event);
		this.notify();
	}
}
