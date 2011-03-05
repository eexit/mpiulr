package fr.univ.lr.mpi.exchanges.impl;

import java.util.HashMap;
import java.util.Map;

import fr.univ.lr.mpi.exchanges.IEvent;

/**
 * Event.java
 * 
 * @author FAUCHER Tony 
 */
public class Event implements IEvent {

	/**
	 * Event type
	 */
	private EventType type;
	
	/**
	 * Event attributes container
	 */
	private Map<String, String> attributes;

	/**
	 * Class constructor
	 * 
	 * @param type
	 */
	public Event(EventType type) {
		this.type = type;
		this.attributes = new HashMap<String, String>();

	}

	/**
	 * Event type getter
	 * 
	 * @return the event type
	 */
	public EventType getEventType() {
		return this.type;
	}

	/**
	 * Event attribute getter by attribute name
	 * 
	 * @param attributeName
	 * @return the value of the attribute
	 */
	public String getAttributeValue(String attributeName) {
		return this.attributes.get(attributeName);
	}

	/**
	 * Event attributes getter
	 * 
	 * @return the list of the attribute and their value
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * Event attribute setter
	 * 
	 * @param attributeName
	 * @param attributeValue
	 */
	public void addAttribute(String attributeName, String attributeValue) {
		this.attributes.put(attributeName, attributeValue);
	}

	@Override
	public String toString() {
		return "Event [type=" + type + ", attributes=" + attributes + "]";
	}

}
