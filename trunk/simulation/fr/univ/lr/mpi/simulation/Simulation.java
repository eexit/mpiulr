package fr.univ.lr.mpi.simulation;

import fr.univ.lr.mpi.commutator.impl.AutoCommutator;
import fr.univ.lr.mpi.lines.impl.Line;

/**
 * 
 * @author Elian ORIOU <elian.oriou@gmail.com>
 * 
 */

public class Simulation {

	public static void main(String[] args) {
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
