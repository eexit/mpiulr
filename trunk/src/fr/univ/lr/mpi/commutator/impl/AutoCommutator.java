package fr.univ.lr.mpi.commutator.impl;

import java.util.ArrayList;
import java.util.List;

import fr.univ.lr.mpi.commutator.IConnection;
import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.exchanges.impl.Event;
import fr.univ.lr.mpi.exchanges.impl.EventType;
import fr.univ.lr.mpi.exchanges.impl.ExchangeAttributeNames;
import fr.univ.lr.mpi.exchanges.impl.Message;
import fr.univ.lr.mpi.exchanges.impl.MessageType;
import fr.univ.lr.mpi.handlers.EventHandler;
import fr.univ.lr.mpi.handlers.MessageHandler;
import fr.univ.lr.mpi.lines.LineState;
import fr.univ.lr.mpi.services.IService;
import fr.univ.lr.mpi.services.impl.AnsweringService;
import fr.univ.lr.mpi.services.impl.BillingService;
import fr.univ.lr.mpi.services.impl.CallTransferService;
import fr.univ.lr.mpi.services.impl.DirectoryService;
import fr.univ.lr.mpi.simulation.MessageObserver;

/**
 * The AutoCommunicator Object, the central point of communications between
 * lines
 * 
 * @version 1.0
 * @author Elian ORIOU <elian.oriou@gmail.com>
 */

public class AutoCommutator implements MessageHandler, EventHandler {

	private static AutoCommutator INSTANCE;
	private Concentrator concentrator;
	private int MAX_CONNECTIONS;

	private List<IConnection> connections;
	private List<IService> services;

	private AutoCommutator() {
		this(10);
	}

	private AutoCommutator(int maxConnections) {
		this.connections = new ArrayList<IConnection>();
		this.services = new ArrayList<IService>();
		this.MAX_CONNECTIONS = maxConnections;

		initServices();
	}

	public void stop() {
		for (int i = 0; i < services.size(); i++) {
			Thread t = (Thread) services.get(i);
			t.stop();
		}
	}

	private void initServices() {
		DirectoryService directory = new DirectoryService();
		directory.start();
		registerService(directory);

		AnsweringService answering = new AnsweringService();
		answering.start();
		registerService(answering);

		BillingService billing = new BillingService();
		billing.start();
		registerService(billing);

		CallTransferService callTransfer = new CallTransferService();
		callTransfer.start();
		registerService(callTransfer);

		MessageObserver messageObserver = new MessageObserver();
		messageObserver.start();
		registerService(messageObserver);

		/* Adding answering machine number */
		IEvent event = new Event(EventType.LINE_CREATION);
		event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, "3103");
		this.sendEvent(event);

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
	 * Instantiates a new connection thread and pull it into the pool of threads
	 * 
	 * @param callerPhoneNumber
	 *            the caller phone number
	 */

	private void launchConnection(String callerPhoneNumber) {
		Connection connection = new Connection(callerPhoneNumber);
		connection.start();
		connections.add(connection);
	}

	/**
	 * Returns the number of active connections (it must not exceed the
	 * MAX_CONNECTIONS)
	 * 
	 * @return The active number of connections
	 */

	public int getActiveConnections() {
		return this.concentrator.getActiveLines().size();
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
	 * 
	 * @return
	 */

	public List<IService> getServices() {
		return this.services;
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
	public synchronized void receiveEvent(IEvent event) {
		switch (event.getEventType()) {
		case CONNECTION_DESTROYED:
			Connection c = ((Connection) getConnection(event
					.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER)));
			c.stop();
			connections.remove(c);
			break;
		default:
			getConnection(
					event
							.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER))
					.receiveEvent(event);
		}

	}

	/**
	 * Receives a message from lines (during the connection establishment)
	 * 
	 * @param message
	 *            The message sent by lines
	 */

	@Override
	public synchronized void receiveMessage(IMessage message) {
		String callerPhoneNumber = message.getCallerPhoneNumber();
		switch (message.getMessageType()) {
		case PICKUP:
			// When pickup from caller => send a tone to it to notify the
			// connection
			if (callerPhoneNumber == null) {
				return;
			}
			/* If too many connections have been established */
			if (connections.size() > MAX_CONNECTIONS) {

				this.concentrator.sendMessage(callerPhoneNumber, new Message(
						MessageType.TOO_MANY_CONNECTIONS, callerPhoneNumber,
						null));
				return;
			}

			launchConnection(callerPhoneNumber);
			this.concentrator.sendMessage(callerPhoneNumber, new Message(
					MessageType.BACKTONE, callerPhoneNumber, null));
			break;
		/* When checking about the recipient line state before ring to his phone */
		case RING:
			String recipientNumber = message.getRecipientPhoneNumber();
			if (concentrator.getActiveLine(recipientNumber).getState().equals(
					LineState.BUSY)) {
				return;
			}
			if (callerPhoneNumber != null) {
				getConnection(callerPhoneNumber).receiveMessage(
						new Message(MessageType.RINGING, callerPhoneNumber,
								recipientNumber));
			} else if (recipientNumber != null) {
				getConnection(recipientNumber).receiveMessage(
						new Message(MessageType.RINGING, callerPhoneNumber,
								recipientNumber));
			}
			break;
		case VOICE_EXCHANGE:
			String recipient = message.getRecipientPhoneNumber();
			concentrator.sendMessage(recipient, message);
			break;
		default:
			/*
			 * By default the message is dispatched to the concerned connection
			 * service
			 */
			if (message.getCallerPhoneNumber() != null) {
				getConnection(message.getCallerPhoneNumber()).receiveMessage(
						message);
			} else if (message.getRecipientPhoneNumber() != null) {
				getConnection(message.getRecipientPhoneNumber())
						.receiveMessage(message);
			}
			break;
		}
	}

	/**
	 * 
	 * @param phoneNumber
	 * @return
	 */

	public IConnection getConnection(String phoneNumber) {
		for (IConnection connection : connections) {
			String caller = ((Connection) connection).getCallerPhoneNumber();
			String recipient = ((Connection) connection)
					.getRecipientPhoneNumber();
			if ((caller != null && caller.equals(phoneNumber))
					| (recipient != null && recipient.equals(phoneNumber))) {
				return connection;
			}
		}
		return null;
	}

	/**
	 * Sets a concentrator to the PABX
	 * 
	 * @param concentrator
	 */

	public void setConcentrator(Concentrator concentrator) {
		this.concentrator = concentrator;
	}

	/**
	 * Sends a message to a line
	 * 
	 * @param phoneNumber
	 * @param message
	 */

	public synchronized void sendMessage(String phoneNumber, IMessage message) {
		concentrator.sendMessage(phoneNumber, message);
	}
}
