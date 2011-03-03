package fr.univ.lr.mpi.services.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import fr.univ.lr.mpi.commutator.impl.AutoCommutator;
import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.exchanges.impl.Event;
import fr.univ.lr.mpi.exchanges.impl.EventType;
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

	private String receptionMessage;

	private Stack<IEvent> eventStack;

	public static final String ANSWERING_MACHINE_PHONENUMBER = "3103";

	/**
	 * Constructor of an Answering Service
	 */
	public AnsweringService() {
		this.messages = new ArrayList<AnsweringMachineMessage>();
		this.eventStack = new Stack<IEvent>();
	}

	public String getReceptionMessage() {
		return this.receptionMessage;
	}

	public void setReceptionMessage(String message) {
		receptionMessage = message;
	}

	private void processEvent() {
		// TODO
		IEvent event = this.eventStack.pop();
		switch (event.getEventType()) {

		case UNAVAILABLE_RECIPIENT:
			// en envoi le message d'acceuil
			IEvent e1 = new Event(EventType.ANSWERING_MACHINE_DIALING);
			e1.addAttribute("message", receptionMessage);
			e1
					.addAttribute(
							event
									.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER),
							receptionMessage);
			AutoCommutator.getInstance().receiveEvent(e1);
			break;

		case GET_ANSWERING_MACHINE_MESSAGE:
			// cas lecture des message
			IEvent e2;
			for (AnsweringMachineMessage l : messages) {
				e2 = new Event(EventType.ANSWERING_MACHINE_DIALING);
				e2
						.addAttribute(
								event
										.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER),
								receptionMessage);
				e2.addAttribute("message", l.getMessage().toString());
			}
			break;

		case ANSWERING_MACHINE_DIALING:
			// on ajoute le message
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
			// traitement des events
			processEvent();
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
	public synchronized void receiveEvent(IEvent event) {
		this.eventStack.add(event);
		this.notify();
	}
}
