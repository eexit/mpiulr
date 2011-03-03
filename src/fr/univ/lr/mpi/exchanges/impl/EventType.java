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
	 * When a connection is destroyed
	 */
	CONNECTION_DESTROYED,
	/**
	 * When a caller requests a recipient number
	 */
	PHONE_NUMBER_REQUEST,
	/**
	 * When the directory service delivers its response
	 */
	PHONE_NUMBER_RESPONSE,
	/**
	 * Before recipient phone ringing we have to check if a call transfer exists
	 */
	CALL_TRANSFER_REQUEST,
	/**
	 * When the call transfer service delivers its response
	 */
	CALL_TRANSFER_RESPONSE,
	/**
	 * When we want create a transfert between two lines
	 */
	CREATE_TRANSFER,
	/**
	 * When we want to remove a transfert
	 */
	REMOVE_TRANSFER,
	/**
	 * When there is no answer from the recipient
	 */
	UNAVAILABLE_RECIPIENT, 
	/**
	 * when need answering machine welcome message
	 */
	GET_ANSWERING_MACHINE_WELCOME,
	/**
	 * send/receive message to answering machine
	 */
	GET_ANSWERING_MACHINE_MESSAGE,
	/**
	 * Dialing to a line
	 */
	ANSWERING_MACHINE_MESSAGE,	
	/**
	 * when need answering machine home message
	 */
	DIALING,
	/**
	 * Send a message
	 */
	MESSAGE_TRANSFER;
}
