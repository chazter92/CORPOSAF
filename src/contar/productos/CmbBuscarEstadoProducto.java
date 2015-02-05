/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.productos;

import BDO.BDO_Estado_Producto;
import DAO.DAO_Estado_Producto;
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
public class CmbBuscarEstadoProducto extends WebComboBox {

    private DAO_Estado_Producto connEstadoProducto = new DAO_Estado_Producto();
    private BDO_Estado_Producto estadoSeleccionado;
    private FrmEstadoProducto ventanaPadre;

    public CmbBuscarEstadoProducto(FrmEstadoProducto ventanaPadre) {
        super();
        this.ventanaPadre = ventanaPadre;
        createUI();
    }

    private void createUI() {
        setEditable(true);
        setEditorColumns(20);
        setToolTipText("Ingrese código o descripción");
        connEstadoProducto = new DAO_Estado_Producto();
        if (Catalogos.getEstados_Producto() != null) {
            cargarEstadosProducto(Catalogos.getEstados_Producto());
            setEstadoSeleccionado(Catalogos.getEstados_Producto().get(0));
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
                    cargarEstadosProducto(new ArrayList(connEstadoProducto.busquedaCodigoTipo(buscado.trim()).values()));
                } else {
                    cargarEstadosProducto(Catalogos.getEstados_Producto());
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
                    if (verificarEstadoSeleccionado(localCombo.getSelectedItem())) {
                        ventanaPadre.actualizarDatos(estadoSeleccionado);
                    }
                }
            }
        });

        getEditor().getEditorComponent().addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                ((JTextField) getEditor().getEditorComponent()).selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
    }

    private boolean verificarEstadoSeleccionado(Object item) {
        if (item != null) {
            String itemStr = item.toString();
            if (!itemStr.isEmpty()) {
                String[] datosProducto = itemStr.split(" | ");
                if (datosProducto.length > 2) {
                    BDO_Estado_Producto estadoEncontrado = Catalogos.buscarEstadoProductoInt(Integer.parseInt(datosProducto[0]));
                    if (estadoEncontrado != null) {
                        setEstadoSeleccionado(estadoEncontrado);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void cargarEstadosProducto(ArrayList<BDO_Estado_Producto> ProductosEncontrados) {
        if (ProductosEncontrados != null) {
            removeAllItems();
            for (int i = 0; i < ProductosEncontrados.size(); i++) {
                addItem(ProductosEncontrados.get(i).basicsToString());
            }
        }

    }

    public BDO_Estado_Producto getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    public void setEstadoSeleccionado(BDO_Estado_Producto estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }
}
