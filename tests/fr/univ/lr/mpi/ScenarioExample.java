package fr.univ.lr.mpi;

import java.util.ArrayList;
import java.util.List;

import fr.univ.lr.mpi.commutator.impl.AutoCommutator;
import fr.univ.lr.mpi.commutator.impl.Concentrator;
import fr.univ.lr.mpi.lines.impl.Line;
import fr.univ.lr.mpi.services.impl.AnsweringService;

public class ScenarioExample {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		System.out.flush();
		System.out.println("Setting up scenario actors...");
		
		Line l1 = new Line("0111111111");
		Line l2 = new Line("0222222222");
		
		Concentrator concentrator = new Concentrator();
		AutoCommutator.getInstance().setConcentrator(concentrator);
		concentrator.registerLine(l1);
		concentrator.registerLine(l2);
		
		System.out.println("Scenario actors setup....DONE.");
		
		Thread.sleep(2000);
		
		System.out.println("\nSimulation starts...\n");
		System.out.println("Case where the phone number doesn't exists :\n");
		l1.pickUp();
		l1.dialTo("0333333333");
		Thread.sleep(1000);
		l1.hangUp();
		
		System.out.println("\n\nCase where Line 1 drops a message to Line 2 answering machine");
		l1.pickUp();
		l1.dialTo(l2.getPhoneNumber());
		Thread.sleep(10000);
		l1.sendMessage("Message à déposer sur répondeur");
		l1.hangUp();
		
		System.out.println("\n\nCase where Line 2 answers to Line 1 call, hangs up and recovers talk with Line 1"
			+ " and finally hangs up on Line 1");
		l1.pickUp();
		l1.dialTo(l2.getPhoneNumber());
		Thread.sleep(1000);
		l2.pickUp();
		l1.sendMessage("Salut !");
		l2.sendMessage("Comment vas-tu ?");
		l1.sendMessage("Bien bien...");
		l2.hangUp();
		l2.pickUp();
		l2.sendMessage("Désolé, j'ai fait une fausse manip'");
		l1.sendMessage("Pas de soucis...");
		l2.hangUp();
		Thread.sleep(4000);
		
		System.out.println("\n\nCase where Line 1 hangs up on Line 2");
		System.out.println("L1 pn: " + l1.getPhoneNumber());
		l1.pickUp();
		Thread.sleep(1000);
		l1.dialTo(l2.getPhoneNumber());
		l2.pickUp();
		l1.sendMessage("Raccroche pas au nez !");
		Thread.sleep(1000);
		l1.hangUp();
		Thread.sleep(1000);
		
		System.out.println("\n\nCase where Line 2 recovers his answering machine messages");
		l2.pickUp();
		l2.dialTo(AnsweringService.ANSWERING_MACHINE_PHONE_NUMBER);
		Thread.sleep(1000);
		l2.hangUp();
		
		System.out.println("\nSimulation DONE.");
	}
}
