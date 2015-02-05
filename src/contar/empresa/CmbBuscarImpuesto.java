/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.empresa;

import BDO.BDO_Impuesto;
import DAO.DAO_Impuesto;
import com.alee.laf.combobox.WebComboBox;
import contar.Catalogos;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JTextField;

/**
 *
 * @author Chaz
 */
public class CmbBuscarImpuesto extends WebComboBox{
    private DAO_Impuesto connImpuesto = new DAO_Impuesto();
    private BDO_Impuesto impuestoSeleccionado;
    private FrmImpuesto ventanaPadre;
    
    public CmbBuscarImpuesto(FrmImpuesto ventanaPadre) {
        super();
        this.ventanaPadre = ventanaPadre;
        createUI();
    }
    
    private void createUI(){
        setEditable(true);
        setEditorColumns(20);
        setToolTipText("Ingrese código o descripción");
        connImpuesto = new DAO_Impuesto();
        cargarImpuestos(Catalogos.getImpuestos());
        setImpuestoSeleccionado(Catalogos.getImpuestos().get(0));
        getEditor().getEditorComponent().addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String buscado = ((JTextField) getEditor().getEditorComponent()).getText();
                if (!buscado.isEmpty()) {
                    cargarImpuestos(new ArrayList(connImpuesto.busquedaIdNombre(buscado.trim()).values()));
                } else {
                    cargarImpuestos(Catalogos.getImpuestos());
                }
                setSelectedItem(buscado);
                setPopupVisible(true);
            }
        });
        
        addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    WebComboBox localCombo = (WebComboBox)e.getSource();
                    if(verificarImpuestoSeleccionado(localCombo.getSelectedItem())){
                        ventanaPadre.actualizarDatos(impuestoSeleccionado);
                    }
                }
            }
            
        });
        
        getEditor().getEditorComponent().addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e) {

                ((JTextField)getEditor().getEditorComponent()).selectAll();                
                
            }

            @Override
            public void focusLost(FocusEvent e) {
                
            }
            
        });
    }

    private boolean verificarImpuestoSeleccionado(Object item){
        if(item != null){
            String itemStr = item.toString();
            if(!itemStr.isEmpty()){
                String[] datosImpuesto = itemStr.split(" | ");
                if(datosImpuesto.length>2){
                    BDO_Impuesto impuestoEncontrado = Catalogos.buscarImpuestoInt(Integer.parseInt(datosImpuesto[0]));
                    if(impuestoEncontrado !=null){
                        setImpuestoSeleccionado(impuestoEncontrado);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public void cargarImpuestos(ArrayList<BDO_Impuesto> impuestosEncontrados) {
        if (impuestosEncontrados != null) {
            removeAllItems();
            for (int i = 0; i < impuestosEncontrados.size(); i++) {
                addItem(impuestosEncontrados.get(i).basicsToString());
            }
        }     
    }

    public BDO_Impuesto getImpuestoSeleccionado() {
        return impuestoSeleccionado;
    }

    public void setImpuestoSeleccionado(BDO_Impuesto impuestoSeleccionado) {
        this.impuestoSeleccionado = impuestoSeleccionado;
    }
    
    
}
