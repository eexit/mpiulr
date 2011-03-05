package fr.univ.lr.mpi.lines.impl;

import fr.univ.lr.mpi.commutator.impl.AutoCommutator;
import fr.univ.lr.mpi.commutator.impl.Concentrator;
import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.exchanges.impl.Event;
import fr.univ.lr.mpi.exchanges.impl.EventType;
import fr.univ.lr.mpi.exchanges.impl.ExchangeAttributeNames;
import fr.univ.lr.mpi.exchanges.impl.Message;
import fr.univ.lr.mpi.exchanges.impl.MessageType;
import fr.univ.lr.mpi.exchanges.impl.PhoneNumberValidator;
import fr.univ.lr.mpi.lines.ILine;
import fr.univ.lr.mpi.lines.LineState;
import fr.univ.lr.mpi.simulation.PhoneWidget;

/**
 * Line.java
 * 
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 */
public class Line implements ILine {

	/**
	 * Phone number assigned to the line
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private String phoneNumber;

	/**
	 * The caller phone number while the line is ringing
	 */
	private String ringingNumber;
	
	/**
	 * The recipient phone number
	 */
	private String recipientNumber;

	/**
	 * Line current state
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private LineState state;

	/**
	 * Concentrator container
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private Concentrator concentrator;

	/**
	 * Phone widget containter
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private PhoneWidget phone;

	/**
	 * Boolean value of the ringing state of the line
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private Boolean isRinging;

	/**
	 * Line contructor
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param number
	 */
	public Line(String number) {
		if (false == PhoneNumberValidator.isValid(number)) {
			return;
		}
		this.phoneNumber = number;
		this.state = LineState.FREE;
		this.isRinging = false;
	}

	/**
	 * Phone widget setter
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param phone
	 */
	public void setPhone(PhoneWidget phone) {
		this.phone = phone;
	}

	/**
	 * Concentrator setter
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param concentrator
	 */
	public void setConcentrator(Concentrator concentrator) {
		this.concentrator = concentrator;
	}

	/**
	 * Gets the line phone number
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @return
	 */
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	/**
	 * Message listener
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param message
	 */
	public void receiveMessage(IMessage message) {
		
		System.out.println("---Line " + this.phoneNumber + " received message : " + message);
		
		switch (message.getMessageType()) {
			case SEARCH:
				this.recipientNumber = message.getRecipientPhoneNumber();
				break;
			case RINGING:
				this.ringingNumber = message.getCallerPhoneNumber();
				this.isRinging = true;
				break;
			case STOP_RINGING:
				this.ringingNumber = null;
				this.isRinging = false;
				break;
			case CONNECTION_ESTABLISHED:
				this.state = LineState.BUSY;
				this.recipientNumber = message.getRecipientPhoneNumber();
				break;
			case CONNECTION_CLOSED:
				this.recipientNumber = null;
				break;
			default:
				// Set the recipient number for all message only if the messages comes
				// from a real recipient (not services)
				if (!this.phoneNumber.equals(message.getCallerPhoneNumber())) {
					this.recipientNumber = message.getCallerPhoneNumber();
				}
				break;
		}
		phone.appendLog(message);
	}

	/**
	 * Line pick up action:
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public void pickUp() {
		if (this.state.equals(LineState.BUSY)) {
			return;
		}
		
		this.state = LineState.BUSY;
		
		if (!this.isRinging) {
			
			System.out.println("---Line pickup: " + this.phoneNumber);
			
			// Sends the message to the concentrator
			this.concentrator.receiveMessage(new Message(
				MessageType.PICKUP, this.phoneNumber, this.recipientNumber
			));
		} else {
			
			System.out.println("---Line pickup " + this.phoneNumber + " as recipient of " + this.ringingNumber);
			
			// Stops the ring
			this.isRinging = false;
			
			// Sets the recipient
			this.recipientNumber = this.ringingNumber;
			
			// Sends the message to the concentrator
			this.concentrator.receiveMessage(new Message(
				MessageType.RECIPIENT_PICKUP, this.ringingNumber, this.phoneNumber
			));
		}
	}

	/**
	 * Line hang up action
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public void hangUp() {
		if (this.state.equals(LineState.FREE)) {
			return;
		}

		this.state = LineState.FREE;

		System.out.println("---Line hangup: " + this.phoneNumber);
		
		// Sends the message to the concentrator
		// We put the last ringing phone in case where we want to recover the connection
		this.concentrator.receiveMessage(new Message(
			MessageType.HANGUP, this.phoneNumber, this.ringingNumber
		));
		
		this.recipientNumber = null;
	}

	/**
	 * Dial action
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param phoneNumber
	 */
	public void dialTo(String phoneNumber) {
		// The line must be piked up and not within a communication to dial
		if (this.state.equals(LineState.FREE) || null != this.recipientNumber) {
			System.out.println("---Line " + this.phoneNumber + " can't dial because not FREE or already in a communication");
			return;
		}
		
		// Sends the message to the concentrator
		this.concentrator.receiveMessage(new Message(
			MessageType.NUMBERING, this.phoneNumber, phoneNumber
		));
		
		this.ringingNumber = phoneNumber;
	}

	/**
	 * Sends a message
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param content
	 */
	public void sendMessage(String content) {
		
		// Can't send any message if the line is not picked up
		if (this.state.equals(LineState.FREE)) {
			System.out.println("---Line " + this.phoneNumber + " can't send message because not connected");
			return;
		}
		
		System.out.println("---Line " + this.phoneNumber + " sent content : " + content);
		
		// Sends the message to the concentrator
		this.concentrator.receiveMessage(new Message(
			MessageType.VOICE_EXCHANGE, this.phoneNumber, this.recipientNumber, content
		));
	}

	/**
	 * Gets the current line state
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @return
	 */
	public LineState getState() {
		return this.state;
	}

	/**
	 * Adds a transfer rule
	 * 
	 * @author Tony FAUCHER
	 * @param toPhoneNumber
	 */
	public void addTransfertRules(String toPhoneNumber) {
		IEvent event = new Event(EventType.TRANSFER_CREATE);
		event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, this.phoneNumber);
		event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, toPhoneNumber);
		AutoCommutator.getInstance().sendEvent(event);
	}

	/**
	 * Removes all transfer rules
	 * 
	 * @author Tony FAUCHER
	 */
	public void removeTransfertRules() {
		IEvent event = new Event(EventType.TRANSFER_REMOVE);
		event.addAttribute(ExchangeAttributeNames.PHONE_NUMBER, this.phoneNumber);
		AutoCommutator.getInstance().sendEvent(event);
	}
}