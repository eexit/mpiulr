package fr.univ.lr.mpi.services;

import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.handlers.EventHandler;

/**
 * @author FAUCHER Tony <faucher.tony85@gmail.com>
 */

public interface IService extends EventHandler {

	
	public void receiveEvent(IEvent event);
	
}
