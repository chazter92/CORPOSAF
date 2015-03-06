/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.factura;

import BDO.BDO_Tipo_Pago_Venta;
import DAO.DAO_Precio_Venta;
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
public class FrmPrecioVenta extends WebInternalFrame {
    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    private CmbBuscarPrecioVenta cmbPrecios;
    private WebTextField txtNombre;
    private WebButton btnGuardar, btnCancelar, btnNuevo, btnEditar;
    private final int columnasDatos = 25;
    private boolean editarPrecioVenta = false;
    private DAO_Precio_Venta connPrecioVenta;
    
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
    
    public FrmPrecioVenta() {
        super("Empresa | Precios de venta", true, true, true, true);
        createUI();
    }
    
    private void createUI() {
        WebPanel pnlPrecioVenta = new WebPanel();
        pnlPrecioVenta.setLayout(new BorderLayout());
        pnlPrecioVenta.add(agregarPanelSuperior(), BorderLayout.NORTH);
        pnlPrecioVenta.add(agregarPanelBotones(), BorderLayout.SOUTH);
        pnlPrecioVenta.add(agregarDatosPrecioVenta(), BorderLayout.CENTER);

        pnlPrecioVenta.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
        add(pnlPrecioVenta);
        setFrameIcon(loadIcon("creditcards.png"));
        cambiarBloqueoCampos(false);
        addInternalFrameListener(new InternalFrameListener(){

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {
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
                guardarPrecioVenta();
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
    
    private Component agregarDatosPrecioVenta() {
        WebPanel pnlDatosIndividuales = new WebPanel();
        pnlDatosIndividuales.setLayout(new FlowLayout());

        WebPanel pnlIzquierdo = new WebPanel();
        pnlIzquierdo.setLayout(new BoxLayout(pnlIzquierdo, BoxLayout.Y_AXIS));
        WebPanel pnlDerecho = new WebPanel();
        pnlDerecho.setLayout(new BoxLayout(pnlDerecho, BoxLayout.Y_AXIS));

        txtNombre = new WebTextField();
        txtNombre.setColumns(columnasDatos);
        txtNombre.setInputPrompt("Nombre del precio de venta");
        txtNombre.setHideInputPromptOnFocus(false);
        txtNombre.addFocusListener(new Validar_Vacio("El nombre del precio de venta "));
        WebPanel pnlNombre = new WebPanel();
        pnlNombre.setLayout(new FlowLayout());
        pnlNombre.add(new WebLabel(loadIcon("id.png")));
        pnlNombre.add(txtNombre);
        TooltipManager.setTooltip(pnlNombre, "Ingrese nombre del precio de venta", TooltipWay.up);
        TooltipManager.setTooltip(txtNombre, "Ingrese nombre del precio de venta", TooltipWay.up);
        pnlIzquierdo.add(pnlNombre);
        
        pnlDatosIndividuales.add(pnlIzquierdo);
        pnlDatosIndividuales.add(pnlDerecho);
        WebScrollPane scroll = new WebScrollPane(pnlDatosIndividuales);
        scroll.setBorder(null);
        actualizarDatos(cmbPrecios.getPrecioVentaSeleccionado());
        return scroll;
    }
    
    private Component agregarPanelSuperior() {
        WebPanel pnlSuperiorDatosCliente = new WebPanel();
        pnlSuperiorDatosCliente.setLayout(new FlowLayout());
        pnlSuperiorDatosCliente.add(new WebLabel(loadIcon("search.png")));
        pnlSuperiorDatosCliente.add(new WebLabel("Buscar:"));
        cmbPrecios = new CmbBuscarPrecioVenta(this);
        pnlSuperiorDatosCliente.add(cmbPrecios);
        btnEditar = new WebButton("Editar precio venta", loadIcon("edit.png"));
        btnNuevo = new WebButton("Nuevo precio venta", loadIcon("add.png"));
        btnEditar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((WebButton) e.getSource()).setEnabled(false);
                editarPrecioVenta();
            }
        });

        btnNuevo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoPrecioVenta();
            }
        });
        pnlSuperiorDatosCliente.add(btnEditar);
        pnlSuperiorDatosCliente.add(btnNuevo);
        return pnlSuperiorDatosCliente;
    }
    
    void actualizarDatos(BDO_Tipo_Pago_Venta precioSeleccionado) {
        if (precioSeleccionado != null) {
            txtNombre.setText(precioSeleccionado.getNombre());            
        }
    }
    
    private void nuevoPrecioVenta() {
        editarPrecioVenta = false;
        limpiarCampos();
        cambiarBloqueoCampos(true);
    }
    
    private void guardarPrecioVenta() {
        if (validarDatos()) {
            
            connPrecioVenta = new DAO_Precio_Venta();
            BDO_Tipo_Pago_Venta nuevo = new BDO_Tipo_Pago_Venta(0,txtNombre.getText().trim());
            if (editarPrecioVenta) {
                connPrecioVenta.actualizarPrecioVenta(nuevo, cmbPrecios.getPrecioVentaSeleccionado());
            } else {
                connPrecioVenta.agregarPrecioVenta(nuevo);
            }
            
            editarPrecioVenta = false;
            cambiarBloqueoCampos(false);
            actualizarPreciosVenta();
        }
    }
    
    private void actualizarPreciosVenta(){
            Catalogos.actualizarTiposPagoVenta();
            cmbPrecios.cargarPreciosVenta(Catalogos.getTiposPagoVenta());
    }
    
    private void cancelarOperacion() {
        editarPrecioVenta = false;
        actualizarDatos(cmbPrecios.getPrecioVentaSeleccionado());
        cambiarBloqueoCampos(false);
    }
    
    private void editarPrecioVenta() {
        editarPrecioVenta = true;
        cambiarBloqueoCampos(true);
    }
    
    private boolean validarDatos() {
        if (txtNombre.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El nombre del precio de venta no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        }   
        
        return true;
    }
    
    private void limpiarCampos() {
        txtNombre.clear();
    }
    
    private void cambiarBloqueoCampos(boolean bloqueo) {
        txtNombre.setEnabled(bloqueo);
        btnGuardar.setEnabled(bloqueo);
        btnCancelar.setEnabled(bloqueo);
        cmbPrecios.setEnabled(!bloqueo);
        btnNuevo.setEnabled(!bloqueo);
        btnEditar.setEnabled(!bloqueo);
    }
}
