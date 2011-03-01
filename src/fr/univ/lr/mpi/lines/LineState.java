package fr.univ.lr.mpi.lines;

/**
 * An enumerated type. Contains all of possibles line states.
 * 
 * @author Elian ORIOU <elian.oriou@gmail.com>
 * 
 */

public enum LineState {

	/**
	 * When the line is free (hook phone)
	 */
	FREE,
	/**
	 * When the line is busy (an active connection is already existing OR the
	 * phone is picked up)
	 */
	BUSY;
}
