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
 * Answering Service
 * 
 * @author FAUCHER Tony <faucher.tony85@gmail.com>
 */

public class AnsweringService implements IService {

	/**
	 * List of different message from an answering Machine
	 */
	
	private List<AnsweringMachineMessage> messages;

	
	/**
	 * Constructor of an Answering Service
	 */
	public AnsweringService() {
		this.messages = new ArrayList<AnsweringMachineMessage>();
	}

	// méthodes

	/**
	 * Add an Answering Machine Message
	 * 
	 * @param AnsweringMachineMessage
	 * 
	 */
	public void postMessage(AnsweringMachineMessage asm) {
		this.messages.add(asm);
	}

	/**
	 * Return the list of the answering machine message
	 * 
	 * @return message list
	 */
	public List<AnsweringMachineMessage> getMessages() {
		return this.messages;
	}

	@Override
	public void receiveEvent(IEvent event) {
		switch (event.getEventType())
		{
		case UNAVAILABLE_RECIPIENT :
			
			// Get the caller phone Number and the recipient phone number
			String callerPhoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
			String recipientPhoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);

			// Get the message 
			String message = event.getAttributeValue(ExchangeAttributeNames.MESSAGE);
			
			// Get the date
			String dateString = event
					.getAttributeValue(ExchangeAttributeNames.DATE);
			DateFormat df = DateFormat.getDateInstance();
			Date date;
			
			try{
				date = df.parse(dateString);
				
				this.messages.add(new AnsweringMachineMessage(date, recipientPhoneNumber, callerPhoneNumber, message));
			}
			catch (ParseException e) {

				e.printStackTrace();
			}

			
			break;
		}
	}
}
