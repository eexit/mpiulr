package fr.univ.lr.mpi.commutator.impl;

import java.util.ArrayList;
import java.util.List;

import fr.univ.lr.mpi.commutator.IConnection;
import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.exchanges.impl.Event;
import fr.univ.lr.mpi.exchanges.impl.EventType;
import fr.univ.lr.mpi.exchanges.impl.ExchangeAttributeNames;
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

	private int MAX_CONNECTIONS;

	private List<IConnection> connections;
	private List<ILine> lines;
	private List<IService> services;

	private AutoCommutator() {
		this(10);
	}

	private AutoCommutator(int maxConnections) {
		this.connections = new ArrayList<IConnection>();
		this.lines = new ArrayList<ILine>();
		this.services = new ArrayList<IService>();
		this.MAX_CONNECTIONS = maxConnections;
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

	/* Private Methods */

	/**
	 * Instantiates a new connection thread and pull it into the pool of threads
	 */

	private void launchConnection(String callPhoneNumber) {
		/* new Connection(); */
		IConnection connection = new Connection();
		connections.add(connection);
	}

	/* Public Methods */

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
		IEvent event = new Event(EventType.LINE_CREATION);
		event.addAttributes(ExchangeAttributeNames.CALLER_PHONE_NUMER, line
				.getPhoneNumber());
		this.sendEvent(event);
		this.lines.add(line);
	}

	/**
	 * Unregisters a phone line
	 * 
	 * @param line
	 *            The service to unregister
	 */

	public void unregisterLine(ILine line) {
		IEvent event = new Event(EventType.LINE_DELETION);
		event.addAttributes(ExchangeAttributeNames.CALLER_PHONE_NUMER, line
				.getPhoneNumber());
		this.sendEvent(event);
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
		switch (event.getEventType()) {
		case CONNECTION_ESTABLISHED:
			break;
		case CONNECTION_CLOSED:
			break;
		case PHONE_NUMBER_REQUEST:
			break;
		}
	}

	/**
	 * Receives a message from lines
	 * 
	 * @param message
	 *            The message sent by lines
	 */

	@Override
	public void receiveMessage(IMessage message) {
		switch (message.getMessageType()) {
		case PICKUP:
			break;
		case BACKTONE:
			break;
		case NUMEROTATION:
			break;
		case SEARCH:
			break;
		case RING:
			break;
		case ECHO:
			break;
		case VOICE_EXCHANGE:
			break;
		case HANGUP:
			break;
		default:
			break;
		}
	}
}
