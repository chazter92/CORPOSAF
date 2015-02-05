/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.productos;

import BDO.BDO_Categoria_Producto;
import DAO.DAO_Categoria_Producto;
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
public class FrmCategoriaProducto extends WebInternalFrame {
    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    private CmbBuscarCategoriaProducto cmbCategoriasProducto;
    private WebButton btnGuardarCategoriaProducto, btnCancelar, btnNuevaCategoriaProducto, btnEditarCategoriaProducto;
    private WebTextField txtNombre, txtMargenGanancia;
    private final int columnasDatos = 25;
    private boolean editarCategoria = false;
    private DAO_Categoria_Producto connCategoriaProducto;

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

    public FrmCategoriaProducto() {
        super("Productos - Categorias", true, true, true, true);
        createUI();
    }
    
    private void createUI() {
        WebPanel pnlCategoriasProducto = new WebPanel();
        pnlCategoriasProducto.setLayout(new BorderLayout());
        pnlCategoriasProducto.add(agregarPanelSuperior(), BorderLayout.NORTH);
        pnlCategoriasProducto.add(agregarPanelBotones(), BorderLayout.SOUTH);
        pnlCategoriasProducto.add(agregarDatosCategoriasProducto(), BorderLayout.CENTER);

        pnlCategoriasProducto.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
        add(pnlCategoriasProducto);
        setFrameIcon(loadIcon("categories.png"));
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
        btnGuardarCategoriaProducto = new WebButton("Guardar", loadIcon("save_new.png"));
        btnCancelar = new WebButton("Cancelar", loadIcon("cancel.png"));
        btnGuardarCategoriaProducto.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCategoriaProducto();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarOperacion();
            }
        });
        pnlBotonesDatosIndividuales.add(btnGuardarCategoriaProducto);
        pnlBotonesDatosIndividuales.add(btnCancelar);
        return pnlBotonesDatosIndividuales;
    }

    private Component agregarDatosCategoriasProducto() {
        WebPanel pnlDatosIndividuales = new WebPanel();
        pnlDatosIndividuales.setLayout(new FlowLayout());

        WebPanel pnlIzquierdo = new WebPanel();
        pnlIzquierdo.setLayout(new BoxLayout(pnlIzquierdo, BoxLayout.Y_AXIS));
        WebPanel pnlDerecho = new WebPanel();
        pnlDerecho.setLayout(new BoxLayout(pnlDerecho, BoxLayout.Y_AXIS));

        txtNombre = new WebTextField();
        txtNombre.setColumns(columnasDatos);
        txtNombre.setInputPrompt("Nombre de la categoria");
        txtNombre.setHideInputPromptOnFocus(false);
        txtNombre.addFocusListener(new Validar_Vacio());
        WebPanel pnlNombre = new WebPanel();
        pnlNombre.setLayout(new FlowLayout());
        pnlNombre.add(new WebLabel(loadIcon("label.png")));
        pnlNombre.add(txtNombre);
        TooltipManager.setTooltip(pnlNombre, "Ingrese nombre de la categoria de productos", TooltipWay.up);
        TooltipManager.setTooltip(txtNombre, "Ingrese nombre de la categoria de productos", TooltipWay.up);
        pnlDerecho.add(pnlNombre);

        txtMargenGanancia = new WebTextField();
        txtMargenGanancia.setColumns(columnasDatos);
        txtMargenGanancia.setInputPrompt("Porcentaje de margen de ganancia.");
        txtMargenGanancia.setHideInputPromptOnFocus(false);
        txtMargenGanancia.addFocusListener(new Validar_Double());

        WebPanel pnlDescuento = new WebPanel();
        pnlDescuento.setLayout(new FlowLayout());
        pnlDescuento.add(new WebLabel(loadIcon("format_percentage.png")));
        pnlDescuento.add(txtMargenGanancia);
        TooltipManager.setTooltip(pnlDescuento, "Márgen de ganancia. Ingrese un número. Ejemplos: 4, 15.23", TooltipWay.up);
        txtMargenGanancia.setToolTipText("Márgen de ganancia. Ingrese un número. Ejemplos: 4, 15.23");
        pnlDerecho.add(pnlDescuento);

        pnlDatosIndividuales.add(pnlIzquierdo);
        pnlDatosIndividuales.add(pnlDerecho);
        WebScrollPane scroll = new WebScrollPane(pnlDatosIndividuales);
        scroll.setBorder(null);
        actualizarDatos(cmbCategoriasProducto.getCategoriaSeleccionada());
        return scroll;
    }

    private Component agregarPanelSuperior() {
        WebPanel pnlSuperiorCategoriasProducto = new WebPanel();
        pnlSuperiorCategoriasProducto.setLayout(new FlowLayout());
        pnlSuperiorCategoriasProducto.add(new WebLabel(loadIcon("package_green.png")));
        pnlSuperiorCategoriasProducto.add(new WebLabel("Buscar:"));
        cmbCategoriasProducto = new CmbBuscarCategoriaProducto(this);
        pnlSuperiorCategoriasProducto.add(cmbCategoriasProducto);
        btnEditarCategoriaProducto = new WebButton("Editar categoria", loadIcon("edit.png"));
        btnNuevaCategoriaProducto = new WebButton("Nueva categoria", loadIcon("add.png"));

        btnEditarCategoriaProducto.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((WebButton) e.getSource()).setEnabled(false);
                editarCategoriaProducto();
            }
        });

        btnNuevaCategoriaProducto.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nuevaCategoriaProducto();
            }
        });
        pnlSuperiorCategoriasProducto.add(btnEditarCategoriaProducto);
        pnlSuperiorCategoriasProducto.add(btnNuevaCategoriaProducto);
        return pnlSuperiorCategoriasProducto;
    }
    
    void actualizarDatos(BDO_Categoria_Producto categoriaSeleccionada) {
        if (categoriaSeleccionada != null) {
            txtNombre.setText(categoriaSeleccionada.getNombre());
            txtMargenGanancia.setText(String.valueOf(categoriaSeleccionada.getMargen_ganancia()));
        }
    }
    
    private void nuevaCategoriaProducto() {
        editarCategoria = false;
        limpiarCampos();
        cambiarBloqueoCampos(true);
    }
    
    private void guardarCategoriaProducto() {
        if (validarDatos()) {

            connCategoriaProducto = new DAO_Categoria_Producto();
            BDO_Categoria_Producto nuevo = new BDO_Categoria_Producto(0, txtNombre.getText().trim(), Double.parseDouble(txtMargenGanancia.getText()));
            if (editarCategoria) {
                connCategoriaProducto.actualizarCategoria(nuevo, cmbCategoriasProducto.getCategoriaSeleccionada());
            } else {
                connCategoriaProducto.agregarCategoria(nuevo);
            }

            editarCategoria = false;
            cambiarBloqueoCampos(false);
            actualizarCategoriasProducto();
        }
    }
    
    private void actualizarCategoriasProducto() {
        Catalogos.actualizarCategoriasProducto();
        cmbCategoriasProducto.cargarCategoriasProducto(Catalogos.getCategorias_Producto());
    }
    
    private void cancelarOperacion() {
        editarCategoria = false;
        actualizarDatos(cmbCategoriasProducto.getCategoriaSeleccionada());
        cambiarBloqueoCampos(false);
    }
    
    private void editarCategoriaProducto() {
        editarCategoria = true;
        cambiarBloqueoCampos(true);
    }
    
    private boolean validarDatos() {
        if (txtNombre.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El nombre de la categoria no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (txtMargenGanancia.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El márgen de ganancia no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            if (txtMargenGanancia.getText().trim().matches("^\\d+(\\.\\d+)?")) {
                Catalogos.mostrarMensajeError("Cifra no válida. Ingrese un número para el márgen de ganancia. Ejemplo: 4, 7.96", "Error", WebOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        return true;
    }
    
    private void limpiarCampos() {
        txtNombre.clear();
        txtMargenGanancia.clear();
    }
    
    private void cambiarBloqueoCampos(boolean bloqueo) {
        txtNombre.setEnabled(bloqueo);
        txtMargenGanancia.setEnabled(bloqueo);
        btnGuardarCategoriaProducto.setEnabled(bloqueo);
        btnCancelar.setEnabled(bloqueo);
        cmbCategoriasProducto.setEnabled(!bloqueo);
        btnNuevaCategoriaProducto.setEnabled(!bloqueo);
        btnEditarCategoriaProducto.setEnabled(!bloqueo);
    }
}
