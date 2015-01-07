/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar;

import com.alee.laf.WebLookAndFeel;

import javax.swing.*;

/**
 *
 * @author Chaz
 */
public class Contar  {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // You should work with UI (including installing L&F) inside Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                // Install WebLaF as application L&F
                WebLookAndFeel.install();
                frmLogin login = new frmLogin();
                
            }

            
        });
    }
}
