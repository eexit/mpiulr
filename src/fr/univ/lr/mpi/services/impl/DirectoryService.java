package fr.univ.lr.mpi.services.impl;

import java.util.ArrayList;
import java.util.List;

import fr.univ.lr.mpi.exchanges.IEvent;
import fr.univ.lr.mpi.services.IService;

/**
 * 
 * @author FAUCHER Tony
 * 
 */
public class DirectoryService implements IService {

	// attribute
	private List<String> directory;

	/**
	 * Constructor
	 * 
	 */
	public DirectoryService() {
		this.directory = new ArrayList<String>();
	}

	/**
	 * Add a phone number in the directory
	 * 
	 * @param phoneNumber
	 */
	public void addPhoneNumber(String phoneNumber) {
		this.directory.add(phoneNumber);
	}

	/**
	 * @param phoneNumber
	 * @return if the phone number is in the directory or not
	 */
	public boolean exist(String phoneNumber) {
		boolean exist = false;
		for (int i = 0; i < this.directory.size() && !exist; i++) {
			if (phoneNumber.equals(this.directory.get(i))) {
				exist = true;
			}
		}
		return exist;
	}

	@Override
	public void receiveEvent(IEvent event) {
		// TODO Auto-generated method stub
	}
}
