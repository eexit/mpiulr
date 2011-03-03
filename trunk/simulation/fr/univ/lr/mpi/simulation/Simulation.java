package fr.univ.lr.mpi.simulation;

import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;

import fr.univ.lr.mpi.commutator.impl.AutoCommutator;
import fr.univ.lr.mpi.commutator.impl.Concentrator;
import fr.univ.lr.mpi.exceptions.LineException;
import fr.univ.lr.mpi.exceptions.PhoneNumberValidatorException;
import fr.univ.lr.mpi.exchanges.IEvent;
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
			PhoneNumberValidatorException, InterruptedException {
		System.setProperty("com.trolltech.qt.thread-check", "no");
		QApplication.initialize(args);

		AutoCommutator commutator = AutoCommutator.getInstance();

		Line l1 = new Line("0102030105");
		Line l2 = new Line("0203040506");
		Line l3 = new Line("0304050607");
		Line l4 = new Line("0405060708");

		List<String> dir = new ArrayList<String>();
		dir.add(l1.getPhoneNumber());
		dir.add(l2.getPhoneNumber());
		dir.add(l3.getPhoneNumber());
		dir.add(l4.getPhoneNumber());
		List<Line> lines = new ArrayList<Line>();
		lines.add(l1);
		lines.add(l2);
		lines.add(l3);
		lines.add(l4);

		IEvent event = new Event(EventType.CREATE_TRANSFER);
		event.addAttribute(ExchangeAttributeNames.CALLER_PHONE_NUMBER, "0203040506");
		event.addAttribute(ExchangeAttributeNames.RECIPIENT_PHONE_NUMBER, "0304050607");
		commutator.sendEvent(event);

		Concentrator concentrator = new Concentrator();
		commutator.setConcentrator(concentrator);

		concentrator.registerLine(l1);
		concentrator.registerLine(l2);
		concentrator.registerLine(l3);
		concentrator.registerLine(l4);

		/**
		 * Windows instanciation
		 */
		QWidget windows = new QWidget();
		windows.setWindowTitle("Telecom Project");

		/**
		 * Differents phone instanciation
		 */

		List<QWidget> phones = new ArrayList<QWidget>();
		int x = 10;
		int y = 10;

		for (int i = 0; i < lines.size(); i++) {

			if (i % 2 == 0) {

				x = 10;
				if (i != 0)
					y += 310;

			} else {
				x = 180;
			}

			QWidget phone = new PhoneWidget(windows, lines.get(i), dir);
			phone.setGeometry(x, y, phone.width(), phone.height());
			phone.show();
			phones.add(phone);
		}

		/**
		 * Log Instanciation
		 */

		windows.show();

		System.out.println("Actives Connections : "
				+ commutator.getActiveConnections());

		QApplication.exec();
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
