/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.clientes;

import BDO.BDO_Atributo_Cliente;
import DAO.DAO_Atributo_Cliente;
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
public class CmbBuscarAtributoCliente extends WebComboBox{
    private DAO_Atributo_Cliente connAtributoCliente = new DAO_Atributo_Cliente();
    private BDO_Atributo_Cliente atributoSeleccionado;
    private FrmAtributoCliente ventanaPadre;
    
    public CmbBuscarAtributoCliente(FrmAtributoCliente ventanaPadre) {
        super();
        this.ventanaPadre = ventanaPadre;
        createUI();
    }
    
    private void createUI(){
        setEditable(true);
        setEditorColumns(20);
        setToolTipText("Ingrese código o descripción");
        connAtributoCliente = new DAO_Atributo_Cliente();
        cargarAtributoCliente(Catalogos.getAtributos_cliente());
        if(Catalogos.getAtributos_cliente().size()>0){
            setAtributoSeleccionado(Catalogos.getAtributos_cliente().get(0));
        }
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
                    cargarAtributoCliente(new ArrayList(connAtributoCliente.busquedaIdNombre(buscado.trim()).values()));
                } else {
                    cargarAtributoCliente(Catalogos.getAtributos_cliente());
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
                    if(verificarAtributoSeleccionado(localCombo.getSelectedItem())){
                        ventanaPadre.actualizarDatos(atributoSeleccionado);
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
    
    private boolean verificarAtributoSeleccionado(Object item){
        if(item != null){
            String itemStr = item.toString();
            if(!itemStr.isEmpty()){
                String[] datosCliente = itemStr.split(" | ");
                if(datosCliente.length>2){
                    BDO_Atributo_Cliente atributoEncontrado = Catalogos.buscarAtributoClienteInt(Integer.parseInt(datosCliente[0]));
                    if(atributoEncontrado !=null){
                        setAtributoSeleccionado(atributoEncontrado);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public void cargarAtributoCliente(ArrayList<BDO_Atributo_Cliente> atributosEncontrados) {
        removeAllItems();
        for (int i = 0; i < atributosEncontrados.size(); i++) {
            addItem(atributosEncontrados.get(i).basicsToString());
        }        
    }

    public BDO_Atributo_Cliente getAtributoSeleccionado() {
        return atributoSeleccionado;
    }

    public void setAtributoSeleccionado(BDO_Atributo_Cliente atributoSeleccionado) {
        this.atributoSeleccionado = atributoSeleccionado;
    }
    
    
}
