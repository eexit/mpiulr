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

	/**
	 * Connection container
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	// private IConnection connection;

	/**
	 * Line current state
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private LineState state;

	private Concentrator concentrator;
	
	private PhoneWidget phone;

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
	}

	public void setPhone(PhoneWidget p)
	{
		phone = p;
	}
	
	
	public void setConcentrator(Concentrator c) {
		concentrator = c;
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
	 * Sets a new connection to the line
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param connection
	 */
	// public void setConnection(IConnection connection) {
	// this.connection = connection;
	// this.state = LineState.BUSY;
	// }

	/**
	 * Message receiver of commutator
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param message
	 */
	public void receiveMessage(IMessage message) {
		System.out.println("Line receive message: " + message);
		System.out.println(message);
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

		concentrator.receiveMessage(new Message(MessageType.PICKUP,
				this.phoneNumber, null));
		// AutoCommutator.getInstance().receiveMessage(
		// new Message(MessageType.PICKUP, this.phoneNumber, null));
	}

	/**
	 * Line hang up action
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public void hangUp() {
		this.state = LineState.FREE;

		concentrator.receiveMessage(new Message(MessageType.HANGUP,
				this.phoneNumber, null));

		// AutoCommutator.getInstance().receiveMessage(
		// new Message(MessageType.HANGUP, this.phoneNumber, null));
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
		concentrator.receiveMessage(new Message(MessageType.NUMBERING,
				this.phoneNumber, null));

		// AutoCommutator.getInstance().receiveMessage(
		// new Message(MessageType.NUMBERING, this.phoneNumber, null));
	}

	public void sendMessage(String content) {
		System.out.println("Line send content: " + content);
		concentrator.receiveMessage(new Message(MessageType.VOICE_EXCHANGE,
				this.getPhoneNumber(), null, content));
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
