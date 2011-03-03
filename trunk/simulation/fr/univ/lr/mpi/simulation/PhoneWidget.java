package fr.univ.lr.mpi.simulation;
import java.util.ArrayList;
import java.util.List;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.*;
import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.lines.impl.Line;

public class PhoneWidget extends QWidget
{
    public QGridLayout gridLayout;
    public QPushButton pickUpButton;
    public QPushButton hangUpButton;
    public QPushButton sendButton;
    public QPushButton clearButton;
    public QComboBox directoryComboBox;
    public QTextEdit messageEdit;
    public QLabel numLabel;
    public QLabel dialLabel;
    public QLabel messageLabel;
    public QTextBrowser logBrowser;
    
    private String phoneNumber;
    private Line line;
    private List<String> directory;

    public PhoneWidget(QWidget parent, Line line, List<String> dir)
    {
        super(parent);
        this.resize(171, 301);
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
        
        numLabel = new QLabel(this);
        numLabel.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter));
        numLabel.setText(this.phoneNumber);
        
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
        
        gridLayout = new QGridLayout(this);
        
        gridLayout.addWidget(numLabel,0,0,1,2);
        gridLayout.addWidget(pickUpButton,1,0,1,1);
        gridLayout.addWidget(hangUpButton,1,1,1,1);
        gridLayout.addWidget(dialLabel,2,0,1,2);
        gridLayout.addWidget(directoryComboBox,3,0,1,2);
        gridLayout.addWidget(messageLabel,4,0,1,1);
        gridLayout.addWidget(sendButton,4,1,1,1);
        gridLayout.addWidget(messageEdit,5,0,1,2);
        gridLayout.addWidget(logBrowser,6,0,1,2);
        gridLayout.addWidget(clearButton,7,0,1,2);
        
        this.connection();
        
    }
    
    private void connection()
    {
    	this.pickUpButton.pressed.connect(this, "pickUpThePhone()");
    	this.hangUpButton.pressed.connect(this, "hangUpThePhone()");
    	this.directoryComboBox.activated.connect(this, "dialing(String)");
    	this.sendButton.pressed.connect(this, "sendMessage()");
    	this.clearButton.pressed.connect(this, "clearTheLog()");
    }
    
    public void appendLog(IMessage message)
    {
    	if(!message.getMessageType().toString().equals("VOICE_EXCHANGE"))
    	{
    		this.logBrowser.append(message.getMessageType().toString());
    	}
    	else
    	{
    		this.logBrowser.append(message.getContent());
    	}
    }
    
    public void pickUpThePhone()
    {
    	this.hangUpButton.setEnabled(true);
    	this.pickUpButton.setEnabled(false);
    	this.line.pickUp();
    }
    
    public void hangUpThePhone()
    {
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
    	this.line.sendMessage(this.messageEdit.toPlainText());
    	this.logBrowser.append(this.messageEdit.toPlainText());
    }
    
    public void clearTheLog()
    {
    	this.logBrowser.clear();
    }
}
