/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.productos;

import BDO.BDO_Atributo_Producto;
import BDO.BDO_Atributo_Valor_Producto;
import BDO.BDO_Categoria_Producto;
import BDO.BDO_Estado_Producto;
import BDO.BDO_Impuesto;
import BDO.BDO_Producto;
import DAO.DAO_Atributo_Valor_Producto;
import DAO.DAO_Producto;
import com.alee.global.GlobalConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.desktoppane.WebInternalFrame;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebTextField;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.utils.swing.UnselectableButtonGroup;
import contar.Catalogos;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import validacion.Validar_Double;
import validacion.Validar_Vacio;

/**
 *
 * @author Chaz
 */
public class FrmProducto extends WebInternalFrame {

    private WebPanel pnlDatosProducto, pnlConsultaProducto, pnlBusquedaAvanzadaProducto;
    private WebTabbedPane pnlPestañasProducto;
    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    private CmbBuscarProducto cmbConsultaProducto, cmbDatosProducto;
    private WebTable tblProductos, tblBusquedaAvanzada;
    private WebButton btnEditar, btnNuevo, btnVer, btnGuardar, btnCancelar;
    private WebTextField txtSKU, txtConcepto, txtCosto, txtSerie, txtLote;
    private WebCheckBox chkAfectoIVA, chkAplicaSerie, chkAplicaLote;
    private WebRadioButton optServicio, optProducto;
    private WebComboBox cmbEstadoProducto, cmbCategoriaProducto, cmbImpuestoIDP;
    private Image imgFoto, imgDefault;
    private WebButton btnFoto;
    private final int IMG_WIDTH = 250;
    private final int IMG_HEIGHT = 1750;
    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final int columnasDatos = 25;
    private DAO_Producto connProducto;
    private DAO_Atributo_Valor_Producto connAtributoValorProducto;
    private boolean editarProducto = false;
    private WebScrollPane scrDatos;
    private WebPanel pnlIzquierdo, pnlDerecho, pnlParametros, pnlTags;
    private ProductosTableModel modelBusquedaAvanzada;
    private ArrayList<BDO_Atributo_Valor_Producto> parametrosBusquedaAvanzada;
    private BDO_Categoria_Producto categoriaBusquedaAvanzada;
    private BDO_Estado_Producto estadoBusquedaAvanzada;
    private BDO_Impuesto impuestoBusquedaAvanzada;
    private BDO_Producto productoSeleccionado;

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

    public FrmProducto() {
        super("Módulo de productos", true, true, true, true);
        createUI();
    }

    private void createUI() {
        pnlDatosProducto = new WebPanel();
        pnlConsultaProducto = new WebPanel();
        pnlBusquedaAvanzadaProducto = new WebPanel();
        pnlPestañasProducto = new WebTabbedPane();
        ImageIcon fImgDefault = new ImageIcon(getClass().getResource("icons/noPicture.png"));
        BufferedImage bImgDefault = toBufferedImage(fImgDefault.getImage());
        imgDefault = getScaledImage(bImgDefault, IMG_WIDTH, IMG_HEIGHT);
        agregarComponentenesDatosProducto();
        agregarComponentenesConsultaProducto();
        agregarComponentesBusquedaAvanzada();
        agregarPestañasProducto();
        add(pnlPestañasProducto);
        setFrameIcon(loadIcon("package.png"));
        addInternalFrameListener(new InternalFrameListener() {

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
                actualizarEstados(cmbEstadoProducto, false);
                actualizarCategorias(cmbCategoriaProducto, false);
                actualizarImpuestos(cmbImpuestoIDP, false);
                actualizarDatos(productoSeleccionado);
            }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
                cancelarOperacion();
            }
        });

        pack();
        cancelarOperacion();
    }

    private void setProductoSeleccionado(BDO_Producto producto){
        this.productoSeleccionado = producto;
    }
    
    private void agregarPestañasProducto() {
        pnlPestañasProducto.addTab("Mis Productos", loadIcon("folder_brick.png"), pnlConsultaProducto);
        pnlPestañasProducto.addTab("Datos del Producto", loadIcon("edit_package.png"), pnlDatosProducto);
        pnlPestañasProducto.addTab("Búsqueda avanzada", loadIcon("package_search.png"), pnlBusquedaAvanzadaProducto);
        pnlPestañasProducto.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (pnlPestañasProducto.getSelectedIndex() == 1){
                    actualizarDatos(productoSeleccionado);
                    
                }
            }
        });
   }

    private void agregarComponentenesDatosProducto() {
        pnlDatosProducto.setLayout(new BorderLayout());
        pnlDatosProducto.add(agregarPanelSuperiorDatosProducto(), BorderLayout.NORTH);
        pnlDatosProducto.add(agregarDatosIndividualesProducto(null), BorderLayout.CENTER);
        pnlDatosProducto.add(agregarPanelBotonesDatos(), BorderLayout.SOUTH);
        pnlDatosProducto.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
    }

    private void agregarComponentesBusquedaAvanzada() {
        pnlBusquedaAvanzadaProducto.setLayout(new BorderLayout());
        pnlBusquedaAvanzadaProducto.add(agregarPanelParametrosBusquedaAvanzada(), BorderLayout.WEST);
        WebPanel pnlCentral = new WebPanel();
        pnlCentral.setLayout(new BorderLayout());
        pnlCentral.add(agregarPanelTagsBusquedaAvanzada(), BorderLayout.NORTH);
        pnlCentral.add(agregarPanelTablaBusquedaAvanzada(), BorderLayout.CENTER);
        pnlBusquedaAvanzadaProducto.add(pnlCentral, BorderLayout.CENTER);
        pnlBusquedaAvanzadaProducto.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
    }

    private Component agregarPanelParametrosBusquedaAvanzada() {
        pnlParametros = new WebPanel();
        parametrosBusquedaAvanzada = new ArrayList<BDO_Atributo_Valor_Producto>();
        pnlParametros.setBorder(new TitledBorder("Parámetros de búsqueda"));
        pnlParametros.setLayout(new BoxLayout(pnlParametros, BoxLayout.PAGE_AXIS));
        connAtributoValorProducto = new DAO_Atributo_Valor_Producto();
        ArrayList<BDO_Atributo_Producto> atributos = Catalogos.getAtributosProducto();
        for (int i = 0; i < atributos.size(); i++) {
            BDO_Atributo_Producto atributo = atributos.get(i);
            WebPanel pnlValores = new WebPanel();
            pnlValores.setLayout(new FlowLayout());
            pnlValores.add(new WebLabel(atributo.getNombre() + ":"));
            pnlValores.add(atributo.getCombo(connAtributoValorProducto.valoresAtributo(atributo.getId_atributo_producto()), columnasDatos, this));
            pnlParametros.add(pnlValores);
        }

        WebPanel pnlCategoriaProducto = new WebPanel();
        pnlCategoriaProducto.setLayout(new FlowLayout());
        pnlCategoriaProducto.add(new WebLabel("Categoria:"));
        WebComboBox cmbCategoriaProductoBusqueda = new WebComboBox();
        cmbCategoriaProductoBusqueda.setEditorColumns(columnasDatos);
        cmbCategoriaProductoBusqueda = actualizarCategorias(cmbCategoriaProductoBusqueda, true);
        cmbCategoriaProductoBusqueda.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    WebComboBox localCombo = (WebComboBox) e.getSource();
                    if (localCombo.getSelectedIndex() != 0) {
                        agregarDatosBusquedaAvanzada(Catalogos.buscarCategoriaProducto(localCombo.getSelectedItem().toString()));
                    }
                }
            }
        });
        pnlCategoriaProducto.add(cmbCategoriaProductoBusqueda);
        pnlParametros.add(pnlCategoriaProducto);

        WebPanel pnlEstadoProducto = new WebPanel();
        pnlEstadoProducto.setLayout(new FlowLayout());
        pnlEstadoProducto.add(new WebLabel("Estado:"));
        WebComboBox cmbEstadoProductoBusqueda = new WebComboBox();
        cmbEstadoProductoBusqueda.setEditorColumns(columnasDatos);
        cmbEstadoProductoBusqueda = actualizarEstados(cmbEstadoProductoBusqueda, true);
        cmbEstadoProductoBusqueda.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    WebComboBox localCombo = (WebComboBox) e.getSource();
                    if (localCombo.getSelectedIndex() != 0) {
                        agregarDatosBusquedaAvanzada(Catalogos.buscarEstadoProducto(localCombo.getSelectedItem().toString()));
                    }
                }
            }
        });
        pnlEstadoProducto.add(cmbEstadoProductoBusqueda);
        pnlParametros.add(pnlEstadoProducto);

        WebPanel pnlImpuestoIDP = new WebPanel();
        pnlImpuestoIDP.setLayout(new FlowLayout());
        pnlImpuestoIDP.add(new WebLabel("Estado:"));
        WebComboBox cmbImpuestoIDPBusqueda = new WebComboBox();
        cmbImpuestoIDPBusqueda.setEditorColumns(columnasDatos);
        cmbImpuestoIDPBusqueda = actualizarImpuestos(cmbImpuestoIDPBusqueda, true);
        cmbImpuestoIDPBusqueda.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    WebComboBox localCombo = (WebComboBox) e.getSource();
                    if (localCombo.getSelectedIndex() != 0) {
                        agregarDatosBusquedaAvanzada(Catalogos.buscarImpuesto(localCombo.getSelectedItem().toString()));
                    }
                }
            }
        });
        pnlImpuestoIDP.add(cmbImpuestoIDPBusqueda);
        pnlParametros.add(pnlImpuestoIDP);

        return pnlParametros;
    }

    private Component agregarPanelTagsBusquedaAvanzada() {
        pnlTags = new WebPanel();
        pnlTags.setLayout(new FlowLayout());
        pnlTags.setBorder(new TitledBorder("Valores seleccionados"));
        WebScrollPane scroll = new WebScrollPane(pnlTags);
        return scroll;
    }

    private Component agregarPanelTablaBusquedaAvanzada() {
        modelBusquedaAvanzada = new ProductosTableModel();
        modelBusquedaAvanzada.setProductos(Catalogos.getProductos());

        tblBusquedaAvanzada = new WebTable(modelBusquedaAvanzada) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        };
        tblBusquedaAvanzada.setEditable(false);
        tblBusquedaAvanzada.setAutoResizeMode(WebTable.AUTO_RESIZE_OFF);
        tblBusquedaAvanzada.setRowSelectionAllowed(true);
        tblBusquedaAvanzada.setColumnSelectionAllowed(false);
        tblBusquedaAvanzada.setPreferredScrollableViewportSize(new Dimension(tblBusquedaAvanzada.getPreferredSize().width, (int) (screenSize.height - screenSize.height * 0.65)));
        tblBusquedaAvanzada.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    WebTable target = (WebTable) e.getSource();
                    int row = target.getSelectedRow();
                    int column = 0;
                    BDO_Producto buscado = Catalogos.buscarProducto(target.getValueAt(row, column).toString());
                    actualizarDatos(buscado);
                    cmbDatosProducto.cargarProductos(Catalogos.getProductos());
                    cmbDatosProducto.setSelectedItem(buscado.basicsToString());
                    pnlPestañasProducto.setSelectedIndex(1);
                }
            }
        });
        WebScrollPane scroll = new WebScrollPane(tblBusquedaAvanzada);
        return scroll;

    }

    private Component agregarPanelBotonesDatos() {

        WebPanel pnlBotonesDatosIndividuales = new WebPanel();
        pnlBotonesDatosIndividuales.setLayout(new FlowLayout());
        btnGuardar = new WebButton("Guardar", loadIcon("save_new.png"));
        btnCancelar = new WebButton("Cancelar", loadIcon("cancel.png"));

        btnGuardar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                guardarProducto();
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

    private Component agregarDatosIndividualesProducto(BDO_Producto ProductoSeleccionado) {
        WebPanel pnlDatosIndividuales = new WebPanel();
        pnlDatosIndividuales.setLayout(new FlowLayout());

        pnlIzquierdo = new WebPanel();
        pnlIzquierdo.setLayout(new BoxLayout(pnlIzquierdo, BoxLayout.Y_AXIS));
        pnlDerecho = new WebPanel();
        pnlDerecho.setLayout(new BoxLayout(pnlDerecho, BoxLayout.Y_AXIS));



        btnFoto = new WebButton(new ImageIcon(imgDefault));
        btnFoto.addActionListener(new ActionListener() {

            private WebFileChooser imageChooser = null;
            private File file = null;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (imageChooser == null) {
                    imageChooser = new WebFileChooser();
                    imageChooser.setMultiSelectionEnabled(false);
                    imageChooser.setAcceptAllFileFilterUsed(false);
                    imageChooser.addChoosableFileFilter(GlobalConstants.IMAGES_FILTER);
                    imageChooser.setFileFilter(GlobalConstants.IMAGES_FILTER);
                }
                if (file != null) {
                    imageChooser.setSelectedFile(file);
                }
                if (imageChooser.showOpenDialog(btnFoto) == WebFileChooser.APPROVE_OPTION) {
                    file = imageChooser.getSelectedFile();
                    ImageIcon imgIcon = new ImageIcon(file.getAbsolutePath());
                    BufferedImage img = toBufferedImage(imgIcon.getImage());
                    imgFoto = getScaledImage(img, IMG_WIDTH, IMG_HEIGHT);
                    btnFoto.setIcon(new ImageIcon(imgFoto));
                }
            }
        });
        WebPanel pnlFoto = new WebPanel();
        pnlFoto.setLayout(new FlowLayout());
        pnlFoto.add(btnFoto);
        TooltipManager.setTooltip(pnlFoto, "Fotografía del Producto", TooltipWay.up);
        TooltipManager.setTooltip(btnFoto, "Fotografía del Producto", TooltipWay.up);
        pnlIzquierdo.add(pnlFoto);

        txtSKU = new WebTextField();
        txtSKU.setColumns(columnasDatos);
        txtSKU.setInputPrompt("SKU del Producto");
        txtSKU.setHideInputPromptOnFocus(false);
        txtSKU.addFocusListener(new Validar_Vacio("El SKU"));
        WebPanel pnlSKU = new WebPanel();
        pnlSKU.setLayout(new FlowLayout());
        pnlSKU.add(new WebLabel(loadIcon("barcode.png")));
        pnlSKU.add(txtSKU);
        TooltipManager.setTooltip(pnlSKU, "SKU del Producto", TooltipWay.up);
        TooltipManager.setTooltip(txtSKU, "SKU del Producto", TooltipWay.up);
        pnlDerecho.add(pnlSKU);


        txtConcepto = new WebTextField();
        txtConcepto.setColumns(columnasDatos);
        txtConcepto.setInputPrompt("Descripción que aparecerá en factura");
        txtConcepto.setHideInputPromptOnFocus(false);
        txtConcepto.addFocusListener(new Validar_Vacio("La descripción en factura"));
        WebPanel pnlConcepto = new WebPanel();
        pnlConcepto.setLayout(new FlowLayout());
        pnlConcepto.add(new WebLabel(loadIcon("catalog_pages.png")));
        pnlConcepto.add(txtConcepto);
        TooltipManager.setTooltip(pnlConcepto, "Ingrese el concepto del producto que aparecerá en la factura", TooltipWay.up);
        TooltipManager.setTooltip(txtConcepto, "Ingrese el concepto del producto que aparecerá en la factura", TooltipWay.up);
        pnlDerecho.add(pnlConcepto);

        txtCosto = new WebTextField();
        txtCosto.setColumns(columnasDatos);
        txtCosto.setInputPrompt("Último costo");
        txtCosto.setHideInputPromptOnFocus(false);
        txtCosto.addFocusListener(new Validar_Double());
        WebPanel pnlCosto = new WebPanel();
        pnlCosto.setLayout(new FlowLayout());
        pnlCosto.add(new WebLabel(loadIcon("money.png")));
        pnlCosto.add(txtCosto);
        TooltipManager.setTooltip(pnlCosto, "Ingrese último costo del producto", TooltipWay.up);
        TooltipManager.setTooltip(txtCosto, "Ingrese último costo del producto", TooltipWay.up);
        pnlDerecho.add(pnlCosto);

        chkAplicaSerie = new WebCheckBox("¿Aplica serie?");
        chkAplicaSerie.setToolTipText("Seleccione la casilla si desea ingresar una serie");
        chkAplicaSerie.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (chkAplicaSerie.isSelected()) {
                    txtSerie.setEditable(true);
                } else {
                    txtSerie.setEditable(false);
                }
            }
        });
        txtSerie = new WebTextField();
        txtSerie.setColumns(columnasDatos - 10);
        txtSerie.setInputPrompt("Serie del producto");
        txtSerie.setHideInputPromptOnFocus(false);
        txtSerie.addFocusListener(new Validar_Vacio("La serie del producto "));
        WebPanel pnlSerie = new WebPanel();
        pnlSerie.setLayout(new FlowLayout());
        pnlSerie.add(new WebLabel(loadIcon("package_green.png")));
        pnlSerie.add(chkAplicaSerie);
        pnlSerie.add(txtSerie);
        TooltipManager.setTooltip(pnlSerie, "Ingrese serie del producto", TooltipWay.up);
        TooltipManager.setTooltip(txtSerie, "Ingrese serie del producto", TooltipWay.up);
        pnlDerecho.add(pnlSerie);

        
        chkAplicaLote = new WebCheckBox("¿Aplica lote?");
        chkAplicaLote.setToolTipText("Seleccione la casilla si desea ingresar un lote");
        chkAplicaLote.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (chkAplicaLote.isSelected()) {
                    txtLote.setEditable(true);
                } else {
                    txtLote.setEditable(false);
                }
            }
        });
        txtLote = new WebTextField();
        txtLote.setColumns(columnasDatos - 10);
        txtLote.setInputPrompt("Lote del producto");
        txtLote.setHideInputPromptOnFocus(false);
        txtLote.addFocusListener(new Validar_Vacio("El lote del producto"));
        WebPanel pnlLote = new WebPanel();
        pnlLote.setLayout(new FlowLayout());
        pnlLote.add(new WebLabel(loadIcon("label.png")));
        pnlLote.add(chkAplicaLote);
        pnlLote.add(txtLote);
        TooltipManager.setTooltip(pnlLote, "Ingrese lote del producto", TooltipWay.up);
        TooltipManager.setTooltip(txtLote, "Ingrese lote del producto", TooltipWay.up);
        pnlDerecho.add(pnlLote);
        
        
        chkAfectoIVA = new WebCheckBox("¿Afecto a IVA?");
        WebPanel pnlAfectoIVA = new WebPanel();
        pnlAfectoIVA.setLayout(new FlowLayout());
        pnlAfectoIVA.add(new WebLabel(loadIcon("save_money.png")));
        pnlAfectoIVA.add(chkAfectoIVA);
        TooltipManager.setTooltip(pnlAfectoIVA, "Marque la casilla si el producto está afecto a IVA", TooltipWay.up);
        chkAfectoIVA.setToolTipText("Marque la casilla si el producto está afecto a IVA");
        pnlIzquierdo.add(pnlAfectoIVA);

        optServicio = new WebRadioButton("Servicio");
        optProducto = new WebRadioButton("Producto");
        UnselectableButtonGroup.group(optServicio, optProducto);
        WebPanel pnlProductoServicio = new WebPanel();
        pnlProductoServicio.setLayout(new FlowLayout());
        pnlProductoServicio.add("Tipo: ");
        pnlProductoServicio.add(optServicio);
        pnlProductoServicio.add(optProducto);
        TooltipManager.setTooltip(pnlProductoServicio, "Marque el tipo del ítem", TooltipWay.up);
        optProducto.setToolTipText("Marque el tipo del ítem");
        optServicio.setToolTipText("Marque el tipo del ítem");
        pnlDerecho.add(pnlProductoServicio);

        cmbCategoriaProducto = new WebComboBox();
        cmbCategoriaProducto.setEditable(false);
        cmbCategoriaProducto.setEditorColumns(columnasDatos);
        cmbCategoriaProducto = actualizarCategorias(cmbCategoriaProducto, false);
        WebPanel pnlCategoriaProducto = new WebPanel();
        pnlCategoriaProducto.setLayout(new FlowLayout());
        pnlCategoriaProducto.add(new WebLabel(loadIcon("categories.png")));
        pnlCategoriaProducto.add(cmbCategoriaProducto);
        TooltipManager.setTooltip(pnlCategoriaProducto, "Seleccione la categoria del producto", TooltipWay.up);
        cmbCategoriaProducto.setToolTipText("Seleccione la categoria del producto");
        pnlIzquierdo.add(pnlCategoriaProducto);

        cmbEstadoProducto = new WebComboBox();
        cmbEstadoProducto.setEditable(false);
        cmbEstadoProducto.setEditorColumns(columnasDatos);
        cmbEstadoProducto = actualizarEstados(cmbEstadoProducto, false);
        WebPanel pnlEstadoProducto = new WebPanel();
        pnlEstadoProducto.setLayout(new FlowLayout());
        pnlEstadoProducto.add(new WebLabel(loadIcon("package_delete.png")));
        pnlEstadoProducto.add(cmbEstadoProducto);
        TooltipManager.setTooltip(pnlEstadoProducto, "Seleccione el estado del Producto", TooltipWay.up);
        cmbEstadoProducto.setToolTipText("Seleccione el estado del Producto");
        pnlDerecho.add(pnlEstadoProducto);

        cmbImpuestoIDP = new WebComboBox();
        cmbImpuestoIDP.setEditable(false);
        cmbImpuestoIDP.setEditorColumns(columnasDatos);
        cmbImpuestoIDP = actualizarImpuestos(cmbImpuestoIDP, false);
        WebPanel pnlImpuestoIDP = new WebPanel();
        pnlImpuestoIDP.setLayout(new FlowLayout());
        pnlImpuestoIDP.add(new WebLabel(loadIcon("oil.png")));
        pnlImpuestoIDP.add(cmbImpuestoIDP);
        TooltipManager.setTooltip(pnlImpuestoIDP, "Seleccione el IDP del Producto", TooltipWay.up);
        cmbImpuestoIDP.setToolTipText("Seleccione el IDP del del Producto");
        pnlDerecho.add(pnlImpuestoIDP);


        if (ProductoSeleccionado != null) {
            ProductoSeleccionado.cargarAtributos();
            ArrayList<BDO_Atributo_Valor_Producto> atributos = ProductoSeleccionado.getAtributos();
            if (atributos != null) {
                for (int i = 0; i < atributos.size(); i++) {
                    if (i % 2 == 0) {
                        pnlDerecho.add(atributos.get(i).toPanel(columnasDatos));
                    } else {
                        pnlIzquierdo.add(atributos.get(i).toPanel(columnasDatos));
                    }
                }
            }
        }


        pnlDatosIndividuales.add(pnlIzquierdo);
        pnlDatosIndividuales.add(pnlDerecho);
        scrDatos = new WebScrollPane(pnlDatosIndividuales);
        scrDatos.setBorder(null);
        //actualizarDatos(cmbDatosProducto.getProductoSeleccionado());
        return scrDatos;
    }

    private Component agregarPanelSuperiorDatosProducto() {
        WebPanel pnlSuperiorDatosProducto = new WebPanel();
        pnlSuperiorDatosProducto.setLayout(new FlowLayout());
        pnlSuperiorDatosProducto.add(new WebLabel(loadIcon("search.png")));
        pnlSuperiorDatosProducto.add(new WebLabel("Buscar Producto:"));
        cmbDatosProducto = new CmbBuscarProducto(this);
        pnlSuperiorDatosProducto.add(cmbDatosProducto);
        btnEditar = new WebButton("Editar Producto", loadIcon("edit.png"));
        btnNuevo = new WebButton("Nuevo Producto", loadIcon("add.png"));

        btnEditar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnEditar.setEnabled(false);
                editarProducto();
            }
        });

        btnNuevo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoProducto();
            }
        });
        pnlSuperiorDatosProducto.add(btnEditar);
        pnlSuperiorDatosProducto.add(btnNuevo);
        return pnlSuperiorDatosProducto;
    }

    private void agregarComponentenesConsultaProducto() {
        pnlConsultaProducto.setLayout(new BorderLayout());
        pnlConsultaProducto.add(agregarPanelBusqueda(), BorderLayout.NORTH);
        pnlConsultaProducto.add(agregarTablaProductos(), BorderLayout.CENTER);
        pnlConsultaProducto.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
    }

    private Component agregarPanelBusqueda() {
        WebPanel pnlSuperiorConsulta = new WebPanel();
        pnlSuperiorConsulta.setLayout(new FlowLayout());
        pnlSuperiorConsulta.add(new WebLabel(loadIcon("search.png")));
        pnlSuperiorConsulta.add(new WebLabel("Buscar Producto:"));
        cmbConsultaProducto = new CmbBuscarProducto(this);
        pnlSuperiorConsulta.add(cmbConsultaProducto);
        btnVer = new WebButton("Ver Producto", loadIcon("package_go.png"));
        btnVer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarDatos(cmbConsultaProducto.getProductoSeleccionado());
                cmbDatosProducto.cargarProductos(Catalogos.getProductos());
                cmbDatosProducto.setSelectedItem(cmbConsultaProducto.getProductoSeleccionado().basicsToString());
                pnlPestañasProducto.setSelectedIndex(1);
            }
        });
        pnlSuperiorConsulta.add(btnVer);
        return pnlSuperiorConsulta;
    }

    private Component agregarTablaProductos() {
        ProductosTableModel ProductosTableModel = new ProductosTableModel();
        ProductosTableModel.setProductos(Catalogos.getProductos());

        tblProductos = new WebTable(ProductosTableModel) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        };
        tblProductos.setEditable(false);
        tblProductos.setAutoResizeMode(WebTable.AUTO_RESIZE_OFF);
        tblProductos.setRowSelectionAllowed(true);
        tblProductos.setColumnSelectionAllowed(false);
        tblProductos.setPreferredScrollableViewportSize(new Dimension(tblProductos.getPreferredSize().width, (int) (screenSize.height - screenSize.height * 0.65)));
        tblProductos.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    WebTable target = (WebTable) e.getSource();
                    int row = target.getSelectedRow();
                    int column = 0;
                    BDO_Producto buscado = Catalogos.buscarProducto(target.getValueAt(row, column).toString());
                    actualizarDatos(buscado);
                    cmbDatosProducto.cargarProductos(Catalogos.getProductos());
                    cmbDatosProducto.setSelectedItem(buscado.basicsToString());
                    pnlPestañasProducto.setSelectedIndex(1);
                }
            }
        });
        WebScrollPane scroll = new WebScrollPane(tblProductos);
        return scroll;
    }

    public void editarProducto() {
        editarProducto = true;
        cambiarBloqueoCampos(true);
    }

    public void guardarProducto() {

        if (validarDatos()) {
            connProducto = new DAO_Producto();
            BDO_Producto nuevo = new BDO_Producto(txtSKU.getText().trim(), txtConcepto.getText().trim(),
                    txtSerie.getText().trim(),txtLote.getText().trim(), BigDecimal.valueOf(Double.parseDouble(txtCosto.getText().trim())),
                    Integer.parseInt(cmbEstadoProducto.getSelectedItem().toString().split(" | ")[0]),
                    Integer.parseInt(cmbCategoriaProducto.getSelectedItem().toString().split(" | ")[0]),
                    Catalogos.getEmpresa().getId_empresa(), 0, chkAfectoIVA.isSelected(), false, imgFoto);
            if (cmbImpuestoIDP.getSelectedIndex() == 0) {
                nuevo.setId_impuesto_idp(-1);
            } else {
                nuevo.setId_impuesto_idp(Integer.parseInt(cmbImpuestoIDP.getSelectedItem().toString().split(" | ")[0]));
            }

            if (optProducto.isSelected()) {
                nuevo.setEs_producto(true);
            }
            nuevo.setFoto(imgFoto);
            if (editarProducto) {
                connProducto.actualizarProducto(nuevo, cmbDatosProducto.getProductoSeleccionado());
            } else {
                connProducto.agregarProducto(nuevo);
            }

            cambiarBloqueoCampos(false);
            editarProducto = false;
            actualizarProductos();
        }
    }

    private void actualizarProductos() {
        Catalogos.actualizarProductos();
        String seleccionado = cmbDatosProducto.getSelectedItem().toString();
        cmbDatosProducto.cargarProductos(Catalogos.getProductos());
        cmbDatosProducto.setSelectedItem(seleccionado);
        seleccionado = cmbConsultaProducto.getSelectedItem().toString();
        cmbConsultaProducto.cargarProductos(Catalogos.getProductos());
        cmbConsultaProducto.setSelectedItem(seleccionado);
        ProductosTableModel Productos = new ProductosTableModel();
        Productos.setProductos(Catalogos.getProductos());
        tblProductos.setModel(Productos);
        tblProductos.repaint();
        actualizarDatos(cmbDatosProducto.getProductoSeleccionado());
    }

    private WebComboBox actualizarEstados(WebComboBox cmbEstados, boolean opcion) {
        String seleccionado = null;
        if (cmbEstados.getSelectedItem() != null) {
            seleccionado = cmbEstados.getSelectedItem().toString();
        }
        cmbEstados.removeAllItems();
        if (opcion) {
            cmbEstados.addItem("Seleccione una opción");
        }
        ArrayList<BDO_Estado_Producto> estadosProducto = Catalogos.getEstados_Producto();
        for (int i = 0; i < estadosProducto.size(); i++) {
            cmbEstados.addItem(estadosProducto.get(i).basicsToString());
        }
        cmbEstados.repaint();
        if (seleccionado == null) {
            cmbEstados.setSelectedIndex(0);
        } else {
            cmbEstados.setSelectedItem(seleccionado);
        }
        return cmbEstados;
    }

    private WebComboBox actualizarCategorias(WebComboBox cmbCategorias, boolean opcion) {
        String seleccionado = null;
        if (cmbCategorias.getSelectedItem() != null) {
            seleccionado = cmbCategorias.getSelectedItem().toString();
        }
        cmbCategorias.removeAllItems();
        if (opcion) {
            cmbCategorias.addItem("Seleccione una opción");
        }
        ArrayList<BDO_Categoria_Producto> CategoriasProducto = Catalogos.getCategorias_Producto();
        for (int i = 0; i < CategoriasProducto.size(); i++) {
            cmbCategorias.addItem(CategoriasProducto.get(i).basicsToString());
        }
        cmbCategorias.repaint();
        if (seleccionado == null) {
            cmbCategorias.setSelectedIndex(0);
        } else {
            cmbCategorias.setSelectedItem(seleccionado);
        }
        return cmbCategorias;
    }

    private WebComboBox actualizarImpuestos(WebComboBox cmbImpuestos, boolean opcion) {
        String seleccionado = null;
        if (cmbImpuestos.getSelectedItem() != null) {
            seleccionado = cmbImpuestos.getSelectedItem().toString();
        }
        cmbImpuestos.removeAllItems();
        if (opcion) {
            cmbImpuestos.addItem("Seleccione una opción");
        } else {
            cmbImpuestos.addItem("No aplica");
        }

        ArrayList<BDO_Impuesto> Impuestos = Catalogos.getImpuestosIDP();
        for (int i = 0; i < Impuestos.size(); i++) {
            cmbImpuestos.addItem(Impuestos.get(i).basicsToString());
        }
        cmbImpuestos.repaint();
        if (seleccionado == null) {
            cmbImpuestos.setSelectedIndex(0);
        } else {
            cmbImpuestos.setSelectedItem(seleccionado);
        }
        return cmbImpuestos;
    }

    public void cancelarOperacion() {
        editarProducto = false;
        actualizarDatos(cmbDatosProducto.getProductoSeleccionado());
        cambiarBloqueoCampos(false);
    }

    public void actualizarDatos(BDO_Producto ProductoSeleccionado) {
        if (ProductoSeleccionado != null) {
            setProductoSeleccionado(ProductoSeleccionado);
            BorderLayout lyt = (BorderLayout) pnlDatosProducto.getLayout();
            if (lyt.getLayoutComponent(BorderLayout.CENTER) != null) {
                pnlDatosProducto.remove(lyt.getLayoutComponent(BorderLayout.CENTER));
            }

            pnlDatosProducto.add(agregarDatosIndividualesProducto(ProductoSeleccionado), BorderLayout.CENTER);
            txtSKU.setText(ProductoSeleccionado.getSku());
            txtConcepto.setText(ProductoSeleccionado.getConcepto());
            if (ProductoSeleccionado.getSerie_producto() == null) {
                chkAplicaSerie.setSelected(false);
                txtSerie.clear();
            } else {
                if (ProductoSeleccionado.getSerie_producto().trim().isEmpty()) {
                    chkAplicaSerie.setSelected(false);
                    txtSerie.clear();
                } else {
                    chkAplicaSerie.setSelected(true);
                }

            }
            
            if (ProductoSeleccionado.getLote() == null) {
                chkAplicaLote.setSelected(false);
                txtLote.clear();
            } else {
                if (ProductoSeleccionado.getLote().trim().isEmpty()) {
                    chkAplicaLote.setSelected(false);
                    txtLote.clear();
                } else {
                    chkAplicaLote.setSelected(true);
                }

            }
            txtLote.setText(ProductoSeleccionado.getLote());
            txtSerie.setText(ProductoSeleccionado.getSerie_producto());
            txtCosto.setText(ProductoSeleccionado.getCosto().toString());
            chkAfectoIVA.setSelected(ProductoSeleccionado.isAfecto_iva());
            if (ProductoSeleccionado.isEs_producto()) {
                optProducto.setSelected(true);
            } else {
                optServicio.setSelected(true);
            }

            if (ProductoSeleccionado.getFoto() != null) {
                imgFoto = ProductoSeleccionado.getFoto();
                BufferedImage img = toBufferedImage(imgFoto);
                imgFoto = getScaledImage(img, IMG_WIDTH, IMG_HEIGHT);
            } else {
                imgFoto = imgDefault;
            }

            btnFoto.setIcon(new ImageIcon(imgFoto));

            cmbCategoriaProducto.setSelectedItem(Catalogos.buscarCategoriaProductoInt(ProductoSeleccionado.getId_categoria()).basicsToString());
            cmbEstadoProducto.setSelectedItem(Catalogos.buscarEstadoProductoInt(ProductoSeleccionado.getId_estado_producto()).basicsToString());

            if (Catalogos.buscarImpuestoInt(ProductoSeleccionado.getId_impuesto_idp()) != null) {
                cmbImpuestoIDP.setSelectedItem(Catalogos.buscarImpuestoInt(ProductoSeleccionado.getId_impuesto_idp()).basicsToString());
            } else {
                cmbImpuestoIDP.setSelectedIndex(0);
            }

            pnlDatosProducto.repaint();
            cambiarBloqueoCampos(false);
            pack();
        }
    }

    private void cambiarBloqueoCampos(boolean bloqueo) {
        /*txtNit.setEnabled(bloqueo);
        txtNombre.setEnabled(bloqueo);
        txtNombreFacturar.setEnabled(bloqueo);
        txtDireccion.setEnabled(bloqueo);
        txtEmail.setEnabled(bloqueo);
        txtTelefono.setEnabled(bloqueo);
        cmbPrecioProducto.setEnabled(bloqueo);
        cmbEstadoProducto.setEnabled(bloqueo);*/
        btnNuevo.setEnabled(!bloqueo);
        btnEditar.setEnabled(!bloqueo);
        cmbDatosProducto.setEnabled(!bloqueo);
        btnGuardar.setEnabled(bloqueo);
        btnCancelar.setEnabled(bloqueo);

        for (int i = 0; i < pnlIzquierdo.getComponentCount(); i++) {
            WebPanel pnlIz = (WebPanel) pnlIzquierdo.getComponent(i);
            for (Component c : pnlIz.getComponents()) {
                c.setEnabled(bloqueo);
            }
        }
        for (int i = 0; i < pnlDerecho.getComponentCount(); i++) {
            WebPanel pnlIz = (WebPanel) pnlDerecho.getComponent(i);
            for (Component c : pnlIz.getComponents()) {
                c.setEnabled(bloqueo);
            }
        }
    }

    private void limpiarCampos() {
        txtSKU.clear();
        txtConcepto.clear();
        txtSerie.clear();
        txtCosto.clear();
        chkAfectoIVA.setSelected(true);
        optProducto.setSelected(true);
        optServicio.setSelected(false);
        cmbCategoriaProducto.setSelectedIndex(0);
        cmbEstadoProducto.setSelectedIndex(0);
        cmbImpuestoIDP.setSelectedIndex(0);
    }

    private void nuevoProducto() {
        editarProducto = false;
        cambiarBloqueoCampos(true);
        txtSKU.setEditable(false);
        limpiarCampos();
    }

    private boolean validarDatos() {
        if (txtSKU.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El campo SKU no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            if (!(Catalogos.buscarProducto(txtSKU.getText().trim()) == null || (Catalogos.buscarProducto(txtSKU.getText().trim()).getSku().equals(cmbDatosProducto.getProductoSeleccionado().getSku()) && editarProducto))) {
                Catalogos.mostrarMensajeError("El SKU ingresado pertenece a otro Producto. Revise la información.", "Error", WebOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        if (txtConcepto.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El campo concepto no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (chkAplicaSerie.isSelected()) {
            if (txtSerie.getText().trim().isEmpty()) {
                Catalogos.mostrarMensajeError("El campo serie no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        
        if (chkAplicaLote.isSelected()) {
            if (txtLote.getText().trim().isEmpty()) {
                Catalogos.mostrarMensajeError("El campo lote no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        if (txtCosto.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El campo Nombre no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            Validar_Double validar = new Validar_Double();
            if (!validar.valirdarDouble(txtCosto.getText().trim())) {
                Catalogos.mostrarMensajeError("Cifra no válida. Ingrese un número. Ejemplo: 4, 7.96", "Error", WebOptionPane.ERROR_MESSAGE);
            }
        }

        return true;
    }

    public void actualizarDatosBusquedaAvanzada() {
        connProducto = new DAO_Producto();
        modelBusquedaAvanzada.setProductos(connProducto.busquedaAvanzada(parametrosBusquedaAvanzada, categoriaBusquedaAvanzada, estadoBusquedaAvanzada, impuestoBusquedaAvanzada));
        modelBusquedaAvanzada.fireTableDataChanged();
        tblBusquedaAvanzada.repaint();
    }

    public void agregarDatosBusquedaAvanzada(BDO_Categoria_Producto categoria) {
        categoriaBusquedaAvanzada = categoria;
        pnlTags.add(panelTag(categoriaBusquedaAvanzada));
        actualizarDatosBusquedaAvanzada();
    }

    public void agregarDatosBusquedaAvanzada(BDO_Estado_Producto estado) {
        estadoBusquedaAvanzada = estado;
        pnlTags.add(panelTag(estadoBusquedaAvanzada));
        actualizarDatosBusquedaAvanzada();
    }

    public void agregarDatosBusquedaAvanzada(BDO_Impuesto impuesto) {
        impuestoBusquedaAvanzada = impuesto;
        pnlTags.add(panelTag(impuestoBusquedaAvanzada));
        actualizarDatosBusquedaAvanzada();
    }

    public Component panelTag(final BDO_Estado_Producto estado) {
        final WebPanel pnlTag = new WebPanel();
        pnlTag.setLayout(new FlowLayout());
        WebLabel lblTitulo = new WebLabel("Estado:");
        lblTitulo.setBoldFont(true);
        pnlTag.add(lblTitulo);
        WebLabel lblValor = new WebLabel(estado.getTipo());
        pnlTag.add(lblValor);

        WebLabel lblIcono = new WebLabel(loadIcon("button-close.png"));
        lblIcono.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                removerDatosBusquedaAvanzada(estado, pnlTag);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        pnlTag.add(lblIcono);
        pnlTag.setBorder(new BevelBorder(BevelBorder.RAISED));
        return pnlTag;
    }

    public Component panelTag(final BDO_Categoria_Producto Categoria) {
        final WebPanel pnlTag = new WebPanel();
        pnlTag.setLayout(new FlowLayout());
        WebLabel lblTitulo = new WebLabel("Categoria:");
        lblTitulo.setBoldFont(true);
        pnlTag.add(lblTitulo);
        WebLabel lblValor = new WebLabel(Categoria.getNombre());
        pnlTag.add(lblValor);

        WebLabel lblIcono = new WebLabel(loadIcon("button-close.png"));
        lblIcono.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                removerDatosBusquedaAvanzada(Categoria, pnlTag);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        pnlTag.add(lblIcono);
        pnlTag.setBorder(new BevelBorder(BevelBorder.RAISED));
        return pnlTag;
    }

    public Component panelTag(final BDO_Impuesto Impuesto) {
        final WebPanel pnlTag = new WebPanel();
        pnlTag.setLayout(new FlowLayout());
        WebLabel lblTitulo = new WebLabel("IDP:");
        lblTitulo.setBoldFont(true);
        pnlTag.add(lblTitulo);
        WebLabel lblValor = new WebLabel(Impuesto.getNombre());
        pnlTag.add(lblValor);

        WebLabel lblIcono = new WebLabel(loadIcon("button-close.png"));
        lblIcono.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                removerDatosBusquedaAvanzada(Impuesto, pnlTag);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        pnlTag.add(lblIcono);
        pnlTag.setBorder(new BevelBorder(BevelBorder.RAISED));
        return pnlTag;
    }

    public void agregarDatosBusquedaAvanzada(int id_atributo_Producto, String valor) {
        BDO_Atributo_Valor_Producto nuevoAtributo = new BDO_Atributo_Valor_Producto(valor, null, id_atributo_Producto);
        parametrosBusquedaAvanzada.add(nuevoAtributo);
        pnlTags.add(nuevoAtributo.panelTag(this));
        actualizarDatosBusquedaAvanzada();
    }

    public void removerDatosBusquedaAvanzada(BDO_Atributo_Producto atributo, String valor, WebPanel pnlTag) {
        for (int i = 0; i < parametrosBusquedaAvanzada.size(); i++) {
            BDO_Atributo_Valor_Producto parametro = parametrosBusquedaAvanzada.get(i);
            if (parametro.getAtributo_producto().getId_atributo_producto() == atributo.getId_atributo_producto()
                    && parametro.getValor().equals(valor)) {
                parametrosBusquedaAvanzada.remove(i);
                break;
            }
        }
        pnlTags.remove(pnlTag);
        pnlTags.repaint();
        actualizarDatosBusquedaAvanzada();
    }

    public void removerDatosBusquedaAvanzada(BDO_Categoria_Producto categoria, WebPanel pnlTag) {
        categoriaBusquedaAvanzada = null;
        pnlTags.remove(pnlTag);
        pnlTags.repaint();
        actualizarDatosBusquedaAvanzada();
    }

    public void removerDatosBusquedaAvanzada(BDO_Estado_Producto estado, WebPanel pnlTag) {
        estadoBusquedaAvanzada = null;
        pnlTags.remove(pnlTag);
        pnlTags.repaint();
        actualizarDatosBusquedaAvanzada();
    }

    public void removerDatosBusquedaAvanzada(BDO_Impuesto impuesto, WebPanel pnlTag) {
        impuestoBusquedaAvanzada = null;
        pnlTags.remove(pnlTag);
        pnlTags.repaint();
        actualizarDatosBusquedaAvanzada();
    }

    private BufferedImage getScaledImage(BufferedImage src, int w, int h) {
        int original_width = src.getWidth();
        int original_height = src.getHeight();
        int bound_width = w;
        int bound_height = h;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        BufferedImage resizedImg = new BufferedImage(new_width, new_height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, new_width, new_height);
        g2.drawImage(src, 0, 0, new_width, new_height, null);
        g2.dispose();
        return resizedImg;
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public class ProductosTableModel extends AbstractTableModel {

        private String[] columnNames = {"SKU", "Descripción", "Tipo", "Último Costo"};
        private Object[][] data;
        public final Object[] longValues = {"String", "String", "String", "String"};

        public void setProductos(ArrayList<BDO_Producto> productos) {
            data = new Object[productos.size()][];

            for (int i = 0; i < productos.size(); i++) {
                BDO_Producto actual = productos.get(i);
                String tipo = "Producto";
                if (!actual.isEs_producto()) {
                    tipo = "Servicio";
                }
                Object[] productoActualArr = {actual.getSku(), actual.getConcepto(), tipo, actual.getCosto().toString()};
                data[i] = productoActualArr;
            }
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
            return col >= 1;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            data[ row][ col] = value;
            fireTableCellUpdated(row, col);
        }
    }
}
