/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDO;

import com.alee.laf.combobox.WebComboBox;
import contar.productos.FrmProducto;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 *
 * @author Chaz
 */
public class BDO_Atributo_Producto {
    private int id_atributo_producto;
    private String nombre, descripcion;

    public BDO_Atributo_Producto(int id_atributo_producto, String nombre, String descripcion) {
        this.id_atributo_producto = id_atributo_producto;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_atributo_producto() {
        return id_atributo_producto;
    }

    public void setId_atributo_producto(int id_atributo_producto) {
        this.id_atributo_producto = id_atributo_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String basicsToString() {
        return this.id_atributo_producto + " | " + this.nombre;
    }

    public WebComboBox getCombo(ArrayList<BDO_Atributo_Valor_Producto> valores, int columnaDatos, final FrmProducto ventana){
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
                        ventana.agregarDatosBusquedaAvanzada(id_atributo_producto, localCombo.getSelectedItem().toString());
                        localCombo.setSelectedIndex(0);
                    }
                }
            }
            
        });
        
        return combo;
    }

}
