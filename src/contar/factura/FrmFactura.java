/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.factura;

import BDO.BDO_Cliente;
import BDO.BDO_Detalle_Factura;
import BDO.BDO_Producto;
import DAO.DAO_Cliente;
import com.alee.laf.button.WebButton;
import com.alee.laf.desktoppane.WebInternalFrame;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebTextField;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import contar.Catalogos;
import contar.factura.FrmFactura.DetalleTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import validacion.Validar_Double;
import validacion.Validar_Entero;

/**
 *
 * @author Chaz
 */
public class FrmFactura extends WebInternalFrame {

    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final int columnasDatos = 25;
    private final int col_subtotal = 3;
    private double descuento;
    private WebTextField txtNIT, txtNombre, txtDireccion, txtCantidad, txtPrecio, txtDescripcion;
    private CmbBuscarProducto cmbProducto;
    private WebButton btnGenerar, btnCancelar, btnAgregar;
    private WebTable tblDetalles;
    private WebLabel lblTotal;
    private DAO_Cliente connCliente;
    private DetalleTableModel detallesTableModel;

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

    public FrmFactura() {
        super("Facturación", true, true, true, true);
        createUI();
    }

    private void createUI() {
        connCliente = new DAO_Cliente();
        WebPanel pnlFacturacion = new WebPanel();
        pnlFacturacion.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();


        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        pnlFacturacion.add(agregarPanelDatosEmpresa(), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 0.5;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        pnlFacturacion.add(agregarPanelDatosFactura(), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 0.5;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        pnlFacturacion.add(agregarPanelDatosCliente(), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.weightx = 0.5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        pnlFacturacion.add(agregarDatosIndividualesProducto(), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 0.5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        pnlFacturacion.add(agregarDatosResolucion(), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.2;
        constraints.weighty = 0.5;
        pnlFacturacion.add(agregarPanelBotones(), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        pnlFacturacion.add(agregarLblTotal(), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        pnlFacturacion.add(agregarPanelTablaProductos(), constraints);


        pnlFacturacion.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
        WebScrollPane scroll = new WebScrollPane(pnlFacturacion);
        setFrameIcon(loadIcon("financial_functions.png"));
        add(scroll);
        //cambiarBloqueoCampos(false);
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
                txtNIT.requestFocus();
            }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
                cancelarOperacion();
            }
        });
        pack();

    }

    private Component agregarPanelDatosEmpresa() {
        WebPanel pnlEmpresa = new WebPanel();
        pnlEmpresa.setLayout(new FlowLayout());
        WebPanel pnlDatosEmpresa = new WebPanel();
        pnlDatosEmpresa.setLayout(new BoxLayout(pnlDatosEmpresa, BoxLayout.Y_AXIS));
        pnlDatosEmpresa.add(new WebLabel(Catalogos.getEmpresa().getNombre_comercial()));
        pnlDatosEmpresa.add(new WebLabel(Catalogos.getEmpresa().getRazon_social() + " NIT: " + Catalogos.getEmpresa().getNit()));
        pnlDatosEmpresa.add(new WebLabel(Catalogos.getEmpresa().getDireccion()));
        pnlDatosEmpresa.add(new WebLabel(Catalogos.getEmpresa().getTelefono()));
        pnlDatosEmpresa.add(new WebLabel(Catalogos.getEmpresa().getDireccion_web()));
        pnlEmpresa.add(new WebLabel(loadIcon("img.jpg")));
        pnlEmpresa.add(pnlDatosEmpresa);
        return pnlEmpresa;
    }

    private Component agregarPanelDatosFactura() {
        WebPanel pnlDatosFactura = new WebPanel();
        pnlDatosFactura.setLayout(new BoxLayout(pnlDatosFactura, BoxLayout.Y_AXIS));
        WebLabel lblFecha = new WebLabel(Catalogos.getFechaLarga());
        lblFecha.setFontSize(15);
        pnlDatosFactura.add(lblFecha);
        WebLabel lblFacturaElectronica = new WebLabel("Factura electrónica");
        lblFacturaElectronica.setFontSize(16);
        lblFacturaElectronica.setBoldFont(true);
        pnlDatosFactura.add(lblFacturaElectronica);
        WebLabel lblSerie = new WebLabel("Serie " + Catalogos.getDocumentos().get(0).getNombre_serie());
        lblSerie.setBoldFont(true);
        pnlDatosFactura.add(lblSerie);
        WebLabel lblNoFactura = new WebLabel("No. " + Catalogos.getNuevoNoFactura(Catalogos.getDocumentos().get(0)));
        lblNoFactura.setBoldFont(true);
        lblNoFactura.setForeground(Color.RED);
        pnlDatosFactura.add(lblNoFactura);

        return pnlDatosFactura;
    }

    private Component agregarDatosIndividualesProducto() {
        WebPanel pnlDatosProducto = new WebPanel();
        pnlDatosProducto.setLayout(new FlowLayout());
        txtCantidad = new WebTextField();
        txtCantidad.setColumns(10);
        txtCantidad.setInputPrompt("Cantidad");
        txtCantidad.setHideInputPromptOnFocus(false);
        txtCantidad.addFocusListener(new Validar_Entero());
        TooltipManager.setTooltip(txtCantidad, "Cantidad a comprar", TooltipWay.up);
        pnlDatosProducto.add(txtCantidad);

        cmbProducto = new CmbBuscarProducto(this);
        cmbProducto.setToolTipText("Ingrese SKU o nombre del producto");
        pnlDatosProducto.add(cmbProducto);

        txtDescripcion = new WebTextField();
        txtDescripcion.setColumns(columnasDatos);
        txtDescripcion.setInputPrompt("Descipción");
        txtDescripcion.setHideInputPromptOnFocus(false);
        txtDescripcion.setEditable(false);
        TooltipManager.setTooltip(txtDescripcion, "Descripción del producto", TooltipWay.up);
        pnlDatosProducto.add(txtDescripcion);

        txtPrecio = new WebTextField();
        txtPrecio.setColumns(columnasDatos / 2);
        txtPrecio.setInputPrompt("Precio individual");
        txtPrecio.setHideInputPromptOnFocus(false);
        txtPrecio.setEditable(false);
        txtPrecio.addFocusListener(new Validar_Double());
        TooltipManager.setTooltip(txtPrecio, "Precio individual", TooltipWay.up);
        pnlDatosProducto.add(txtPrecio);

        btnAgregar = new WebButton("Agregar", loadIcon("add.png"));
        btnAgregar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtCantidad.getText().isEmpty()) {
                    Catalogos.mostrarMensajeError("La cantidad de producto no puede estar vacio.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
                } else {
                    if (cmbProducto.getProductoSeleccionado() != null) {
                        BDO_Detalle_Factura nuevo = new BDO_Detalle_Factura(0, "0", "0", cmbProducto.getProductoSeleccionado().getSku(), Integer.parseInt(txtCantidad.getText()), txtDescripcion.getText(), new BigDecimal(1.5));
                        BigDecimal subtotal = BigDecimal.valueOf(Integer.parseInt(txtCantidad.getText()));
                        subtotal = subtotal.multiply(BigDecimal.valueOf(Double.parseDouble(txtPrecio.getText())));
                        nuevo.setSubtotal(subtotal);
                        detallesTableModel.addDetalle(nuevo);
                        txtDescripcion.setEditable(false);
                        tblDetalles.repaint();
                        txtCantidad.clear();
                        txtCantidad.requestFocus();
                    } else {
                        Catalogos.mostrarMensajeError("Por favor seleccione un producto.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        pnlDatosProducto.add(btnAgregar);
        actualizarDatos(cmbProducto.getProductoSeleccionado());
        return pnlDatosProducto;
    }

    public void actualizarDatos(BDO_Producto producto) {
        txtDescripcion.setText(producto.getConcepto());
        txtPrecio.setText(getPrecioProducto(producto));
        if (producto.isEs_producto()) {
            txtDescripcion.setEditable(false);
            txtPrecio.setEditable(false);
        } else {
            txtDescripcion.setEditable(true);
            txtPrecio.setEditable(true);
        }

    }

    public String getPrecioProducto(BDO_Producto producto) {
        BigDecimal precioVenta = Catalogos.getPrecioVenta(producto.getSku());
        BigDecimal precioDescuento = precioVenta.multiply(BigDecimal.valueOf(descuento));
        precioVenta = precioVenta.subtract(precioDescuento);

        precioVenta = precioVenta.setScale(2, BigDecimal.ROUND_DOWN);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        String result = df.format(precioVenta);

        return result;
    }

    private Component agregarPanelDatosCliente() {
        WebPanel pnlCliente = new WebPanel();
        pnlCliente.setLayout(new BoxLayout(pnlCliente, BoxLayout.Y_AXIS));

        WebPanel pnlNIT = new WebPanel();
        pnlNIT.setLayout(new FlowLayout());
        pnlNIT.add(new WebLabel("      NIT:"));
        txtNIT = new WebTextField();
        txtNIT.setColumns(columnasDatos);
        txtNIT.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                txtNIT.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                String buscado = txtNIT.getText();
                if (!buscado.isEmpty()) {
                    if (valirdarNit(buscado)) {
                        if (connCliente.busquedaNitActivo(buscado.trim()) != null) {
                            ArrayList<BDO_Cliente> cliente = new ArrayList(connCliente.busquedaNitActivo(buscado.trim()).values());
                            if (cliente.isEmpty()) {
                                cliente = new ArrayList(connCliente.busquedaNit(buscado.trim()).values());
                                if (cliente.isEmpty()) {
                                    if (WebOptionPane.showOptionDialog(null, "El NIT ingresado no está registrado. ¿Desea agregarlo", "NIT no registrado", WebOptionPane.YES_NO_OPTION, WebOptionPane.QUESTION_MESSAGE, null, new Object[]{"Sí", "No"}, "No") == 0) {
                                        FrmAgregarCliente agregar = new FrmAgregarCliente(buscado);
                                        agregar.show();
                                        cliente = new ArrayList(connCliente.busquedaNit(buscado.trim()).values());
                                        if (cliente.size() > 0) {
                                            txtNombre.setText(cliente.get(0).getNombre());
                                            txtDireccion.setText(cliente.get(0).getDireccion());
                                            descuento = Catalogos.buscarPrecioClienteInt(cliente.get(0).getPrecio()).getDescuento() / 100 + 1;
                                            txtNIT.setEditable(false);
                                        }
                                    } else {
                                        txtNIT.requestFocus();
                                        txtNIT.selectAll();
                                    }
                                } else {
                                    Catalogos.mostrarMensajeError("El NIT ingresado pertenece a un cliente que no puede facturar.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
                                    txtNIT.requestFocus();
                                }
                            }
                        } else {
                            if (WebOptionPane.showOptionDialog(null, "El NIT ingresado no está registrado. ¿Desea agregarlo", "NIT no registrado", WebOptionPane.YES_NO_OPTION, WebOptionPane.QUESTION_MESSAGE, null, new Object[]{"Sí", "No"}, "No") == 0) {
                                FrmAgregarCliente agregar = new FrmAgregarCliente(buscado);
                                agregar.show();
                                ArrayList<BDO_Cliente> cliente = new ArrayList(connCliente.busquedaNit(buscado.trim()).values());
                                if (cliente.size() > 0) {
                                    txtNombre.setText(cliente.get(0).getNombre());
                                    txtDireccion.setText(cliente.get(0).getDireccion());
                                }
                            } else {
                                txtNIT.requestFocus();
                                txtNIT.selectAll();
                            }
                        }
                    } else {

                        txtNIT.requestFocus();
                        txtNIT.selectAll();

                    }


                } else {
                    Catalogos.mostrarMensajeError("El campo NIT no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
                    txtNIT.requestFocus();
                }
            }

            private boolean valirdarNit(String nitIngresado) {
                if (nitIngresado.matches("^[1-9][0-9]+[kK]?")) {

                    String primeraParte = nitIngresado.substring(0, nitIngresado.length() - 1);
                    int factor = primeraParte.length() + 1;
                    int suma = 0;
                    for (int i = 0; i < primeraParte.length(); i++) {
                        int mult = Character.getNumericValue(primeraParte.charAt(i)) * factor;
                        suma += mult;
                        factor--;
                    }

                    int residuo = (11 - (suma % 11)) % 11;

                    String verificadorStr = nitIngresado.substring(nitIngresado.length() - 1);
                    int verificador = 0;

                    if (verificadorStr.equalsIgnoreCase("k")) {
                        verificador = 10;
                    } else {
                        verificador = Integer.parseInt(verificadorStr);
                    }

                    if (residuo == verificador) {
                        return true;
                    } else {
                        Catalogos.mostrarMensajeError("El NIT ingresado no es válido. Por favor revise la información ingresada.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
                        return false;
                    }
                } else if (nitIngresado.matches("^CF|cf")) {
                    return true;
                } else {
                    Catalogos.mostrarMensajeError("Formato no válido para el NIT ingresado. Ejemplos: 3456789, 457896k.", "Error", WebOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        });
        txtNIT.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String buscado = txtNIT.getText();
                if (!buscado.isEmpty()) {
                    if (connCliente.busquedaNitActivo(buscado.trim()) != null) {
                        ArrayList<BDO_Cliente> cliente = new ArrayList(connCliente.busquedaNitActivo(buscado.trim()).values());
                        if (cliente.size() > 0) {
                            txtNombre.setText(cliente.get(0).getNombre());
                            txtDireccion.setText(cliente.get(0).getDireccion());
                            txtNIT.setEditable(false);
                            descuento = Catalogos.buscarPrecioClienteInt(cliente.get(0).getPrecio()).getDescuento();
                            if (descuento > 0) {
                                descuento = descuento / 100;
                            }
                            cmbProducto.setSelectedIndex(1);
                            cmbProducto.setSelectedIndex(0);
                        } else {
                            txtNombre.clear();
                            txtDireccion.clear();
                        }
                    } else {
                        txtNombre.clear();
                        txtDireccion.clear();
                    }
                } else {
                    txtNombre.clear();
                    txtDireccion.clear();
                }
            }
        });
        pnlNIT.add(txtNIT);
        pnlCliente.add(pnlNIT);


        WebPanel pnlNombre = new WebPanel();
        pnlNombre.setLayout(new FlowLayout());
        pnlNombre.add(new WebLabel("  Nombre:"));
        txtNombre = new WebTextField();
        txtNombre.setColumns(columnasDatos);
        txtNombre.setEditable(false);
        pnlNombre.add(txtNombre);
        pnlCliente.add(pnlNombre);

        WebPanel pnlDireccion = new WebPanel();
        pnlDireccion.setLayout(new FlowLayout());
        pnlDireccion.add(new WebLabel("Dirección:"));
        txtDireccion = new WebTextField();
        txtDireccion.setColumns(columnasDatos);
        txtDireccion.setEditable(false);
        pnlDireccion.add(txtDireccion);
        pnlCliente.add(pnlDireccion);

        return pnlCliente;
    }

    private Component agregarPanelBotones() {
        WebPanel pnlBotones = new WebPanel();
        pnlBotones.setLayout(new FlowLayout());
        btnGenerar = new WebButton("Cobrar", loadIcon("save_new.png"));
        btnGenerar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarDatos()) {
                    FrmCobrar cobrar = new FrmCobrar(tblDetalles, columnasDatos, detallesTableModel.getTotal(), Catalogos.getNuevoNoFactura(Catalogos.getDocumentos().get(0)));
                    cobrar.show();
                }

            }
        });
        pnlBotones.add(btnGenerar);
        btnCancelar = new WebButton("Cancelar", loadIcon("cancel.png"));
        pnlBotones.add(btnCancelar);
        return pnlBotones;
    }

    private boolean validarDatos() {
        if (txtNIT.getText().isEmpty()) {
            Catalogos.mostrarMensajeError("El campo NIT no puede estar vacío.", "Error", WebOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (detallesTableModel.getTotal().compareTo(BigDecimal.ZERO) == 0) {
            Catalogos.mostrarMensajeError("No hay productos que facturar", "Error", WebOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private Component agregarBtnCancelar() {
        WebPanel pnlBtnCancelar = new WebPanel();
        btnCancelar = new WebButton("Cancelar", loadIcon("cancel.png"));
        pnlBtnCancelar.add(btnCancelar);
        return pnlBtnCancelar;
    }

    private Component agregarDatosResolucion() {
        WebPanel pnlDatosResolucion = new WebPanel();
        pnlDatosResolucion.add(new WebLabel("Autorizado según resolución " + Catalogos.getDocumentos().get(0).getResolucion() + " del " + Catalogos.getDocumentos().get(0).getCorrelativo_inicial() + " al " + Catalogos.getDocumentos().get(0).getCorrelativo_final()));
        return pnlDatosResolucion;
    }

    private Component agregarPanelTablaProductos() {
        detallesTableModel = new DetalleTableModel();

        detallesTableModel.setDetalle(Catalogos.getDetalleFactura());
        tblDetalles = new WebTable(detallesTableModel) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        };
        tblDetalles.setRowHeight(25);
        tblDetalles.setAutoResizeMode(WebTable.AUTO_RESIZE_OFF);
        tblDetalles.setRowSelectionAllowed(true);
        tblDetalles.setColumnSelectionAllowed(false);
        tblDetalles.setPreferredScrollableViewportSize(new Dimension(tblDetalles.getPreferredSize().width, (int) (screenSize.height - screenSize.height * 0.65)));

        Action remover = new AbstractAction("+") {

            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                int row = Integer.valueOf(e.getActionCommand());
                DetalleTableModel model = (DetalleTableModel) table.getModel();
                model.removeDetalle(row);
            }
        };

        ButtonColumn del = new ButtonColumn(tblDetalles, remover, 4);

        WebScrollPane scroll = new WebScrollPane(tblDetalles);
        return scroll;
    }

    private Component agregarLblTotal() {
        WebPanel pnlTotal = new WebPanel();
        pnlTotal.setLayout(new FlowLayout());
        lblTotal = new WebLabel("150");
        lblTotal.setFontSize(26);
        lblTotal.setForeground(Color.BLUE);
        WebLabel lblTitulo = new WebLabel("Total: ");
        lblTitulo.setFontSize(26);
        pnlTotal.add(lblTitulo);
        pnlTotal.add(lblTotal);
        return pnlTotal;


    }

    private void cancelarOperacion() {
        limpiarCampos();
        cambiarBloqueoCampos(true);
    }

    private void limpiarCampos() {
        txtNIT.clear();
        txtNombre.clear();
        txtDireccion.clear();
        txtCantidad.clear();
        txtPrecio.clear();
        txtDescripcion.clear();
        cmbProducto.setSelectedIndex(0);
        detallesTableModel = new DetalleTableModel();

    }

    private void cambiarBloqueoCampos(Boolean bloqueo) {
        txtNIT.setEnabled(bloqueo);
        txtNIT.setEditable(bloqueo);
    }

    public class DetalleTableModel extends AbstractTableModel {

        private String[] columnNames = {"Cantidad", "SKU", "Descripción", "Precio", "Eliminar"};
        private Object[][] data;
        public final Object[] longValues = {"int", "String", "String", "BigDecimal", "int"};
        private ArrayList<BDO_Detalle_Factura> detalles = new ArrayList<BDO_Detalle_Factura>();

        public void setDetalle(ArrayList<BDO_Detalle_Factura> detalles) {
            if (detalles != null) {
                this.detalles = detalles;
                cargarDatos();
            }
        }

        public void addDetalle(BDO_Detalle_Factura detalle) {
            this.detalles.add(detalle);
            cargarDatos();
        }

        public void removeDetalle(int row) {
            this.detalles.remove(row);
            cargarDatos();
        }

        private void cargarDatos() {

            if (detalles != null) {
                data = new Object[detalles.size()][];
                for (int i = 0; i < detalles.size(); i++) {
                    BDO_Detalle_Factura actual = detalles.get(i);
                    if (actual != null) {
                        Object[] productoActualArr = {actual.getCantidad(), actual.getSku(), actual.getDescripcion(), actual.getSubtotal(), loadIcon("button-close.png")};
                        data[i] = productoActualArr;
                    }
                }
                this.fireTableDataChanged();
                lblTotal.setText("Q. " + getTotal());

            }
        }

        public BigDecimal getTotal() {
            BigDecimal total = new BigDecimal(0.0);
            for (int i = 0; i < detalles.size(); i++) {
                total = total.add(detalles.get(i).getSubtotal());
            }
            return total;
        }

        public void addEmptyRow() {
            data = new Object[detalles.size()][];
            int i;
            for (i = 0; i < detalles.size(); i++) {
                BDO_Detalle_Factura actual = detalles.get(i);
                Object[] productoActualArr = {actual.getCantidad(), actual.getSku(), actual.getDescripcion(), actual.getSubtotal()};
                data[i] = productoActualArr;
            }
            Object[] productoActualArr = {"", "", "", ""};
            data[i] = productoActualArr;
            this.fireTableDataChanged();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[ col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return data[ row][ col];
        }

        @Override
        public Class getColumnClass(int c) {
            return longValues[ c].getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            switch (col) {
                case 0:
                case 4:
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void setValueAt(Object value, int row, int col) {

            switch (col) {
                case 0:
                    if (value.toString().matches("^\\d+")) {
                        data[ row][ col] = value;
                        BigDecimal subtotal = BigDecimal.valueOf(Integer.parseInt(value.toString()));
                        subtotal = subtotal.multiply(detalles.get(row).getSubtotal());
                        data[ row][col_subtotal] = subtotal;
                        BDO_Detalle_Factura detalle = detalles.get(row);
                        detalle.setSubtotal(subtotal);
                        detalles.set(row, detalle);
                        fireTableCellUpdated(row, col_subtotal);
                        lblTotal.setText("Q. " + getTotal());
                    } else {
                        Catalogos.mostrarMensajeError("Cantidad ingresada no válida. Por favor ingrese un número.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
                    }
                    break;
                default:
                    data[ row][ col] = value;
            }


            fireTableCellUpdated(row, col);
        }
    }
}
