package fr.univ.lr.mpi.commutator.impl;

import java.util.ArrayList;
import java.util.List;

import fr.univ.lr.mpi.commutator.IConnection;
import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.handlers.EventHandler;
import fr.univ.lr.mpi.handlers.MessageHandler;
import fr.univ.lr.mpi.lines.ILine;
import fr.univ.lr.mpi.services.IService;

/**
 * The AutoCommunicator Object, the central point of communications between
 * lines
 * 
 * @version 1.0
 * @author Elian ORIOU <elian.oriou@gmail.com>
 */

public class AutoCommutator implements MessageHandler, EventHandler {

	private static AutoCommutator INSTANCE;

	private static final int MAX_CONNECTIONS = 10;

	private List<IConnection> connections;
	private List<ILine> lines;
	private List<IService> services;

	private AutoCommutator() {
		this.connections = new ArrayList<IConnection>();
		this.lines = new ArrayList<ILine>();
		this.services = new ArrayList<IService>();
	}

	/**
	 * Singleton Pattern Method
	 * 
	 * @return the current instance (with static access)
	 */

	public static AutoCommutator getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AutoCommutator();
		}
		return INSTANCE;
	}

	/**
	 * Returns the number of active connections (it must not exceed the
	 * MAX_CONNECTIONS)
	 * 
	 * @return The active number of connections
	 */

	public int getActiveConnections() {
		return this.connections.size();
	}

	/**
	 * Registers a service into the service pool
	 * 
	 * @param service
	 *            The service to register
	 */

	public void registerService(IService service) {
		this.services.add(service);
	}

	/**
	 * Unregisters a service from the service pool
	 * 
	 * @param service
	 *            The service to unregister
	 */

	public void unregisterService(IService service) {
		this.services.remove(service);
	}

	/**
	 * Registers a phone line
	 * 
	 * @param line
	 *            The new phone line
	 */

	public void registerLine(ILine line) {
		this.lines.add(line);
	}

	/**
	 * Unregisters a phone line
	 * 
	 * @param line
	 *            The service to unregister
	 */

	public void unregisterLine(ILine line) {
		this.lines.remove(line);
	}

	/**
	 * Send an event to all registered services
	 * 
	 * @param event
	 *            The event to be sent
	 */

	public void sendEvent(IEvent event) {
		for (IService service : this.services) {
			service.receiveEvent(event);
		}
	}

	/**
	 * Receives an event from all registered service
	 * 
	 * @param event
	 *            The received event
	 */

	@Override
	public void receiveEvent(IEvent event) {

	}

	/**
	 * 
	 */

	@Override
	public void receiveMessage(IMessage message) {

	}
}
