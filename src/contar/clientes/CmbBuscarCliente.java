/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.clientes;

import BDO.BDO_Cliente;
import DAO.DAO_Cliente;
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
public class CmbBuscarCliente extends WebComboBox {

    private DAO_Cliente connCliente = new DAO_Cliente();
    private BDO_Cliente clienteSeleccionado;
    private FrmCliente ventanaPadre;
    public CmbBuscarCliente(FrmCliente ventanaPadre) {
        super();
        this.ventanaPadre = ventanaPadre;
        createUI();
    }

    private void createUI() {
        setEditable(true);
        setEditorColumns(20);
        setToolTipText("Ingrese NIT o apellido");
        connCliente = new DAO_Cliente();
        cargarClientes(Catalogos.getClientes());
        setClienteSeleccionado(Catalogos.getClientes().get(0));
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
                    cargarClientes(new ArrayList(connCliente.busquedaNitApellido(buscado.trim()).values()));
                } else {
                    cargarClientes(Catalogos.getClientes());
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
                    if(verificarClienteSeleccionado(localCombo.getSelectedItem())){
                        ventanaPadre.actualizarDatos(clienteSeleccionado);
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

    private boolean verificarClienteSeleccionado(Object item){
        if(item != null){
            String itemStr = item.toString();
            if(!itemStr.isEmpty()){
                String[] datosCliente = itemStr.split(" | ");
                if(datosCliente.length>2){
                    BDO_Cliente clienteEncontrado = Catalogos.buscarCliente(datosCliente[0]);
                    if(clienteEncontrado !=null){
                        setClienteSeleccionado(clienteEncontrado);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public void cargarClientes(ArrayList<BDO_Cliente> clientesEncontrados) {
        removeAllItems();
        for (int i = 0; i < clientesEncontrados.size(); i++) {
            addItem(clientesEncontrados.get(i).basicsToString());
        }        
    }

    public BDO_Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    private void setClienteSeleccionado(BDO_Cliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }
}
