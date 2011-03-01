package fr.univ.lr.mpi.exchanges;

import java.util.Map;

import fr.univ.lr.mpi.exchanges.impl.EventType;

public interface IEvent {
	
	public EventType getEventType();
	public String getAttributeValue(String attributeName);
	public Map<String, String> getAttributes();
	public void addAttributes(String attributeNames, String attributeValue);
}
