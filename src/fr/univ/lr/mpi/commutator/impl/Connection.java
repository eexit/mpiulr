package fr.univ.lr.mpi.commutator.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
	 * Caller line container
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private String callerPhoneNumber;

	/**
	 * Recipient line container
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
	 * Gets the recipient line
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @return
	 */
	public String getRecipientPhoneNumber() {
		return this.recipientPhoneNumber;
	}

	/**
	 * 
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param event
	 */
	public void receiveEvent(IEvent event) {
		System.out.println("Connection event: " + event);

		// The recipient phone number
		this.recipientPhoneNumber = event
				.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);

		switch (event.getEventType()) {
		// When the phone number exists (from the directory service)
		case PHONE_NUMBER_RESPONSE:
			// The phone number doesn't exist
			if (event.getAttributeValue(ExchangeAttributeNames.EXISTS).equals(
					Boolean.toString(false))) {
				// Sends an UNKNOWN_NUMBER to the caller
				AutoCommutator.getInstance().sendMessage(
						this.callerPhoneNumber,
						new Message(MessageType.UNKNOWN_NUMBER,
								this.recipientPhoneNumber, this.recipientPhoneNumber));
			}
			// Sends a call transfer exist request
			IEvent call_trans_event = new Event(EventType.CALL_TRANSFER_REQUEST);
			call_trans_event.addAttribute(
					ExchangeAttributeNames.CALLER_PHONE_NUMBER,
					this.callerPhoneNumber);
			call_trans_event.addAttribute(
					ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
					this.recipientPhoneNumber);
			AutoCommutator.getInstance().sendEvent(call_trans_event);
			break;

		// When the call transfer returns a number
		case CALL_TRANSFER_RESPONSE:
			// Sends a RING message to the recipient
			AutoCommutator.getInstance().receiveMessage(
					new Message(MessageType.RING, this.callerPhoneNumber,
						this.recipientPhoneNumber));
			break;
		}
	}

	/**
	 * Message receiver of commutator container
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public void receiveMessage(IMessage message) {
		System.out.println("Connection message: " + message);

		// The recipient phone number
		this.recipientPhoneNumber = message.getRecipientPhoneNumber();		

		switch (message.getMessageType()) {

		// When the caller is numbering
		case NUMBERING:
			// Sends to the caller a SEARCH message
			AutoCommutator.getInstance().sendMessage(
					this.callerPhoneNumber,
					new Message(MessageType.SEARCH, this.callerPhoneNumber,
							this.recipientPhoneNumber));

			// Sends to the directory service a phone number exist request
			IEvent num_req_event = new Event(EventType.PHONE_NUMBER_REQUEST);
			num_req_event.addAttribute(
					ExchangeAttributeNames.CALLER_PHONE_NUMBER,
					this.callerPhoneNumber);
			num_req_event.addAttribute(
					ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
					this.recipientPhoneNumber);
			AutoCommutator.getInstance().sendEvent(num_req_event);
			break;

		// When the recipient phone is ringing
		case RINGING:
			/* Sets the recipient phone number */
			this.recipientPhoneNumber = message.getRecipientPhoneNumber();

			// Sends an ECHO message to the caller
			AutoCommutator.getInstance().sendMessage(
					this.callerPhoneNumber,
					new Message(MessageType.ECHO, this.callerPhoneNumber,
							this.recipientPhoneNumber));

			// Sets up the answering machine timer
			Date ans_expire = new Date();
			ans_expire.setSeconds(ans_expire.getSeconds()
					+ ANSWERING_MACHINE_TIMEOUT);
			this.timer = new Timer();
			this.timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// Sends a unavailable recipient event
					IEvent event = new Event(EventType.UNAVAILABLE_RECIPIENT);
					event.addAttribute(
							ExchangeAttributeNames.CALLER_PHONE_NUMBER,
							callerPhoneNumber);
					event.addAttribute(
							ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
							recipientPhoneNumber);
					event.addAttribute(ExchangeAttributeNames.DATE, new Date()
							.toLocaleString());
					event.addAttribute(ExchangeAttributeNames.MESSAGE,
							"Message Cool !");
					AutoCommutator.getInstance().sendEvent(event);
				}
			}, ans_expire);

			// Sends an RINGING message to the recipient
			AutoCommutator.getInstance().sendMessage(this.recipientPhoneNumber, new Message(
					MessageType.RINGING, this.callerPhoneNumber, this.recipientPhoneNumber
			));
			break;

		// When the recipient pick up the phone
		case RECIPIENT_PICKUP:
			// If there is any timer, we kill it!
			if (null != this.timer) {
				this.timer.cancel();
				this.timer.purge();
			}
			
			this.startTime = Calendar.getInstance();
			IEvent event = new Event(EventType.CONNECTION_ESTABLISHED);
			event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER,
					this.callerPhoneNumber);
			event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
					recipientPhoneNumber);
			AutoCommutator.getInstance().sendEvent(event);

			// Sends message VOICE_EXCHANGE to the caller and the recipient
			IMessage exchange_message = new Message(
					MessageType.CONNECTION_ESTABLISHED, this.callerPhoneNumber,
					this.recipientPhoneNumber);
			AutoCommutator.getInstance().sendMessage(this.callerPhoneNumber,
					exchange_message);
			AutoCommutator.getInstance().sendMessage(this.recipientPhoneNumber,
					exchange_message);
			break;

		// When the caller or the recipient hang up the phone
		case HANGUP:
			final IEvent kill_event = new Event(EventType.CONNECTION_DESTROYED);
			kill_event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER,
					this.callerPhoneNumber);

			if (this.isConnected()) {
				// Sets up a connection keepalive timeout
				this.timer = new Timer();
				Date hang_expire = new Date();
				hang_expire.setSeconds(hang_expire.getSeconds()
						+ HANGUP_TIMEOUT);
				this.endTime = Calendar.getInstance();
				this.timer.schedule(new TimerTask() {

					@Override
					public void run() {
						// Sends the connection duration to the billing servive
						Long duration = endTime.getTimeInMillis()
								- startTime.getTimeInMillis() / 1000;
						IEvent event = new Event(EventType.CONNECTION_CLOSED);
						event.addAttribute(
								ExchangeAttributeNames.CALLER_PHONE_NUMBER,
								callerPhoneNumber);
						event.addAttribute(
								ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
								recipientPhoneNumber);
						event.addAttribute(
								ExchangeAttributeNames.CONNECTION_DURATION,
								duration.toString());
						AutoCommutator.getInstance().sendEvent(event);
						AutoCommutator.getInstance().receiveEvent(kill_event);
					}
				}, hang_expire);
			} else {
				AutoCommutator.getInstance().receiveEvent(kill_event);
			}
			break;
		}
	}
}
