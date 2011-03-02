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
 * Answering Service
 * 
 * @author FAUCHER Tony <faucher.tony85@gmail.com>
 */

public class AnsweringService extends Thread implements IService {

	/**
	 * List of different message from an answering Machine
	 */

	private List<AnsweringMachineMessage> messages;

	private Stack<IEvent> eventStack;

	public static final String ANSWERING_MACHINE_PHONENUMBER = "3103";

	/**
	 * Constructor of an Answering Service
	 */
	public AnsweringService() {
		this.messages = new ArrayList<AnsweringMachineMessage>();
		this.eventStack = new Stack<IEvent>();
	}

	public void run() {
		while (true) {
			if (this.eventStack.isEmpty()) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			IEvent event = this.eventStack.pop();
			switch (event.getEventType()) {
			case UNAVAILABLE_RECIPIENT:

				// Get the caller phone Number and the recipient phone number
				String callerPhoneNumber = event
						.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
				String recipientPhoneNumber = event
						.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);

				// Get the message
				String message = event
						.getAttributeValue(ExchangeAttributeNames.MESSAGE);

				// Get the date
				String dateString = event
						.getAttributeValue(ExchangeAttributeNames.DATE);
				DateFormat df = DateFormat.getDateInstance();
				Date date;

				try {
					date = df.parse(dateString);

					this.messages.add(new AnsweringMachineMessage(date,
							recipientPhoneNumber, callerPhoneNumber, message));
				} catch (ParseException e) {

					e.printStackTrace();
				}
				break;
			}
		}
	}

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

	/**
	 * 
	 */

	@Override
	public void receiveEvent(IEvent event) {
		this.eventStack.add(event);
		this.notify();
	}
}
