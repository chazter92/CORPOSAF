/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package validacion;

import com.alee.laf.optionpane.WebOptionPane;
import contar.Catalogos;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;

/**
 *
 * @author Chaz
 */
public class Validar_Entero extends FocusAdapter {
    @Override
    public void focusLost(FocusEvent ev) {
        JTextField tEntrada = (JTextField) (ev.getSource());
        String ingresado = tEntrada.getText().trim();
        if (!ingresado.isEmpty()) {
            if (!valirdarDouble(ingresado)) {
                Catalogos.mostrarMensajeError("Cifra no válida. Ingrese un número. Ejemplo: 4, 17, 26", "Error", WebOptionPane.ERROR_MESSAGE);
            }
        } 
    }

    public boolean valirdarDouble(String ingresado) {
        if(ingresado.matches("^\\d+")){
            return true;
        }        
        return false;
    }
    
}
