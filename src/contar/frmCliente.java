/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar;

import BDO.BDO_Cliente;
import DAO.DAO_Cliente;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.desktoppane.WebInternalFrame;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebTextField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.DefaultListModel;
import javax.swing.JTextField;
/**
 *
 * @author Chaz
 */
public class FrmCliente extends WebInternalFrame {

    private WebPanel pnlNuevoCliente;
    private WebPanel pnlConsultaCliente;
    private WebTabbedPane pnlPestañasCliente;
    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    private WebComboBox cmbClientes;
    private WebTable tblClientes;
    private DAO_Cliente connCliente;
    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
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
    private DefaultListModel modelClientes;
    public FrmCliente() {
        super("Módulo de clientes", true, true, true, true);
        createUI();
    }

    private void createUI() {
        pnlNuevoCliente = new WebPanel();
        pnlConsultaCliente = new WebPanel();
        pnlPestañasCliente = new WebTabbedPane();
        agregarComponentenesNuevoCliente();
        agregarComponentenesConsultaCliente();
        agregarPestañasCliente();
        add(pnlPestañasCliente);
        setFrameIcon(loadIcon("users_4.png"));
        pack();
    }

    private void agregarPestañasCliente() {

        pnlPestañasCliente.addTab("Mis clientes", loadIcon("user_edit.png"), pnlConsultaCliente);
        pnlPestañasCliente.addTab("Agregar Cliente", loadIcon("user_add.png"), pnlNuevoCliente);
    }

    private void agregarComponentenesNuevoCliente() {
        pnlNuevoCliente.setLayout(new BorderLayout());
        
    }

    private void agregarComponentenesConsultaCliente() {
        pnlConsultaCliente.setLayout(new BorderLayout());
        pnlConsultaCliente.add(agregarPanelBusqueda(), BorderLayout.NORTH);
        pnlConsultaCliente.add(agregarTablaClientes(), BorderLayout.CENTER);
        pnlConsultaCliente.setBorder(new EmptyBorder((int)(screenSize.height*0.025),(int)(screenSize.width*0.025),(int)(screenSize.height*0.025),(int)(screenSize.width*0.025)));
        
    }

    private Component agregarPanelBusqueda() {
        WebPanel pnlSuperiorConsulta = new WebPanel();
        pnlSuperiorConsulta.setLayout(new FlowLayout());
        pnlSuperiorConsulta.add(new WebLabel(loadIcon("user_green.png")));
        pnlSuperiorConsulta.add(new WebLabel("Buscar cliente:"));
        pnlSuperiorConsulta.add(agregarComboBoxClientes());
        return pnlSuperiorConsulta;
    }

    private Component agregarComboBoxClientes() {
        cmbClientes = new WebComboBox();
        cmbClientes.setEditable(true);
        cmbClientes.setEditorColumns(14);
        cmbClientes.setToolTipText("Ingrese NIT o apellido");
        
        connCliente = new DAO_Cliente();
        
        cmbClientes.getEditor().getEditorComponent().addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String buscado = ((JTextField)cmbClientes.getEditor().getEditorComponent()).getText().trim();
                ArrayList<BDO_Cliente> clientesEncontrados;
                if(!buscado.isEmpty()){
                    clientesEncontrados = connCliente.busquedaNitApellido(buscado);
                }else{
                    clientesEncontrados = connCliente.todosClientes();
                }
                cmbClientes.removeAllItems();
                for(int i = 0; i <clientesEncontrados.size(); i++){
                    cmbClientes.addItem(clientesEncontrados.get(i).basicsToString());
                }
                cmbClientes.setPopupVisible(true);
                cmbClientes.setSelectedItem(buscado);
            }
            
        });
        return cmbClientes;
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
        tblClientes.setPreferredScrollableViewportSize(new Dimension(tblClientes.getPreferredSize().width, (int)(screenSize.height - screenSize.height*0.55)));
       
        WebScrollPane scroll = new WebScrollPane(tblClientes);
        return scroll;
    }

    public class ClientesTableModel extends AbstractTableModel {

        private String[] columnNames = {"NIT", "Nombre completo", "Dirección", "Nombre facturar", "Telefono"};
        private Object[][] data;
        public final Object[] longValues = {"String", "String", "String", "String", "String"};

        public void setClientes(ArrayList<BDO_Cliente> clientes) {
            data = new Object[clientes.size()][];

            for (int i = 0; i < clientes.size(); i++) {
                BDO_Cliente actual = clientes.get(i);
                Object[] clienteActualArr = {actual.getNit(), actual.getNombre(), actual.getDireccion(), actual.getNombre_factura(), actual.getTelefono()};
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
