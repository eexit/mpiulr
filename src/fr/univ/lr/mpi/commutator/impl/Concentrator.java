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

	public void registerLine(ILine line) {
		lines.add(line);
	}

	public int unregisterLine(String numberLine) {
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).getPhoneNumber() == numberLine) {
				lines.remove(i);
				return 0;
			}
		}
		return 1;
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
		int i = 0;
		while (phoneNumber != lines.get(i).getPhoneNumber() || i > lines.size()) {
			i++;
		}
		lines.get(i).receiveMessage(message);
	}

	/**
	 * 
	 * @param message
	 */

	public void receiveMessage(IMessage message) {
		AutoCommutator.getInstance().receiveMessage(message);
	}
}
