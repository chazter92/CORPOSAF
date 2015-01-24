/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.clientes;

import BDO.BDO_Estado_Cliente;
import DAO.DAO_Estado_Cliente;
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
public class CmbBuscarEstadoCliente extends WebComboBox{
    private DAO_Estado_Cliente connEstadoCliente = new DAO_Estado_Cliente();
    private BDO_Estado_Cliente estadoSeleccionado;
    private FrmEstadoCliente ventanaPadre;
    
    public CmbBuscarEstadoCliente(FrmEstadoCliente ventanaPadre) {
        super();
        this.ventanaPadre = ventanaPadre;
        createUI();
    }
    
    private void createUI(){
        setEditable(true);
        setEditorColumns(20);
        setToolTipText("Ingrese código o descripción");
        connEstadoCliente = new DAO_Estado_Cliente();
        cargarEstadosCliente(Catalogos.getEstados_cliente());
        setEstadoSeleccionado(Catalogos.getEstados_cliente().get(0));
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
                    cargarEstadosCliente(new ArrayList(connEstadoCliente.busquedaCodigoTipo(buscado.trim()).values()));
                } else {
                    cargarEstadosCliente(Catalogos.getEstados_cliente());
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
                    if(verificarEstadoSeleccionado(localCombo.getSelectedItem())){
                        ventanaPadre.actualizarDatos(estadoSeleccionado);
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
    
    private boolean verificarEstadoSeleccionado(Object item){
        if(item != null){
            String itemStr = item.toString();
            if(!itemStr.isEmpty()){
                String[] datosCliente = itemStr.split(" | ");
                if(datosCliente.length>2){
                    BDO_Estado_Cliente estadoEncontrado = Catalogos.buscarEstadoClienteInt(Integer.parseInt(datosCliente[0]));
                    if(estadoEncontrado !=null){
                        setEstadoSeleccionado(estadoEncontrado);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public void cargarEstadosCliente(ArrayList<BDO_Estado_Cliente> clientesEncontrados) {
        removeAllItems();
        for (int i = 0; i < clientesEncontrados.size(); i++) {
            addItem(clientesEncontrados.get(i).basicsToString());
        }        
    }

    public BDO_Estado_Cliente getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    public void setEstadoSeleccionado(BDO_Estado_Cliente estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }
}
