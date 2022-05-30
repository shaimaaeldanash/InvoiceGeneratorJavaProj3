
package com.sales.model;

import java.util.ArrayList;

public class InvoiceNewSales {
    private int num;
    private String Date;
    private String Customer;
    private ArrayList<LineNewSales> lines;
    

    public InvoiceNewSales() {
    }

    public InvoiceNewSales(int num, String Date, String Customer) {
        this.num = num;
        this.Date = Date;
        this.Customer = Customer;
    }
    public double getInvoiceTotal()  {
        double total = 0.0 ;
        for (LineNewSales line : getLines()){
           total += line.getLineTotal();
        }
        return total ;
    }

    public ArrayList<LineNewSales> getLines() {
        if (lines == null){
            lines = new ArrayList<>();
            
        }
        return lines;
    }
    

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String Customer) {
        this.Customer = Customer;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    @Override
    public String toString() {
        return "Invoice{" + "num=" + num + ", date=" + Date + ", Customer=" + Customer + '}';
    }
    
    public String getAsCSV(){
        return num + "," + Date + "," + Customer ; 
    
    }
    
    
}
