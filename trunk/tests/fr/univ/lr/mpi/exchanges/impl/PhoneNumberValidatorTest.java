/**
 * 
 */
package fr.univ.lr.mpi.exchanges.impl;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 *
 */
public class PhoneNumberValidatorTest {

	/**
	 * Test method for {@link fr.univ.lr.mpi.exchanges.impl.PhoneNumberValidator#isValid(java.lang.String)}.
	 */
	@Test
	public void testIsValid() {
		assertTrue(PhoneNumberValidator.isValid("0123456789"));
		assertFalse(PhoneNumberValidator.isValid("e-10"));
		assertFalse(PhoneNumberValidator.isValid("0011223344"));
		assertFalse(PhoneNumberValidator.isValid("          "));
		assertFalse(PhoneNumberValidator.isValid(""));
	}
}
