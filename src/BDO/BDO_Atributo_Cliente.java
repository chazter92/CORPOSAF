/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDO;

import com.alee.laf.combobox.WebComboBox;
import contar.clientes.FrmCliente;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 *
 * @author Chaz
 */
public class BDO_Atributo_Cliente {
    private int id_atributo_cliente;
    private String nombre, descripcion;
    
    public BDO_Atributo_Cliente(int id_atributo_cliente, String nombre, String descripcion) {
        this.id_atributo_cliente = id_atributo_cliente;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_atributo_cliente() {
        return id_atributo_cliente;
    }

    public void setId_atributo_cliente(int id_atributo_cliente) {
        this.id_atributo_cliente = id_atributo_cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String basicsToString() {
        return this.id_atributo_cliente + " | " + this.nombre;
    }
    
    public WebComboBox getCombo(ArrayList<BDO_Atributo_Valor_Cliente> valores, int columnaDatos, final FrmCliente ventana){
        WebComboBox combo = new WebComboBox();
        combo.setEditorColumns(columnaDatos);
        combo.addItem("Seleccione una opción");
        for(int i=0;i<valores.size();i++){
            combo.addItem(valores.get(i).getValor());
        }
        
        combo.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    WebComboBox localCombo = (WebComboBox)e.getSource();
                    if(localCombo.getSelectedIndex() != 0){
                        ventana.agregarDatosBusquedaAvanzada(id_atributo_cliente, localCombo.getSelectedItem().toString());
                    }
                }
            }
            
        });
        
        return combo;
    }
}
