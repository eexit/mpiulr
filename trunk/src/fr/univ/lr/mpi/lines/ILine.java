package fr.univ.lr.mpi.lines;

import fr.univ.lr.mpi.commutator.impl.Concentrator;
import fr.univ.lr.mpi.exceptions.LineException;
import fr.univ.lr.mpi.exceptions.PhoneNumberValidatorException;
import fr.univ.lr.mpi.handlers.MessageHandler;

/**
 * ILine.java
 *
 * @author Joris Berthelot <joris.berthelot@gmail.com>
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
	public void setConcentrator(Concentrator c);
	
	/**
	 * Gets the current line state
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @return
	 */
	public LineState getState();
	
	/**
	 * Line pick up action:
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @throws LineException 
	 */
	public void pickUp() throws LineException;
	
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
	 * @throws PhoneNumberValidatorException 
	 * @throws LineException 
	 */
	public void dialTo(String phoneNumber) throws PhoneNumberValidatorException, LineException;
	
	/**
	 * Line message sender
	 * 
	 * @param content
	 */
	public void sendMessage(String content);
	
	/**
	 * To create a transfer rule
	 * 
	 * @param toPhoneNumber
	 */
	public void addTransfertRules(String toPhoneNumber);
	
	/**
	 * To remove a transfer rule
	 */
	public void removeTransfertRules();
}
