package fr.univ.lr.mpi.handlers;

import fr.univ.lr.mpi.exchanges.IMessage;

public interface MessageHandler {
	
	/**
	 * Message receiver from commutator container
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public void receiveMessage(IMessage message);
}
