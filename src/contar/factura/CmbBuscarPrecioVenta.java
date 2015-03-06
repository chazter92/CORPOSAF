/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.factura;

import BDO.BDO_Tipo_Pago_Venta;
import DAO.DAO_Precio_Venta;
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
public class CmbBuscarPrecioVenta extends WebComboBox{
    private DAO_Precio_Venta connPrecioVenta = new DAO_Precio_Venta();
    private BDO_Tipo_Pago_Venta precioSeleccionado;
    private FrmPrecioVenta ventanaPadre;
    
    public CmbBuscarPrecioVenta(FrmPrecioVenta ventanaPadre) {
        super();
        this.ventanaPadre = ventanaPadre;
        createUI();
    }
    
    private void createUI(){
        setEditable(true);
        setEditorColumns(20);
        setToolTipText("Ingrese código o nombre");
        connPrecioVenta = new DAO_Precio_Venta();
        cargarPreciosVenta(Catalogos.getTiposPagoVenta());
        setPrecioSeleccionado(Catalogos.getTiposPagoVenta().get(0));
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
                    cargarPreciosVenta(new ArrayList(connPrecioVenta.busquedaIdNombre(buscado.trim()).values()));
                } else {
                    cargarPreciosVenta(Catalogos.getTiposPagoVenta());
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
                    if(verificarPrecioSeleccionado(localCombo.getSelectedItem())){
                        ventanaPadre.actualizarDatos(precioSeleccionado);
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
    
    private boolean verificarPrecioSeleccionado(Object item){
        if(item != null){
            String itemStr = item.toString();
            if(!itemStr.isEmpty()){
                String[] datosImpuesto = itemStr.split(" | ");
                if(datosImpuesto.length>2){
                    BDO_Tipo_Pago_Venta precioEncontrado = Catalogos.buscarTipoPagoVentaInt(Integer.parseInt(datosImpuesto[0]));
                    if(precioEncontrado !=null){
                        setPrecioSeleccionado(precioEncontrado);
                        return true;
                    }
                }
            }
        }
        return false;
    } 
     
    public void cargarPreciosVenta(ArrayList<BDO_Tipo_Pago_Venta> preciosEncontrados) {
        if (preciosEncontrados != null) {
            removeAllItems();
            for (int i = 0; i < preciosEncontrados.size(); i++) {
                addItem(preciosEncontrados.get(i).basicsToString());
            }
        }     
    }
    
    public BDO_Tipo_Pago_Venta getPrecioVentaSeleccionado() {
        return precioSeleccionado;
    }
    
    public void setPrecioSeleccionado(BDO_Tipo_Pago_Venta precioSeleccionado) {
        this.precioSeleccionado = precioSeleccionado;
    }
}
