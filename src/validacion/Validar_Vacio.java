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
public class Validar_Vacio extends FocusAdapter {
    private String campo;
    public Validar_Vacio(String campo){
        super();
        this.campo = campo;
    }
    
    @Override
    public void focusLost(FocusEvent ev) {
        JTextField tEntrada = (JTextField) (ev.getSource());
        String ingresado = tEntrada.getText().trim();
        if (ingresado.isEmpty()) {
            Catalogos.mostrarMensajeError(campo+" no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
        }
    }
}
