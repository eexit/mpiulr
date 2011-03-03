package fr.univ.lr.mpi.simulation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QTextBrowser;
import com.trolltech.qt.gui.QWidget;

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

	private QWidget logBrowser;
	private QTextBrowser logBrowserText;

	public MessageObserver() {

		ui = new SimulationUI();
		this.evenement = new LinkedList<IEvent>();

		this.logBrowser = new QWidget();
		this.logBrowserText = new QTextBrowser(logBrowser);
		this.logBrowserText.setGeometry(0, 0, 800, 255);
		this.logBrowser.setGeometry(450, 500, 800, 250);
		
		try {
			FileWriter logFile = new FileWriter(
					"simulation/fr/univ/lr/mpi/log/log.txt");
			logFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * If the observer receive a message, he stock it in the queue
	 */
	public synchronized void receiveEvent(IEvent event) {
		System.out.println("log --- "+event);
		this.evenement.add(event);
		
	
		/**
		 * Wake up the thread
		 */
		this.notify();
	}

	public QWidget getLogWidget() {
		return this.logBrowser;
	}

	/**
	 * Write in the log file the different event
	 */
	public void run() {

		while (true) {
			/**
			 * If there is no event in the queue, we pause the Thread
			 */
			if (this.evenement.isEmpty()) {
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			/**
			 * We write in the file
			 */
			try {
				
				System.out.println("log ----- ");
				String log = this.evenement.poll().toString();
				
				this.logBrowserText.append(log);
				this.logBrowser.repaint();
				
				FileWriter logFile = new FileWriter(
						"simulation/fr/univ/lr/mpi/log/log.txt", true);
				
				
				
				logFile.write(log + "\r\n");
				logFile.close();
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
