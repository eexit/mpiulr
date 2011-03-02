/********************************************************************************
** Form generated from reading ui file 'PhoneWidget.jui'
**
** Created: mer. 2. mars 14:43:33 2011
**      by: Qt User Interface Compiler version 4.5.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package fr.univ.lr.mpi.simulation;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_PhoneWidget implements com.trolltech.qt.QUiForm<QWidget>
{
    public QWidget widget;
    public QGridLayout gridLayout;
    public QHBoxLayout horizontalLayout;
    public QPushButton pickUpButton;
    public QPushButton hangUpButton;
    public QComboBox directoryComboBox;
    public QTextEdit messageEdit;
    public QLabel numLabel;

    public Ui_PhoneWidget() { super(); }

    public void setupUi(QWidget PhoneWidget)
    {
        PhoneWidget.setObjectName("PhoneWidget");
        PhoneWidget.resize(new QSize(196, 195).expandedTo(PhoneWidget.minimumSizeHint()));
        widget = new QWidget(PhoneWidget);
        widget.setObjectName("widget");
        widget.setGeometry(new QRect(11, 11, 171, 176));
        gridLayout = new QGridLayout(widget);
        gridLayout.setObjectName("gridLayout");
        horizontalLayout = new QHBoxLayout();
        horizontalLayout.setObjectName("horizontalLayout");
        pickUpButton = new QPushButton(widget);
        pickUpButton.setObjectName("pickUpButton");
        pickUpButton.setMinimumSize(new QSize(39, 41));
        pickUpButton.setIcon(new QIcon(new QPixmap("../../../../../../content/pick_up.png")));
        pickUpButton.setIconSize(new QSize(41, 39));

        horizontalLayout.addWidget(pickUpButton);

        hangUpButton = new QPushButton(widget);
        hangUpButton.setObjectName("hangUpButton");
        hangUpButton.setIcon(new QIcon(new QPixmap("../../../../../../content/hang_up.png")));
        hangUpButton.setIconSize(new QSize(41, 39));

        horizontalLayout.addWidget(hangUpButton);


        gridLayout.addLayout(horizontalLayout, 1, 0, 1, 1);

        directoryComboBox = new QComboBox(widget);
        directoryComboBox.setObjectName("directoryComboBox");

        gridLayout.addWidget(directoryComboBox, 2, 0, 1, 1);

        messageEdit = new QTextEdit(widget);
        messageEdit.setObjectName("messageEdit");

        gridLayout.addWidget(messageEdit, 3, 0, 1, 1);

        numLabel = new QLabel(widget);
        numLabel.setObjectName("numLabel");
        numLabel.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter));

        gridLayout.addWidget(numLabel, 0, 0, 1, 1);

        QWidget.setTabOrder(pickUpButton, directoryComboBox);
        QWidget.setTabOrder(directoryComboBox, messageEdit);
        retranslateUi(PhoneWidget);

        PhoneWidget.connectSlotsByName();
    } // setupUi

    void retranslateUi(QWidget PhoneWidget)
    {
        PhoneWidget.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("PhoneWidget", "Form", null));
        pickUpButton.setText("");
        hangUpButton.setText("");
        numLabel.setText(com.trolltech.qt.core.QCoreApplication.translate("PhoneWidget", "TextLabel", null));
    } // retranslateUi

}

