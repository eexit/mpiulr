package fr.univ.lr.mpi.exceptions;

/**
 * 
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 *
 */
public class PhoneNumberValidatorException extends Exception {
	
	final public static String WRONG_FORMAT = "Wrong phone number format!";

	public PhoneNumberValidatorException(String message) {
		System.out.println(message);
	}
}
