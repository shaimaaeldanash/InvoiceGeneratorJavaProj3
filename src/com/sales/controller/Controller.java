
package com.sales.controller;

import com.sales.model.InvoiceNewSales;
import com.sales.model.InvoicesTableModel;
import com.sales.model.LineNewSales;
import com.sales.model.LinesTableModel;
import com.sales.view.InvoiceSalesDialog;
import com.sales.view.LineSalesDialog;
import com.sales.view.SalesInvoiceNewFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Controller implements ActionListener, ListSelectionListener {
   
    private SalesInvoiceNewFrame frame ;
    private InvoiceSalesDialog invoiceDialogs;
    private LineSalesDialog lineDialog;
    
    
    public Controller(SalesInvoiceNewFrame frame) {
        this.frame = frame;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("Action: " + actionCommand);
        switch (actionCommand) {
            case "Load File":
                loadFile();
                break;
            
            case "Create New Invoice":
                createNewInvoice();
                break;
            
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "Create New Item":
                createNewItem();
                break;
            case "Delete Item":
                deleteItem();
                break;
           
            case "createInvoiceCancel":
                createSalesInvoiceCancel();
                break;
            case "createInvoiceAdd":
                createSalesInvoiceOK();
                break;
            
            case "createSalesLineOK":
                createLineOK();
                break;
             
            case "createLineCancel":
                createLineCancel();
                break;
            case "Save File":
                saveFile();
                break;
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = frame.getInvoiceTable() .getSelectedRow();
        if (selectedIndex != -1){
        System.out.println("You have selected row: " + selectedIndex);
        InvoiceNewSales currentInvoice = frame.getInvoices().get(selectedIndex);
        frame.getInvoiceNumLabel().setText(""+ currentInvoice.getNum());
        frame.getInvoiceDateLabel(). setText(currentInvoice.getDate());
        frame.getCustomerNameLabel().setText (currentInvoice.getCustomer());
        frame.getInvoiceTotalLabel().setText (""+currentInvoice.getInvoiceTotal());
        LinesTableModel linesTableModel = new LinesTableModel(currentInvoice.getLines());
        frame.getLineTable() .setModel(linesTableModel);
        linesTableModel.fireTableDataChanged();
        }
        }

    private void loadFile() {
        JFileChooser fc = new JFileChooser();
        try{
        int result = fc.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION){
            File headerFile = fc.getSelectedFile();
            Path headerPath = Paths.get(headerFile.getAbsolutePath());
            List<String> headerLines = Files.readAllLines(headerPath);
            System.out.println("Invoices have been read");
            // 1,11-22-2020,Ali
            // 2,10-13-2021,Saleh
            // 3,1-9-2019,Ibrahim
            ArrayList<InvoiceNewSales> invoicesArray = new ArrayList<> ();
            for (String headerLine: headerLines){
                try {
                String [] headerParts = headerLine.split(",");
                int invoiceNum = Integer.parseInt(headerParts[0]);
                String invoiceDate = headerParts[1];
                String customerName = headerParts [2];
                InvoiceNewSales invoice = new InvoiceNewSales (invoiceNum , invoiceDate , customerName);
                invoicesArray.add(invoice);
                    System.out.println(invoice.toString());
                } catch ( Exception ex ){ 
                    ex.printStackTrace ();
                    JOptionPane.showMessageDialog(frame, "Error in line format", " Error" , JOptionPane.ERROR_MESSAGE);
                }
            
            }
            System.out.println("Check point");
            result = fc.showOpenDialog(frame);
            if(result == JFileChooser.APPROVE_OPTION){
                File lineFile = fc.getSelectedFile();
                Path linePath = Paths.get(lineFile.getAbsolutePath());
                List<String> lineLines = Files.readAllLines(linePath);
                System.out.println("Lines have been read");
                for (String lineLine : lineLines ) {
                    try {
                    String lineParts[] = lineLine.split(",");
                    int invoiceNum = Integer.parseInt(lineParts[0]);
                    String itemName = lineParts[1];
                    double itemPrice = Double.parseDouble(lineParts[2]);
                    int count = Integer.parseInt(lineParts[3]);
                    InvoiceNewSales inv = null;
                    for (InvoiceNewSales invoice : invoicesArray){
                        if (invoice.getNum() == invoiceNum ){
                            inv = invoice;
                            break;
                           
                        }
                    }
                    
                    LineNewSales line = new LineNewSales (itemName , itemPrice , count , inv);
                    inv.getLines() .add(line);
                        System.out.println(line.toString());
                    } catch ( Exception ex ){ 
                    ex.printStackTrace ();
                    JOptionPane.showMessageDialog(frame, "Error in line format", " Error" , JOptionPane.ERROR_MESSAGE);
                }
                    
                }
                System.out.println("Check point");
            }
            
            frame.setInvoices(invoicesArray);
            InvoicesTableModel invoicesTableModel = new InvoicesTableModel(invoicesArray);
            frame.setInvoicesTableModel (invoicesTableModel);
            frame.getInvoiceTable ().setModel(invoicesTableModel);
            frame.getInvoicesTableModel ().fireTableDataChanged();
            
        }
        
         } catch (IOException ex) {
             ex.printStackTrace ();
             JOptionPane.showMessageDialog(frame, "cannot read file", " Error" , JOptionPane.ERROR_MESSAGE);
            
         }
    }

    private void saveFile() {
        ArrayList<InvoiceNewSales> invoices = frame.getInvoices();
        String headers = "";
        String lines = "" ;
        for (InvoiceNewSales invoice : invoices){
            String invCSV = invoice.getAsCSV ();
            headers += invCSV;
            headers += "\n" ;
            
            for (LineNewSales line : invoice.getLines()){
                String lineCSV = line.getAsCSV();
                lines += lineCSV;
                lines += "\n" ;
            }
       
        }
        System.out.println("check point");
        try {
        JFileChooser fc = new JFileChooser();
        int result = fc.showSaveDialog(frame );
        if (result == JFileChooser.APPROVE_OPTION){
            File headerFile = fc.getSelectedFile();
            FileWriter hfw = new FileWriter(headerFile);
            hfw.write(headers);
            hfw.flush();
            hfw.close();
            result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION){
                File lineFile = fc.getSelectedFile();
                FileWriter lfw = new FileWriter(lineFile);
                lfw.write(lines);
                lfw.flush();
                lfw.close();
                
            }
        }
        }catch (Exception ex){
            
        }
         }

    private void createNewInvoice() {
        invoiceDialogs = new InvoiceSalesDialog(frame);
        invoiceDialogs.setVisible(true);
        }

    private void deleteInvoice() {
        int selectedRow = frame.getInvoiceTable().getSelectedRow();
        if (selectedRow != -1){
            frame.getInvoices().remove(selectedRow);
            frame.getInvoicesTableModel() .fireTableDataChanged();
        }
        }

    private void createNewItem() {
        lineDialog = new LineSalesDialog (frame);
        lineDialog.setVisible(true);
        }

    private void deleteItem() {
        int selectedRow = frame.getLineTable().getSelectedRow();
        
        if ( selectedRow != -1){
         
            LinesTableModel linesTableModel = (LinesTableModel) frame.getLineTable().getModel();
            linesTableModel.getLines(). remove(selectedRow);
            linesTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();

//            frame.setInvoiceTotalLabel();
        }
        }

    private void createSalesInvoiceCancel() {
        invoiceDialogs.setVisible(false);
        invoiceDialogs.dispose();
        invoiceDialogs = null;
        }

    private void createSalesInvoiceOK() {
        
        String date = invoiceDialogs.getInvDateField().getText();
        String customer = invoiceDialogs.getCustNameField().getText();
        int num = frame.getNextInvoiceNum();
        try {
            String[] dateParts = date.split("-");// "22-5-2013" -> ("22" "05" "2013")
            if (dateParts.length < 3 ){
                JOptionPane.showMessageDialog (frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt (dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                if (day > 31 || month > 12 ){
                    JOptionPane.showMessageDialog (frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
                }else {
                    
                }
            InvoiceNewSales invoice = new InvoiceNewSales (num , date , customer);
            frame.getInvoices().add(invoice);
            frame.getInvoicesTableModel().fireTableDataChanged();
            invoiceDialogs.setVisible(false);
            invoiceDialogs.dispose();
            invoiceDialogs = null;
            }
        } catch (Exception ex){
            JOptionPane.showMessageDialog (frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
        }
       
        
        
         }

    private void createLineOK() {
        String item = lineDialog.getItemNameField().getText();
        String countStr = lineDialog.getItemCountField().getText();
        String priceStr = lineDialog.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoice = frame.getInvoiceTable() .getSelectedRow();
        if (selectedInvoice != -1){
        InvoiceNewSales invoice = frame.getInvoices().get(selectedInvoice);
        LineNewSales line = new LineNewSales (item, price, count, invoice);
        invoice.getLines().add(line);
        LinesTableModel linesTableModel = (LinesTableModel) frame.getLineTable().getModel();
        //linesTableModel.getLines().add(line);
        linesTableModel.fireTableDataChanged();
        frame.getInvoicesTableModel().fireTableDataChanged();
        }
        
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
         }

    private void createLineCancel() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

   

   
    
}
