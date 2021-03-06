package fr.univ.lr.mpi.simulation;

import java.util.ArrayList;
import java.util.List;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.gui.*;
import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.lines.impl.Line;

public class PhoneWidget extends QWidget {
	
	public QGroupBox groupBox;
	public QGridLayout gridLayout;
	public QPushButton pickUpButton;
	public QPushButton hangUpButton;
	public QPushButton sendButton;
	public QPushButton clearButton;
	public QPushButton transfertButton;
	public QComboBox directoryComboBox;
	public QTextEdit messageEdit;
	public QLabel dialLabel;
	public QLabel messageLabel;
	public QTextBrowser logBrowser;
	public QTimer timer;

	/**
	 * The phone number
	 */
	private String phoneNumber;
	/**
	 * The line which is represent by the phoneWidget
	 */
	private Line line;
	/**
	 * A list who contain all the different phone numbers
	 */
	private List<String> directory;
	/**
	 * This boolean is used in blink()
	 */
	private Boolean defaultColor;

	/**
	 * This signal start the timer
	 */
	public Signal0 startBlink = new Signal0();
	/**
	 * Thi signal stop the timer
	 */
	public Signal0 stopBlink = new Signal0();

	/**
	 * Class constructor
	 * @param parent
	 * @param line
	 * @param dir
	 */
	public PhoneWidget(QWidget parent, Line line, List<String> dir) {
		super(parent);
		this.resize(175, 350);
		this.defaultColor = true;
		this.line = line;
		this.line.setPhone(this);
		this.phoneNumber = this.line.getPhoneNumber();
		
		// Set the directory, and remove the phone number of the phoneWidget in the list
		this.directory = new ArrayList<String>();
		for (int i = 0; i < dir.size(); i++) {
			if (!this.phoneNumber.equals(dir.get(i))) {
				this.directory.add(dir.get(i));
			}
		}
		
		// Initialisation of  the different widget :
		groupBox = new QGroupBox(this.phoneNumber, this);
		groupBox.setGeometry(0, 0, 175, 350);

		pickUpButton = new QPushButton(this);
		pickUpButton.setIcon(new QIcon(new QPixmap("content/pick_up.png")));
		pickUpButton.setIconSize(new QSize(41, 39));

		hangUpButton = new QPushButton(this);
		hangUpButton.setIcon(new QIcon(new QPixmap("content/hang_up.png")));
		hangUpButton.setIconSize(new QSize(41, 39));
		hangUpButton.setEnabled(false);

		dialLabel = new QLabel(this);
		dialLabel.setText("Dial :");
		dialLabel
				.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag
						.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter));

		directoryComboBox = new QComboBox(this);
		directoryComboBox.addItems(this.directory);

		messageLabel = new QLabel(this);
		messageLabel.setText("Message :");
		messageLabel
				.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag
						.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter));

		sendButton = new QPushButton(this);
		sendButton.setText("Send");

		messageEdit = new QTextEdit(this);

		logBrowser = new QTextBrowser(this);

		clearButton = new QPushButton(this);
		clearButton.setText("Clear log");

		transfertButton = new QPushButton("Transfer rule", this);

		timer = new QTimer(this);
		timer.setInterval(500);

		gridLayout = new QGridLayout(this);

		// Add the different widget to the layout :
		gridLayout.addWidget(pickUpButton, 0, 0, 1, 1);
		gridLayout.addWidget(hangUpButton, 0, 1, 1, 1);
		gridLayout.addWidget(dialLabel, 1, 0, 1, 2);
		gridLayout.addWidget(directoryComboBox, 2, 0, 1, 2);
		gridLayout.addWidget(messageLabel, 3, 0, 1, 1);
		gridLayout.addWidget(sendButton, 3, 1, 1, 1);
		gridLayout.addWidget(messageEdit, 4, 0, 1, 2);
		gridLayout.addWidget(logBrowser, 5, 0, 1, 2);
		gridLayout.addWidget(clearButton, 6, 0, 1, 2);
		gridLayout.addWidget(transfertButton, 7, 0, 1, 2);

		groupBox.setLayout(gridLayout);

		this.connection();

	}

	/**
	 * This function made the connections between the slots and the different signals
	 */
	private void connection() {
		this.pickUpButton.pressed.connect(this, "pickUpThePhone()");
		this.hangUpButton.pressed.connect(this, "hangUpThePhone()");
		this.directoryComboBox.activated.connect(this, "dialing(String)");
		this.sendButton.pressed.connect(this, "sendMessage()");
		this.clearButton.pressed.connect(this, "clearTheLog()");
		this.timer.timeout.connect(this, "blink()");
		this.startBlink.connect(this.timer, "start()");
		this.stopBlink.connect(this.timer, "stop()");
		this.transfertButton.pressed.connect(this, "askForTransfert()");
	}

	/**
	 * This function write the messages received by the Line in the textBrowser
	 * @param message
	 */
	public void appendLog(IMessage message) {
		switch (message.getMessageType()) {
		case VOICE_EXCHANGE:
			this.logBrowser.append("[" + message.getCallerPhoneNumber()
					+ "] : " + message.getContent());
			break;

		// When the message type is BUSY or RINGING, the signal startBlink is emitted
		case BUSY:
		case RINGING:
			this.startBlink.emit();
			this.logBrowser.append(message.getMessageType().toString());
			break;

		// When the line receive a STOP_RINGING message, the signal stopBlink is emitted and we set the default icon 
		// on the button Pick up
		case STOP_RINGING:
    			this.stopBlink.emit();
				pickUpButton.setIcon(new QIcon(new QPixmap("content/pick_up.png")));
				this.defaultColor = true;
    			break;
		
    	// When the line receive a CONNECTION_CLOSED message, the signal stopBlink is emitted and we set the default icon 
    	// on the button Hang up
		case CONNECTION_CLOSED:
			this.stopBlink.emit();
			pickUpButton.setIcon(new QIcon(new QPixmap("content/pick_up.png")));
			this.logBrowser.append(message.getMessageType().toString());
			break;
			
		default:
			this.logBrowser.append(message.getMessageType().toString());
			break;
		}
	}

	/**
	 * This slot is called when the user press the PickUpButton
	 */
	public void pickUpThePhone() {
		// The signal stopBlink is emitted, and the default icon is set
		this.stopBlink.emit();
		pickUpButton.setIcon(new QIcon(new QPixmap("content/pick_up.png")));
		this.defaultColor = true;
		this.hangUpButton.setEnabled(true);
		this.pickUpButton.setEnabled(false);
		this.line.pickUp();
	}

	/**
	 * This slot is called when the HangUpButton is pressed
	 */
	public void hangUpThePhone() {
		this.stopBlink.emit();
		hangUpButton.setIcon(new QIcon(new QPixmap("content/hang_up.png")));
		this.defaultColor = false;
		this.pickUpButton.setEnabled(true);
		this.hangUpButton.setEnabled(false);
		this.line.hangUp();
	}

	/**
	 * This slot is called when the user choose a phone number in the comboBox
	 * @param number
	 */
	public void dialing(String number) {
		this.line.dialTo(number);
		this.logBrowser.append(number);
	}

	/**
	 * The slot sendMessage() is called when the button Send is called
	 */
	public void sendMessage() {
		String message = this.messageEdit.toPlainText();
		if (!message.equals("")) {
			this.messageEdit.clear();
			this.line.sendMessage(message);
		}
	}

	/**
	 * This function is called when the button clear is pressed. This function erase the content of the textBrowser
	 */
	public void clearTheLog() {
		this.logBrowser.clear();
	}

	/**
	 * This slot is called when the timer emit the signal timerOut
	 * When the line is free, the pickUp button blink, and when the line is busy, the hangUp button blink
	 */
	public void blink() {
		switch (this.line.getState()) {
		case FREE:
			if (this.defaultColor) {
				pickUpButton.setIcon(new QIcon(new QPixmap(
						"content/pick_up_blink.png")));
				this.defaultColor = false;
			} else {
				pickUpButton.setIcon(new QIcon(new QPixmap(
						"content/pick_up.png")));
				this.defaultColor = true;
			}
			break;

		case BUSY:
			if (this.defaultColor) {
				hangUpButton.setIcon(new QIcon(new QPixmap(
						"content/hang_up_blink.png")));
				this.defaultColor = false;
			} else {
				hangUpButton.setIcon(new QIcon(new QPixmap(
						"content/hang_up.png")));
				this.defaultColor = true;
			}
			break;
		}
		this.timer.start();
	}

	/**
	 * This slot is called when the button Transfer is pressed
	 * We create a DialogWindow, and if the user press "OK", we add a transfer rule, 
	 * and if he press "Cancel", we remove the transfer rule
	 */
	public void askForTransfert() {

		String transfer = QInputDialog.getItem(this, "Transfer", "Select the  recipient number :", this.directory, 0, false);
		if (transfer != null) {
			this.line.addTransfertRules(transfer);
			this.logBrowser.append("Add transfer to " + transfer);

		} else {
			this.line.removeTransfertRules();
			this.logBrowser.append("Remove transfer rule");
		}

	}
}
