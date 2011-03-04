package fr.univ.lr.mpi.exceptions;

/**
 * 
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 *
 */
public class LineException extends Exception {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Error message when the line is busy
	 */
	final public static String ERROR_BUSY = "The line is currently busy!";
	
	/**
	 * Error message when the line is free
	 */
	final public static String ERROR_FREE = "The line is currently free!";
	
	/**
	 * Constructor
	 * @param message
	 */
	public LineException(String message) {
		System.out.println(message);
	}
}
