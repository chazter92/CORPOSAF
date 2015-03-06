/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.empresa;

import BDO.BDO_Impuesto;
import DAO.DAO_Impuesto;
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
import validacion.Validar_Double;
import validacion.Validar_Vacio;

/**
 *
 * @author Chaz
 */
public class FrmImpuesto extends WebInternalFrame {
    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    private CmbBuscarImpuesto cmbImpuestos;
    private WebTextField txtNombre, txtValor;
    private WebCheckBox chkIDP, chkAplicaPrecioVenta;
    private WebButton btnGuardar, btnCancelar, btnNuevo, btnEditar;
    private final int columnasDatos = 25;
    private boolean editarImpuesto = false;
    private DAO_Impuesto connImpuesto;
    
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
    
    public FrmImpuesto() {
        super("Empresa | Impuestos", true, true, true, true);
        createUI();
    }
    
    private void createUI() {
        WebPanel pnlImpuestos = new WebPanel();
        pnlImpuestos.setLayout(new BorderLayout());
        pnlImpuestos.add(agregarPanelSuperior(), BorderLayout.NORTH);
        pnlImpuestos.add(agregarPanelBotones(), BorderLayout.SOUTH);
        pnlImpuestos.add(agregarDatosImpuesto(), BorderLayout.CENTER);

        pnlImpuestos.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
        add(pnlImpuestos);
        setFrameIcon(loadIcon("impuesto.png"));
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
                guardarImpuesto();
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

    private Component agregarDatosImpuesto() {
        WebPanel pnlDatosIndividuales = new WebPanel();
        pnlDatosIndividuales.setLayout(new FlowLayout());

        WebPanel pnlIzquierdo = new WebPanel();
        pnlIzquierdo.setLayout(new BoxLayout(pnlIzquierdo, BoxLayout.Y_AXIS));
        WebPanel pnlDerecho = new WebPanel();
        pnlDerecho.setLayout(new BoxLayout(pnlDerecho, BoxLayout.Y_AXIS));

        txtNombre = new WebTextField();
        txtNombre.setColumns(columnasDatos);
        txtNombre.setInputPrompt("Nombre del impuesto");
        txtNombre.setHideInputPromptOnFocus(false);
        txtNombre.addFocusListener(new Validar_Vacio("El nombre del impuesto"));
        WebPanel pnlNombre = new WebPanel();
        pnlNombre.setLayout(new FlowLayout());
        pnlNombre.add(new WebLabel(loadIcon("id.png")));
        pnlNombre.add(txtNombre);
        TooltipManager.setTooltip(pnlNombre, "Ingrese nombre del impuesto", TooltipWay.up);
        TooltipManager.setTooltip(txtNombre, "Ingrese nombre del impuesto", TooltipWay.up);
        pnlIzquierdo.add(pnlNombre);
        
        txtValor = new WebTextField();
        txtValor.setColumns(columnasDatos);
        txtValor.setInputPrompt("Tasa a pagar");
        txtValor.setHideInputPromptOnFocus(false);
        txtValor.addFocusListener(new Validar_Double());
        WebPanel pnlValor = new WebPanel();
        pnlValor.setLayout(new FlowLayout());
        pnlValor.add(new WebLabel(loadIcon("percentage.png")));
        pnlValor.add(txtValor);
        TooltipManager.setTooltip(pnlValor, "Ingrese tasa a pagar del impuesto", TooltipWay.up);
        TooltipManager.setTooltip(txtValor, "Ingrese tasa a pagar del impuesto", TooltipWay.up);
        pnlDerecho.add(pnlValor);

        chkIDP = new WebCheckBox("Impuesto derivado del petroleo");

        WebPanel pnlIDP = new WebPanel();
        pnlIDP.setLayout(new FlowLayout());
        pnlIDP.add(new WebLabel(loadIcon("oil.png")));
        pnlIDP.add(chkIDP);
        TooltipManager.setTooltip(pnlIDP, "Marque la casilla si es un impuesto de derivados del petroleo", TooltipWay.up);
        chkIDP.setToolTipText("Marque la casilla si es un impuesto de derivados del petroleo");
        pnlIzquierdo.add(pnlIDP);
        
        chkAplicaPrecioVenta = new WebCheckBox("¿Afecta el precio de venta?");

        WebPanel pnlPrecioVenta = new WebPanel();
        pnlPrecioVenta.setLayout(new FlowLayout());
        pnlPrecioVenta.add(new WebLabel(loadIcon("financial_functions.png")));
        pnlPrecioVenta.add(chkAplicaPrecioVenta);
        TooltipManager.setTooltip(pnlPrecioVenta, "Marque la casilla si el impuesto afecta el precio de venta", TooltipWay.up);
        chkAplicaPrecioVenta.setToolTipText("Marque la casilla si el impuesto afecta el precio de venta");
        pnlDerecho.add(pnlPrecioVenta);

        pnlDatosIndividuales.add(pnlIzquierdo);
        pnlDatosIndividuales.add(pnlDerecho);
        WebScrollPane scroll = new WebScrollPane(pnlDatosIndividuales);
        scroll.setBorder(null);
        actualizarDatos(cmbImpuestos.getImpuestoSeleccionado());
        return scroll;
    }

    private Component agregarPanelSuperior() {
        WebPanel pnlSuperiorDatosCliente = new WebPanel();
        pnlSuperiorDatosCliente.setLayout(new FlowLayout());
        pnlSuperiorDatosCliente.add(new WebLabel(loadIcon("search.png")));
        pnlSuperiorDatosCliente.add(new WebLabel("Buscar:"));
        cmbImpuestos = new CmbBuscarImpuesto(this);
        pnlSuperiorDatosCliente.add(cmbImpuestos);
        btnEditar = new WebButton("Editar impuesto", loadIcon("edit.png"));
        btnNuevo = new WebButton("Nuevo impuesto", loadIcon("add.png"));
        btnEditar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((WebButton) e.getSource()).setEnabled(false);
                editarImpuesto();
            }
        });

        btnNuevo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoImpuesto();
            }
        });
        pnlSuperiorDatosCliente.add(btnEditar);
        pnlSuperiorDatosCliente.add(btnNuevo);
        return pnlSuperiorDatosCliente;
    }

    void actualizarDatos(BDO_Impuesto impuestoSeleccionado) {
        if (impuestoSeleccionado != null) {
            txtNombre.setText(impuestoSeleccionado.getNombre());
            txtValor.setText(String.valueOf(impuestoSeleccionado.getValor()));
            chkIDP.setSelected(impuestoSeleccionado.isEs_idp());
            chkAplicaPrecioVenta.setSelected(impuestoSeleccionado.isAplica_precio_venta());
        }
    }
    
    private void nuevoImpuesto() {
        editarImpuesto = false;
        limpiarCampos();
        cambiarBloqueoCampos(true);
    }
    
    private void guardarImpuesto() {
        if (validarDatos()) {
            
            connImpuesto = new DAO_Impuesto();
            BDO_Impuesto nuevo = new BDO_Impuesto(0,txtNombre.getText().trim(),Double.parseDouble(txtValor.getText().trim()),chkIDP.isSelected(),chkAplicaPrecioVenta.isSelected(),Catalogos.getEmpresa().getId_empresa());
            if (editarImpuesto) {
                connImpuesto.actualizarImpuesto(nuevo, cmbImpuestos.getImpuestoSeleccionado());
            } else {
                connImpuesto.agregarImpuesto(nuevo);
            }
            
            editarImpuesto = false;
            cambiarBloqueoCampos(false);
            actualizarImpuestos();
        }
    }
    
    private void actualizarImpuestos(){
            Catalogos.actualizarImpuestos();
            cmbImpuestos.cargarImpuestos(Catalogos.getImpuestos());
    }
    
    private void cancelarOperacion() {
        editarImpuesto = false;
        actualizarDatos(cmbImpuestos.getImpuestoSeleccionado());
        cambiarBloqueoCampos(false);
    }
    
    private void editarImpuesto() {
        editarImpuesto = true;
        cambiarBloqueoCampos(true);
    }
    
    private boolean validarDatos() {
        if (txtNombre.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El nombre del impuesto no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        }   
        
        if (txtValor.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("La tasa a pagar del impuesto no puede estar vacía.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            Validar_Double validar = new Validar_Double();
            if(!validar.valirdarDouble(txtValor.getText().trim())){
                Catalogos.mostrarMensajeError("Cifra no válida. Ingrese un número. Ejemplo: 4, 7.96", "Error", WebOptionPane.ERROR_MESSAGE);
                return false;
            }            
        } 
        return true;
    }
    
    private void limpiarCampos() {
        txtNombre.clear();
        txtValor.clear();
        chkIDP.setSelected(false);
        chkAplicaPrecioVenta.setSelected(false);
    }
    
    private void cambiarBloqueoCampos(boolean bloqueo) {
        txtNombre.setEnabled(bloqueo);
        txtValor.setEnabled(bloqueo);
        chkIDP.setEnabled(bloqueo);
        chkAplicaPrecioVenta.setEnabled(bloqueo);
        btnGuardar.setEnabled(bloqueo);
        btnCancelar.setEnabled(bloqueo);
        cmbImpuestos.setEnabled(!bloqueo);
        btnNuevo.setEnabled(!bloqueo);
        btnEditar.setEnabled(!bloqueo);
    }
}
