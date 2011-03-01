package fr.univ.lr.mpi.commutator.impl;

import java.util.List;

import fr.univ.lr.mpi.commutator.IConnection;
import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.handlers.EventHandler;
import fr.univ.lr.mpi.handlers.MessageHandler;
import fr.univ.lr.mpi.lines.ILine;
import fr.univ.lr.mpi.services.IService;

/**
 * 
 * @author Elian ORIOU
 * 
 */
public class AutoCommutator implements MessageHandler, EventHandler {

	private static final int MAX_CONNECTIONS = 10;

	private List<IConnection> connections;
	private List<ILine> lines;
	private List<IService> services;

	private int getActiveConnections() {
		return -1;
	}

	private void registerService(IService service) {

	}

	private void unregisterEvent(IService service) {

	}

	private void registerLine(ILine line) {

	}

	private void unregisterLine(ILine line) {

	}

	@Override
	public void receiveEvent(IEvent event) {
		// TODO Auto-generated method stub

	}

	private void sendEvent(IEvent event) {

	}

	@Override
	public void receiveMessage(IMessage message) {
		// TODO Auto-generated method stub

	}

}
