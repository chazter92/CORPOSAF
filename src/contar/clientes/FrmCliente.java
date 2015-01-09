/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.clientes;

import BDO.BDO_Cliente;
import DAO.DAO_Cliente;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.desktoppane.WebInternalFrame;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebTextField;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Chaz
 */
public class FrmCliente extends WebInternalFrame {

    private WebPanel pnlDatosCliente;
    private WebPanel pnlConsultaCliente;
    private WebTabbedPane pnlPestañasCliente;
    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    private CmbBuscarCliente cmbConsultaCliente, cmbDatosCliente;
    private WebTable tblClientes;
    private DAO_Cliente connCliente;
    private WebButton btnEditarCliente, btnNuevoCliente, btnVerCliente, btnGuardarCliente, btnCancelar;
    private WebTextField txtNit, txtNombre, txtDireccion, txtEmail, txtTelefono, txtNombreFacturar;
    private WebComboBox cmbTipoCliente, cmbEstadoCliente;
    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    final int columnasDatos = 25;

    public ImageIcon loadIcon(final String path) {
        return loadIcon(getClass(), path);
    }

    public ImageIcon loadIcon(final Class nearClass, final String path) {
        final String key = nearClass.getCanonicalName() + ":" + path;
        if (!iconsCache.containsKey(key)) {
            iconsCache.put(key, new ImageIcon(nearClass.getResource("icons/" + path)));
        }
        return iconsCache.get(key);
    }


    public FrmCliente() {
        super("Módulo de clientes", true, true, true, true);
        createUI();
    }

    private void createUI() {
        pnlDatosCliente = new WebPanel();
        pnlConsultaCliente = new WebPanel();
        pnlPestañasCliente = new WebTabbedPane();
        agregarComponentenesDatosCliente();
        agregarComponentenesConsultaCliente();
        agregarPestañasCliente();
        add(pnlPestañasCliente);
        setFrameIcon(loadIcon("users_4.png"));
        pack();
    }

    private void agregarPestañasCliente() {
        pnlPestañasCliente.addTab("Mis clientes", loadIcon("users_edit.png"), pnlConsultaCliente);
        pnlPestañasCliente.addTab("Datos del cliente", loadIcon("folder_user.png"), pnlDatosCliente);
    }

    private void agregarComponentenesDatosCliente() {
        pnlDatosCliente.setLayout(new BorderLayout());
        pnlDatosCliente.add(agregarPanelSuperiorDatosCliente(),BorderLayout.NORTH);
        pnlDatosCliente.add(agregarDatosIndividualesCliente(),BorderLayout.CENTER);
        pnlDatosCliente.add(agregarPanelBotonesDatos(),BorderLayout.SOUTH);
        pnlDatosCliente.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
    }
    
    private Component agregarPanelBotonesDatos(){
        
        WebPanel pnlBotonesDatosIndividuales = new WebPanel();
        pnlBotonesDatosIndividuales.setLayout(new FlowLayout());
        btnGuardarCliente = new WebButton("Guardar",loadIcon("save_new.png"));
        btnCancelar = new WebButton("Cancelar", loadIcon("cancel.png"));
        pnlBotonesDatosIndividuales.add(btnGuardarCliente);
        pnlBotonesDatosIndividuales.add(btnCancelar);
        return pnlBotonesDatosIndividuales;
    }
    
    private Component agregarDatosIndividualesCliente(){
        WebPanel pnlDatosIndividuales = new WebPanel();
        pnlDatosIndividuales.setLayout(new FlowLayout());
        
        WebPanel pnlIzquierdo = new WebPanel();
        pnlIzquierdo.setLayout(new BoxLayout(pnlIzquierdo, BoxLayout.Y_AXIS));
        WebPanel pnlDerecho = new WebPanel();
        pnlDerecho.setLayout(new BoxLayout(pnlDerecho, BoxLayout.Y_AXIS));
        
        
        txtNit = new WebTextField();
        txtNit.setColumns(columnasDatos);
        txtNit.setInputPrompt("NIT del cliente");
        WebPanel pnlNit = new WebPanel();
        pnlNit.setLayout(new FlowLayout());
        pnlNit.add(new WebLabel(loadIcon("nit.png")));
        pnlNit.add(txtNit);
        TooltipManager.setTooltip ( pnlNit, "Ejemplo: 345678-9", TooltipWay.up);
        pnlIzquierdo.add(pnlNit);
        
        
        txtNombre = new WebTextField();
        txtNombre.setColumns(columnasDatos);
        txtNombre.setInputPrompt("Nombre completo del cliente");
        WebPanel pnlNombre = new WebPanel();
        pnlNombre.setLayout(new FlowLayout());
        pnlNombre.add(new WebLabel(loadIcon("user_orange.png")));
        pnlNombre.add(txtNombre);
        TooltipManager.setTooltip ( pnlNombre, "Ingrese nombre del cliente", TooltipWay.up);
        pnlDerecho.add(pnlNombre);
        
        txtNombreFacturar = new WebTextField();
        txtNombreFacturar.setColumns(columnasDatos);
        txtNombreFacturar.setInputPrompt("Nombre que aparecerá en factura");
        WebPanel pnlNombreFacturar = new WebPanel();
        pnlNombreFacturar.setLayout(new FlowLayout());
        pnlNombreFacturar.add(new WebLabel(loadIcon("user_gray.png")));
        pnlNombreFacturar.add(txtNombreFacturar);
        TooltipManager.setTooltip ( pnlNombreFacturar, "Ingrese nombre a facturar", TooltipWay.up);
        pnlIzquierdo.add(pnlNombreFacturar);
        
        txtDireccion = new WebTextField();
        txtDireccion.setColumns(columnasDatos);
        txtDireccion.setInputPrompt("Dirección domiciliar");
        WebPanel pnlDireccion = new WebPanel();
        pnlDireccion.setLayout(new FlowLayout());
        pnlDireccion.add(new WebLabel(loadIcon("mail_box.png")));
        pnlDireccion.add(txtDireccion);
        TooltipManager.setTooltip ( pnlDireccion, "Ingrese dirección del cliente", TooltipWay.up);
        pnlDerecho.add(pnlDireccion);
        
        txtEmail = new WebTextField();
        txtEmail.setColumns(columnasDatos);
        txtEmail.setInputPrompt("Correo Electrónico");
        WebPanel pnlEmail = new WebPanel();
        pnlEmail.setLayout(new FlowLayout());
        pnlEmail.add(new WebLabel(loadIcon("email.png")));
        pnlEmail.add(txtEmail);
        TooltipManager.setTooltip ( pnlEmail, "Ejemplo: jLopez@gmail.com", TooltipWay.up);
        pnlIzquierdo.add(pnlEmail);
        
        txtTelefono = new WebTextField();
        txtTelefono.setColumns(columnasDatos);
        txtTelefono.setInputPrompt("Número de teléfono");
        WebPanel pnlTelefono = new WebPanel();
        pnlTelefono.setLayout(new FlowLayout());
        pnlTelefono.add(new WebLabel(loadIcon("phone.png")));
        pnlTelefono.add(txtTelefono);
        TooltipManager.setTooltip ( pnlTelefono, "Ejemplo: 23456789", TooltipWay.up);
        pnlDerecho.add(pnlTelefono);
        
        cmbTipoCliente = new WebComboBox();
        cmbTipoCliente.setEditable(false);
        WebPanel pnlTipoCliente = new WebPanel();
        pnlTipoCliente.setLayout(new FlowLayout());
        pnlTipoCliente.add(new WebLabel(loadIcon("users_5.png")));
        pnlTipoCliente.add(cmbTipoCliente);
        TooltipManager.setTooltip ( pnlTipoCliente, "Seleccione el descuento autorizado", TooltipWay.up);
        pnlIzquierdo.add(pnlTipoCliente);
        
        cmbEstadoCliente = new WebComboBox();
        cmbEstadoCliente.setEditable(false);
        WebPanel pnlEstadoCliente = new WebPanel();
        pnlEstadoCliente.setLayout(new FlowLayout());
        pnlEstadoCliente.add(new WebLabel(loadIcon("user_delete.png")));
        pnlEstadoCliente.add(cmbEstadoCliente);
        TooltipManager.setTooltip ( pnlEstadoCliente, "Seleccione el estado del cliente", TooltipWay.up);
        pnlDerecho.add(pnlEstadoCliente);
        
        pnlDatosIndividuales.add(pnlIzquierdo);
        pnlDatosIndividuales.add(pnlDerecho);
        WebScrollPane scroll = new WebScrollPane(pnlDatosIndividuales);
        scroll.setBorder(null);
        return scroll;
    }
    
    private Component agregarPanelSuperiorDatosCliente(){
        WebPanel pnlSuperiorDatosCliente = new WebPanel();
        pnlSuperiorDatosCliente.setLayout(new FlowLayout());
        pnlSuperiorDatosCliente.add(new WebLabel(loadIcon("user_green.png")));
        pnlSuperiorDatosCliente.add(new WebLabel("Buscar cliente:"));
        cmbDatosCliente = new CmbBuscarCliente();
        pnlSuperiorDatosCliente.add(cmbDatosCliente);
        btnEditarCliente = new WebButton("Editar cliente", loadIcon("user_edit.png"));
        btnNuevoCliente = new WebButton("Nuevo cliente", loadIcon("user_add.png"));
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
        pnlSuperiorConsulta.add(new WebLabel(loadIcon("user_green.png")));
        pnlSuperiorConsulta.add(new WebLabel("Buscar cliente:"));
        cmbConsultaCliente = new CmbBuscarCliente();
        pnlSuperiorConsulta.add(cmbConsultaCliente);
        btnVerCliente = new WebButton("Ver cliente");
        pnlSuperiorConsulta.add(btnVerCliente);
        return pnlSuperiorConsulta;
    }

    private Component agregarTablaClientes() {
        connCliente = new DAO_Cliente();
        ClientesTableModel clientesTableModel = new ClientesTableModel();
        clientesTableModel.setClientes(connCliente.todosClientes());

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

        WebScrollPane scroll = new WebScrollPane(tblClientes);
        return scroll;
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
