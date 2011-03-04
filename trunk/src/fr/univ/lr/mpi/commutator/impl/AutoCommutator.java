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
		System.out.println("------Connection registered for number: "
				+ callerPhoneNumber + " (" + connection + ")");
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
		System.out.println("------Commutator forwarded event : " + event);
		for (IService service : this.services) {
			service.receiveEvent(event);
		}
	}

	/**
	 * Receives an event from all registered service
	 * 
	 * @param event
	 */
	@Override
	public synchronized void receiveEvent(IEvent event) {
		switch (event.getEventType()) {
		// When the connection timed out and needs to be killed
		case CONNECTION_DESTROY:
			Connection connection = ((Connection) getConnection(event
					.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER)));
			if (connection == null) {
				return;
			}
			connection.endConnection();
			connection.stop();
			System.out
					.println("------Connection killed for number: "
							+ event
									.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER)
							+ " (" + connection + ")");
			connections.remove(connection);
			break;
		default:
			Connection conn = (Connection) getConnection(event
					.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER));
			if (conn == null) {
				return;
			}
			conn.receiveEvent(event);
		}
	}

	/**
	 * Receives a message from lines (during the connection establishment)
	 * 
	 * @param message
	 */

	@Override
	public synchronized void receiveMessage(IMessage message) {
		// Connection handler
		Connection connection = null;
		// Caller phone number
		final String callerPhoneNumber = message.getCallerPhoneNumber();
		// Recipient phone number
		final String recipientPhoneNumber = message.getRecipientPhoneNumber();

		System.out.println("Com CN : " + callerPhoneNumber + " / RN :"
				+ recipientPhoneNumber);

		switch (message.getMessageType()) {
		// When a line is picked up
		case PICKUP:
			System.out
					.println("------Commutator received message : " + message);
			connection = (Connection) this.recoverConnection(callerPhoneNumber);
			if (null != connection) {
				connection.receiveMessage(new Message(
						MessageType.RECIPIENT_PICKUP, callerPhoneNumber,
						recipientPhoneNumber));
			} else {
				// If too many connections have been established
				if (connections.size() > MAX_CONNECTIONS) {
					this.concentrator.sendMessage(callerPhoneNumber,
							new Message(MessageType.TOO_MANY_CONNECTIONS,
									callerPhoneNumber));
					return;
				}

				// Creates a new connection
				this.launchConnection(callerPhoneNumber);
			}
			break;

		// When the connection wants to ring a line
		case IS_BUSY:
			System.out
					.println("------Commutator received message : " + message);
			// The line is busy
			/**
			 * TODO avoid to ask the line state, just check a connection
			 * existence
			 */
			if (concentrator.getActiveLine(recipientPhoneNumber).getState()
					.equals(LineState.BUSY)) {
				this.getConnection(callerPhoneNumber).receiveMessage(
						new Message(MessageType.BUSY, callerPhoneNumber,
								recipientPhoneNumber));
				return;
			}

			this.getConnection(callerPhoneNumber).receiveMessage(
					new Message(MessageType.RINGING, callerPhoneNumber,
							recipientPhoneNumber));
			break;
		// All others messages
		default:
			connection = (Connection) getConnection(callerPhoneNumber);

			if (null == connection && null != recipientPhoneNumber) {
				connection = (Connection) getConnection(recipientPhoneNumber);
			}

			if (null != connection) {
				connection.receiveMessage(message);
			}
			break;
		}
	}

	/**
	 * Tries to find an existing connection identified by the caller phonenumber
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public IConnection getConnection(String phoneNumber) {
		for (IConnection connection : connections) {
			String caller = ((Connection) connection).getCallerPhoneNumber();
			if (caller.equals(phoneNumber)) {
				System.out.println("------Connection retrieved by number: "
						+ phoneNumber + " (" + connection + ")");
				return connection;
			}
		}
		return null;
	}

	/**
	 * Tries to recover an existing connection following a fast hang up and pick
	 * up
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param phoneNumber
	 * @return
	 */
	public IConnection recoverConnection(String phoneNumber) {
		for (IConnection connection : connections) {
			String called = ((Connection) connection).getCalledPhoneNumber();
			if (((Connection) connection).isConnected()
					&& called.equals(phoneNumber)) {
				System.out.println("------Connection recovered by number: "
						+ phoneNumber + " (" + connection + ")");
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
		System.out.println("------Commutator sent message : " + message);
		concentrator.sendMessage(phoneNumber, message);
	}

	public boolean isConnected(String phoneNumber) {
		boolean connected = false;

		for (int i = 0; i < this.connections.size() && !connected; i++) {
			IConnection c = this.connections.get(i);
			if (c != null) {
				String called = c.getCalledPhoneNumber();
				String caller = c.getCallerPhoneNumber();
				boolean connect = c.isConnected();

				if ((called != null && caller != null)
						&& (called.equals(phoneNumber) || caller
								.equals(phoneNumber)) && connect) {
					System.out.println("---CONNECTION----\n" + c
							+ "------------");
					connected = true;
				}
			}
		}
		return connected;
	}

}
