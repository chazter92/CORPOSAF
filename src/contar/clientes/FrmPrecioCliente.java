/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.clientes;

import BDO.BDO_Precio_Cliente;
import DAO.DAO_Precio_Cliente;
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
import validacion.Validar_Double;
import validacion.Validar_Vacio;

/**
 *
 * @author Chaz
 */
public class FrmPrecioCliente extends WebInternalFrame {

    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    private CmbBuscarPrecioCliente cmbPreciosCliente;
    private WebButton btnGuardarPrecio, btnCancelar, btnNuevoPrecioCliente, btnEditarPrecioCliente;
    private WebTextField txtNombre, txtDescuento;
    private final int columnasDatos = 25;
    private boolean editarPrecio = false;
    private DAO_Precio_Cliente connPrecioCliente;

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

    public FrmPrecioCliente() {
        super("Clientes - Listas de precios", true, true, true, true);
        createUI();
    }

    private void createUI() {
        WebPanel pnlPreciosCliente = new WebPanel();
        pnlPreciosCliente.setLayout(new BorderLayout());
        pnlPreciosCliente.add(agregarPanelSuperior(), BorderLayout.NORTH);
        pnlPreciosCliente.add(agregarPanelBotones(), BorderLayout.SOUTH);
        pnlPreciosCliente.add(agregarDatosPreciosCliente(), BorderLayout.CENTER);

        pnlPreciosCliente.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
        add(pnlPreciosCliente);
        setFrameIcon(loadIcon("account_balances.png"));
        cambiarBloqueoCampos(false);
        addInternalFrameListener(new InternalFrameListener() {

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
        btnGuardarPrecio = new WebButton("Guardar", loadIcon("save_new.png"));
        btnCancelar = new WebButton("Cancelar", loadIcon("cancel.png"));
        btnGuardarPrecio.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                guardarPrecioCliente();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarOperacion();
            }
        });
        pnlBotonesDatosIndividuales.add(btnGuardarPrecio);
        pnlBotonesDatosIndividuales.add(btnCancelar);
        return pnlBotonesDatosIndividuales;
    }

    private Component agregarDatosPreciosCliente() {
        WebPanel pnlDatosIndividuales = new WebPanel();
        pnlDatosIndividuales.setLayout(new FlowLayout());

        WebPanel pnlIzquierdo = new WebPanel();
        pnlIzquierdo.setLayout(new BoxLayout(pnlIzquierdo, BoxLayout.Y_AXIS));
        WebPanel pnlDerecho = new WebPanel();
        pnlDerecho.setLayout(new BoxLayout(pnlDerecho, BoxLayout.Y_AXIS));

        txtNombre = new WebTextField();
        txtNombre.setColumns(columnasDatos);
        txtNombre.setInputPrompt("Nombre de la lista de precios");
        txtNombre.setHideInputPromptOnFocus(false);
        txtNombre.addFocusListener(new Validar_Vacio());
        WebPanel pnlTipo = new WebPanel();
        pnlTipo.setLayout(new FlowLayout());
        pnlTipo.add(new WebLabel(loadIcon("users_5.png")));
        pnlTipo.add(txtNombre);
        TooltipManager.setTooltip(pnlTipo, "Ingrese nombre de la lista de precios del cliente", TooltipWay.up);
        TooltipManager.setTooltip(txtNombre, "Ingrese nombre de la lista de precios del cliente", TooltipWay.up);
        pnlDerecho.add(pnlTipo);

        txtDescuento = new WebTextField();
        txtDescuento.setColumns(columnasDatos);
        txtDescuento.setInputPrompt("Porcentaje de descuento.");
        txtDescuento.setHideInputPromptOnFocus(false);
        txtDescuento.addFocusListener(new Validar_Double());

        WebPanel pnlDescuento = new WebPanel();
        pnlDescuento.setLayout(new FlowLayout());
        pnlDescuento.add(new WebLabel(loadIcon("format_percentage.png")));
        pnlDescuento.add(txtDescuento);
        TooltipManager.setTooltip(pnlDescuento, "Descuento del cliente. Ingrese un número. Ejemplos: 4, 15.23", TooltipWay.up);
        txtDescuento.setToolTipText("Descuento del cliente. Ingrese un número. Ejemplos: 4, 15.23");
        pnlDerecho.add(pnlDescuento);

        pnlDatosIndividuales.add(pnlIzquierdo);
        pnlDatosIndividuales.add(pnlDerecho);
        WebScrollPane scroll = new WebScrollPane(pnlDatosIndividuales);
        scroll.setBorder(null);
        actualizarDatos(cmbPreciosCliente.getPrecioSeleccionado());
        return scroll;
    }

    private Component agregarPanelSuperior() {
        WebPanel pnlSuperiorDatosCliente = new WebPanel();
        pnlSuperiorDatosCliente.setLayout(new FlowLayout());
        pnlSuperiorDatosCliente.add(new WebLabel(loadIcon("search_accounts.png")));
        pnlSuperiorDatosCliente.add(new WebLabel("Buscar:"));
        cmbPreciosCliente = new CmbBuscarPrecioCliente(this);
        pnlSuperiorDatosCliente.add(cmbPreciosCliente);
        btnEditarPrecioCliente = new WebButton("Editar lista de precios", loadIcon("edit.png"));
        btnNuevoPrecioCliente = new WebButton("Nueva lista de precios", loadIcon("add.png"));

        btnEditarPrecioCliente.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((WebButton) e.getSource()).setEnabled(false);
                editarPrecioCliente();
            }
        });

        btnNuevoPrecioCliente.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoPrecioCliente();
            }
        });
        pnlSuperiorDatosCliente.add(btnEditarPrecioCliente);
        pnlSuperiorDatosCliente.add(btnNuevoPrecioCliente);
        return pnlSuperiorDatosCliente;
    }

    void actualizarDatos(BDO_Precio_Cliente precioSeleccionado) {
        if (precioSeleccionado != null) {
            txtNombre.setText(precioSeleccionado.getTipo());
            txtDescuento.setText(precioSeleccionado.getDescuento().toString());
        }
    }

    private void nuevoPrecioCliente() {
        editarPrecio = false;
        limpiarCampos();
        cambiarBloqueoCampos(true);
    }

    private void guardarPrecioCliente() {
        if (validarDatos()) {

            connPrecioCliente = new DAO_Precio_Cliente();
            BDO_Precio_Cliente nuevo = new BDO_Precio_Cliente(0, txtNombre.getText().trim(), Double.parseDouble(txtDescuento.getText()));
            if (editarPrecio) {
                connPrecioCliente.actualizarPrecio(nuevo, cmbPreciosCliente.getPrecioSeleccionado());
            } else {
                connPrecioCliente.agregarPrecio(nuevo);
            }

            editarPrecio = false;
            cambiarBloqueoCampos(false);
            actualizarPreciosCliente();
        }
    }

    private void actualizarPreciosCliente() {
        Catalogos.actualizarPreciosClientes();
        cmbPreciosCliente.cargarPreciosCliente(Catalogos.getPrecios_cliente());
    }

    private void cancelarOperacion() {
        editarPrecio = false;
        actualizarDatos(cmbPreciosCliente.getPrecioSeleccionado());
        cambiarBloqueoCampos(false);
    }

    private void editarPrecioCliente() {
        editarPrecio = true;
        cambiarBloqueoCampos(true);
    }

    private boolean validarDatos() {
        if (txtNombre.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El nombre de la lista de precios no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (txtDescuento.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El descuento del cliente no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            if (txtDescuento.getText().trim().matches("^\\d+(\\.\\d+)?")) {
                Catalogos.mostrarMensajeError("Cifra no válida. Ingrese un número para el descuento. Ejemplo: 4, 7.96", "Error", WebOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        return true;
    }
    
    private void limpiarCampos() {
        txtNombre.clear();
        txtDescuento.clear();
    }
    
    private void cambiarBloqueoCampos(boolean bloqueo) {
        txtNombre.setEnabled(bloqueo);
        txtDescuento.setEnabled(bloqueo);
        btnGuardarPrecio.setEnabled(bloqueo);
        btnCancelar.setEnabled(bloqueo);
        cmbPreciosCliente.setEnabled(!bloqueo);
        btnNuevoPrecioCliente.setEnabled(!bloqueo);
        btnEditarPrecioCliente.setEnabled(!bloqueo);
    }
}
