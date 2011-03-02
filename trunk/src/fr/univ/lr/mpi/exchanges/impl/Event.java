package fr.univ.lr.mpi.exchanges.impl;

import java.util.HashMap;
import java.util.Map;

import fr.univ.lr.mpi.exchanges.IEvent;

/**
 * Event Class
 * 
 * @author FAUCHER Tony
 * 
 */
public class Event implements IEvent {

	// attributes
	private EventType type;
	private Map<String, String> attributes;

	/**
	 * Constructor
	 * 
	 * @param type
	 */
	public Event(EventType type) {
		this.type = type;
		this.attributes = new HashMap<String, String>();

	}

	/**
	 * 
	 * @return the event type
	 */
	public EventType getEventType() {
		return this.type;
	}

	/**
	 * 
	 * @param attributeName
	 * @return the value of the attribute
	 */
	public String getAttributeValue(String attributeName) {
		return this.attributes.get(attributeName);
	}

	/**
	 * 
	 * @return the list of the attribute and their value
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * 
	 * @param attributeName
	 * @param attributeValue
	 */
	public void addAttribute(String attributeName, String attributeValue) {
		this.attributes.put(attributeName, attributeValue);
	}

	@Override
	public String toString() {
		return "Event [attributes=" + attributes + ", type=" + type + "]";
	}

}
