package fr.univ.lr.mpi.exchanges;

public class Message implements IMessage {

	// attributs
	private String calledPhoneNumber;
	private String recipientPhoneNumber;

	// constructor
	public Message() {

	}

	// getter and setter

	// called phone number
	/**
	 * @return the called phone number
	 */
	public String getCalledPhoneNumber() {
		return calledPhoneNumber;
	}

	public void setCalledPhoneNumber(String calledPhoneNumber) {
		this.calledPhoneNumber = calledPhoneNumber;
	}

	// recipient phone number
	/**
	 * @return the recipient phone number
	 */
	public String getRecipientPhoneNumber() {
		return recipientPhoneNumber;
	}

	public void setRecipientPhoneNumber(String recipientPhoneNumber) {
		this.recipientPhoneNumber = recipientPhoneNumber;
	}

}
