package fr.univ.lr.mpi.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import fr.univ.lr.mpi.commutator.impl.AutoCommutator;
import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.exchanges.impl.Event;
import fr.univ.lr.mpi.exchanges.impl.EventType;
import fr.univ.lr.mpi.exchanges.impl.ExchangeAttributeNames;
import fr.univ.lr.mpi.services.IService;

/**
 * CallTransfertService.java
 * 
 * @author FAUCHER Tony <faucher.tony85@gmail.com>
 */
public class CallTransferService extends Thread implements IService {

	/**
	 * Map to know the different transfert rule between two lines
	 */
	private Map<String, String> transferRulesTables;
	
	/**
	 * Event stack
	 */
	private Stack<IEvent> eventStack;

	/**
	 * Class constructor
	 */
	public CallTransferService() {
		this.transferRulesTables = new HashMap<String, String>();
		this.eventStack = new Stack<IEvent>();
	}

	/**
	 * Class runner
	 */
	@Override
	public void run() {
		while (true) {
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
			 * Tests if a transfer exists from a line
			 */
			case CALL_TRANSFER_REQUEST:
				String recipientPhoneNumber = event.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);
				String callerPhoneNumber = event.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);

				/**
				 * Tests if we must change the recipient
				 */
				if (this.transferRulesTables.containsKey(recipientPhoneNumber)) {
					recipientPhoneNumber = this.transferRulesTables.get(recipientPhoneNumber);
				}
				/**
				 * Creates and sends event response
				 */
				IEvent newEvent = new Event(EventType.CALL_TRANSFER_RESPONSE);
				newEvent.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, callerPhoneNumber);
				newEvent.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, recipientPhoneNumber);
				AutoCommutator.getInstance().receiveEvent(newEvent);
				break;

			/**
			 * Creates a transfer from line 1 to line 2
			 */
			case TRANSFER_CREATE:
				String oldPhoneNumber = event.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
				String newPhoneNumber = event.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);
				this.addTransferCallRule(oldPhoneNumber, newPhoneNumber);
				break;

			/**
			 * Removes a transfer from a line
			 */
			case TRANSFER_REMOVE:
				String phoneNumber = event.getAttributeValue(ExchangeAttributeNames.PHONE_NUMBER);
				this.removeTransferCallRule(phoneNumber);
				break;
			}
		}
	}

	/**
	 * Returns the transfered phone number if exists
	 * 
	 * @author FAUCHER Tony
	 * @param phoneNumber
	 * @return the phone number where you must call
	 */
	public String transferCall(String phoneNumber) {
		// if a rule about the phoneNumber exist, we return the new phone number
		if (this.transferRulesTables.containsKey(phoneNumber)) {
			return this.transferRulesTables.get(phoneNumber);
		} else {
			return phoneNumber;
		}
	}

	/**
	 * Add a rule about the originalPhoneNumber,
	 * "original phone number => new phone number"
	 * 
	 * @author FAUCHER Tony
	 * @param originalPhoneNumber
	 * @param newPhoneNumber
	 */
	public void addTransferCallRule(String originalPhoneNumber, String newPhoneNumber) {
		this.transferRulesTables.put(originalPhoneNumber, newPhoneNumber);
		
		System.out.println("---------CallTransferService added transfer rule from " + originalPhoneNumber + " to " + newPhoneNumber);
	}

	/**
	 * Removes a call transfer rule
	 * 
	 * @param originalPhoneNumber
	 */
	public void removeTransferCallRule(String originalPhoneNumber) {
		if (this.transferRulesTables.containsKey(originalPhoneNumber)) {
			this.transferRulesTables.remove(originalPhoneNumber);
			
			System.out.println("---------CallTransferService removed transfer rule for " + originalPhoneNumber);
		}
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
