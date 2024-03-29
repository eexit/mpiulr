package fr.univ.lr.mpi.lines;

/**
 * LineState.java
 * 
 * An enumerated type. Contains all of possibles line states.
 * 
 * @author Elian ORIOU
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
