package fr.univ.lr.mpi.simulation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import fr.univ.lr.mpi.exchanges.IEvent;
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
	public synchronized void receiveEvent(IEvent event) {
		this.evenement.add(event);
		this.notify();
	}

	public void run() {

		while (true) {
			if (this.evenement.isEmpty()) {
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			try {
				FileWriter logFile = new FileWriter("log.txt",true);
				String log = this.evenement.poll().toString();
				System.out.println("\n ----- ecriture :              " + log);
				logFile.write(log+"\r\n");
				logFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
