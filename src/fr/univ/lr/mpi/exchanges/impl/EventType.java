package fr.univ.lr.mpi.exchanges.impl;

/**
 * An enumerated type. Represents all event type (connections between
 * AutoCommutator and all registered services).
 * 
 * @author Elian ORIOU <elian.oriou@gmail.com>
 */

public enum EventType {

	/**
	 * When a line is created
	 */
	LINE_CREATION,
	/**
	 * When a line is deleted
	 */
	LINE_DELETION,
	/**
	 * When a connection is established between caller & recipient
	 */
	CONNECTION_ESTABLISHED,
	/**
	 * When a connection is closed between caller & recipient
	 */
	CONNECTION_CLOSED,
	/**
	 * When a caller requests a recipient number
	 */
	PHONE_NUMBER_REQUEST;
}
