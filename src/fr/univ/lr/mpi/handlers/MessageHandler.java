package fr.univ.lr.mpi.handlers;

import fr.univ.lr.mpi.exchanges.IMessage;

/**
 * MessageHandler.java
 * 
 * @author Joris Berthelot
 */
public interface MessageHandler {
	
	/**
	 * Message listener
	 * 
	 * @author Joris Berthelot
	 */
	public void receiveMessage(IMessage message);
}
