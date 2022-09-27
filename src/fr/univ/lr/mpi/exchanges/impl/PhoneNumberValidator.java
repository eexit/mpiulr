/**
 * 
 */
package fr.univ.lr.mpi.exchanges.impl;

/**
 * PhoneNumberValidator.java
 * 
 * @author Joris Berthelot
 *
 */
public class PhoneNumberValidator {
	/**
	 * Internal regex to validate phone number
	 */
	private static String EXPREG = "^0[1-5][0-9]{8}$";
	
	/**
	 * Validator method
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isValid(String phoneNumber) {
		return phoneNumber.matches(EXPREG);
	}
}
