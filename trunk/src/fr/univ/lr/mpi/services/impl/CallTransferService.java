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
		case CALL_TRANSFER_REQUEST:
			String recipientPhoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);

			if (this.transferRulesTables.containsKey(recipientPhoneNumber)) {
				String newRecipientPhoneNumber = this.transferRulesTables
						.get(recipientPhoneNumber);
				IEvent newEvent = new Event(EventType.CALL_TRANSFER_RESPONSE);
				newEvent.addAttributes(
						ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
						newRecipientPhoneNumber);
				AutoCommutator.getInstance().receiveEvent(newEvent);
			}

			break;
		}
	}
}
