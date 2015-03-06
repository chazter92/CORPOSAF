/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.clientes;

import BDO.BDO_Estado_Cliente;
import DAO.DAO_Estado_Cliente;
import com.alee.laf.button.WebButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.desktoppane.WebInternalFrame;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import contar.Catalogos;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import validacion.Validar_Vacio;

/**
 *
 * @author Chaz
 */
public class FrmEstadoCliente extends WebInternalFrame {

    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    private CmbBuscarEstadoCliente cmbEstadosCliente;
    private WebTextField txtTipo;
    private WebCheckBox chkFacturar;
    private WebButton btnGuardarEstado, btnCancelar, btnNuevoEstado, btnEditarEstado;
    private final int columnasDatos = 25;
    private boolean editarEstado = false;
    private DAO_Estado_Cliente connEstadoCliente;

    public ImageIcon loadIcon(final String path) {
        return loadIcon(getClass(), path);
    }

    public static ImageIcon loadIcon(final Class nearClass, final String path) {
        try {
            final String key = nearClass.getCanonicalName() + ":" + path;
            if (!iconsCache.containsKey(key)) {
                iconsCache.put(key, new ImageIcon(nearClass.getResource("icons/" + path)));
            }
            return iconsCache.get(key);
        } catch (Exception e) {
            return new ImageIcon();
        }
    }

    public FrmEstadoCliente() {
        super("Clientes | Estados", true, true, true, true);
        createUI();
    }

    private void createUI() {
        WebPanel pnlEstadosCliente = new WebPanel();
        pnlEstadosCliente.setLayout(new BorderLayout());
        pnlEstadosCliente.add(agregarPanelSuperior(), BorderLayout.NORTH);
        pnlEstadosCliente.add(agregarPanelBotones(), BorderLayout.SOUTH);
        pnlEstadosCliente.add(agregarDatosEstadoCliente(), BorderLayout.CENTER);

        pnlEstadosCliente.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
        add(pnlEstadosCliente);
        setFrameIcon(loadIcon("account_menu.png"));
        cambiarBloqueoCampos(false);
        addInternalFrameListener(new InternalFrameListener(){

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                //Catalogos.mostrarNotificacion("opened", NotificationIcon.color);
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                //Catalogos.mostrarNotificacion("closing", NotificationIcon.color);
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                //Catalogos.mostrarNotificacion("closed", NotificationIcon.color);
            }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) {
                //Catalogos.mostrarNotificacion("icon", NotificationIcon.color);
            }

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {
                //Catalogos.mostrarNotificacion("deicon", NotificationIcon.color);
            }

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {

            }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
                cancelarOperacion();
            }
            
        });
        pack();
    }

    private Component agregarPanelBotones() {

        WebPanel pnlBotonesDatosIndividuales = new WebPanel();
        pnlBotonesDatosIndividuales.setLayout(new FlowLayout());
        btnGuardarEstado = new WebButton("Guardar", loadIcon("save_new.png"));
        btnCancelar = new WebButton("Cancelar", loadIcon("cancel.png"));

        btnGuardarEstado.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                guardarEstadoCliente();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarOperacion();
            }
        });

        pnlBotonesDatosIndividuales.add(btnGuardarEstado);
        pnlBotonesDatosIndividuales.add(btnCancelar);
        return pnlBotonesDatosIndividuales;
    }

    private Component agregarDatosEstadoCliente() {
        WebPanel pnlDatosIndividuales = new WebPanel();
        pnlDatosIndividuales.setLayout(new FlowLayout());

        WebPanel pnlIzquierdo = new WebPanel();
        pnlIzquierdo.setLayout(new BoxLayout(pnlIzquierdo, BoxLayout.Y_AXIS));
        WebPanel pnlDerecho = new WebPanel();
        pnlDerecho.setLayout(new BoxLayout(pnlDerecho, BoxLayout.Y_AXIS));

        txtTipo = new WebTextField();
        txtTipo.setColumns(columnasDatos);
        txtTipo.setInputPrompt("Nombre del estado del cliente");
        txtTipo.setHideInputPromptOnFocus(false);
        txtTipo.addFocusListener(new Validar_Vacio("El nombre del estado "));
        WebPanel pnlTipo = new WebPanel();
        pnlTipo.setLayout(new FlowLayout());
        pnlTipo.add(new WebLabel(loadIcon("users_5.png")));
        pnlTipo.add(txtTipo);
        TooltipManager.setTooltip(pnlTipo, "Ingrese nombre del estado del cliente", TooltipWay.up);
        TooltipManager.setTooltip(txtTipo, "Ingrese nombre del estado del cliente", TooltipWay.up);
        pnlDerecho.add(pnlTipo);

        chkFacturar = new WebCheckBox("Puede facturar");

        WebPanel pnlPuedeFacturar = new WebPanel();
        pnlPuedeFacturar.setLayout(new FlowLayout());
        pnlPuedeFacturar.add(new WebLabel(loadIcon("user_delete.png")));
        pnlPuedeFacturar.add(chkFacturar);
        TooltipManager.setTooltip(pnlPuedeFacturar, "Marque la casilla si un cliente puede facturar en este estado", TooltipWay.up);
        chkFacturar.setToolTipText("Marque la casilla si un cliente puede facturar en este estado");
        pnlDerecho.add(pnlPuedeFacturar);

        pnlDatosIndividuales.add(pnlIzquierdo);
        pnlDatosIndividuales.add(pnlDerecho);
        WebScrollPane scroll = new WebScrollPane(pnlDatosIndividuales);
        scroll.setBorder(null);
        actualizarDatos(cmbEstadosCliente.getEstadoSeleccionado());
        return scroll;
    }

    private Component agregarPanelSuperior() {
        WebPanel pnlSuperiorDatosCliente = new WebPanel();
        pnlSuperiorDatosCliente.setLayout(new FlowLayout());
        pnlSuperiorDatosCliente.add(new WebLabel(loadIcon("search.png")));
        pnlSuperiorDatosCliente.add(new WebLabel("Buscar:"));
        cmbEstadosCliente = new CmbBuscarEstadoCliente(this);
        pnlSuperiorDatosCliente.add(cmbEstadosCliente);
        btnEditarEstado = new WebButton("Editar estado", loadIcon("edit.png"));
        btnNuevoEstado = new WebButton("Nuevo estado", loadIcon("add.png"));

        btnEditarEstado.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((WebButton) e.getSource()).setEnabled(false);
                editarEstadoCliente();
            }
        });

        btnNuevoEstado.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoEstadoCliente();
            }
        });
        pnlSuperiorDatosCliente.add(btnEditarEstado);
        pnlSuperiorDatosCliente.add(btnNuevoEstado);
        return pnlSuperiorDatosCliente;
    }

    void actualizarDatos(BDO_Estado_Cliente estadoSeleccionado) {
        if (estadoSeleccionado != null) {
            txtTipo.setText(estadoSeleccionado.getTipo());
            chkFacturar.setSelected(estadoSeleccionado.getPuede_facturar());
        }
    }

    private void nuevoEstadoCliente() {
        editarEstado = false;
        limpiarCampos();
        cambiarBloqueoCampos(true);
    }

    private void guardarEstadoCliente() {
        if (validarDatos()) {
            
            connEstadoCliente = new DAO_Estado_Cliente();
            BDO_Estado_Cliente nuevo = new BDO_Estado_Cliente(0,txtTipo.getText().trim(),chkFacturar.isSelected());
            if (editarEstado) {
                connEstadoCliente.actualizarEstado(nuevo, cmbEstadosCliente.getEstadoSeleccionado());
            } else {
                connEstadoCliente.agregarEstado(nuevo);
            }
            
            editarEstado = false;
            cambiarBloqueoCampos(false);
            actualizarEstadosCliente();
        }
    }
    
    private void actualizarEstadosCliente(){
            Catalogos.actualizarEstadosClientes();
            cmbEstadosCliente.cargarEstadosCliente(Catalogos.getEstados_cliente());
    }

    private void cancelarOperacion() {
        editarEstado = false;
        actualizarDatos(cmbEstadosCliente.getEstadoSeleccionado());
        cambiarBloqueoCampos(false);
    }

    private void editarEstadoCliente() {
        editarEstado = true;
        cambiarBloqueoCampos(true);
    }

    private boolean validarDatos() {
        if (txtTipo.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El nombre del estado no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        }        
        return true;
    }

    private void limpiarCampos() {
        txtTipo.clear();
        chkFacturar.setSelected(true);
    }

    private void cambiarBloqueoCampos(boolean bloqueo) {
        txtTipo.setEnabled(bloqueo);
        chkFacturar.setEnabled(bloqueo);
        btnGuardarEstado.setEnabled(bloqueo);
        btnCancelar.setEnabled(bloqueo);
        cmbEstadosCliente.setEnabled(!bloqueo);
        btnNuevoEstado.setEnabled(!bloqueo);
        btnEditarEstado.setEnabled(!bloqueo);
    }
}
