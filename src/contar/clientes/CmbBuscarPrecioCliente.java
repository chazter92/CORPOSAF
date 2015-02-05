/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.clientes;

import BDO.BDO_Precio_Cliente;
import DAO.DAO_Precio_Cliente;
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
public class CmbBuscarPrecioCliente extends WebComboBox {

    private DAO_Precio_Cliente connPrecioCliente = new DAO_Precio_Cliente();
    private BDO_Precio_Cliente precioSeleccionado;
    private FrmPrecioCliente ventanaPadre;

    CmbBuscarPrecioCliente(FrmPrecioCliente ventanaPadre) {
        super();
        this.ventanaPadre = ventanaPadre;
        createUI();
    }

    private void createUI() {

        setEditable(true);
        setEditorColumns(20);
        setToolTipText("Ingrese código o descripción");
        connPrecioCliente = new DAO_Precio_Cliente();
        cargarPreciosCliente(Catalogos.getPrecios_cliente());
        setPrecioSeleccionado(Catalogos.getPrecios_cliente().get(0));
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
                    cargarPreciosCliente(new ArrayList(connPrecioCliente.busquedaCodigoTipo(buscado.trim()).values()));
                } else {
                    cargarPreciosCliente(Catalogos.getPrecios_cliente());
                }
                setSelectedItem(buscado);
                setPopupVisible(true);
            }
        });

        addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    WebComboBox localCombo = (WebComboBox) e.getSource();
                    if (verificarPrecioSeleccionado(localCombo.getSelectedItem())) {
                        ventanaPadre.actualizarDatos(precioSeleccionado);
                    }
                }
            }
        });

        getEditor().getEditorComponent().addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                ((JTextField)getEditor().getEditorComponent()).selectAll();
                
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
    }

    private boolean verificarPrecioSeleccionado(Object item) {
        if (item != null) {
            String itemStr = item.toString();
            if (!itemStr.isEmpty()) {
                String[] datosCliente = itemStr.split(" | ");
                if (datosCliente.length > 2) {
                    BDO_Precio_Cliente precioEncontrado = Catalogos.buscarPrecioClienteInt(Integer.parseInt(datosCliente[0]));
                    if (precioEncontrado != null) {
                        setPrecioSeleccionado(precioEncontrado);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void cargarPreciosCliente(ArrayList<BDO_Precio_Cliente> preciosEncontrados) {
        if (preciosEncontrados != null) {
            removeAllItems();
            for (int i = 0; i < preciosEncontrados.size(); i++) {
                addItem(preciosEncontrados.get(i).basicsToString());
            }
        } 
    }

    public BDO_Precio_Cliente getPrecioSeleccionado() {
        return precioSeleccionado;
    }

    private void setPrecioSeleccionado(BDO_Precio_Cliente precioSeleccionado) {
        this.precioSeleccionado = precioSeleccionado;
    }
}
