/**
 * 
 */
package fr.univ.lr.mpi.exchanges.impl;

/**
 * Phone number validator
 * 
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 *
 */
public class PhoneNumberValidator {
	
	private static String EXPREG = "^0[1-5][0-9]{8}$";
	
	public static boolean isValid(String phoneNumber) {
		return phoneNumber.matches(EXPREG);
	}
}
