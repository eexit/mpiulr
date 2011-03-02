package fr.univ.lr.mpi.commutator.impl;

import java.util.ArrayList;
import java.util.List;

import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.lines.ILine;

/**
 * 
 * @author Elian ORIOU <elian.oriou@gmail.com>
 * 
 */

public class Concentrator {

	private List<ILine> lines;

	public Concentrator() {
		this.lines = new ArrayList<ILine>();
	}

	/**
	 * 
	 * @return
	 */

	public List<ILine> getActiveLines() {
		return this.lines;
	}

	/**
	 * 
	 * @param phoneNumber
	 * @param message
	 */

	public void sendMessage(String phoneNumber, IMessage message) {

	}

	/**
	 * 
	 * @param message
	 */

	public void receiveMessage(IMessage message) {

	}
}
