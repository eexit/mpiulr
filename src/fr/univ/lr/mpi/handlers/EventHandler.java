package fr.univ.lr.mpi.handlers;

import fr.univ.lr.mpi.exchanges.IEvent;

public interface EventHandler {

	
	public void receiveEvent(IEvent event);
	
}
