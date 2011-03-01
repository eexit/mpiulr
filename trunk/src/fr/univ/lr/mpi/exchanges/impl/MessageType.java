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
	 * When the AutoCommutator returns the tone (the connection between the
	 * caller and the AutoCommutator) to the caller
	 */
	BACKTONE,
	/**
	 * When the caller compose a phone number
	 */
	NUMEROTATION,
	/**
	 * When the AutoCommutator searches for a recipient and returns this signal
	 * to the caller
	 */
	SEARCH,
	/**
	 * When the AutoCommutator send a signal to the recipient (only if the line
	 * is free)
	 */
	RING,
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
	 */
	HANGUP;
}
