
package SalesInvoiceM;

import com.sales.view.SalesInvoiceNewFrame;

public class Main {
public static void main(String[] args) {
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new SalesInvoiceNewFrame().setVisible(true);
        }
    });
       
    }
    
}