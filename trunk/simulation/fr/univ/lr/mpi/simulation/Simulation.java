package fr.univ.lr.mpi.simulation;

import java.util.Date;

import fr.univ.lr.mpi.commutator.impl.AutoCommutator;
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

	public static void main(String[] args) throws PhoneNumberValidatorException, LineException {
		AutoCommutator commutator = AutoCommutator.getInstance();

		Line l1 = new Line("0102030105");
		Line l2 = new Line("0203040506");
		Line l3 = new Line("0304050607");
		Line l4 = new Line("0405060708");

		commutator.registerLine(l1);
		commutator.registerLine(l2);
		commutator.registerLine(l3);
		commutator.registerLine(l4);

		l1.pickUp();
		l2.pickUp();

		Event e;
		/* Directory Service test */
		e = new Event(EventType.PHONE_NUMBER_REQUEST);
		e.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
				"0102030105");
		AutoCommutator.getInstance().sendEvent(e);

		/* Billing Service test */
		// e = new Event(EventType.CONNECTION_CLOSED);
		// e.addAttributes(ExchangeAttributeNames.CALLER_PHONE_NUMBER,
		// "0102030105");
		// e.addAttributes(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
		// "0203040506");
		// e.addAttributes(ExchangeAttributeNames.DATE, new Date().toString());
		// e.addAttributes(ExchangeAttributeNames.CONNECTION_DURATION, null);
		// AutoCommutator.getInstance().sendEvent(e);

		/* transfert service test */

		// create a transfert
		// Event e2 = new Event(EventType.CREATE_TRANSFER);
		// e2.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER,
		// "0203040506");
		// e2.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER,
		// "0304050607");
		//		
		// System.out.println("Built Event : "+e2);
		//		
		// AutoCommutator.getInstance().sendEvent(e2);

		// //test transfert
		// e = new Event(EventType.CALL_TRANSFER_REQUEST);
		// e.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER,
		// "0203040506");
		// AutoCommutator.getInstance().sendEvent(e);

		// //remove
		// e = new Event(EventType.REMOVE_TRANSFER);
		// e.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER,
		// "0203040506");
		// AutoCommutator.getInstance().sendEvent(e);

		// new test
		// e = new Event(EventType.CALL_TRANSFER_REQUEST);
		// e.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER,
		// "0203040506");
		// AutoCommutator.getInstance().sendEvent(e);

		System.out.println("Actives Connections : "
				+ commutator.getActiveConnections());
	}

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
