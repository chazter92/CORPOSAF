/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.clientes;

import BDO.BDO_Atributo_Cliente;
import DAO.DAO_Atributo_Cliente;
import com.alee.laf.button.WebButton;
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
public class FrmAtributoCliente extends WebInternalFrame{
    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    private CmbBuscarAtributoCliente cmbAtributosCliente;
    private WebTextField txtNombre, txtDescripcion;
    private WebButton btnGuardar, btnCancelar, btnNuevo, btnEditar;
    private final int columnasDatos = 25;
    private boolean editarAtributo = false;
    private DAO_Atributo_Cliente connAtributoCliente;
    
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

    public FrmAtributoCliente() {
        super("Clientes | Atributos", true, true, true, true);
        createUI();
    }
    
    private void createUI() {
        WebPanel pnlAtributosCliente = new WebPanel();
        pnlAtributosCliente.setLayout(new BorderLayout());
        pnlAtributosCliente.add(agregarPanelSuperior(), BorderLayout.NORTH);
        pnlAtributosCliente.add(agregarPanelBotones(), BorderLayout.SOUTH);
        pnlAtributosCliente.add(agregarDatosAtributoCliente(), BorderLayout.CENTER);

        pnlAtributosCliente.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
        add(pnlAtributosCliente);
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
        btnGuardar = new WebButton("Guardar", loadIcon("save_new.png"));
        btnCancelar = new WebButton("Cancelar", loadIcon("cancel.png"));

        btnGuardar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                guardarAtributoCliente();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarOperacion();
            }
        });

        pnlBotonesDatosIndividuales.add(btnGuardar);
        pnlBotonesDatosIndividuales.add(btnCancelar);
        return pnlBotonesDatosIndividuales;
    }
    
    private Component agregarDatosAtributoCliente() {
        WebPanel pnlDatosIndividuales = new WebPanel();
        pnlDatosIndividuales.setLayout(new FlowLayout());

        WebPanel pnlIzquierdo = new WebPanel();
        pnlIzquierdo.setLayout(new BoxLayout(pnlIzquierdo, BoxLayout.Y_AXIS));
        WebPanel pnlDerecho = new WebPanel();
        pnlDerecho.setLayout(new BoxLayout(pnlDerecho, BoxLayout.Y_AXIS));

        txtNombre = new WebTextField();
        txtNombre.setColumns(columnasDatos);
        txtNombre.setInputPrompt("Nombre del atributo del cliente");
        txtNombre.setHideInputPromptOnFocus(false);
        txtNombre.addFocusListener(new Validar_Vacio());
        WebPanel pnlTipo = new WebPanel();
        pnlTipo.setLayout(new FlowLayout());
        pnlTipo.add(new WebLabel(loadIcon("nit.png")));
        pnlTipo.add(txtNombre);
        TooltipManager.setTooltip(pnlTipo, "Ingrese el nombre del atributo del cliente", TooltipWay.up);
        TooltipManager.setTooltip(txtNombre, "Ingrese el nombre del atributo del cliente", TooltipWay.up);
        pnlDerecho.add(pnlTipo);

        
        txtDescripcion = new WebTextField();
        txtDescripcion.setColumns(columnasDatos);
        txtDescripcion.setInputPrompt("Descripción breve del atributo");
        txtDescripcion.setHideInputPromptOnFocus(false);
        txtDescripcion.addFocusListener(new Validar_Vacio());
        WebPanel pnlPuedeFacturar = new WebPanel();
        pnlPuedeFacturar.setLayout(new FlowLayout());
        pnlPuedeFacturar.add(new WebLabel(loadIcon("catalog_pages.png")));
        pnlPuedeFacturar.add(txtDescripcion);
        TooltipManager.setTooltip(pnlPuedeFacturar, "Ingrese una breve descripción del atributo del cliente", TooltipWay.up);
        TooltipManager.setTooltip(txtDescripcion, "Ingrese una breve descripción del atributo del cliente", TooltipWay.up);
        pnlDerecho.add(pnlPuedeFacturar);

        pnlDatosIndividuales.add(pnlIzquierdo);
        pnlDatosIndividuales.add(pnlDerecho);
        WebScrollPane scroll = new WebScrollPane(pnlDatosIndividuales);
        scroll.setBorder(null);
        actualizarDatos(cmbAtributosCliente.getAtributoSeleccionado());
        return scroll;
    }
    
    private Component agregarPanelSuperior() {
        WebPanel pnlSuperiorDatosCliente = new WebPanel();
        pnlSuperiorDatosCliente.setLayout(new FlowLayout());
        pnlSuperiorDatosCliente.add(new WebLabel(loadIcon("search.png")));
        pnlSuperiorDatosCliente.add(new WebLabel("Buscar:"));
        cmbAtributosCliente = new CmbBuscarAtributoCliente(this);
        pnlSuperiorDatosCliente.add(cmbAtributosCliente);
        btnEditar = new WebButton("Editar estado", loadIcon("edit.png"));
        btnNuevo = new WebButton("Nuevo estado", loadIcon("add.png"));

        btnEditar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((WebButton) e.getSource()).setEnabled(false);
                editarAtributoCliente();
            }
        });

        btnNuevo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoAtributoCliente();
            }
        });
        pnlSuperiorDatosCliente.add(btnEditar);
        pnlSuperiorDatosCliente.add(btnNuevo);
        return pnlSuperiorDatosCliente;
    }
    
    void actualizarDatos(BDO_Atributo_Cliente atributoSeleccionado) {
        if (atributoSeleccionado != null) {
            txtNombre.setText(atributoSeleccionado.getNombre());
            txtDescripcion.setText(atributoSeleccionado.getDescripcion());
        }
    }
    
    private void nuevoAtributoCliente() {
        editarAtributo = false;
        limpiarCampos();
        cambiarBloqueoCampos(true);
    }
    
    private void guardarAtributoCliente() {
        if (validarDatos()) {
            
            connAtributoCliente = new DAO_Atributo_Cliente();
            BDO_Atributo_Cliente nuevo = new BDO_Atributo_Cliente(0,txtNombre.getText().trim(),txtDescripcion.getText().trim());
            if (editarAtributo) {
                connAtributoCliente.actualizarAtributoCliente(nuevo, cmbAtributosCliente.getAtributoSeleccionado());
            } else {
                connAtributoCliente.agregarAtributoCliente(nuevo);
            }
            
            editarAtributo = false;
            cambiarBloqueoCampos(false);
            actualizarAtributosCliente();
        }
    }
    
    private void actualizarAtributosCliente(){
            Catalogos.actualizarAtributosCliente();
            cmbAtributosCliente.cargarAtributoCliente(Catalogos.getAtributos_cliente());
    }
    
    private void cancelarOperacion() {
        editarAtributo = false;
        actualizarDatos(cmbAtributosCliente.getAtributoSeleccionado());
        cambiarBloqueoCampos(false);
    }
    
    private void editarAtributoCliente() {
        editarAtributo = true;
        cambiarBloqueoCampos(true);
    }
    
    private boolean validarDatos() {
        if (txtNombre.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El nombre del atributo no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void limpiarCampos() {
        txtNombre.clear();
        txtDescripcion.clear();
    }
    
    private void cambiarBloqueoCampos(boolean bloqueo) {
        txtNombre.setEnabled(bloqueo);
        txtDescripcion.setEnabled(bloqueo);
        btnGuardar.setEnabled(bloqueo);
        btnCancelar.setEnabled(bloqueo);
        cmbAtributosCliente.setEnabled(!bloqueo);
        btnNuevo.setEnabled(!bloqueo);
        btnEditar.setEnabled(!bloqueo);
    }
}
