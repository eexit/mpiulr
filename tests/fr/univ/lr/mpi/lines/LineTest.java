/**
 * 
 *
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 */
package fr.univ.lr.mpi.lines;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fr.univ.lr.mpi.commutator.impl.Connection;
import fr.univ.lr.mpi.lines.impl.Line;

/**
 *
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 *
 */
public class LineTest {
	
	private static Line instance;
	
	private static String number = "1234567890";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		instance = new Line(number);
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
		//instance.setConnection(new Connection(instance, new Line(number)));
		instance.setConnection(new Connection());
		assertTrue(instance.getState().equals(LineState.BUSY));
	}

	/**
	 * Test method for {@link fr.univ.lr.mpi.lines.impl.Line#getLineState()}.
	 */
	@Test
	public void testGetLineState() {
	}

	/**
	 * Test method for {@link fr.univ.lr.mpi.lines.impl.Line#setState(fr.univ.lr.mpi.lines.LineState)}.
	 */
	@Test
	public void testSetState() {
	}

	/**
	 * Test method for {@link fr.univ.lr.mpi.lines.impl.Line#receiveMessage(fr.univ.lr.mpi.exchanges.IMessage)}.
	 */
	@Test
	public void testReceiveMessage() {
	}

	/**
	 * Test method for {@link fr.univ.lr.mpi.lines.impl.Line#pickUp()}.
	 */
	@Test
	public void testPickUp() {
	}

	/**
	 * Test method for {@link fr.univ.lr.mpi.lines.impl.Line#hangUp()}.
	 */
	@Test
	public void testHangUp() {
	}

	/**
	 * Test method for {@link fr.univ.lr.mpi.lines.impl.Line#dialTo(java.lang.String)}.
	 */
	@Test
	public void testDialTo() {
	}

}
