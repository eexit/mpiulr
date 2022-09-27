package fr.univ.lr.mpi.commutator;

import fr.univ.lr.mpi.handlers.EventHandler;
import fr.univ.lr.mpi.handlers.MessageHandler;

/**
 * IConnection.java
 *
 * @author Joris Berthelot
 */
public interface IConnection extends MessageHandler, EventHandler {

	/**
	 * Called phone number getter
	 * 
	 * @return the called Phone number
	 */
	public String getCalledPhoneNumber();
	
	/**
	 * Caller phone number getter
	 * 
	 * @return the caller phone number
	 */
	public String getCallerPhoneNumber();
	
	/**
	 * Connection connected state getter
	 * 
	 * @return if the connection is established
	 */
	public boolean isConnected();
}
