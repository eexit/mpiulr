package fr.univ.lr.mpi.services.impl;

import java.util.HashMap;
import java.util.Map;

import fr.univ.lr.mpi.commutator.impl.AutoCommutator;
import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.exchanges.impl.Event;
import fr.univ.lr.mpi.exchanges.impl.EventType;
import fr.univ.lr.mpi.exchanges.impl.ExchangeAttributeNames;
import fr.univ.lr.mpi.services.IService;

/**
 * Call Transfert Service
 * 
 * @author FAUCHER Tony <faucher.tony85@gmail.com>
 * 
 */
public class CallTransferService implements IService {

	/**
	 * Map to know the different transfert rule between two lines
	 */
	private Map<String, String> transferRulesTables;

	/**
	 * Constructor
	 */
	public CallTransferService() {
		this.transferRulesTables = new HashMap<String, String>();
	}

	/**
	 * @author FAUCHER Tony
	 * @param phoneNumber
	 * @return the phone number where you must call
	 */
	public String transferCall(String phoneNumber) {
		// if a rule about the phoneNumber exist, we return the new phone number
		if (this.transferRulesTables.containsKey(phoneNumber)) {
			return this.transferRulesTables.get(phoneNumber);
		}
		// else, we return the old phone number
		else
			return phoneNumber;
	}

	/**
	 * Add a rule about the originalPhoneNumber,
	 * "original phone number => new phone number"
	 * 
	 * @author FAUCHER Tony
	 * @param originalPhoneNumber
	 * @param newPhoneNumber
	 */
	public void addTransferCallRule(String originalPhoneNumber,
			String newPhoneNumber) {
		this.transferRulesTables.put(originalPhoneNumber, newPhoneNumber);
	}

	/**
	 * Remove a call transfert rule
	 * 
	 * @param originalPhoneNumber
	 */
	public void removeTransferCallRule(String originalPhoneNumber) {
		if (this.transferRulesTables.containsKey(originalPhoneNumber)) {
			this.transferRulesTables.remove(originalPhoneNumber);
		}
	}

	@Override
	public void receiveEvent(IEvent event) {
		switch (event.getEventType()) {
		/**
		 * Test if a transfert exist from a line
		 */
		case CALL_TRANSFER_REQUEST:
			String recipientPhoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);
			String newRecipientPhoneNumber = recipientPhoneNumber;
			if (this.transferRulesTables.containsKey(recipientPhoneNumber)) {
				newRecipientPhoneNumber = this.transferRulesTables
						.get(recipientPhoneNumber);
			}
			IEvent newEvent = new Event(EventType.CALL_TRANSFER_RESPONSE);
			newEvent
					.addAttribute(
							ExchangeAttributeNames.CALLER_PHONE_NUMBER,
							event
									.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER));
			newEvent.addAttribute(
					ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
					newRecipientPhoneNumber);
			AutoCommutator.getInstance().receiveEvent(newEvent);

			break;

		/**
		 * Create a transfer from line 1 to line 2
		 */
		case CREATE_TRANSFER:
			String oldPhoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
			String newPhoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);
			this.addTransferCallRule(oldPhoneNumber, newPhoneNumber);
			break;

		/**
		 * Remove a transfer from a line
		 */
		case REMOVE_TRANSFER:
			String phoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
			this.removeTransferCallRule(phoneNumber);
			break;
		}
	}
}
