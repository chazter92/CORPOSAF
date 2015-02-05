/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.productos;

/**
 *
 * @author Chaz
 */
import BDO.BDO_Estado_Producto;
import DAO.DAO_Estado_Producto;
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
public class FrmEstadoProducto extends WebInternalFrame {

    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    private CmbBuscarEstadoProducto cmbEstadosProducto;
    private WebTextField txtTipo;
    private WebCheckBox chkFacturar;
    private WebButton btnGuardarEstado, btnCancelar, btnNuevoEstado, btnEditarEstado;
    private final int columnasDatos = 25;
    private boolean editarEstado = false;
    private DAO_Estado_Producto connEstadoProducto;

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

    public FrmEstadoProducto() {
        super("Productos | Estados", true, true, true, true);
        createUI();
    }

    private void createUI() {
        WebPanel pnlEstadosProducto = new WebPanel();
        pnlEstadosProducto.setLayout(new BorderLayout());
        pnlEstadosProducto.add(agregarPanelSuperior(), BorderLayout.NORTH);
        pnlEstadosProducto.add(agregarPanelBotones(), BorderLayout.SOUTH);
        pnlEstadosProducto.add(agregarDatosEstadoProducto(), BorderLayout.CENTER);

        pnlEstadosProducto.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
        add(pnlEstadosProducto);
        setFrameIcon(loadIcon("account_menu.png"));
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
        btnGuardarEstado = new WebButton("Guardar", loadIcon("save_new.png"));
        btnCancelar = new WebButton("Cancelar", loadIcon("cancel.png"));

        btnGuardarEstado.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                guardarEstadoProducto();
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

    private Component agregarDatosEstadoProducto() {
        WebPanel pnlDatosIndividuales = new WebPanel();
        pnlDatosIndividuales.setLayout(new FlowLayout());

        WebPanel pnlIzquierdo = new WebPanel();
        pnlIzquierdo.setLayout(new BoxLayout(pnlIzquierdo, BoxLayout.Y_AXIS));
        WebPanel pnlDerecho = new WebPanel();
        pnlDerecho.setLayout(new BoxLayout(pnlDerecho, BoxLayout.Y_AXIS));

        txtTipo = new WebTextField();
        txtTipo.setColumns(columnasDatos);
        txtTipo.setInputPrompt("Nombre del estado del producto");
        txtTipo.setHideInputPromptOnFocus(false);
        txtTipo.addFocusListener(new Validar_Vacio());
        WebPanel pnlTipo = new WebPanel();
        pnlTipo.setLayout(new FlowLayout());
        pnlTipo.add(new WebLabel(loadIcon("package_green.png")));
        pnlTipo.add(txtTipo);
        TooltipManager.setTooltip(pnlTipo, "Ingrese nombre del estado del producto", TooltipWay.up);
        TooltipManager.setTooltip(txtTipo, "Ingrese nombre del estado del producto", TooltipWay.up);
        pnlDerecho.add(pnlTipo);

        chkFacturar = new WebCheckBox("¿Puede facturarse?");

        WebPanel pnlPuedeFacturar = new WebPanel();
        pnlPuedeFacturar.setLayout(new FlowLayout());
        pnlPuedeFacturar.add(new WebLabel(loadIcon("package_delete.png")));
        pnlPuedeFacturar.add(chkFacturar);
        TooltipManager.setTooltip(pnlPuedeFacturar, "Marque la casilla si el producto puede ser facturado en este estado", TooltipWay.up);
        chkFacturar.setToolTipText("Marque la casilla si el producto puede ser facturado en este estado");
        pnlDerecho.add(pnlPuedeFacturar);

        pnlDatosIndividuales.add(pnlIzquierdo);
        pnlDatosIndividuales.add(pnlDerecho);
        WebScrollPane scroll = new WebScrollPane(pnlDatosIndividuales);
        scroll.setBorder(null);
        actualizarDatos(cmbEstadosProducto.getEstadoSeleccionado());
        return scroll;
    }

    private Component agregarPanelSuperior() {
        WebPanel pnlSuperiorDatosProducto = new WebPanel();
        pnlSuperiorDatosProducto.setLayout(new FlowLayout());
        pnlSuperiorDatosProducto.add(new WebLabel(loadIcon("search.png")));
        pnlSuperiorDatosProducto.add(new WebLabel("Buscar:"));
        cmbEstadosProducto = new CmbBuscarEstadoProducto(this);
        pnlSuperiorDatosProducto.add(cmbEstadosProducto);
        btnEditarEstado = new WebButton("Editar estado", loadIcon("edit.png"));
        btnNuevoEstado = new WebButton("Nuevo estado", loadIcon("add.png"));

        btnEditarEstado.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((WebButton) e.getSource()).setEnabled(false);
                editarEstadoProducto();
            }
        });

        btnNuevoEstado.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoEstadoProducto();
            }
        });
        pnlSuperiorDatosProducto.add(btnEditarEstado);
        pnlSuperiorDatosProducto.add(btnNuevoEstado);
        return pnlSuperiorDatosProducto;
    }

    void actualizarDatos(BDO_Estado_Producto estadoSeleccionado) {
        if (estadoSeleccionado != null) {
            txtTipo.setText(estadoSeleccionado.getTipo());
            chkFacturar.setSelected(estadoSeleccionado.getPuede_facturar());
        }
    }

    private void nuevoEstadoProducto() {
        editarEstado = false;
        limpiarCampos();
        cambiarBloqueoCampos(true);
    }

    private void guardarEstadoProducto() {
        if (validarDatos()) {
            
            connEstadoProducto = new DAO_Estado_Producto();
            BDO_Estado_Producto nuevo = new BDO_Estado_Producto(0,txtTipo.getText().trim(),chkFacturar.isSelected());
            if (editarEstado) {
                connEstadoProducto.actualizarEstado(nuevo, cmbEstadosProducto.getEstadoSeleccionado());
            } else {
                connEstadoProducto.agregarEstado(nuevo);
            }
            
            editarEstado = false;
            cambiarBloqueoCampos(false);
            actualizarEstadosProducto();
        }
    }
    
    private void actualizarEstadosProducto(){
            Catalogos.actualizarEstadosProducto();
            cmbEstadosProducto.cargarEstadosProducto(Catalogos.getEstados_Producto());
    }

    private void cancelarOperacion() {
        editarEstado = false;
        actualizarDatos(cmbEstadosProducto.getEstadoSeleccionado());
        cambiarBloqueoCampos(false);
    }

    private void editarEstadoProducto() {
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
        cmbEstadosProducto.setEnabled(!bloqueo);
        btnNuevoEstado.setEnabled(!bloqueo);
        btnEditarEstado.setEnabled(!bloqueo);
    }
}
