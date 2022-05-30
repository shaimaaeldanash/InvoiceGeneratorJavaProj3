
package com.sales.view;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class InvoiceSalesDialog extends JDialog {
    private JTextField custNameField;
    private JTextField invDateField;
    private JLabel custNameLbl;
    private JLabel invDateLbl;
    private JButton addBton;
    private JButton cancelBton;

    public InvoiceSalesDialog(SalesInvoiceNewFrame frame) {
        custNameLbl = new JLabel("Customer Name:");
        custNameField = new JTextField(20);
        invDateLbl = new JLabel("Invoice Date:");
        invDateField = new JTextField(20);
        addBton = new JButton("Add");
        cancelBton = new JButton("Cancel");
        
        addBton.setActionCommand("createInvoiceAdd");
        cancelBton.setActionCommand("createInvoiceCancel");
        
        addBton.addActionListener(frame.getController());
        cancelBton.addActionListener(frame.getController());
        setLayout(new GridLayout(3, 2));
        
        add(invDateLbl);
        add(invDateField);
        add(custNameLbl);
        add(custNameField);
        add(addBton);
        add(cancelBton);
        
        pack();
        
    }

    public JTextField getCustNameField() {
        return custNameField;
    }

    public JTextField getInvDateField() {
        return invDateField;
    }   
}

