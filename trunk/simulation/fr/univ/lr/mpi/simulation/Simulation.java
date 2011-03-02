package fr.univ.lr.mpi.simulation;

import java.util.Date;

import fr.univ.lr.mpi.commutator.impl.AutoCommutator;
import fr.univ.lr.mpi.commutator.impl.Concentrator;
import fr.univ.lr.mpi.exceptions.LineException;
import fr.univ.lr.mpi.exceptions.PhoneNumberValidatorException;
import fr.univ.lr.mpi.exchanges.impl.Event;
import fr.univ.lr.mpi.exchanges.impl.EventType;
import fr.univ.lr.mpi.exchanges.impl.ExchangeAttributeNames;
import fr.univ.lr.mpi.lines.impl.Line;

/**
 * 
 * @author Elian ORIOU <elian.oriou@gmail.com>
 * 
 */

public class Simulation {

	public static void main(String[] args) throws LineException,
			PhoneNumberValidatorException {
		AutoCommutator commutator = AutoCommutator.getInstance();

		Line l1 = new Line("0102030105");
		Line l2 = new Line("0203040506");
		Line l3 = new Line("0304050607");
		Line l4 = new Line("0405060708");

		Concentrator concentrator = new Concentrator();
		commutator.setConcentrator(concentrator);

		concentrator.registerLine(l1);
		concentrator.registerLine(l2);
		concentrator.registerLine(l3);
		concentrator.registerLine(l4);

		l1.pickUp();
		l2.pickUp();

		/* L2 => L4 */
		l2.dialTo("0405060708");
		/* L1 => L3 */
		// l1.dialTo("0304050607");

		// Event e;
		// /* Directory Service test */
		// e = new Event(EventType.PHONE_NUMBER_REQUEST);
		// e
		// .addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER,
		// "0102030105");
		// e.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
		// "0203040506");
		// AutoCommutator.getInstance().sendEvent(e);
		//
		// /* Billing Service test */
		// e = new Event(EventType.CONNECTION_CLOSED);
		// e
		// .addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER,
		// "0102030105");
		// e.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
		// "0203040506");
		// e
		// .addAttribute(ExchangeAttributeNames.DATE, new Date()
		// .toLocaleString());
		// e.addAttribute(ExchangeAttributeNames.CONNECTION_DURATION, "12.2");
		// AutoCommutator.getInstance().sendEvent(e);
		//
		// /* transfert service test */
		//
		// // // create a transfert
		// Event e2 = new Event(EventType.CREATE_TRANSFER);
		// e2.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER,
		// "0203040506");
		// e2.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
		// "0304050607");
		// AutoCommutator.getInstance().sendEvent(e2);
		//
		// // test transfer
		// e = new Event(EventType.CALL_TRANSFER_REQUEST);
		// e
		// .addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER,
		// "0102030105");
		// e.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
		// "0203040506");
		// AutoCommutator.getInstance().sendEvent(e);
		// // remove
		// e = new Event(EventType.REMOVE_TRANSFER);
		// e.addAttribute(ExchangeAttributeNames.PHONE_NUMBER, "0203040506");
		// AutoCommutator.getInstance().sendEvent(e);
		//
		// // test transfer
		// e = new Event(EventType.CALL_TRANSFER_REQUEST);
		// e
		// .addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER,
		// "0102030105");
		// e.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
		// "0203040506");
		// AutoCommutator.getInstance().sendEvent(e);
		//
		// System.out.println("Actives Connections : "
		// + commutator.getActiveConnections());
	}

	/**
	 * 
	 * @param length
	 * @return
	 */

	public static String generatePhoneNumber(int length) {
		String returns = "05";
		for (int i = 0; i < length - 2; i++) {
			int j = (int) Math.round(Math.random() * 10);
			returns += j;
		}
		System.out.println(returns);
		return returns;
	}
}
