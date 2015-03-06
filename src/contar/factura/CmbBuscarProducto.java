/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.factura;

import BDO.BDO_Producto;
import DAO.DAO_Producto;
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
public class CmbBuscarProducto extends WebComboBox{
    private DAO_Producto connProducto = new DAO_Producto();
    private BDO_Producto productoSeleccionado;
    private FrmFactura ventanaPadre;
    
    public CmbBuscarProducto(FrmFactura ventanaPadre) {
        super();
        this.ventanaPadre = ventanaPadre;
        createUI();
    }
    
    private void createUI(){
        setEditable(true);
        setEditorColumns(20);
        setToolTipText("Ingrese SKU o descripción");
        connProducto = new DAO_Producto();
        cargarProductos(Catalogos.getProductosActivos());
        setProductoSeleccionado(Catalogos.getProductosActivos().get(0));
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
                    cargarProductos(new ArrayList(connProducto.busquedaSKUConceptoActivo(buscado.trim()).values()));
                } else {
                    cargarProductos(Catalogos.getProductosActivos());
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
                    if(verificarProductoSeleccionado(localCombo.getSelectedItem())){
                        ventanaPadre.actualizarDatos(productoSeleccionado);
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
    
     private boolean verificarProductoSeleccionado(Object item){
        if(item != null){
            String itemStr = item.toString();
            if(!itemStr.isEmpty()){
                String[] datosProducto = itemStr.split(" | ");
                if(datosProducto.length>2){
                    BDO_Producto productoSel = Catalogos.buscarProductoActivo(datosProducto[0]);
                    if(productoSel !=null){
                        setProductoSeleccionado(productoSel);
                        return true;
                    }
                }
            }
        }
        return false;
    }
     
    public void cargarProductos(ArrayList<BDO_Producto> productosEncontrados) {
        if (productosEncontrados != null) {
            removeAllItems();
            for (int i = 0; i < productosEncontrados.size(); i++) {
                addItem(productosEncontrados.get(i).basicsToString());
            }
        }      
    }

    public BDO_Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(BDO_Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }
    
    
    
}

