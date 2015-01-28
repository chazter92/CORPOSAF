/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.clientes;

import BDO.BDO_Atributo_Cliente;
import BDO.BDO_Atributo_Valor_Cliente;
import BDO.BDO_Cliente;
import BDO.BDO_Estado_Cliente;
import BDO.BDO_Precio_Cliente;
import DAO.DAO_Atributo_Valor_Cliente;
import DAO.DAO_Cliente;
import com.alee.global.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.desktoppane.WebInternalFrame;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.table.WebTable;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import validacion.Validar_Nit;
import validacion.Validar_Vacio;

/**
 *
 * @author Chaz
 */
public class FrmCliente extends WebInternalFrame {

    private WebPanel pnlDatosCliente, pnlConsultaCliente, pnlBusquedaAvanzadaCliente;
    private WebTabbedPane pnlPestañasCliente;
    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    private CmbBuscarCliente cmbConsultaCliente, cmbDatosCliente;
    private WebTable tblClientes, tblBusquedaAvanzada;
    private WebButton btnEditarCliente, btnNuevoCliente, btnVerCliente, btnGuardarCliente, btnCancelar;
    private WebTextField txtNit, txtNombre, txtDireccion, txtEmail, txtTelefono, txtNombreFacturar;
    private WebComboBox cmbPrecioCliente, cmbEstadoCliente;
    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final int columnasDatos = 25;
    private DAO_Cliente connCliente;
    private DAO_Atributo_Valor_Cliente connAtributoValorCliente;
    private boolean editarCliente = false;
    private WebScrollPane scrDatos;
    private WebPanel pnlIzquierdo, pnlDerecho, pnlParametros, pnlTags;
    private ClientesTableModel modelBusquedaAvanzada;
    private ArrayList<BDO_Atributo_Valor_Cliente> parametrosBusquedaAvanzada;

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
    
    public FrmCliente() {
        super("Módulo de clientes", true, true, true, true);
        createUI();
    }

    private void createUI() {
        pnlDatosCliente = new WebPanel();
        pnlConsultaCliente = new WebPanel();
        pnlBusquedaAvanzadaCliente = new WebPanel();
        pnlPestañasCliente = new WebTabbedPane();
        agregarComponentenesDatosCliente();
        agregarComponentenesConsultaCliente();
        agregarComponentesBusquedaAvanzada();
        agregarPestañasCliente();
        add(pnlPestañasCliente);
        setFrameIcon(loadIcon("users_4.png"));
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
                actualizarClientes();
                actualizarEstados();
                actualizarPrecios();
            }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
                cancelarOperacion();
            }
            
        });
        pack();
        cancelarOperacion();
    }

    private void agregarPestañasCliente() {
        pnlPestañasCliente.addTab("Mis clientes", loadIcon("users_edit.png"), pnlConsultaCliente);
        pnlPestañasCliente.addTab("Datos del cliente", loadIcon("folder_user.png"), pnlDatosCliente);
        pnlPestañasCliente.addTab("Búsqueda avanzada", loadIcon("search_accounts.png"), pnlBusquedaAvanzadaCliente);
    }

    private void agregarComponentenesDatosCliente() {
        pnlDatosCliente.setLayout(new BorderLayout());
        pnlDatosCliente.add(agregarPanelSuperiorDatosCliente(), BorderLayout.NORTH);
        pnlDatosCliente.add(agregarDatosIndividualesCliente(null), BorderLayout.CENTER);
        pnlDatosCliente.add(agregarPanelBotonesDatos(), BorderLayout.SOUTH);
        pnlDatosCliente.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
    }
    
    private void agregarComponentesBusquedaAvanzada() {
        pnlBusquedaAvanzadaCliente.setLayout(new BorderLayout());
        pnlBusquedaAvanzadaCliente.add(agregarPanelParametrosBusquedaAvanzada(), BorderLayout.WEST);
        WebPanel pnlCentral = new WebPanel();
        pnlCentral.setLayout(new BorderLayout());
        pnlCentral.add(agregarPanelTagsBusquedaAvanzada(), BorderLayout.NORTH);
        pnlCentral.add(agregarPanelTablaBusquedaAvanzada(), BorderLayout.CENTER);
        pnlBusquedaAvanzadaCliente.add(pnlCentral, BorderLayout.CENTER);
        pnlBusquedaAvanzadaCliente.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
    }
    
    private Component agregarPanelParametrosBusquedaAvanzada(){
        pnlParametros = new WebPanel();
        parametrosBusquedaAvanzada = new ArrayList<BDO_Atributo_Valor_Cliente>();
        pnlParametros.setBorder(new TitledBorder("Parámetros de búsqueda"));
        pnlParametros.setLayout(new BoxLayout(pnlParametros, BoxLayout.PAGE_AXIS));
        connAtributoValorCliente = new DAO_Atributo_Valor_Cliente();
        ArrayList<BDO_Atributo_Cliente> atributos = Catalogos.getAtributos_cliente();
        for(int i=0;i<atributos.size();i++){
            BDO_Atributo_Cliente atributo = atributos.get(i);
            WebPanel pnlValores = new WebPanel();
            pnlValores.setLayout(new FlowLayout());
            pnlValores.add(new WebLabel(atributo.getNombre()+":"));
            pnlValores.add(atributo.getCombo(connAtributoValorCliente.valoresAtributo(atributo.getId_atributo_cliente()),columnasDatos, this));
            pnlParametros.add(pnlValores);
        }
        return pnlParametros;
    }
    
    private Component agregarPanelTagsBusquedaAvanzada(){
        pnlTags = new WebPanel();
        pnlTags.setLayout(new FlowLayout());
        pnlTags.setBorder(new TitledBorder("Valores seleccionados"));
        return pnlTags;
    }
    
    private Component agregarPanelTablaBusquedaAvanzada(){
        modelBusquedaAvanzada = new ClientesTableModel();
        modelBusquedaAvanzada.setClientes(Catalogos.getClientes());

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
                    BDO_Cliente buscado = Catalogos.buscarCliente(target.getValueAt(row, column).toString());
                    actualizarDatos(buscado);
                    cmbDatosCliente.cargarClientes(Catalogos.getClientes());
                    cmbDatosCliente.setSelectedItem(buscado.basicsToString());
                    pnlPestañasCliente.setSelectedIndex(1);
                }
            }
        });
        WebScrollPane scroll = new WebScrollPane(tblBusquedaAvanzada);
        return scroll;

    }

    private Component agregarPanelBotonesDatos() {

        WebPanel pnlBotonesDatosIndividuales = new WebPanel();
        pnlBotonesDatosIndividuales.setLayout(new FlowLayout());
        btnGuardarCliente = new WebButton("Guardar", loadIcon("save_new.png"));
        btnCancelar = new WebButton("Cancelar", loadIcon("cancel.png"));

        btnGuardarCliente.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCliente();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarOperacion();
            }
        });

        pnlBotonesDatosIndividuales.add(btnGuardarCliente);
        pnlBotonesDatosIndividuales.add(btnCancelar);
        return pnlBotonesDatosIndividuales;
    }

    private Component agregarDatosIndividualesCliente(BDO_Cliente clienteSeleccionado) {
        WebPanel pnlDatosIndividuales = new WebPanel();
        pnlDatosIndividuales.setLayout(new FlowLayout());

        pnlIzquierdo = new WebPanel();
        pnlIzquierdo.setLayout(new BoxLayout(pnlIzquierdo, BoxLayout.Y_AXIS));
        pnlDerecho = new WebPanel();
        pnlDerecho.setLayout(new BoxLayout(pnlDerecho, BoxLayout.Y_AXIS));


        txtNit = new WebTextField();
        txtNit.setColumns(columnasDatos);
        txtNit.setInputPrompt("NIT del cliente");
        txtNit.setHideInputPromptOnFocus(false);
        txtNit.addFocusListener(new Validar_Nit());
        WebPanel pnlNit = new WebPanel();
        pnlNit.setLayout(new FlowLayout());
        pnlNit.add(new WebLabel(loadIcon("nit.png")));
        pnlNit.add(txtNit);
        TooltipManager.setTooltip(pnlNit, "NIT del cliente, Ejemplos: 3456789, 457896k", TooltipWay.up);
        TooltipManager.setTooltip(txtNit, "NIT del cliente, Ejemplos: 3456789, 457896k", TooltipWay.up);
        pnlIzquierdo.add(pnlNit);


        txtNombre = new WebTextField();
        txtNombre.setColumns(columnasDatos);
        txtNombre.setInputPrompt("Nombre completo del cliente");
        txtNombre.setHideInputPromptOnFocus(false);
        txtNombre.addFocusListener(new Validar_Vacio());
        WebPanel pnlNombre = new WebPanel();
        pnlNombre.setLayout(new FlowLayout());
        pnlNombre.add(new WebLabel(loadIcon("user_orange.png")));
        pnlNombre.add(txtNombre);
        TooltipManager.setTooltip(pnlNombre, "Ingrese nombre del cliente", TooltipWay.up);
        TooltipManager.setTooltip(txtNombre, "Ingrese nombre del cliente", TooltipWay.up);
        pnlDerecho.add(pnlNombre);

        txtNombreFacturar = new WebTextField();
        txtNombreFacturar.setColumns(columnasDatos);
        txtNombreFacturar.setInputPrompt("Nombre que aparecerá en factura");
        txtNombreFacturar.setHideInputPromptOnFocus(false);
        txtNombreFacturar.addFocusListener(new Validar_Vacio());
        WebPanel pnlNombreFacturar = new WebPanel();
        pnlNombreFacturar.setLayout(new FlowLayout());
        pnlNombreFacturar.add(new WebLabel(loadIcon("user_gray.png")));
        pnlNombreFacturar.add(txtNombreFacturar);
        TooltipManager.setTooltip(pnlNombreFacturar, "Ingrese nombre a facturar", TooltipWay.up);
        TooltipManager.setTooltip(txtNombreFacturar, "Ingrese nombre a facturar", TooltipWay.up);
        pnlIzquierdo.add(pnlNombreFacturar);

        txtDireccion = new WebTextField();
        txtDireccion.setColumns(columnasDatos);
        txtDireccion.setInputPrompt("Dirección domiciliar");
        txtDireccion.setHideInputPromptOnFocus(false);
        txtDireccion.addFocusListener(new Validar_Vacio());
        WebPanel pnlDireccion = new WebPanel();
        pnlDireccion.setLayout(new FlowLayout());
        pnlDireccion.add(new WebLabel(loadIcon("mail_box.png")));
        pnlDireccion.add(txtDireccion);
        TooltipManager.setTooltip(pnlDireccion, "Ingrese dirección del cliente", TooltipWay.up);
        TooltipManager.setTooltip(txtDireccion, "Ingrese dirección del cliente", TooltipWay.up);
        pnlDerecho.add(pnlDireccion);

        txtEmail = new WebTextField();
        txtEmail.setColumns(columnasDatos);
        txtEmail.setInputPrompt("Correo Electrónico");
        txtEmail.setHideInputPromptOnFocus(false);
        WebPanel pnlEmail = new WebPanel();
        pnlEmail.setLayout(new FlowLayout());
        pnlEmail.add(new WebLabel(loadIcon("email.png")));
        pnlEmail.add(txtEmail);
        TooltipManager.setTooltip(pnlEmail, "Email del cliente, ejemplo: jLopez@gmail.com", TooltipWay.up);
        TooltipManager.setTooltip(txtEmail, "Email del cliente, ejemplo: jLopez@gmail.com", TooltipWay.up);
        pnlIzquierdo.add(pnlEmail);

        txtTelefono = new WebTextField();
        txtTelefono.setColumns(columnasDatos);
        txtTelefono.setInputPrompt("Número de teléfono");
        txtTelefono.setHideInputPromptOnFocus(false);
        WebPanel pnlTelefono = new WebPanel();
        pnlTelefono.setLayout(new FlowLayout());
        pnlTelefono.add(new WebLabel(loadIcon("phone.png")));
        pnlTelefono.add(txtTelefono);
        TooltipManager.setTooltip(pnlTelefono, "Telefono del cliente, Ejemplos: 47985685, +502 47985685", TooltipWay.up);
        TooltipManager.setTooltip(txtTelefono, "Telefono del cliente, Ejemplos: 47985685, +502 47985685", TooltipWay.up);
        pnlDerecho.add(pnlTelefono);

        cmbPrecioCliente = new WebComboBox();
        cmbPrecioCliente.setEditable(false);
        cmbPrecioCliente.setEditorColumns(columnasDatos);
        cmbPrecioCliente.removeAllItems();
        ArrayList<BDO_Precio_Cliente> preciosCliente = Catalogos.getPrecios_cliente();
        for (int i = 0; i < preciosCliente.size(); i++) {
            cmbPrecioCliente.addItem(preciosCliente.get(i).basicsToString());
        }
        WebPanel pnlTipoCliente = new WebPanel();
        pnlTipoCliente.setLayout(new FlowLayout());
        pnlTipoCliente.add(new WebLabel(loadIcon("users_5.png")));
        pnlTipoCliente.add(cmbPrecioCliente);
        TooltipManager.setTooltip(pnlTipoCliente, "Seleccione el descuento autorizado", TooltipWay.up);
        cmbPrecioCliente.setToolTipText("Seleccione el descuento autorizado");
        pnlIzquierdo.add(pnlTipoCliente);

        cmbEstadoCliente = new WebComboBox();
        cmbEstadoCliente.setEditable(false);
        cmbEstadoCliente.setEditorColumns(columnasDatos);
        cmbEstadoCliente.removeAllItems();
        ArrayList<BDO_Estado_Cliente> estadosCliente = Catalogos.getEstados_cliente();
        for (int i = 0; i < estadosCliente.size(); i++) {
            cmbEstadoCliente.addItem(estadosCliente.get(i).basicsToString());
        }
        WebPanel pnlEstadoCliente = new WebPanel();
        pnlEstadoCliente.setLayout(new FlowLayout());
        pnlEstadoCliente.add(new WebLabel(loadIcon("user_delete.png")));
        pnlEstadoCliente.add(cmbEstadoCliente);
        TooltipManager.setTooltip(pnlEstadoCliente, "Seleccione el estado del cliente", TooltipWay.up);
        cmbEstadoCliente.setToolTipText("Seleccione el estado del cliente");
        pnlDerecho.add(pnlEstadoCliente);
        
        
        if (clienteSeleccionado != null) {
            clienteSeleccionado.cargarAtributos();
            ArrayList<BDO_Atributo_Valor_Cliente> atributos = clienteSeleccionado.getAtributos();
            if(atributos != null){
            for(int i=0; i<atributos.size();i++){
                if(i%2 == 0){
                    pnlDerecho.add(atributos.get(i).toPanel(columnasDatos));
                }else{
                    pnlIzquierdo.add(atributos.get(i).toPanel(columnasDatos));
                }
            }
            }
        }
        
        
        pnlDatosIndividuales.add(pnlIzquierdo);
        pnlDatosIndividuales.add(pnlDerecho);
        scrDatos = new WebScrollPane(pnlDatosIndividuales);
        scrDatos.setBorder(null);
        //actualizarDatos(cmbDatosCliente.getClienteSeleccionado());
        return scrDatos;
    }

    private Component agregarPanelSuperiorDatosCliente() {
        WebPanel pnlSuperiorDatosCliente = new WebPanel();
        pnlSuperiorDatosCliente.setLayout(new FlowLayout());
        pnlSuperiorDatosCliente.add(new WebLabel(loadIcon("search.png")));
        pnlSuperiorDatosCliente.add(new WebLabel("Buscar cliente:"));
        cmbDatosCliente = new CmbBuscarCliente(this);
        pnlSuperiorDatosCliente.add(cmbDatosCliente);
        btnEditarCliente = new WebButton("Editar cliente", loadIcon("edit.png"));
        btnNuevoCliente = new WebButton("Nuevo cliente", loadIcon("add.png"));

        btnEditarCliente.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnEditarCliente.setEnabled(false);
                editarCliente();
            }
        });

        btnNuevoCliente.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoCliente();
            }
        });
        pnlSuperiorDatosCliente.add(btnEditarCliente);
        pnlSuperiorDatosCliente.add(btnNuevoCliente);
        return pnlSuperiorDatosCliente;
    }

    private void agregarComponentenesConsultaCliente() {
        pnlConsultaCliente.setLayout(new BorderLayout());
        pnlConsultaCliente.add(agregarPanelBusqueda(), BorderLayout.NORTH);
        pnlConsultaCliente.add(agregarTablaClientes(), BorderLayout.CENTER);
        pnlConsultaCliente.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
    }

    private Component agregarPanelBusqueda() {
        WebPanel pnlSuperiorConsulta = new WebPanel();
        pnlSuperiorConsulta.setLayout(new FlowLayout());
        pnlSuperiorConsulta.add(new WebLabel(loadIcon("search.png")));
        pnlSuperiorConsulta.add(new WebLabel("Buscar cliente:"));
        cmbConsultaCliente = new CmbBuscarCliente(this);
        pnlSuperiorConsulta.add(cmbConsultaCliente);
        btnVerCliente = new WebButton("Ver cliente", loadIcon("user_go.png"));
        btnVerCliente.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarDatos(cmbConsultaCliente.getClienteSeleccionado());
                cmbDatosCliente.cargarClientes(Catalogos.getClientes());
                cmbDatosCliente.setSelectedItem(cmbConsultaCliente.getClienteSeleccionado().basicsToString());
                pnlPestañasCliente.setSelectedIndex(1);
            }
        });
        pnlSuperiorConsulta.add(btnVerCliente);
        return pnlSuperiorConsulta;
    }

    private Component agregarTablaClientes() {
        ClientesTableModel clientesTableModel = new ClientesTableModel();
        clientesTableModel.setClientes(Catalogos.getClientes());

        tblClientes = new WebTable(clientesTableModel) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        };
        tblClientes.setEditable(false);
        tblClientes.setAutoResizeMode(WebTable.AUTO_RESIZE_OFF);
        tblClientes.setRowSelectionAllowed(true);
        tblClientes.setColumnSelectionAllowed(false);
        tblClientes.setPreferredScrollableViewportSize(new Dimension(tblClientes.getPreferredSize().width, (int) (screenSize.height - screenSize.height * 0.65)));
        tblClientes.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    WebTable target = (WebTable) e.getSource();
                    int row = target.getSelectedRow();
                    int column = 0;
                    BDO_Cliente buscado = Catalogos.buscarCliente(target.getValueAt(row, column).toString());
                    actualizarDatos(buscado);
                    cmbDatosCliente.cargarClientes(Catalogos.getClientes());
                    cmbDatosCliente.setSelectedItem(buscado.basicsToString());
                    pnlPestañasCliente.setSelectedIndex(1);
                }
            }
        });
        WebScrollPane scroll = new WebScrollPane(tblClientes);
        return scroll;
    }

    public void editarCliente() {
        editarCliente = true;
        cambiarBloqueoCampos(true);
    }

    public void guardarCliente() {

        if (validarDatos()) {
            connCliente = new DAO_Cliente();
            BDO_Cliente nuevo = new BDO_Cliente(txtNit.getText().trim(), txtNombre.getText().trim(),
                    txtDireccion.getText().trim(), txtEmail.getText().trim(), txtNombreFacturar.getText().trim(),
                    txtTelefono.getText().trim(), Integer.parseInt(cmbPrecioCliente.getSelectedItem().toString().split(" | ")[0]),
                    Integer.parseInt(cmbEstadoCliente.getSelectedItem().toString().split(" | ")[0]),cmbDatosCliente.getClienteSeleccionado().getAtributos());
            if (editarCliente) {
                connCliente.actualizarCliente(nuevo, cmbDatosCliente.getClienteSeleccionado());
            } else {
                connCliente.agregarCliente(nuevo);
            }

            cambiarBloqueoCampos(false);
            editarCliente = false;
            actualizarClientes();
        }
    }
    
    private void actualizarClientes(){
            Catalogos.actualizarClientes();
            String seleccionado = cmbDatosCliente.getSelectedItem().toString();
            cmbDatosCliente.cargarClientes(Catalogos.getClientes());
            cmbDatosCliente.setSelectedItem(seleccionado);
            seleccionado = cmbConsultaCliente.getSelectedItem().toString();
            cmbConsultaCliente.cargarClientes(Catalogos.getClientes());
            cmbConsultaCliente.setSelectedItem(seleccionado);
            ClientesTableModel clientes = new ClientesTableModel();
            clientes.setClientes(Catalogos.getClientes());
            tblClientes.setModel(clientes);
            tblClientes.repaint();
            actualizarDatos(cmbDatosCliente.getClienteSeleccionado());
    }
    
    private void actualizarEstados(){
        String seleccionado = cmbEstadoCliente.getSelectedItem().toString();
        cmbEstadoCliente.removeAllItems();
        ArrayList<BDO_Estado_Cliente> estadosCliente = Catalogos.getEstados_cliente();
        for (int i = 0; i < estadosCliente.size(); i++) {
            cmbEstadoCliente.addItem(estadosCliente.get(i).basicsToString());
        }
        cmbEstadoCliente.repaint();        
        cmbEstadoCliente.setSelectedItem(seleccionado);
    }
    
    private void actualizarPrecios(){
        String seleccionado = cmbPrecioCliente.getSelectedItem().toString();
        cmbPrecioCliente.removeAllItems();
        ArrayList<BDO_Precio_Cliente> preciosCliente = Catalogos.getPrecios_cliente();
        for (int i = 0; i < preciosCliente.size(); i++) {
            cmbPrecioCliente.addItem(preciosCliente.get(i).basicsToString());
        }
        cmbPrecioCliente.repaint(); 
        cmbPrecioCliente.setSelectedItem(seleccionado);
    }

    public void cancelarOperacion() {
        editarCliente = false;
        actualizarDatos(cmbDatosCliente.getClienteSeleccionado());
        cambiarBloqueoCampos(false);
    }

    public void actualizarDatos(BDO_Cliente clienteSeleccionado) {
        if (clienteSeleccionado != null) {
            BorderLayout lyt = (BorderLayout) pnlDatosCliente.getLayout();
            if(lyt.getLayoutComponent(BorderLayout.CENTER) != null){
                pnlDatosCliente.remove(lyt.getLayoutComponent(BorderLayout.CENTER));
            }
            
            pnlDatosCliente.add(agregarDatosIndividualesCliente(clienteSeleccionado), BorderLayout.CENTER);
            txtNit.setText(clienteSeleccionado.getNit());
            txtNombre.setText(clienteSeleccionado.getNombre());
            txtNombreFacturar.setText(clienteSeleccionado.getNombre_factura());
            txtDireccion.setText(clienteSeleccionado.getDireccion());
            txtEmail.setText(clienteSeleccionado.getEmail());
            txtTelefono.setText(clienteSeleccionado.getTelefono());
            cmbPrecioCliente.setSelectedItem(Catalogos.buscarPrecioClienteInt(clienteSeleccionado.getPrecio()).basicsToString());
            cmbEstadoCliente.setSelectedItem(Catalogos.buscarEstadoClienteInt(clienteSeleccionado.getEstado()).basicsToString());
            pnlDatosCliente.repaint();
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
        cmbPrecioCliente.setEnabled(bloqueo);
        cmbEstadoCliente.setEnabled(bloqueo);*/
        btnNuevoCliente.setEnabled(!bloqueo);
        btnEditarCliente.setEnabled(!bloqueo);
        cmbDatosCliente.setEnabled(!bloqueo);
        btnGuardarCliente.setEnabled(bloqueo);
        btnCancelar.setEnabled(bloqueo);
        
       
        
        for(int i =0;i<pnlIzquierdo.getComponentCount();i++){
            WebPanel pnlIz = (WebPanel) pnlIzquierdo.getComponent(i);
            for (Component c : pnlIz.getComponents()){
                c.setEnabled(bloqueo);
            }
        }
        for(int i =0;i<pnlDerecho.getComponentCount();i++){
            WebPanel pnlIz = (WebPanel) pnlDerecho.getComponent(i);
             for (Component c : pnlIz.getComponents()){
                c.setEnabled(bloqueo);
            }
        }
    }

    private void limpiarCampos() {
        txtNit.clear();
        txtNombre.clear();
        txtNombreFacturar.clear();
        txtDireccion.clear();
        txtEmail.clear();
        txtTelefono.clear();
    }

    private void nuevoCliente() {
        editarCliente = false;        
        cambiarBloqueoCampos(true);
        limpiarCampos();
    }

    private boolean validarDatos() {
        if (txtNit.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El campo NIT no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            if (Catalogos.buscarCliente(txtNit.getText().trim()) == null || ( Catalogos.buscarCliente(txtNit.getText().trim()).getNit().equals(cmbDatosCliente.getClienteSeleccionado().getNit()) && editarCliente)) {
                Validar_Nit nit = new Validar_Nit();
                if (!nit.valirdarNit(txtNit.getText().trim())) {
                    if (WebOptionPane.showOptionDialog(null, "El NIT ingresado no es válido. ¿Desea continuar?", "NIT no válido", WebOptionPane.YES_NO_OPTION, WebOptionPane.QUESTION_MESSAGE, null, new Object[]{"Sí", "No"}, "No") == 1) {
                        txtNit.requestFocus();
                        txtNit.selectAll();
                        return false;
                    }

                }else{
                    return false;
                }
            } else {
                Catalogos.mostrarMensajeError("El NIT ingresado pertenece a otro cliente. Revise la información.", "Error", WebOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        if (txtNombre.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El campo Nombre no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (txtNombreFacturar.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El campo Nombre no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (txtDireccion.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El campo Nombre no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!txtTelefono.getText().trim().isEmpty()) {
            if (!txtTelefono.getText().trim().matches("[1-9]\\d+|\\+\\d{3} [1-9]\\d+")) {
                Catalogos.mostrarMensajeError("Formáto no válido para el teléfono ingresado. Ejemplos: 47985685, +502 47985685", "Advertencia", WebOptionPane.WARNING_MESSAGE);
                return false;
            }
        }


        return true;
    }

    public void actualizarDatosBusquedaAvanzada() {
        connCliente = new DAO_Cliente();
        modelBusquedaAvanzada.setClientes(connCliente.busquedaAvanzada(parametrosBusquedaAvanzada));
        modelBusquedaAvanzada.fireTableDataChanged();
        tblBusquedaAvanzada.repaint();
    }
    
    public void agregarDatosBusquedaAvanzada(int id_atributo_cliente, String valor){
        BDO_Atributo_Valor_Cliente nuevoAtributo = new BDO_Atributo_Valor_Cliente(valor, null, id_atributo_cliente);
        parametrosBusquedaAvanzada.add(nuevoAtributo);
        pnlTags.add(nuevoAtributo.panelTag(this));
        actualizarDatosBusquedaAvanzada();
    }

    public void removerDatosBusquedaAvanzada(BDO_Atributo_Cliente atributo, String valor, WebPanel pnlTag) {
        for(int i=0;i<parametrosBusquedaAvanzada.size();i++){
            BDO_Atributo_Valor_Cliente parametro = parametrosBusquedaAvanzada.get(i);
            if(parametro.getAtributo_cliente().getId_atributo_cliente()==atributo.getId_atributo_cliente() &&
                    parametro.getValor().equals(valor)){
                parametrosBusquedaAvanzada.remove(i);
                break;
            }
        }
        pnlTags.remove(pnlTag);
        pnlTags.repaint();
        actualizarDatosBusquedaAvanzada();
    }

    public class ClientesTableModel extends AbstractTableModel {

        private String[] columnNames = {"NIT", "Nombre", "Dirección", "Email", "Telefono"};
        private Object[][] data;
        public final Object[] longValues = {"String", "String", "String", "String", "String"};

        public void setClientes(ArrayList<BDO_Cliente> clientes) {
            data = new Object[clientes.size()][];

            for (int i = 0; i < clientes.size(); i++) {
                BDO_Cliente actual = clientes.get(i);
                Object[] clienteActualArr = {actual.getNit(), actual.getNombre(), actual.getDireccion(), actual.getEmail(), actual.getTelefono()};
                data[i] = clienteActualArr;
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
