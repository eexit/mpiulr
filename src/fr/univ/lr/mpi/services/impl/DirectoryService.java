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
 */
public class DirectoryService extends Thread implements IService {

	/**
	 * List of the different lines
	 */
	private List<String> directory;

	/**
	 * Event stack
	 */
	private Stack<IEvent> eventStack;

	/**
	 * Class constructor
	 * 
	 */
	public DirectoryService() {
		this.directory = new ArrayList<String>();
		this.eventStack = new Stack<IEvent>();
	}

	/**
	 * Class runner
	 */
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
			
			/**
			 * TODO export the following code into a dedicated method
			 */
			IEvent event = eventStack.pop();
			switch (event.getEventType()) {

			/**
			 * Case of line creation
			 */
			case LINE_CREATION:
				phoneNumber = event.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
				this.addPhoneNumber(phoneNumber);	
				break;

			/**
			 * Case of line deletion
			 */
			case LINE_DELETION:
				phoneNumber = event.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
				this.removePhoneNumber(phoneNumber);				
				break;

			/**
			 * We need to know if the number is in the directory
			 */
			case PHONE_NUMBER_REQUEST:
				phoneNumber = event.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);

				// if the phone number is in the directory, we return true, else
				// we return false

				// Create a new event
				Event newEvent = new Event(EventType.PHONE_NUMBER_RESPONSE);
				newEvent.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, event.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER));
				newEvent.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, phoneNumber);
				newEvent.addAttribute(ExchangeAttributeNames.EXISTS, Boolean.toString(this.exist(phoneNumber)));

				// Send to the autocommutator the event
				AutoCommutator.getInstance().receiveEvent(newEvent);
				break;
			}
		}
	}

	/**
	 * Adds a phone number in the directory
	 * 
	 * @param phoneNumber
	 */
	public void addPhoneNumber(String phoneNumber) {
		this.directory.add(phoneNumber);
		
		System.out.println("---------DirectoryService added line for phone number : " + phoneNumber);
	}
	
	/**
	 * Removes a phone number in the directory
	 * 
	 * @param phoneNumber
	 */
	public void removePhoneNumber(String phoneNumber) {
		if (this.exist(phoneNumber)) {
			this.directory.remove(phoneNumber);
			
			System.out.println("---------DirectoryService removed line for phone number : " + phoneNumber);
			
		}
	}

	/**
	 * Tests if a phone number exists
	 * 
	 * @param phoneNumber
	 * @return if the phone number is in the directory or not
	 */
	public boolean exist(String phoneNumber) {
		
		System.out.println("---------DirectoryService requested for phone number : " + phoneNumber);
		
		return this.directory.contains(phoneNumber);
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
