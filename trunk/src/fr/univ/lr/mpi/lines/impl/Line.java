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
 * 
 * MPI_PROJECT/fr.univ.lr.mpi.lines/Line.java
 * 
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 * @date Mar 1, 2011
 * 
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
	 * Message receiver of commutator
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param message
	 */
	public void receiveMessage(IMessage message) {
		System.out.println("---Line received message : " + message);
		switch (message.getMessageType()) {
			case RINGING:
				this.ringingNumber = message.getCallerPhoneNumber();
				this.isRinging = true;
				break;
			case STOP_RINGING:
				this.ringingNumber = null;
				this.isRinging = false;
			break;
			default:
				this.ringingNumber = message.getCallerPhoneNumber();
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
		if (this.state != null && !this.state.equals(LineState.FREE)) {
			return;
		}
		this.state = LineState.BUSY;

		if (!this.isRinging) {
			System.out.println("---Line pickup: " + this.phoneNumber);
			// Sends the message to the concentrator
			this.concentrator.receiveMessage(new Message(MessageType.PICKUP,
					this.phoneNumber));
		} else {
			System.out.println("---Line pickup " + this.phoneNumber + " as recipient of " + this.phoneNumber);
			// Stops the ring
			this.isRinging = false;
			// Sends the message to the concentrator
			this.concentrator.receiveMessage(new Message(
					MessageType.RECIPIENT_PICKUP, this.ringingNumber, this.phoneNumber));
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
		this.concentrator.receiveMessage(new Message(MessageType.HANGUP,
				this.phoneNumber, this.ringingNumber));
	}

	/**
	 * Dial action
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param phoneNumber
	 */
	public void dialTo(String phoneNumber) {
		if (!this.state.equals(LineState.BUSY)) {
			return;
		}

		// Sends the message to the concentrator
		this.concentrator.receiveMessage(new Message(MessageType.NUMBERING,
				this.phoneNumber, phoneNumber));
	}

	/**
	 * 
	 * @param content
	 */

	public void sendMessage(String content) {
		System.out.println("---Line sent content : " + content);
		// Sends the message to the concentrator
		this.concentrator.receiveMessage(new Message(
			MessageType.VOICE_EXCHANGE, this.phoneNumber, this.ringingNumber, content
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
	
	
	public void addTransfertRules(String toPhoneNumber)
	{
		IEvent event = new Event(EventType.CREATE_TRANSFER);
		event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, this.phoneNumber);
		event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, toPhoneNumber);
		AutoCommutator.getInstance().sendEvent(event);
	}
	
	public void removeTransfertRules()
	{
		IEvent event = new Event(EventType.REMOVE_TRANSFER);
		event.addAttribute(ExchangeAttributeNames.PHONE_NUMBER, this.phoneNumber);
		AutoCommutator.getInstance().sendEvent(event);
	}
}
