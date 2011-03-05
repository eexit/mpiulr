package fr.univ.lr.mpi.handlers;

import fr.univ.lr.mpi.exchanges.IEvent;

/**
 * EventHandler.java
 * 
 * @author Etienne RAGONNEAU
 */
public interface EventHandler {
	/**
	 * Event listener
	 * @param event
	 */
	public void receiveEvent(IEvent event);	
}
