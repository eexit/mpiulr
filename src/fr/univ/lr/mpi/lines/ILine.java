package fr.univ.lr.mpi.lines;

import fr.univ.lr.mpi.commutator.IConnection;
import fr.univ.lr.mpi.handlers.MessageHandler;

/**
 * 
 * MPI_PROJECT/fr.univ.lr.mpi.lines/ILine.java
 *
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 * @date Mar 1, 2011
 *
 */
public interface ILine extends MessageHandler {
	
	/**
	 * Gets the line phone number
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @return
	 */
	public String getPhoneNumber();
	
	/**
	 * Sets a new connection to the line
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param connection
	 */
	public void setConnection(IConnection connection);
	
	/**
	 * Gets the current line state
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @return
	 */
	public LineState getState();
	
	/**
	 * Sets the line current state
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param state
	 */
	public void setState(LineState state);
	
	/**
	 * Line pick up action:
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public void pickUp();
	
	/**
	 * Line hang up action
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public void hangUp();
	
	/**
	 * Dial action
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param phoneNumber
	 */
	public void dialTo(String phoneNumber);
}
