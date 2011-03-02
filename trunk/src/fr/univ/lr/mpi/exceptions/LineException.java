package fr.univ.lr.mpi.exceptions;

/**
 * 
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 *
 */
public class LineException extends Exception {
	
	final public static String ERROR_BUSY = "The line is currently busy!";
	
	final public static String ERROR_FREE = "The line is currently free!";
	
	public LineException(String message) {
		System.out.println(message);
	}
}
