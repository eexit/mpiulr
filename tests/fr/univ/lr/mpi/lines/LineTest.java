/**
 * 
 *
 * @author Joris Berthelot
 */
package fr.univ.lr.mpi.lines;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fr.univ.lr.mpi.commutator.impl.Connection;
import fr.univ.lr.mpi.exceptions.LineException;
import fr.univ.lr.mpi.lines.impl.Line;

/**
 * @author Joris Berthelot
 */
public class LineTest {
	
	private static Line instance;
	
	private static String number = "0123456789";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		instance = new Line(number);
		assertTrue(instance.getState().equals(LineState.FREE));
	}

	/**
	 * Test method for {@link fr.univ.lr.mpi.lines.impl.Line#getPhoneNumber()}.
	 */
	@Test
	public void testGetPhoneNumber() {
		assertTrue(number == instance.getPhoneNumber());
	}

	/**
	 * Test method for {@link fr.univ.lr.mpi.lines.impl.Line#setConnection(fr.univ.lr.mpi.commutator.IConnection)}.
	 */
	@Test
	public void testSetConnection() {
		/**
		 * FIXME
		 */
		//instance.setConnection(new Connection(instance));
//		instance.setConnection(new Connection(instance.getPhoneNumber()));
		assertTrue(instance.getState().equals(LineState.BUSY));
	}

	/**
	 * Test method for {@link fr.univ.lr.mpi.lines.impl.Line#receiveMessage(fr.univ.lr.mpi.exchanges.IMessage)}.
	 */
	@Test
	public void testReceiveMessage() {
		/**
		 * TODO
		 */
	}

	/**
	 * Test method for {@link fr.univ.lr.mpi.lines.impl.Line#pickUp()}.
	 * @throws LineException 
	 */
	@Test
	public void testPickUp() throws LineException {
		/**
		 * TODO add more stuff here
		 */
		assertFalse(instance.getState().equals(LineState.BUSY));
		instance.pickUp();
		assertTrue(instance.getState().equals(LineState.BUSY));
	}

	/**
	 * Test method for {@link fr.univ.lr.mpi.lines.impl.Line#hangUp()}.
	 * @throws LineException 
	 */
	@Test
	public void testHangUp() throws LineException {
		/**
		 * TODO add more stuff here
		 */
		assertFalse(instance.getState().equals(LineState.BUSY));
		instance.pickUp();
		assertTrue(instance.getState().equals(LineState.BUSY));
		instance.hangUp();
		assertFalse(instance.getState().equals(LineState.BUSY));
	}

	/**
	 * Test method for {@link fr.univ.lr.mpi.lines.impl.Line#dialTo(java.lang.String)}.
	 */
	@Test
	public void testDialTo() {
		//assertFalse(instance.dialTo("0011223344"));
		/**
		 * TODO mock object to test dialTo method
		 */
	}
}
