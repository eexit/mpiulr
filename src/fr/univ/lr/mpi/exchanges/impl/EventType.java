package fr.univ.lr.mpi.exchanges.impl;

/**
 * EventType.java
 * 
 * An enumerated type. Represents all event type (connections between
 * AutoCommutator and all registered services).
 * 
 * @author Elian ORIOU
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
	 * When a connection need to be create
	 */
	CONNECTION_CREATED,
	/**
	 * When a connection is destroyed
	 */
	CONNECTION_DESTROY,
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
	 * When we want create a transfer between two lines
	 */
	TRANSFER_CREATE,
	/**
	 * When we want to remove a transfer
	 */
	TRANSFER_REMOVE,
	/**
	 * When there is no answer from the recipient
	 */
	UNAVAILABLE_RECIPIENT, 
	/**
	 * When we want to get the answering machine welcome message
	 */
	ANSWERING_MACHINE_WELCOME_MESSAGE,
	/**
	 * When we want to retrieve answering machine messages
	 */
	ANSWERING_MACHINE_PULL_MESSAGE,
	/**
	 * When we want to push a message to the answering machine
	 */
	ANSWERING_MACHINE_PUSH_MESSAGE,
	/**
	 * When the answering machine sends message to the answering machine owner
	 */
	ANSWERING_MESSAGE;
}
