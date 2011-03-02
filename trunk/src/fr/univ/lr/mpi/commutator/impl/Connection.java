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
	private String caller;

	/**
	 * Recipient line container
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private String recipient;

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
	 * Class constructor
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param callerLine
	 */
	public Connection(String caller) {
		this.caller = caller;
	}
	
	/**
	 * Sets the recipient line
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param recipient
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	/**
	 * Gets the caller line
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @return
	 */
	public String getCaller() {
		return this.caller;
	}
	
	/**
	 * Gets the recipient line
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @return
	 */
	public String getRecipient() {
		return this.recipient; 
	}

	/**
	 * Message forwarding between the two connected lines
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param message
	 */
	public void forwardMessage(Message message) {
		/**
		 * FIXME
		 */
		// AutoCommutator.getInstance().forwardMessage(message);
	}

	/**
	 * Message receiver of commutator container
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public void receiveMessage(IMessage message) {
		switch (message.getMessageType()) {
			case RING :
				Date ans_expire = new Date();
				ans_expire.setSeconds(ans_expire.getSeconds() + ANSWERING_MACHINE_TIMEOUT);
				this.timer = new Timer();
				this.timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						IEvent event = new Event(EventType.UNAVAILABLE_RECIPIENT);
						event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, caller);
						event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, recipient);
						AutoCommutator.getInstance().sendEvent(event);
					}
				}, ans_expire);
				
				break;
				
			case RECIPIENT_PICKUP :
				if (null != this.timer) {
					this.timer.cancel();
					this.timer = null;
				}
				
				this.recipient = message.getRecipientPhoneNumber();
				this.startTime = Calendar.getInstance();
				IEvent event = new Event(EventType.CONNECTION_ESTABLISHED);
				event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, caller);
				event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, recipient);
				AutoCommutator.getInstance().sendEvent(event);
				break;
				
			case HANGUP :
				this.timer = new Timer();
				Date hang_expire = new Date();
				hang_expire.setSeconds(hang_expire.getSeconds() + HANGUP_TIMEOUT);
				this.endTime = Calendar.getInstance();
				this.timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						Long duration = endTime.getTimeInMillis() - startTime.getTimeInMillis() / 1000;
						IEvent event = new Event(EventType.CONNECTION_CLOSED);
						event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, caller);
						event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, recipient);
						event.addAttribute(ExchangeAttributeNames.CONNECTION_DURATION, duration.toString());
						AutoCommutator.getInstance().sendEvent(event);
					}
				}, hang_expire);
				break;
				
			default :
				break;
		}
	}
}
