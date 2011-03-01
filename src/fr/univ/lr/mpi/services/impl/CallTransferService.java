package fr.univ.lr.mpi.services.impl;

import java.util.HashMap;
import java.util.Map;

import fr.univ.lr.mpi.services.IService;

/**
 * Call Transfert Service
 * @author FAUCHER Tony
 *
 */
public class CallTransferService implements IService {

	// attribute
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
	 * @author FAUCHER Tony
	 * Add a rule about the originalPhoneNumber, "original phone number => new phone number"
	 * @param originalPhoneNumber
	 * @param newPhoneNumber
	 */
	public void addTransferCallRule(String originalPhoneNumber,
			String newPhoneNumber) {
		this.transferRulesTables.put(originalPhoneNumber, newPhoneNumber);
	}
	
	/**
	 * remove a call transfert rule
	 * @param originalPhoneNumber
	 */
	public void removeTransferCallRule(String originalPhoneNumber)
	{
		if(this.transferRulesTables.containsKey(originalPhoneNumber)){
			this.transferRulesTables.remove(originalPhoneNumber);
		}
		
	}

}
