package fr.univ.lr.mpi.simulation;

import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;

import fr.univ.lr.mpi.commutator.impl.AutoCommutator;
import fr.univ.lr.mpi.commutator.impl.Concentrator;
import fr.univ.lr.mpi.exceptions.LineException;
import fr.univ.lr.mpi.exceptions.PhoneNumberValidatorException;

import fr.univ.lr.mpi.lines.impl.Line;

/**
 * 
 * @author FAUCHER Tony <faucher.tony85@gmail.com>
 * 
 */

public class SimulationUI {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws LineException,
			PhoneNumberValidatorException, InterruptedException {

		System.setProperty("com.trolltech.qt.thread-check", "no");

		/**
		 * UI instanciation
		 */

		QApplication.initialize(args);

		/**
		 * Autocommutator end line instanciation
		 */
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

		List<QWidget> phones = new ArrayList();
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
			lines.get(i).setPhone((PhoneWidget) phone);
			phone.show();
			phones.add(phone);
		}

		/**
		 * Log Instanciation
		 */

		// QWidget logBrowser = ((MessageObserver)
		// commutator.getServices().get(4))
		// .getLogWidget();
		// logBrowser.setParent(windows);
		// logBrowser.setGeometry(410, windows.height() - 300, 800, 250);
		// logBrowser.show();
		
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
