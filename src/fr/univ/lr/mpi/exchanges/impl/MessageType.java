package fr.univ.lr.mpi.exchanges.impl;

/**
 * An enumerated type. Contains all messages exchanged within our system.
 * 
 * @author Elian ORIOU <elian.oriou@gmail.com>
 * 
 */

public enum MessageType {

	/**
	 * When the caller picks up his phone
	 */
	PICKUP,
	/**
	 * When the recipient picks up his phone
	 */
	RECIPIENT_PICKUP,
	/**
	 * When the AutoCommutator returns the tone (the connection between the
	 * caller and the AutoCommutator) to the caller
	 */
	BACKTONE,
	/**
	 * When the caller compose a phone number
	 */
	NUMBERING,
	/**
	 * When the AutoCommutator searches for a recipient and returns this signal
	 * to the caller
	 */
	SEARCH,
	/**
	 * When the Connection ask the recipient line to ring
	 */
	RING,
	/**
	 * When the recipient line is ringing
	 */
	RINGING,
	/**
	 * When the recipient phone rings the AutoCommutator send an ECHO signal to
	 * the caller
	 */
	ECHO,
	/**
	 * When the connection is established, the caller and the recipient can
	 * exchange voice data
	 */
	VOICE_EXCHANGE,
	/**
	 * When the caller OR the recipient hang up their phones
	 * new connection is refused
	 */
	HANGUP,
	/**
	 * When too many connections have been established on the AutoCommutator, a
	 */
	TOO_MANY_CONNECTIONS,
	/**
	 * When the number doesn't not exist
	 */
	UNKNOWN_NUMBER;
}
