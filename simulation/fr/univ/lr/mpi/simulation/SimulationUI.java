package fr.univ.lr.mpi.simulation;

import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QWidget;

import fr.univ.lr.mpi.commutator.impl.AutoCommutator;
import fr.univ.lr.mpi.commutator.impl.Concentrator;
import fr.univ.lr.mpi.exceptions.LineException;
import fr.univ.lr.mpi.exceptions.PhoneNumberValidatorException;

import fr.univ.lr.mpi.lines.impl.Line;

/**
 * 
 * @author FAUCHER Tony
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
		Line l5 = new Line("0506070809");
		Line l6 = new Line("0107080910");
		Line l7 = new Line("0208091011");
		Line l8 = new Line("0309101112");


		
		
		List<String> dir = new ArrayList<String>();
		dir.add(l1.getPhoneNumber());
		dir.add(l2.getPhoneNumber());
		dir.add(l3.getPhoneNumber());
		dir.add(l4.getPhoneNumber());
		dir.add(l5.getPhoneNumber());
		dir.add(l6.getPhoneNumber());
		dir.add(l7.getPhoneNumber());
		dir.add(l8.getPhoneNumber());
		dir.add("3103");
		List<Line> lines = new ArrayList<Line>();
		lines.add(l1);
		lines.add(l2);
		lines.add(l3);
		lines.add(l4);
		lines.add(l5);
		lines.add(l6);
		lines.add(l7);
		lines.add(l8);

		Concentrator concentrator = new Concentrator();
		commutator.setConcentrator(concentrator);

		concentrator.registerLine(l1);
		concentrator.registerLine(l2);
		concentrator.registerLine(l3);
		concentrator.registerLine(l4);
		concentrator.registerLine(l5);
		concentrator.registerLine(l6);
		concentrator.registerLine(l7);
		concentrator.registerLine(l8);

		/**
		 * Windows instanciation
		 */
		QWidget windows = new QWidget();
		windows.setWindowTitle("Telecom Project");

		/**
		 * Differents phone instanciation
		 */

		QGridLayout layout = new QGridLayout();
		List<QWidget> phones = new ArrayList();
		int x = 0;
		int y = 0;
		
		for (int i = 0; i < lines.size(); i++) {
			QWidget phone = new PhoneWidget(windows, lines.get(i), dir);
			lines.get(i).setPhone((PhoneWidget) phone);
			phones.add(phone);
			if (i % 2 == 0) {
				y = 1;
			} else {
				y = 0;
			}
			phone.setGeometry(0, 0, 180, 355);
			layout.addWidget(phone, y, x, 1, 1);
			if(i % 2 != 0) {
				x++;
			}
		}
		windows.setLayout(layout);
		windows.setGeometry(50, 50,x*180,2*355);
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
