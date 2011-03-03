package fr.univ.lr.mpi.simulation;
import java.util.ArrayList;
import java.util.List;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.gui.*;
import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.lines.impl.Line;

public class PhoneWidget extends QWidget
{
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
    
    private String phoneNumber;
    private Line line;
    private List<String> directory;
    private Boolean defaultColor;
    
    public Signal0 startBlink = new Signal0();
    public Signal0 stopBlink = new Signal0();


    public PhoneWidget(QWidget parent, Line line, List<String> dir)
    {
        super(parent);
        this.resize(171, 301);
        this.defaultColor = true;
        this.line = line;
        this.line.setPhone(this);
        this.phoneNumber = this.line.getPhoneNumber();
        this.directory = new ArrayList<String>();
        for(int i = 0; i < dir.size(); i++)
        {
        	if(!this.phoneNumber.equals(dir.get(i)))
        	{
        		this.directory.add(dir.get(i));
        	}
        }
        groupBox = new QGroupBox(this.phoneNumber, this);
        groupBox.setGeometry(0, 0, 171, 301);
        
        pickUpButton = new QPushButton(this);
        pickUpButton.setIcon(new QIcon(new QPixmap("content/pick_up.png")));
        pickUpButton.setIconSize(new QSize(41, 39));
        
        hangUpButton = new QPushButton(this);
        hangUpButton.setIcon(new QIcon(new QPixmap("content/hang_up.png")));
        hangUpButton.setIconSize(new QSize(41, 39));
        hangUpButton.setEnabled(false);
        
        dialLabel = new QLabel(this);
        dialLabel.setText("Dial :");
        dialLabel.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter));
        
        directoryComboBox = new QComboBox(this);
        directoryComboBox.addItems(this.directory);
        
        messageLabel = new QLabel(this);
        messageLabel.setText("Message :");
        messageLabel.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter));

        sendButton = new QPushButton(this);
        sendButton.setText("Send");
        
        messageEdit = new QTextEdit(this);
       
        
        logBrowser = new QTextBrowser(this);
        
        clearButton = new QPushButton(this);
        clearButton.setText("Clear log");
        
        transfertButton = new QPushButton("Make a transfert rule", this);
        
        timer = new QTimer(this);
        timer.setInterval(500);
        
        gridLayout = new QGridLayout(this);
        
        gridLayout.addWidget(pickUpButton,0,0,1,1);
        gridLayout.addWidget(hangUpButton,0,1,1,1);
        gridLayout.addWidget(dialLabel,1,0,1,2);
        gridLayout.addWidget(directoryComboBox,2,0,1,2);
        gridLayout.addWidget(messageLabel,3,0,1,1);
        gridLayout.addWidget(sendButton,3,1,1,1);
        gridLayout.addWidget(messageEdit,4,0,1,2);
        gridLayout.addWidget(logBrowser,5,0,1,2);
        gridLayout.addWidget(clearButton,6,0,1,2);
        gridLayout.addWidget(transfertButton,7,0,1,2);
        
        groupBox.setLayout(gridLayout);
        
        this.connection();
        
    }
    
    private void connection()
    {
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
    
    public void appendLog(IMessage message)
    {
    	switch(message.getMessageType())
    	{
    		case VOICE_EXCHANGE :
    			this.logBrowser.append(message.getContent());
    			break;
    			
    		case BUSY :
    		case RINGING :
    			this.startBlink.emit();
    			this.logBrowser.append(message.getMessageType().toString());
    			break;
    			
    		case CONNECTION_CLOSED :
    			this.hangUpButton.pressed.emit();
    			this.logBrowser.append(message.getMessageType().toString());
    			break;
    		
    		default :
    			this.logBrowser.append(message.getMessageType().toString());
    			break;
    	}
    }
    
    public void pickUpThePhone()
    {
    	this.stopBlink.emit();
    	pickUpButton.setIcon(new QIcon(new QPixmap("content/pick_up.png")));
		this.defaultColor = true;
    	this.hangUpButton.setEnabled(true);
    	this.pickUpButton.setEnabled(false);
    	this.line.pickUp();
    }
    
    public void hangUpThePhone()
    {
    	this.stopBlink.emit();
    	hangUpButton.setIcon(new QIcon(new QPixmap("content/hang_up.png")));
		this.defaultColor = false;
    	this.pickUpButton.setEnabled(true);
    	this.hangUpButton.setEnabled(false);
    	this.line.hangUp();
    }
    
    public void dialing(String number)
    {
    	this.line.dialTo(number);
    	this.logBrowser.append(number);
    }
    
    public void sendMessage()
    {
    	String message = this.messageEdit.toPlainText();
    	if(!message.equals(""))
    	{
    		this.messageEdit.clear();
    		this.line.sendMessage(message);
    	}
    }
    
    public void clearTheLog()
    {
    	this.logBrowser.clear();
    }
    
    public void blink()
    {
    	switch(this.line.getState())
    	{
    	case FREE :
    		this.logBrowser.append("RING!!");
        	if(this.defaultColor) {
        		pickUpButton.setIcon(new QIcon(new QPixmap("content/pick_up_blink.png")));
        		this.defaultColor = false;
        	} else {
        		pickUpButton.setIcon(new QIcon(new QPixmap("content/pick_up.png")));
        		this.defaultColor = true;
        	}
    		break;
    		
    	case BUSY :
    		this.logBrowser.append("Recipient is busy, please hang up!");
    		if(this.defaultColor) {
    			hangUpButton.setIcon(new QIcon(new QPixmap("content/hang_up_blink.png")));
        		this.defaultColor = false;
        	} else {
        		hangUpButton.setIcon(new QIcon(new QPixmap("content/hang_up.png")));
        		this.defaultColor = true;
        	}
    		break;
    	}
    	this.timer.start();
    }
    
    public void askForTransfert()
    {
    	String transfer = QInputDialog.getItem(this, "Transfert", "Select the  recipient number :", this.directory);
    	if(!transfer.equals("")) {
    		// Envoyer un message transfert
    	}
    }
}
