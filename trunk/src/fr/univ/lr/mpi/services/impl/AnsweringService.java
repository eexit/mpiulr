package fr.univ.lr.mpi.services.impl;

import java.util.ArrayList;
import java.util.List;

import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.services.IService;

/**
 * Answering Service
 * 
 * @author FAUCHER Tony
 */

public class AnsweringService implements IService {

	// attributes
	private List<AnsweringMachineMessage> messages;

	// constructor
	/**
	 * Constructor of an Answering Service
	 */
	public AnsweringService() {
		this.messages = new ArrayList<AnsweringMachineMessage>();
	}

	// méthodes

	/**
	 * Add an Answering Machine Message
	 * 
	 * @param AnsweringMachineMessage
	 * 
	 */
	public void postMessage(AnsweringMachineMessage asm) {
		this.messages.add(asm);
	}

	/**
	 * Return the list of the answering machine message
	 * 
	 * @return message list
	 */
	public List<AnsweringMachineMessage> getMessages() {
		return this.messages;
	}

	@Override
	public void receiveEvent(IEvent event) {
		// TODO Auto-generated method stub

	}
}
