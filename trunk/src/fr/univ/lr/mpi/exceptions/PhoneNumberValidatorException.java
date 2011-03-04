package fr.univ.lr.mpi.exceptions;

/**
 * 
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 *
 */
public class PhoneNumberValidatorException extends Exception {
	
	
	private static final long serialVersionUID = 1L;
	/**
	 * Error message when the phone number doesn't respect the good format
	 */
	final public static String ERROR_WRONG_FORMAT = "Wrong phone number format!";
	
	/**
	 * Constructor
	 * @param message
	 */
	public PhoneNumberValidatorException(String message) {
		System.out.println(message);
	}
}
