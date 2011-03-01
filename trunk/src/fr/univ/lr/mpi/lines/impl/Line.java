package fr.univ.lr.mpi.lines.impl;

import fr.univ.lr.mpi.commutator.IConnection;
import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.lines.ILine;

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
	 */
	public Line(String number) {
		this.phoneNumber = number;
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
		this.setState(LineState.)
	}
	
	/**
	 * Sets the line current state
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param state
	 */
	public void setState(LineState state) {
		
	}
	
	/**
	 * Message receiver of commutator
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param message
	 */
	public void receiveMessage(IMessage message) {
		
	}
	
	/**
	 * Line pick up action:
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public void pickUp() {
		
	}
	
	/**
	 * Line hang up action
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public void hangUp() {
		
	}
	
	/**
	 * Dial action
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param phoneNumber
	 */
	public void dialTo(String phoneNumber) {
		
	}

	/**
	 * Gets the current line state
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @return
	 */
	public LineState getState() {
		return null;
	}
}
