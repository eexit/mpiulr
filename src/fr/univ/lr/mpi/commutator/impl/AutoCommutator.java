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
import fr.univ.lr.mpi.services.IService;
import fr.univ.lr.mpi.services.impl.AnsweringService;
import fr.univ.lr.mpi.services.impl.BillingService;
import fr.univ.lr.mpi.services.impl.CallTransferService;
import fr.univ.lr.mpi.services.impl.DirectoryService;

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
//	private List<ILine> lines;
	private List<IService> services;

	private AutoCommutator() {
		this(10);
	}

	private AutoCommutator(int maxConnections) {
		this.connections = new ArrayList<IConnection>();
//		this.lines = new ArrayList<ILine>();
		this.services = new ArrayList<IService>();
		this.MAX_CONNECTIONS = maxConnections;

		initServices();
	}

	private void initServices() {
		registerService(new DirectoryService());
		registerService(new AnsweringService());
		registerService(new BillingService());
		registerService(new CallTransferService());

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
		// IConnection connection = new Connection(callerPhoneNumber);
		connections.add(new Connection(callerPhoneNumber));
		// FIXME
	}

	/**
	 * Returns a phone line that corresponds to the phone number
	 * 
	 * @param phoneNumber
	 *            the line phone number
	 */

//	private ILine getLine(String phoneNumber) {
//		for (ILine line : this.lines) {
//			if (line.getPhoneNumber().equals(phoneNumber)) {
//				return line;
//			}
//		}
//		return null;
//	}

	/**
	 * Returns the number of active connections (it must not exceed the
	 * MAX_CONNECTIONS)
	 * 
	 * @return The active number of connections
	 */

	public int getActiveConnections() {
		return this.concentrator.getActiveLines().size();
//		return this.connections.size();
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
	 * Registers a phone line
	 * 
	 * @param line
	 *            The new phone line
	 */

//	public void registerLine(ILine line) {
//		IEvent event = new Event(EventType.LINE_CREATION);
//		event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, line
//				.getPhoneNumber());
//		this.sendEvent(event);
//		this.lines.add(line);
//	}

	/**
	 * Unregisters a phone line
	 * 
	 * @param line
	 *            The service to unregister
	 */

//	public void unregisterLine(ILine line) {
//		IEvent event = new Event(EventType.LINE_DELETION);
//		event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, line
//				.getPhoneNumber());
//		this.sendEvent(event);
//		this.lines.remove(line);
//	}

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
		/* In the case of a Directory Service Response */
		case PHONE_NUMBER_RESPONSE:
			/*
			 * => The Directory Service returns TRUE OR FALSE (if the recipient
			 * phone number exists or not)
			 */
			if (event.getAttributeValue(ExchangeAttributeNames.EXISTS).equals(
					Boolean.toString(false))) {
				/* The recipient number doesn't exists */
				return;
			}
			System.out.println("The phone number exists !");
			String recipientPhoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);
			/* => Checks if call transfer rules exists */
			IEvent e = new Event(EventType.CALL_TRANSFER_REQUEST);
			e
					.addAttribute(
							ExchangeAttributeNames.CALLER_PHONE_NUMBER,
							event
									.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER));
			e.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
					recipientPhoneNumber);
			sendEvent(e);
			break;
		/* In the case of a Call Transfer Service Response */
		case CALL_TRANSFER_RESPONSE:
			// => Send Rings Message to the recipient
			String callerPhoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
			recipientPhoneNumber = event
					.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);

			//!!!!!!!!!!!!!!!!!!!!!
			this.concentrator.sendMessage(callerPhoneNumber, new Message(
					MessageType.RING, callerPhoneNumber, recipientPhoneNumber));
			
			
//			getLine(recipientPhoneNumber).receiveMessage(
//					new Message(MessageType.RING, callerPhoneNumber,
//							recipientPhoneNumber));
			break;
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
		System.out.println(message);
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
				
				
//				getLine(callerPhoneNumber).receiveMessage(
//						new Message(MessageType.TOO_MANY_CONNECTIONS,
//								callerPhoneNumber, null));
				return;
			}
			
			launchConnection(callerPhoneNumber);
			this.concentrator.sendMessage(callerPhoneNumber, new Message(
					MessageType.BACKTONE, callerPhoneNumber, null));

			// getLine(callerPhoneNumber).receiveMessage(
			// new Message(MessageType.BACKTONE, callerPhoneNumber, null));
			break;
		default:
			break;
		}
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
