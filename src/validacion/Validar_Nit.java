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
public class Validar_Nit extends FocusAdapter {

    @Override
    public void focusLost(FocusEvent ev) {
        JTextField tEntrada = (JTextField) (ev.getSource());
        String nitIngresado = tEntrada.getText().trim();
        if (!nitIngresado.isEmpty()) {
            if (!valirdarNit(nitIngresado)) {
                if (WebOptionPane.showOptionDialog(null, "El NIT ingresado no es válido. ¿Desea continuar?", "NIT no válido", WebOptionPane.YES_NO_OPTION, WebOptionPane.QUESTION_MESSAGE, null, new Object[]{"Sí", "No"}, "No") == 1) {
                    tEntrada.requestFocus();
                    tEntrada.selectAll();
                }
            }
        } else {
            Catalogos.mostrarMensajeError("Este campo no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            tEntrada.requestFocus();
            tEntrada.selectAll();
        }
    }

    public boolean valirdarNit(String nitIngresado) {
        if (nitIngresado.matches("^[1-9][0-9]+[kK]?")) {
            
            String primeraParte = nitIngresado.substring(0, nitIngresado.length()-1);
            int factor = primeraParte.length() + 1;
            int suma = 0;
            for (int i = 0; i < primeraParte.length(); i++) {
                int mult = Character.getNumericValue(primeraParte.charAt(i)) * factor;
                suma += mult;
                factor--;
            }

            int residuo = (11 - (suma % 11)) % 11;

            String verificadorStr = nitIngresado.substring(nitIngresado.length()-1);
            int verificador = 0;

            if (verificadorStr.equalsIgnoreCase("k")) {
                verificador = 10;
            } else {
                verificador = Integer.parseInt(verificadorStr);
            }

            if (residuo == verificador) {
                return true;
            } else {
                return false;
            }
        } else {
            Catalogos.mostrarMensajeError("Formato no válido para el NIT ingresado. Verifique la información.", "Error", WebOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
