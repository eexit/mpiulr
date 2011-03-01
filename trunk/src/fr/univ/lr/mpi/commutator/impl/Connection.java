package fr.univ.lr.mpi.commutator.impl;

import java.sql.Date;
import java.util.Timer;
import fr.univ.lr.mpi.commutator.IConnection;
import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.exchanges.impl.Message;
import fr.univ.lr.mpi.lines.ILine;

/**
 * 
 * MPI_PROJECT/fr.univ.lr.mpi.commutator.impl/Connection.java
 * 
 * @author Joris Berthelot <joris.berthelot@gmail.com>
 * @date Mar 1, 2011
 * 
 */
public class Connection implements IConnection {

	/**
	 * Caller line container
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private ILine caller;

	/**
	 * Recipient line container
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private ILine recipient;

	/**
	 * Connection start time
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private Date startTime;

	/**
	 * Connection end time
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private Date endTime;

	/**
	 * Connection timer which will be launch when one of the connection lines
	 * hang up. When to timer reaches it limit, the connection is destroyed.
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	private Timer timer;

	/**
	 * Message forwarding between the two connected lines
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 * @param message
	 */
	public void forwardMessage(Message message) {

	}

	/**
	 * Message receiver of commutator container
	 * 
	 * @author Joris Berthelot <joris.berthelot@gmail.com>
	 */
	public void receiveMessage(IMessage message) {
		// TODO Auto-generated method stub

	}
}
