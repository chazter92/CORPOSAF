/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDO;

import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import contar.Catalogos;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/**
 *
 * @author Chaz
 */
public class BDO_Atributo_Valor_Cliente {

    String valor, nit;
    BDO_Atributo_Cliente atributo_cliente;

    public BDO_Atributo_Valor_Cliente(String valor, String nit, int atributo_cliente) {
        this.valor = valor;
        this.nit = nit;
        this.atributo_cliente = Catalogos.buscarAtributoClienteInt(atributo_cliente);
    }

    public BDO_Atributo_Cliente getAtributo_cliente() {
        return atributo_cliente;
    }

    public void setAtributo_cliente(BDO_Atributo_Cliente atributo_cliente) {
        this.atributo_cliente = atributo_cliente;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String basicsToString() {
        if (atributo_cliente != null) {
            return this.nit + " | " + this.atributo_cliente.getId_atributo_cliente() + " | " + this.valor;
        } else {
            return String.valueOf(this.hashCode());
        }
    }

    public Component toPanel(int columnasDatos) {
        WebTextField txtValor = new WebTextField();
        txtValor.setColumns(columnasDatos);
        txtValor.setInputPrompt(atributo_cliente.descripcion);
        txtValor.setHideInputPromptOnFocus(false);
        txtValor.setText(valor);
        txtValor.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e) {
                ((JTextField)e.getComponent()).selectAll(); 
            }

            @Override
            public void focusLost(FocusEvent e) {
                valor = ((JTextField)e.getComponent()).getText();
            }
            
        });
        WebPanel pnlValor = new WebPanel();
        pnlValor.setLayout(new FlowLayout());
        pnlValor.add(new WebLabel(atributo_cliente.nombre + ":"));
        pnlValor.add(txtValor);
        TooltipManager.setTooltip(pnlValor, atributo_cliente.descripcion, TooltipWay.up);
        TooltipManager.setTooltip(txtValor, atributo_cliente.descripcion, TooltipWay.up);

        return pnlValor;

    }
}
