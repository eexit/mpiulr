package fr.univ.lr.mpi.lines.impl;

import fr.univ.lr.mpi.commutator.impl.Concentrator;
import fr.univ.lr.mpi.exchanges.IMessage;
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
	
	private String dialToPhoneNumber;

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
		this.dialToPhoneNumber = "";
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
		System.out.println("Line (" + this.phoneNumber + ") receive message: "
				+ message);
		switch (message.getMessageType()) {
		case RINGING:
			this.isRinging = true;
			this.dialToPhoneNumber = message.getCallerPhoneNumber();
			break;
		case VOICE_EXCHANGE:
			System.out.println("--------------VOICE EXCHANGE (from "
					+ message.getCallerPhoneNumber() + ")--------------");
			
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
			// Sends the message to the concentrator
			this.concentrator.receiveMessage(new Message(
				MessageType.PICKUP, this.phoneNumber
			));
			
		} else {
			// Stops the ring
			this.isRinging = false;
			// Sends the message to the concentrator
			this.concentrator.receiveMessage(new Message(
				MessageType.RECIPIENT_PICKUP, null, this.phoneNumber
			));
		}
	}

	/**
	 * 
	 * @return the dial phone number
	 */
	public String getDialPhone()
	{
		return this.dialToPhoneNumber;
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
		this.dialToPhoneNumber = "";
		// Sends the message to the concentrator
		this.concentrator.receiveMessage(new Message(
			MessageType.HANGUP, this.phoneNumber
		));
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
		
		this.dialToPhoneNumber = phoneNumber;
		// Sends the message to the concentrator
		this.concentrator.receiveMessage(new Message(
			MessageType.NUMBERING, this.phoneNumber, phoneNumber
		));
	}
	
	/**
	 * 
	 * @param content
	 */

	public void sendMessage(String content) {
		if(!this.dialToPhoneNumber.equals("")){
			System.out.println("Line send content: " + content);
			
			IMessage message = new Message(
				MessageType.VOICE_EXCHANGE, this.getPhoneNumber(), this.dialToPhoneNumber, content
			);
			
			// Sends the message to the concentrator
			this.concentrator.receiveMessage(message);
		}
		
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
}
