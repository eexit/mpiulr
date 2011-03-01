package fr.univ.lr.mpi.exchanges;

import java.util.Map;
import fr.univ.lr.mpi.exchanges.impl.EventType;

/**
 * @author Tony FAUCHER
 */
public interface IEvent {
	
	/**
	 * Gets the event type
	 * 
	 * @author Tony FAUCHER
	 * @return
	 */
	public EventType getEventType();
	
	/**
	 * Gets the event attribute value
	 * 
	 * @author Tony FAUCHER
	 * @param attributeName
	 * @return
	 */
	public String getAttributeValue(String attributeName);
	
	/**
	 * Gets the event attributes
	 * 
	 * @author Tony FAUCHER
	 * @return
	 */
	public Map<String, String> getAttributes();
	
	/**
	 * Appends a new attribute to an event
	 * 
	 * @author Tony FAUCHER
	 * @param attributeNames
	 * @param attributeValue
	 */
	public void addAttributes(String attributeNames, String attributeValue);
}
