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
 * @author FAUCHER Tony
 */
public class AnsweringService extends Thread implements IService {

	/**
	 * List of different message from an answering Machine
	 */
	private List<AnsweringMachineMessage> messages;
	
	/**
	 * Default welcome message
	 */
	private String receptionMessage = "Merci de laisser un message après le bip... *BIIP*";
	
	/**
	 * Default empty message
	 */
	final private String noMessage = "Vous n'avez aucun nouveau message";
	
	/**
	 * Event stack
	 */
	private Stack<IEvent> eventStack;

	/**
	 * Answering service phone number
	 */
	public static final String ANSWERING_MACHINE_PHONE_NUMBER = "3103";

	/**
	 * Constructor of an Answering Service
	 */
	public AnsweringService() {
		this.messages = new ArrayList<AnsweringMachineMessage>();
		this.eventStack = new Stack<IEvent>();
	}

	/**
	 * Welcome message getter
	 * 
	 * @return
	 */
	public String getReceptionMessage() {
		return this.receptionMessage;
	}
	
	/**
	 * Welcome message setter
	 * 
	 * @param message
	 */
	public void setReceptionMessage(String message) {
		receptionMessage = message;
	}
	
	/**
	 * Logical code to be executed by stacked events
	 */
	private void processEvent() {
		IEvent stack_event = this.eventStack.pop();
		
		final String callerPhoneNumber = stack_event.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
		final String recipientPhoneNumber = stack_event.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);
		
		switch (stack_event.getEventType()) {

		// When the answering machine is called to push a message
		case UNAVAILABLE_RECIPIENT:
			IEvent answ_push_event = new Event(EventType.ANSWERING_MACHINE_WELCOME_MESSAGE);
			answ_push_event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, callerPhoneNumber);
			answ_push_event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, recipientPhoneNumber);
			answ_push_event.addAttribute(ExchangeAttributeNames.MESSAGE, this.receptionMessage);
			AutoCommutator.getInstance().receiveEvent(answ_push_event);
			break;
		
		// When then answering machine is called to pull messages
		case ANSWERING_MACHINE_PULL_MESSAGE:
			// Creates an event which will contain all messages
			IEvent answ_content_event = new Event(EventType.ANSWERING_MESSAGE);
			answ_content_event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, callerPhoneNumber);
			answ_content_event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, callerPhoneNumber);
			
			/**
			 * TODO must remove message from message list once pulled
			 */
			if (!this.getMessages().isEmpty()) {
				for (AnsweringMachineMessage message_entry : this.getMessages()) {
					if (message_entry.getOwnerPhoneNumber().equals(callerPhoneNumber)) {
						String message_content = "Message de la part de " + message_entry.getPosterPhoneNumber() + " : " + message_entry.getMessage().toString();
						answ_content_event.addAttribute(ExchangeAttributeNames.MESSAGE, message_content);
						
						System.out.println("---------AnsweringService pulled message from " + callerPhoneNumber + " : " + message_content);
						
						AutoCommutator.getInstance().receiveEvent(answ_content_event);
					}
				}
			} else {
				
				System.out.println("---------AnsweringService pulled message from " + callerPhoneNumber + " : " + this.noMessage);
				
				answ_content_event.addAttribute(ExchangeAttributeNames.MESSAGE, this.noMessage);
				AutoCommutator.getInstance().receiveEvent(answ_content_event);
			}
			break;
		
		// When the answering machine records a new message
		case ANSWERING_MACHINE_PUSH_MESSAGE:
			String message = stack_event.getAttributeValue(ExchangeAttributeNames.MESSAGE);
			String message_date = stack_event.getAttributeValue(ExchangeAttributeNames.DATE);
			DateFormat df = DateFormat.getDateInstance();
			Date date;

			try {
				date = df.parse(message_date);
				this.messages.add(new AnsweringMachineMessage(
					date, recipientPhoneNumber, callerPhoneNumber, message
				));
				
				System.out.println("---------AnsweringService pushed message from " + callerPhoneNumber +" to " + recipientPhoneNumber + " : " + message);
			
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		}
	}
	
	/**
	 * Service runner
	 */
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
			processEvent();
		}
	}

	/**
	 * Add an Answering Machine Message
	 * 
	 * @param AnsweringMachineMessage
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
	 * Event listener
	 */
	@Override
	public synchronized void receiveEvent(IEvent event) {
		this.eventStack.add(event);
		this.notify();
	}
}
