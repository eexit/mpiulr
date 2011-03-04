package fr.univ.lr.mpi.commutator;

import fr.univ.lr.mpi.handlers.EventHandler;
import fr.univ.lr.mpi.handlers.MessageHandler;

/**
 * 
 * MPI_PROJECT/fr.univ.lr.mpi.commutator/IConnection.java
 *
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 * @date Mar 1, 2011
 *
 */
public interface IConnection extends MessageHandler, EventHandler {

	/**
	 * 
	 * @return the called Phone number
	 */
	public String getCalledPhoneNumber();
	
	/**
	 * 
	 * @return the caller phone number
	 */
	public String getCallerPhoneNumber();
	
	/**
	 * 
	 * @return if the connection is established
	 */
	public boolean isConnected();
}
