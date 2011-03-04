package fr.univ.lr.mpi.commutator.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import fr.univ.lr.mpi.commutator.IConnection;
import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.exchanges.impl.Event;
import fr.univ.lr.mpi.exchanges.impl.EventType;
import fr.univ.lr.mpi.exchanges.impl.ExchangeAttributeNames;
import fr.univ.lr.mpi.exchanges.impl.Message;
import fr.univ.lr.mpi.exchanges.impl.MessageType;

/**
 * 
 * MPI_PROJECT/fr.univ.lr.mpi.commutator.impl/Connection.java
 * 
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 * @date Mar 1, 2011
 * 
 */
public class Connection extends Thread implements IConnection {

	/**
	 * Hang up timer delay value (in seconds)
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	final public static int HANGUP_TIMEOUT = 3;

	/**
	 * Answering machine timer delay (in seconds)
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	final public static int ANSWERING_MACHINE_TIMEOUT = 8;
	
	/**
	 * Answering machine phone number
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public static final String ANSWERING_MACHINE_PHONE_NUMBER = "3103";

	/**
	 * Caller line
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private String callerPhoneNumber;

	/**
	 * Called line
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private String calledPhoneNumber;
	
	/**
	 * Message/Event issuer
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private String issuerPhoneNumber;
	
	/**
	 * Message/Event recipient
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private String recipientPhoneNumber;
	
	/**
	 * Connection start time
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private Calendar startTime;

	/**
	 * Connection end time
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private Calendar endTime;

	/**
	 * Connection timer which will be launch when one of the connection lines
	 * hang up. When to timer reaches it limit, the connection is destroyed.
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private Timer timer;
	
	/**
	 * Connection state (established or not with the recipient)
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private Boolean connected;
	
	/**
	 * Class constructor
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param callerLine
	 */
	public Connection(String callerPhoneNumber) {
		this.callerPhoneNumber = callerPhoneNumber;
		this.connected = false;
		
		System.out.println("---------Connection built from line : " + this.callerPhoneNumber);
		
		// Sends to the caller a BACKTONE message
		AutoCommutator.getInstance().sendMessage(this.callerPhoneNumber, new Message(
			MessageType.BACKTONE,
			this.callerPhoneNumber
		));
	}
	
	/**
	 * Returns the state of the connection (is it established with
	 * the recipient or not)
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @return
	 */
	public Boolean isConnected() {
		return this.connected;
	}

	/**
	 * Gets the caller line
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @return
	 */
	public String getCallerPhoneNumber() {
		return this.callerPhoneNumber;
	}

	/**
	 * Gets the called line
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @return
	 */
	public String getCalledPhoneNumber() {
		return this.calledPhoneNumber;
	}

	/**
	 * Event receiver
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param event
	 */
	public void receiveEvent(IEvent event) {
		System.out.println("---------Connection received event: " + event);
		
		// The issuer phone number
		this.issuerPhoneNumber = event.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
		
		// The recipient phone number
		this.recipientPhoneNumber = event.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);

		switch (event.getEventType()) {
		
		// When the phone number exists (from the directory service)
		case PHONE_NUMBER_RESPONSE:
			// The phone number doesn't exist
			if (event.getAttributeValue(ExchangeAttributeNames.EXISTS).equals(Boolean.toString(false))) {
				// Sends an UNKNOWN_NUMBER to the caller
				AutoCommutator.getInstance().sendMessage(this.callerPhoneNumber, new Message(
					MessageType.UNKNOWN_NUMBER,
					this.callerPhoneNumber, this.recipientPhoneNumber
				));
			}
			
			// Sends a call transfer exist request
			IEvent call_trans_event = new Event(EventType.CALL_TRANSFER_REQUEST);
			call_trans_event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, this.callerPhoneNumber);
			call_trans_event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, this.recipientPhoneNumber);
			AutoCommutator.getInstance().sendEvent(call_trans_event);
			break;

		// When the call transfer returns a number
		case CALL_TRANSFER_RESPONSE:
			// Sends a RING message to the recipient
			AutoCommutator.getInstance().receiveMessage(new Message(
				MessageType.IS_BUSY, this.callerPhoneNumber, this.recipientPhoneNumber
			));
			break;
		
		// When the answering machine sends the welcome message to the caller
		case ANSWERING_MACHINE_WELCOME_MESSAGE:
			AutoCommutator.getInstance().sendMessage(this.callerPhoneNumber, new Message(
				MessageType.VOICE_EXCHANGE,
				this.recipientPhoneNumber,
				this.callerPhoneNumber,
				event.getAttributeValue(ExchangeAttributeNames.MESSAGE)
			));
			break;
		
		// When the answering machine sends registered message to the caller (answering machine owner)
		case ANSWERING_MESSAGE:
			for (Entry<String, String> attr: event.getAttributes().entrySet()) {
				if (attr.getKey().equals(ExchangeAttributeNames.MESSAGE)) {
					AutoCommutator.getInstance().sendMessage(this.callerPhoneNumber, new Message(
						MessageType.VOICE_EXCHANGE, this.issuerPhoneNumber, this.callerPhoneNumber, attr.getValue()
					));
				}
			}
			break;
		}
	}

	/**
	 * Message receiver of commutator container
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public void receiveMessage(IMessage message) {
		System.out.println("---------Connection received message: " + message);
		
		// The issuer phone number
		this.issuerPhoneNumber = message.getCallerPhoneNumber();
		// The recipient phone number
		this.recipientPhoneNumber = message.getRecipientPhoneNumber();		

		switch (message.getMessageType()) {

		// When the caller is numbering
		case NUMBERING:
			
			//System.out.println("Numbering CN : " + this.issuerPhoneNumber + " / RN : " + this.recipientPhoneNumber);
			
			if (this.recipientPhoneNumber.equals(ANSWERING_MACHINE_PHONE_NUMBER)) {
				IEvent answ_pull_event = new Event(EventType.ANSWERING_MACHINE_PULL_MESSAGE);
				answ_pull_event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, this.callerPhoneNumber);
				AutoCommutator.getInstance().sendEvent(answ_pull_event);				
			} else {
				// Sends to the caller a SEARCH message
				AutoCommutator.getInstance().sendMessage(this.callerPhoneNumber, new Message(
					MessageType.SEARCH, this.callerPhoneNumber, this.recipientPhoneNumber
				));

				// Sends to the directory service a phone number exist request
				IEvent num_req_event = new Event(EventType.PHONE_NUMBER_REQUEST);
				num_req_event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, this.callerPhoneNumber);
				num_req_event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, this.recipientPhoneNumber);
				//System.out.println("QUERY: "+num_req_event);
				AutoCommutator.getInstance().sendEvent(num_req_event);
			}
			break;
		
		// When the recipient line is busy
		case BUSY :
			this.calledPhoneNumber = this.recipientPhoneNumber;
			IEvent event = new Event(EventType.UNAVAILABLE_RECIPIENT);
			event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, this.callerPhoneNumber);
			event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, this.recipientPhoneNumber);
			event.addAttribute(ExchangeAttributeNames.DATE, new Date().toLocaleString());
			AutoCommutator.getInstance().sendEvent(event);
			break;
		
		// When the recipient phone is ringing
		case RINGING :
			this.calledPhoneNumber = this.recipientPhoneNumber;
			
			// Sends an ECHO message to the caller
			AutoCommutator.getInstance().sendMessage(this.callerPhoneNumber, new Message(
				MessageType.ECHO, this.callerPhoneNumber, this.recipientPhoneNumber
			));
			
			//System.out.println("Ringing CN : "+ this.callerPhoneNumber + " / RN :" + this.recipientPhoneNumber);
			
			// Sets up the answering machine timer
			Date ans_expire = new Date();
			ans_expire.setSeconds(ans_expire.getSeconds() + ANSWERING_MACHINE_TIMEOUT);
			this.timer = new Timer();
			this.timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// Sends to the recipient line a STOP_RINGING message
					AutoCommutator.getInstance().sendMessage(recipientPhoneNumber, new Message(
						MessageType.STOP_RINGING, recipientPhoneNumber
					));

					// Sends a unavailable recipient event
					IEvent answ_call_event = new Event(EventType.UNAVAILABLE_RECIPIENT);
					answ_call_event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, callerPhoneNumber);
					answ_call_event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, recipientPhoneNumber);
					answ_call_event.addAttribute(ExchangeAttributeNames.DATE, new Date().toLocaleString());
					AutoCommutator.getInstance().sendEvent(answ_call_event);
				}
			}, ans_expire);
			
			//System.out.println("Ringing CN : "+ this.callerPhoneNumber + " / RN :" + this.calledPhoneNumber);

			// Sends an RINGING message to the recipient
			AutoCommutator.getInstance().sendMessage(this.recipientPhoneNumber, new Message(
				MessageType.RINGING, this.callerPhoneNumber, this.calledPhoneNumber
			));
			break;

		// When the recipient pick up the phone
		case RECIPIENT_PICKUP :
			// Connection established confirmation message
			IMessage exchange_message = new Message(MessageType.CONNECTION_ESTABLISHED, this.callerPhoneNumber, this.calledPhoneNumber);
			
			if (!this.isConnected()) {
				this.connected = true;
				this.calledPhoneNumber = this.recipientPhoneNumber;
				this.startTime = Calendar.getInstance();
				
				// Sends message CONNECTION_ESTABLISHED to the caller and the recipient
				AutoCommutator.getInstance().sendMessage(this.callerPhoneNumber, exchange_message);
				AutoCommutator.getInstance().sendMessage(this.calledPhoneNumber, exchange_message);
			} else if (this.issuerPhoneNumber.equals(this.calledPhoneNumber)) {
				// Re-sends message CONNECTION_ESTABLISHED to the recipient
				AutoCommutator.getInstance().sendMessage(this.calledPhoneNumber, exchange_message);
			}

			// If there is any timer, we kill it!
			if (null != this.timer) {
				this.timer.cancel();
				this.timer.purge();
			}
			break;
		
		case VOICE_EXCHANGE :
			if (this.isConnected()) {
				
				if (this.issuerPhoneNumber.equals(this.callerPhoneNumber)) {
					AutoCommutator.getInstance().sendMessage(this.calledPhoneNumber, message);
				} else {
					AutoCommutator.getInstance().sendMessage(this.callerPhoneNumber, message);
				}
				
			} else if (null != this.calledPhoneNumber) {
				IEvent answ_message = new Event(EventType.ANSWERING_MACHINE_PUSH_MESSAGE);
				answ_message.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, this.callerPhoneNumber);
				answ_message.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, this.recipientPhoneNumber);
				answ_message.addAttribute(ExchangeAttributeNames.DATE, new Date().toLocaleString());
				answ_message.addAttribute(ExchangeAttributeNames.MESSAGE, message.getContent());
				AutoCommutator.getInstance().sendEvent(answ_message);
			}
			break;
		
		// When the caller or the recipient hang up the phone
		case HANGUP :
			final IEvent kill_event = new Event(EventType.CONNECTION_DESTROY);
			kill_event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, this.callerPhoneNumber);
			
			//System.out.println("Kill event :" + kill_event);
			
			if (this.isConnected()) {
				this.endTime = Calendar.getInstance();
				final Long duration = endTime.getTimeInMillis() - startTime.getTimeInMillis() / 1000;
				
				if (this.issuerPhoneNumber.equals(this.callerPhoneNumber)) {
					System.out.println("Connection killed by the caller");
					IEvent close_connection_event = new Event(EventType.CONNECTION_CLOSED);
					close_connection_event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, callerPhoneNumber);
					close_connection_event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, calledPhoneNumber);
					close_connection_event.addAttribute(ExchangeAttributeNames.CONNECTION_DURATION, duration.toString());
					close_connection_event.addAttribute(ExchangeAttributeNames.DATE, new Date().toLocaleString());
					AutoCommutator.getInstance().sendEvent(close_connection_event);
					
					AutoCommutator.getInstance().sendMessage(this.callerPhoneNumber, new Message(
						MessageType.CONNECTION_CLOSED, this.callerPhoneNumber
					));
					AutoCommutator.getInstance().sendMessage(this.calledPhoneNumber, new Message(
						MessageType.CONNECTION_CLOSED, this.callerPhoneNumber
					));
					
					AutoCommutator.getInstance().receiveEvent(kill_event);
				} else {
					System.out.println("Connection killed by the called");
					// Sets up a connection keep alive timeout
					this.timer = new Timer();
					Date hang_expire = new Date();
					hang_expire.setSeconds(hang_expire.getSeconds() + HANGUP_TIMEOUT);
					this.timer.schedule(new TimerTask() {

						@Override
						public void run() {
							// Sends the connection duration to the billing service
							IEvent givup_event = new Event(EventType.CONNECTION_CLOSED);
							givup_event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, callerPhoneNumber);
							givup_event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, calledPhoneNumber);
							givup_event.addAttribute(ExchangeAttributeNames.CONNECTION_DURATION, duration.toString());
							givup_event.addAttribute(ExchangeAttributeNames.DATE, new Date().toLocaleString());
							AutoCommutator.getInstance().sendEvent(givup_event);
							
							AutoCommutator.getInstance().sendMessage(callerPhoneNumber, new Message(
								MessageType.CONNECTION_CLOSED, callerPhoneNumber
							));
							
							// Kills the connection
							AutoCommutator.getInstance().receiveEvent(kill_event);
							
						}
					}, hang_expire);
					
					AutoCommutator.getInstance().sendMessage(calledPhoneNumber, new Message(
						MessageType.CONNECTION_CLOSED, callerPhoneNumber
					));
				}

			} else {
				// If there is any timer, we kill it!
				if (null != this.timer) {
					this.timer.cancel();
					this.timer.purge();
				}
				
				AutoCommutator.getInstance().sendMessage(this.callerPhoneNumber, new Message(
					MessageType.CONNECTION_CLOSED, this.callerPhoneNumber
				));
				AutoCommutator.getInstance().receiveEvent(kill_event);
			}
			break;
		}
	}
	
	public void endConnection(){
		this.timer.cancel();
	}
}
