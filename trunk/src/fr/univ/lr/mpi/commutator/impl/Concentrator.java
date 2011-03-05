package fr.univ.lr.mpi.commutator.impl;

import java.util.ArrayList;
import java.util.List;

import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.exchanges.impl.Event;
import fr.univ.lr.mpi.exchanges.impl.EventType;
import fr.univ.lr.mpi.exchanges.impl.ExchangeAttributeNames;
import fr.univ.lr.mpi.lines.ILine;

/**
 * Concentrator.java
 * 
 * @author Elian ORIOU <elian.oriou@gmail.com>
 */
public class Concentrator {
	
	/**
	 * List of lines
	 */
	private List<ILine> lines;
	
	/**
	 * Class constructor
	 */
	public Concentrator() {
		this.lines = new ArrayList<ILine>();
	}
	
	/**
	 * Line registerer
	 * 
	 * @param line
	 */
	public void registerLine(ILine line) {
		lines.add(line);
		line.setConcentrator(this);
		/* Call Directory Service to register the line number */
		Event e = new Event(EventType.LINE_CREATION);
		e.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, line
				.getPhoneNumber());
		AutoCommutator.getInstance().sendEvent(e);
	}
	
	/**
	 * Line unregisterer
	 * 
	 * @param numberLine
	 * @return
	 */
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
	 * Active line getter
	 * 
	 * @return
	 */
	public List<ILine> getActiveLines() {
		return this.lines;
	}
	
	/**
	 * Active line getter by phone number
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public ILine getActiveLine(String phoneNumber) {
		for (ILine line : lines) {
			if (line.getPhoneNumber().equals(phoneNumber)) {
				return line;
			}
		}
		return null;
	}

	/**
	 * Message dispatcher
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	public void sendMessage(String phoneNumber, IMessage message) {
		for (ILine l : lines) {
			if (l.getPhoneNumber().equals(phoneNumber)) {
				l.receiveMessage(message);
				return;
			}
		}
	}

	/**
	 * Message listener
	 * 
	 * @param message
	 */
	public void receiveMessage(IMessage message) {
		AutoCommutator.getInstance().receiveMessage(message);
	}
}