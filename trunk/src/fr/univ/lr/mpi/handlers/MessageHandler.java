package fr.univ.lr.mpi.handlers;

import fr.univ.lr.mpi.exchanges.IMessage;

/**
 * MessageHandler.java
 * 
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 */
public interface MessageHandler {
	
	/**
	 * Message listener
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public void receiveMessage(IMessage message);
}
