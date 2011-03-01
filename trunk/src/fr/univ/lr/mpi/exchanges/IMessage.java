package fr.univ.lr.mpi.exchanges;

import fr.univ.lr.mpi.exchanges.impl.MessageType;

public interface IMessage {

	public MessageType getMessageType();
	public String getCallerPhoneNumber();
	public String getRecipientPhoneNumber();
	
}
