package fr.univ.lr.mpi.simulation;
import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.*;

import fr.univ.lr.mpi.exchanges.IMessage;
import fr.univ.lr.mpi.exchanges.impl.Message;
import fr.univ.lr.mpi.lines.impl.Line;

public class PhoneWidget extends QWidget
{
    public QGridLayout gridLayout;
    public QPushButton pickUpButton;
    public QPushButton hangUpButton;
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
        this.line.setPhone(this);
        this.line = line;
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
        
        dialLabel = new QLabel(this);
        dialLabel.setText("Dial :");
        dialLabel.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter));
        
        directoryComboBox = new QComboBox(this);
        directoryComboBox.addItems(this.directory);
        
        messageLabel = new QLabel(this);
        messageLabel.setText("Message :");
        messageLabel.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter));

        messageEdit = new QTextEdit(this);
        
        logBrowser = new QTextBrowser(this);
        
        gridLayout = new QGridLayout(this);
        
        gridLayout.addWidget(numLabel,0,0,1,2);
        gridLayout.addWidget(pickUpButton,1,0,1,1);
        gridLayout.addWidget(hangUpButton,1,1,1,1);
        gridLayout.addWidget(dialLabel,2,0,1,2);
        gridLayout.addWidget(directoryComboBox,3,0,1,2);
        gridLayout.addWidget(messageLabel,4,0,1,2);
        gridLayout.addWidget(messageEdit,5,0,1,2);
        gridLayout.addWidget(logBrowser,6,0,1,2);
        
        
    }
    
    private void connection()
    {
    	
    }
    
    public void appendLog(IMessage message)
    {
    	
    }
}
