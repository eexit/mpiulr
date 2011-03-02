package fr.univ.lr.mpi.simulation;

import java.util.LinkedList;
import java.util.Queue;

import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.exchanges.impl.ExchangeAttributeNames;
import fr.univ.lr.mpi.services.IService;

/**
 * 
 * @author FAUCHER Tony <faucher.tony85@gmail.com>
 * 
 */

public class MessageObserver extends Thread implements IService {

	public SimulationUI ui;
	private Queue<IEvent> evenement;

	public MessageObserver() {

		ui = new SimulationUI();
		this.evenement = new LinkedList<IEvent>();

	}

	/**
	 * If the observer receive a message, he stock it in the queue
	 */
	public void receiveEvent(IEvent event) {
		this.evenement.add(event);
		this.notify();
	}

	public void run() {
		String callerPhoneNumber;
		String recipientPhoneNumber;
		while (this.evenement.peek() != null) {
			IEvent event = this.evenement.poll();

			switch (event.getEventType()) {

			/**
			 * End of communication
			 */
			case CONNECTION_CLOSED:

				callerPhoneNumber = event
						.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
				recipientPhoneNumber = event
						.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);

				// action à effectuer, à completer selon l'ui

				break;

			/**
			 * Connection strart
			 */
			case CONNECTION_ESTABLISHED:
				//
				callerPhoneNumber = event
						.getAttributeValue(ExchangeAttributeNames.CALLER_PHONE_NUMBER);
				recipientPhoneNumber = event
						.getAttributeValue(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER);

				// à compléter
				break;
			
				/**
				 * Create a line
				 */
			case LINE_CREATION:
				break;
				
			case LINE_DELETION:
				break;
			
			case DIALING :
				break;
				
			case MESSAGE_TRANSFER:
				break;
			}
		}

		/**
		 * When the queue is empty, we pause the thread
		 */
		try {
			this.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
