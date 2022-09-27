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
 * AutoCommutator.java
 * 
 * @author Elian ORIOU
 */
public class AutoCommutator implements MessageHandler, EventHandler {
	
	/**
	 * Singleton definition
	 */
	private static AutoCommutator INSTANCE;
	
	/**
	 * Concentrator container
	 */
	private Concentrator concentrator;
	
	/**
	 * Maximal number of connection allowed
	 */
	private int MAX_CONNECTIONS;
	
	/**
	 * Connection list container
	 */
	private List<IConnection> connections;
	
	/**
	 * Service list container
	 */
	private List<IService> services;
	
	/**
	 * Class constructor with the max number connection as parameter
	 * @param maxConnections
	 */
	private AutoCommutator(int maxConnections) {
		this.connections = new ArrayList<IConnection>();
		this.services = new ArrayList<IService>();
		this.MAX_CONNECTIONS = maxConnections;

		this.initServices();
	}
	
	/**
	 * Stops all services
	 */
	public void stop() {
		for (int i = 0; i < services.size(); i++) {
			Thread t = (Thread) services.get(i);
			t.stop();
		}
	}
	
	/**
	 * Service initializer
	 */
	private void initServices() {
		/**
		 * TODO refactor code to make dependency injection here instead of non testable code
		 */
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
		event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, AnsweringService.ANSWERING_MACHINE_PHONE_NUMBER);
		this.sendEvent(event);
	}

	/**
	 * Singleton Pattern Method
	 * 
	 * @return the current instance (with static access)
	 */

	public static AutoCommutator getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AutoCommutator(10);
		}
		return INSTANCE;
	}

	/**
	 * Instantiates a new connection thread and pull it into the pool of threads
	 * 
	 * @param callerPhoneNumber
	 */
	private void launchConnection(String callerPhoneNumber) {
		
		System.out.println("------Commutator created connection for caller number : " + callerPhoneNumber);
		
		/**
		 * TODO adds a test to check if any connection already exist
		 */
		
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
	 */
	public void registerService(IService service) {
		this.services.add(service);
	}

	/**
	 * Service getter
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
	 */
	public void unregisterService(IService service) {
		this.services.remove(service);
	}

	/**
	 * Send an event to all registered services
	 * 
	 * @param event
	 */
	public void sendEvent(IEvent event) {
		
		System.out.println("------Commutator dispatched event : " + event);
		
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
				Connection connection = ((Connection) getConnection(event.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER)));
				connection.killsTimer();
				connection.stop();
				
				System.out.println("------Commutator killed connection for number : "
					+ event.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER));
				
				connections.remove(connection);
				break;
			default:
				Connection conn = (Connection) getConnection(event.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER));
				conn.receiveEvent(event);
				break;
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

		switch (message.getMessageType()) {
		// When a line is picked up
		case PICKUP:
			
			System.out.println("------Commutator received message : " + message);
			
			connection = (Connection) this.recoverConnection(callerPhoneNumber);
			if (null != connection && connection.isConnected()) {
				connection.receiveMessage(new Message(MessageType.RECIPIENT_PICKUP, callerPhoneNumber, recipientPhoneNumber));
			} else {
				// If too many connections have been established
				if (connections.size() > MAX_CONNECTIONS) {
					this.concentrator.sendMessage(callerPhoneNumber, new Message(
						MessageType.TOO_MANY_CONNECTIONS, callerPhoneNumber
					));
					return;
				}

				// Creates a new connection
				this.launchConnection(callerPhoneNumber);
			}
			break;

		// When the connection wants to ring a line
		case IS_BUSY:
			
			System.out.println("------Commutator received message : " + message);
			
			// The line is busy
			/**
			 * TODO avoid to ask the line state, just check a connection
			 * existence
			 */
			if (concentrator.getActiveLine(recipientPhoneNumber).getState().equals(LineState.BUSY)) {
				this.getConnection(callerPhoneNumber).receiveMessage(new Message(
					MessageType.BUSY, callerPhoneNumber, recipientPhoneNumber
				));
				return;
			}

			this.getConnection(callerPhoneNumber).receiveMessage(new Message(
				MessageType.RINGING, callerPhoneNumber, recipientPhoneNumber
			));
			break;
		// All others messages
		default:
			connection = (Connection) getConnection(callerPhoneNumber);

			if (null == connection && null != recipientPhoneNumber) {
				connection = (Connection) getConnection(recipientPhoneNumber);
			}

			if (null != connection) {
				connection.receiveMessage(message);
			} else {
				
				System.err.println("------Commutator didn't found any connection for caller : "  + callerPhoneNumber);
			
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
				
				System.out.println("------Commutator retrieved connection for caller number : " + phoneNumber);
				
				return connection;
			}
		}
		return null;
	}

	/**
	 * Tries to recover an existing connection following a fast hang up and pick
	 * up
	 * 
	 * @author Joris Berthelot
	 * @param phoneNumber
	 * @return
	 */
	public IConnection recoverConnection(String phoneNumber) {
		for (IConnection connection : connections) {
			String called = ((Connection) connection).getCalledPhoneNumber();
			if (((Connection) connection).isConnected()
					&& called.equals(phoneNumber)) {
				
				System.out.println("------Commutator recovered connection for called number : " + phoneNumber);
				
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
		
		System.out.println("------Commutator sent message to " + phoneNumber + " : " + message);
		
		concentrator.sendMessage(phoneNumber, message);
	}
}
