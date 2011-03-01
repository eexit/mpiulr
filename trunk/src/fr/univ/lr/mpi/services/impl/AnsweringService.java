package fr.univ.lr.mpi.services.impl;

import java.util.ArrayList;
import java.util.List;

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
	 * @author FAUCHER Tony constructor of an Answering Service
	 */
	public AnsweringService() {
		this.messages = new ArrayList<AnsweringMachineMessage>();
	}

	// méthodes

	/**
	 * @author FAUCHER Tony Add an Answering Machine Message
	 * @param AnsweringMachineMessage
	 * 
	 */
	public void postMessage(AnsweringMachineMessage asm) {
		this.messages.add(asm);
	}

	/**
	 * @author FAUCHER Tony return the list of the answering machine message
	 * @return message list
	 */
	public List<AnsweringMachineMessage> getMessages() {
		return this.messages;
	}

}
