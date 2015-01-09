/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.clientes;

import BDO.BDO_Cliente;
import DAO.DAO_Cliente;
import com.alee.laf.combobox.WebComboBox;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JTextField;

/**
 *
 * @author Chaz
 */
public class CmbBuscarCliente extends WebComboBox {

    DAO_Cliente connCliente = new DAO_Cliente();
    ArrayList<BDO_Cliente> todosClientes;
    
    public CmbBuscarCliente() {
        super();
        createUI();
    }

    private void createUI() {
        setEditable(true);
        setEditorColumns(20);
        setToolTipText("Ingrese NIT o apellido");
        connCliente = new DAO_Cliente();
        todosClientes = connCliente.todosClientes();
        cargarClientes(todosClientes);
        getEditor().getEditorComponent().addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String buscado = ((JTextField) getEditor().getEditorComponent()).getText();
                if (!buscado.isEmpty()) {
                    cargarClientes(connCliente.busquedaNitApellido(buscado.trim()));
                } else {
                    todosClientes = connCliente.todosClientes();
                    cargarClientes(todosClientes);
                }
                setSelectedItem(buscado);
                setPopupVisible(true);
            }
        });
    }

    private void cargarClientes(ArrayList<BDO_Cliente> clientesEncontrados) {
        removeAllItems();
        for (int i = 0; i < clientesEncontrados.size(); i++) {
            addItem(clientesEncontrados.get(i).basicsToString());
        }        
    }
}
