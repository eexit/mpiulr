package fr.univ.lr.mpi.lines.impl;

import fr.univ.lr.mpi.commutator.IConnection;
import fr.univ.lr.mpi.commutator.impl.AutoCommutator;
import fr.univ.lr.mpi.exceptions.LineException;
import fr.univ.lr.mpi.exceptions.PhoneNumberValidatorException;
import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.exchanges.impl.Message;
import fr.univ.lr.mpi.exchanges.impl.MessageType;
import fr.univ.lr.mpi.exchanges.impl.PhoneNumberValidator;
import fr.univ.lr.mpi.lines.ILine;
import fr.univ.lr.mpi.lines.LineState;

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
	private IConnection connection;

	/**
	 * Line current state
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private LineState state;

	/**
	 * Line contructor
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param number
	 * @throws PhoneNumberValidatorException 
	 */
	public Line(String number) throws PhoneNumberValidatorException {
		if (false == PhoneNumberValidator.isValid(number)) {
			throw new PhoneNumberValidatorException(PhoneNumberValidatorException.WRONG_FORMAT);
		}
		this.phoneNumber = number;
		this.state = LineState.FREE;
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
	public void setConnection(IConnection connection) {
		this.connection = connection;
		this.state = LineState.BUSY;
	}

	/**
	 * Message receiver of commutator
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param message
	 */
	public void receiveMessage(IMessage message) {
		System.out.println(message);
	}

	/**
	 * Line pick up action:
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @throws LineException 
	 */
	public void pickUp() throws LineException {
		if (!this.state.equals(LineState.FREE)) {
			throw new LineException(LineException.ERROR_BUSY);
		}
		this.state = LineState.BUSY;
		AutoCommutator.getInstance().receiveMessage(
				new Message(MessageType.PICKUP, this.phoneNumber));
	}

	/**
	 * Line hang up action
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public void hangUp() {
		this.state = LineState.FREE;
		AutoCommutator.getInstance().receiveMessage(
				new Message(MessageType.HANGUP, this.phoneNumber));
	}

	/**
	 * Dial action
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param phoneNumber
	 * @throws LineException 
	 */
	public void dialTo(String phoneNumber) throws PhoneNumberValidatorException, LineException {
		if (false == PhoneNumberValidator.isValid(phoneNumber)) {
			throw new PhoneNumberValidatorException(PhoneNumberValidatorException.WRONG_FORMAT);
		}
		
		if (!this.state.equals(LineState.BUSY)) {
			throw new LineException(LineException.ERROR_FREE);
		}
		AutoCommutator.getInstance().receiveMessage(
				new Message(MessageType.HANGUP, this.phoneNumber));
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
