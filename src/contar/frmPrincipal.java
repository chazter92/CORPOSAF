/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar;

import contar.clientes.FrmCliente;
import com.alee.extended.layout.ToolbarLayout;
import com.alee.extended.painter.TexturePainter;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.statusbar.WebStatusLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.desktoppane.WebDesktopPane;
import com.alee.laf.desktoppane.WebInternalFrame;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import contar.clientes.FrmAtributoCliente;
import contar.clientes.FrmEstadoCliente;
import contar.clientes.FrmPrecioCliente;
import contar.empresa.FrmImpuesto;
import contar.productos.FrmCategoriaProducto;
import contar.productos.FrmAtributoProducto;
import contar.productos.FrmEstadoProducto;
import contar.productos.FrmProducto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;


/**
 *
 * @author Chaz
 */
public class FrmPrincipal extends WebFrame {
    
    private WebDesktopPane desktopPane;
    private WebPanel contentPane;    
    private WebStatusBar statusBar;
    private WebTabbedPane pnlPestañas;
    private WebDesktopPane facturasDesktop;
    private WebDesktopPane clientesDesktop;
    private WebDesktopPane productosDesktop;
    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon> ();
    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
    public FrmPrincipal(){
        super();
        Catalogos.cargarCatologos();
        createUI();
        show();
    }
    
    private void createUI(){
        desktopPane = new WebDesktopPane();
        contentPane = new WebPanel(); 
        statusBar = new WebStatusBar ();
        pnlPestañas = new WebTabbedPane ();
        facturasDesktop = new WebDesktopPane ();
        clientesDesktop = new WebDesktopPane ();        
        productosDesktop = new WebDesktopPane ();
        desktopPane.setOpaque ( false );
        facturasDesktop.setOpaque( false );
        clientesDesktop.setOpaque( false );
        productosDesktop.setOpaque( false ); 
        //createSimpleFrame("Facturas", "Agregar facturas",25,desktopPane, Color.WHITE);
        //createSimpleFrame("Clientes", "Agregar clientes",125,desktopPane, Color.WHITE);
        //createSimpleFrame("Proveedores", "Agregar proveedores",225,desktopPane, Color.WHITE);
        createSimpleFrame("Nueva factura", "Crear nueva factura", 25, facturasDesktop, Color.BLACK);
        createSimpleFrame("Imprimir factura", "Imprimir factura existente", 125, facturasDesktop, Color.BLACK);
        createSimpleFrame("Visualizar factura", "Ver factura existente o anularla", 225, facturasDesktop, Color.BLACK);
        createSimpleFrame("Libro de ventas", "Ver reporte de facturación", 325, facturasDesktop, Color.BLACK);
        createSimpleFrame("Nuevo cliente", "Agregar un cliente", 25, clientesDesktop, Color.BLACK);
        createSimpleFrame("Modificar cliente", "Modificar un cliente o dar de baja", 125, clientesDesktop, Color.BLACK);
        createSimpleFrame("Ver cliente", "Ver los datos de un cliente", 225, clientesDesktop, Color.BLACK);
        createSimpleFrame("Nuevo producto", "Agregar un producto", 25, productosDesktop, Color.BLACK);
        createSimpleFrame("Modificar producto", "Modificar un producto o dar de baja", 125, productosDesktop, Color.BLACK);
        createSimpleFrame("Precios de venta", "Agregar precios de venta a los productos", 225, productosDesktop, Color.BLACK);
        HotkeyManager.installShowAllHotkeysAction ( getRootPane (), Hotkey.F1 );
        crearModuloImpuestos();
        crearModuloClientes();
        crearModuloEstadoClientes();
        crearModuloPrecioClientes();
        crearModuloCategoriasProducto();
        crearModuloAtributosCliente();
        crearModuloAtributosProducto();
        crearModuloProductos();
        crearModuloEstadosProducto();
        contentPane.add(agregarBarraEstado(), BorderLayout.SOUTH);
        contentPane.add(agregarPestañas(), BorderLayout.CENTER);
        setSize(screenSize.width-100,screenSize.height-100);
        setLocationRelativeTo(null);
        setTitle ( "Contar" );
        setIconImages ( WebLookAndFeel.getImages () );
        setLayout ( new BorderLayout () );
        setContentPane(contentPane);
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
    }
    
    public WebStatusBar agregarBarraEstado(){
        statusBar = new WebStatusBar ();
        statusBar.add ( new WebStatusLabel ( "CONECTADO - Última sincronización 18/12/2014 15:23", loadIcon ( "info.png" ) ) );
        WebMemoryBar memoryBar = new WebMemoryBar ();
        memoryBar.setPreferredWidth ( memoryBar.getPreferredSize ().width + 20 );
        statusBar.add ( memoryBar, ToolbarLayout.END );
        return statusBar;        
    }
    
    public void cambiarBarraEstado(String texto){
        statusBar.removeAll();
        statusBar.add ( new WebStatusLabel ( texto, loadIcon ( "info.png" ) ) );
        WebMemoryBar memoryBar = new WebMemoryBar ();
        memoryBar.setPreferredWidth ( memoryBar.getPreferredSize ().width + 20 );
        statusBar.add ( memoryBar, ToolbarLayout.END );
        statusBar.repaint(); 
    }

    
    public ImageIcon loadIcon ( final String path )
    {
        return loadIcon ( getClass (), path );
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
    
    private void crearModuloEstadosProducto(){
        agregarBtnFrmEstadoProducto(225,desktopPane, Color.WHITE);
    }
    
    private void crearModuloAtributosProducto(){
        agregarBtnFrmAtributoProducto(25,desktopPane, Color.WHITE);
    }
    
    private void crearModuloClientes(){
        agregarBtnFrmCliente(25,desktopPane, Color.WHITE);
    }
    
    private void crearModuloAtributosCliente(){
        agregarBtnFrmAtributoCliente(425,desktopPane, Color.WHITE);
    }
    
    private void crearModuloPrecioClientes(){
        agregarBtnFrmPrecioCliente(125,desktopPane, Color.WHITE);
    }
    
    private void crearModuloEstadoClientes(){
        agregarBtnFrmEstadoCliente(225,desktopPane, Color.WHITE);
    }
    
    private void crearModuloCategoriasProducto(){
        agregarBtnFrmCategoriaProducto(325,desktopPane, Color.WHITE);
    }
    
    private void crearModuloImpuestos(){
        agregarBtnFrmImpuesto(125,desktopPane, Color.WHITE);
    }
    
    private void crearModuloProductos(){
        agregarBtnFrmProducto(325,desktopPane, Color.WHITE);
    }
    
    private void agregarBtnFrmEstadoProducto(int posY, final WebDesktopPane desktopPane, Color colorFuente){
        final FrmEstadoProducto frmEstadoProducto = new FrmEstadoProducto();
        final WebButton btnFrmEstadoProducto = new WebButton ( "Estados de producto", loadIcon ( "account_menu.png" ) );
        TooltipManager.setTooltip(btnFrmEstadoProducto, "Estados de producto", TooltipWay.down);
        btnFrmEstadoProducto.setForeground(colorFuente);
        btnFrmEstadoProducto.setRolloverDecoratedOnly ( true );
        btnFrmEstadoProducto.setHorizontalTextPosition ( WebButton.CENTER );
        btnFrmEstadoProducto.setVerticalTextPosition ( WebButton.BOTTOM );
        btnFrmEstadoProducto.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( btnFrmEstadoProducto.getClientProperty ( DesktopPaneIconMoveAdapter.DRAGGED_MARK ) != null )
                {
                    return;
                }
                if ( frmEstadoProducto.isClosed () )
                {
                    if ( frmEstadoProducto.getParent () == null )
                    {
                        desktopPane.add ( frmEstadoProducto );
                    }
                    frmEstadoProducto.open ();
                    frmEstadoProducto.pack();
                    frmEstadoProducto.setIcon ( false );
                }
                else
                {
                    frmEstadoProducto.setIcon ( !frmEstadoProducto.isIcon () );
                }
            }
        } );
        DesktopPaneIconMoveAdapter moveAdapter1 = new DesktopPaneIconMoveAdapter ();
        btnFrmEstadoProducto.addMouseListener ( moveAdapter1 );
        btnFrmEstadoProducto.addMouseMotionListener ( moveAdapter1 );
        btnFrmEstadoProducto.setBounds ( 95, posY, 120, 75 );
        desktopPane.add(btnFrmEstadoProducto);
        //frmCliente.setBounds(25,15,(int)(screenSize.width-screenSize.width*0.25), (int)(screenSize.height - screenSize.height*0.3));
        frmEstadoProducto.setLocation(20,20);
        frmEstadoProducto.pack();
        frmEstadoProducto.close();
    }
    
    private void agregarBtnFrmProducto(int posY, final WebDesktopPane desktopPane, Color colorFuente){
        final FrmProducto frmProducto = new FrmProducto();
        final WebButton btnFrmProducto = new WebButton ( "Productos", loadIcon ( "package.png" ) );
        TooltipManager.setTooltip(btnFrmProducto, "Productos", TooltipWay.down);
        btnFrmProducto.setForeground(colorFuente);
        btnFrmProducto.setRolloverDecoratedOnly ( true );
        btnFrmProducto.setHorizontalTextPosition ( WebButton.CENTER );
        btnFrmProducto.setVerticalTextPosition ( WebButton.BOTTOM );
        btnFrmProducto.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( btnFrmProducto.getClientProperty ( DesktopPaneIconMoveAdapter.DRAGGED_MARK ) != null )
                {
                    return;
                }
                if ( frmProducto.isClosed () )
                {
                    if ( frmProducto.getParent () == null )
                    {
                        desktopPane.add ( frmProducto );
                    }
                    frmProducto.open ();
                    frmProducto.pack();
                    frmProducto.setIcon ( false );
                }
                else
                {
                    frmProducto.setIcon ( !frmProducto.isIcon () );
                }
            }
        } );
        DesktopPaneIconMoveAdapter moveAdapter1 = new DesktopPaneIconMoveAdapter ();
        btnFrmProducto.addMouseListener ( moveAdapter1 );
        btnFrmProducto.addMouseMotionListener ( moveAdapter1 );
        btnFrmProducto.setBounds ( 95, posY, 120, 75 );
        desktopPane.add(btnFrmProducto);
        //frmCliente.setBounds(25,15,(int)(screenSize.width-screenSize.width*0.25), (int)(screenSize.height - screenSize.height*0.3));
        frmProducto.setLocation(20,20);
        frmProducto.pack();
        frmProducto.close();
    }
    
    private void agregarBtnFrmImpuesto(int posY, final WebDesktopPane desktopPane, Color colorFuente){
        final FrmImpuesto frmImpuesto = new FrmImpuesto();
        final WebButton btnFrmImpuesto = new WebButton ( "Impuestos", loadIcon ( "impuesto.png" ) );
        TooltipManager.setTooltip(btnFrmImpuesto, "Impuestos", TooltipWay.down);
        btnFrmImpuesto.setForeground(colorFuente);
        btnFrmImpuesto.setRolloverDecoratedOnly ( true );
        btnFrmImpuesto.setHorizontalTextPosition ( WebButton.CENTER );
        btnFrmImpuesto.setVerticalTextPosition ( WebButton.BOTTOM );
        btnFrmImpuesto.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( btnFrmImpuesto.getClientProperty ( DesktopPaneIconMoveAdapter.DRAGGED_MARK ) != null )
                {
                    return;
                }
                if ( frmImpuesto.isClosed () )
                {
                    if ( frmImpuesto.getParent () == null )
                    {
                        desktopPane.add ( frmImpuesto );
                    }
                    frmImpuesto.open ();
                    frmImpuesto.pack();
                    frmImpuesto.setIcon ( false );
                }
                else
                {
                    frmImpuesto.setIcon ( !frmImpuesto.isIcon () );
                }
            }
        } );
        DesktopPaneIconMoveAdapter moveAdapter1 = new DesktopPaneIconMoveAdapter ();
        btnFrmImpuesto.addMouseListener ( moveAdapter1 );
        btnFrmImpuesto.addMouseMotionListener ( moveAdapter1 );
        btnFrmImpuesto.setBounds ( 95, posY, 120, 75 );
        desktopPane.add(btnFrmImpuesto);
        //frmCliente.setBounds(25,15,(int)(screenSize.width-screenSize.width*0.25), (int)(screenSize.height - screenSize.height*0.3));
        frmImpuesto.setLocation(20,20);
        frmImpuesto.pack();
        frmImpuesto.close();
    }
    
    private void agregarBtnFrmCategoriaProducto(int posY, final WebDesktopPane desktopPane, Color colorFuente){
        final FrmCategoriaProducto frmCategoriaProducto = new FrmCategoriaProducto();
        final WebButton btnFrmCategoriaProducto = new WebButton ( "Categorias de productos", loadIcon ( "categories.png" ) );
        TooltipManager.setTooltip(btnFrmCategoriaProducto, "Categorias de productos", TooltipWay.down);
        btnFrmCategoriaProducto.setForeground(colorFuente);
        btnFrmCategoriaProducto.setRolloverDecoratedOnly ( true );
        btnFrmCategoriaProducto.setHorizontalTextPosition ( WebButton.CENTER );
        btnFrmCategoriaProducto.setVerticalTextPosition ( WebButton.BOTTOM );
        btnFrmCategoriaProducto.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( btnFrmCategoriaProducto.getClientProperty ( DesktopPaneIconMoveAdapter.DRAGGED_MARK ) != null )
                {
                    return;
                }
                if ( frmCategoriaProducto.isClosed () )
                {
                    if ( frmCategoriaProducto.getParent () == null )
                    {
                        desktopPane.add ( frmCategoriaProducto );
                    }
                    frmCategoriaProducto.open ();
                    frmCategoriaProducto.pack();
                    frmCategoriaProducto.setIcon ( false );
                }
                else
                {
                    frmCategoriaProducto.setIcon ( !frmCategoriaProducto.isIcon () );
                }
            }
        } );
        DesktopPaneIconMoveAdapter moveAdapter1 = new DesktopPaneIconMoveAdapter ();
        btnFrmCategoriaProducto.addMouseListener ( moveAdapter1 );
        btnFrmCategoriaProducto.addMouseMotionListener ( moveAdapter1 );
        btnFrmCategoriaProducto.setBounds ( 5, posY, 120, 75 );
        desktopPane.add(btnFrmCategoriaProducto);
        //frmCliente.setBounds(25,15,(int)(screenSize.width-screenSize.width*0.25), (int)(screenSize.height - screenSize.height*0.3));
        frmCategoriaProducto.setLocation(20,20);
        frmCategoriaProducto.pack();
        frmCategoriaProducto.close();
    }
    
    private void agregarBtnFrmAtributoCliente(int posY, final WebDesktopPane desktopPane, Color colorFuente){
        final FrmAtributoCliente frmAtributoCliente = new FrmAtributoCliente();
        final WebButton btnFrmAtributoCliente = new WebButton ( "Atributos de los clientes", loadIcon ( "categories.png" ) );
        TooltipManager.setTooltip(btnFrmAtributoCliente, "Atributos de clientes", TooltipWay.down);
        btnFrmAtributoCliente.setForeground(colorFuente);
        btnFrmAtributoCliente.setRolloverDecoratedOnly ( true );
        btnFrmAtributoCliente.setHorizontalTextPosition ( WebButton.CENTER );
        btnFrmAtributoCliente.setVerticalTextPosition ( WebButton.BOTTOM );
        btnFrmAtributoCliente.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( btnFrmAtributoCliente.getClientProperty ( DesktopPaneIconMoveAdapter.DRAGGED_MARK ) != null )
                {
                    return;
                }
                if ( frmAtributoCliente.isClosed () )
                {
                    if ( frmAtributoCliente.getParent () == null )
                    {
                        desktopPane.add ( frmAtributoCliente );
                    }
                    frmAtributoCliente.open ();
                    frmAtributoCliente.pack();
                    frmAtributoCliente.setIcon ( false );
                }
                else
                {
                    frmAtributoCliente.setIcon ( !frmAtributoCliente.isIcon () );
                }
            }
        } );
        DesktopPaneIconMoveAdapter moveAdapter1 = new DesktopPaneIconMoveAdapter ();
        btnFrmAtributoCliente.addMouseListener ( moveAdapter1 );
        btnFrmAtributoCliente.addMouseMotionListener ( moveAdapter1 );
        btnFrmAtributoCliente.setBounds ( 5, posY, 120, 75 );
        desktopPane.add(btnFrmAtributoCliente);
        //frmCliente.setBounds(25,15,(int)(screenSize.width-screenSize.width*0.25), (int)(screenSize.height - screenSize.height*0.3));
        frmAtributoCliente.setLocation(20,20);
        frmAtributoCliente.pack();
        frmAtributoCliente.close();
    }
    
    private void agregarBtnFrmPrecioCliente(int posY, final WebDesktopPane desktopPane, Color colorFuente){
        final FrmPrecioCliente frmPrecioCliente = new FrmPrecioCliente();
        final WebButton btnFrmPreciosCliente = new WebButton ( "Precios de cliente", loadIcon ( "account_balances.png" ) );
        TooltipManager.setTooltip(btnFrmPreciosCliente, "Precios de cliente", TooltipWay.down);
        btnFrmPreciosCliente.setForeground(colorFuente);
        btnFrmPreciosCliente.setRolloverDecoratedOnly ( true );
        btnFrmPreciosCliente.setHorizontalTextPosition ( WebButton.CENTER );
        btnFrmPreciosCliente.setVerticalTextPosition ( WebButton.BOTTOM );
        btnFrmPreciosCliente.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( btnFrmPreciosCliente.getClientProperty ( DesktopPaneIconMoveAdapter.DRAGGED_MARK ) != null )
                {
                    return;
                }
                if ( frmPrecioCliente.isClosed () )
                {
                    if ( frmPrecioCliente.getParent () == null )
                    {
                        desktopPane.add ( frmPrecioCliente );
                    }
                    frmPrecioCliente.open ();
                    frmPrecioCliente.pack();
                    frmPrecioCliente.setIcon ( false );
                }
                else
                {
                    frmPrecioCliente.setIcon ( !frmPrecioCliente.isIcon () );
                }
            }
        } );
        DesktopPaneIconMoveAdapter moveAdapter1 = new DesktopPaneIconMoveAdapter ();
        btnFrmPreciosCliente.addMouseListener ( moveAdapter1 );
        btnFrmPreciosCliente.addMouseMotionListener ( moveAdapter1 );
        btnFrmPreciosCliente.setBounds ( 5, posY, 120, 75 );
        desktopPane.add(btnFrmPreciosCliente);
        //frmCliente.setBounds(25,15,(int)(screenSize.width-screenSize.width*0.25), (int)(screenSize.height - screenSize.height*0.3));
        frmPrecioCliente.setLocation(20,20);
        frmPrecioCliente.pack();
        frmPrecioCliente.close();
    }
    
    private void agregarBtnFrmEstadoCliente(int posY, final WebDesktopPane desktopPane, Color colorFuente){
        final FrmEstadoCliente frmEstadoCliente = new FrmEstadoCliente();
        final WebButton btnFrmEstadosCliente = new WebButton ( "Estados de cliente", loadIcon ( "account_menu.png" ) );
        TooltipManager.setTooltip(btnFrmEstadosCliente, "Estados de cliente", TooltipWay.down);
        btnFrmEstadosCliente.setForeground(colorFuente);
        btnFrmEstadosCliente.setRolloverDecoratedOnly ( true );
        btnFrmEstadosCliente.setHorizontalTextPosition ( WebButton.CENTER );
        btnFrmEstadosCliente.setVerticalTextPosition ( WebButton.BOTTOM );
        btnFrmEstadosCliente.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( btnFrmEstadosCliente.getClientProperty ( DesktopPaneIconMoveAdapter.DRAGGED_MARK ) != null )
                {
                    return;
                }
                if ( frmEstadoCliente.isClosed () )
                {
                    if ( frmEstadoCliente.getParent () == null )
                    {
                        desktopPane.add ( frmEstadoCliente );
                    }
                    frmEstadoCliente.open ();
                    frmEstadoCliente.pack();
                    frmEstadoCliente.setIcon ( false );
                }
                else
                {
                    frmEstadoCliente.setIcon ( !frmEstadoCliente.isIcon () );
                }
            }
        } );
        DesktopPaneIconMoveAdapter moveAdapter1 = new DesktopPaneIconMoveAdapter ();
        btnFrmEstadosCliente.addMouseListener ( moveAdapter1 );
        btnFrmEstadosCliente.addMouseMotionListener ( moveAdapter1 );
        btnFrmEstadosCliente.setBounds ( 5, posY, 120, 75 );
        desktopPane.add(btnFrmEstadosCliente);
        //frmCliente.setBounds(25,15,(int)(screenSize.width-screenSize.width*0.25), (int)(screenSize.height - screenSize.height*0.3));
        frmEstadoCliente.setLocation(20,20);
        frmEstadoCliente.pack();
        frmEstadoCliente.close();
    }
    
    private void agregarBtnFrmCliente(int posY, final WebDesktopPane desktopPane, Color colorFuente){
        final FrmCliente frmCliente = new FrmCliente();
        final WebButton btnFrmCliente = new WebButton ( "Clientes", loadIcon ( "users_4.png" ) );
        TooltipManager.setTooltip(btnFrmCliente, "Módulo de cliente", TooltipWay.down);
        btnFrmCliente.setForeground(colorFuente);
        btnFrmCliente.setRolloverDecoratedOnly ( true );
        btnFrmCliente.setHorizontalTextPosition ( WebButton.CENTER );
        btnFrmCliente.setVerticalTextPosition ( WebButton.BOTTOM );
        btnFrmCliente.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( btnFrmCliente.getClientProperty ( DesktopPaneIconMoveAdapter.DRAGGED_MARK ) != null )
                {
                    return;
                }
                if ( frmCliente.isClosed () )
                {
                    if ( frmCliente.getParent () == null )
                    {
                        desktopPane.add ( frmCliente );
                    }
                    frmCliente.open ();
                    frmCliente.pack();
                    frmCliente.setIcon ( false );
                }
                else
                {
                    frmCliente.setIcon ( !frmCliente.isIcon () );
                }
            }
        } );
        DesktopPaneIconMoveAdapter moveAdapter1 = new DesktopPaneIconMoveAdapter ();
        btnFrmCliente.addMouseListener ( moveAdapter1 );
        btnFrmCliente.addMouseMotionListener ( moveAdapter1 );
        btnFrmCliente.setBounds ( 5, posY, 120, 75 );
        desktopPane.add(btnFrmCliente);
        //frmCliente.setBounds(25,15,(int)(screenSize.width-screenSize.width*0.25), (int)(screenSize.height - screenSize.height*0.3));
        frmCliente.setLocation(20,20);
        frmCliente.pack();
        frmCliente.close();
    }
    
    private void agregarBtnFrmAtributoProducto(int posY, final WebDesktopPane desktopPane, Color colorFuente){
        final FrmAtributoProducto frmAtributoProducto = new FrmAtributoProducto();
        final WebButton btnFrmAtributoProducto = new WebButton ( "Atributos de los productos", loadIcon ( "categories.png" ) );
        TooltipManager.setTooltip(btnFrmAtributoProducto, "Atributos de productos", TooltipWay.down);
        btnFrmAtributoProducto.setForeground(colorFuente);
        btnFrmAtributoProducto.setRolloverDecoratedOnly ( true );
        btnFrmAtributoProducto.setHorizontalTextPosition ( WebButton.CENTER );
        btnFrmAtributoProducto.setVerticalTextPosition ( WebButton.BOTTOM );
        btnFrmAtributoProducto.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( btnFrmAtributoProducto.getClientProperty ( DesktopPaneIconMoveAdapter.DRAGGED_MARK ) != null )
                {
                    return;
                }
                if ( frmAtributoProducto.isClosed () )
                {
                    if ( frmAtributoProducto.getParent () == null )
                    {
                        desktopPane.add ( frmAtributoProducto );
                    }
                    frmAtributoProducto.open ();
                    frmAtributoProducto.pack();
                    frmAtributoProducto.setIcon ( false );
                }
                else
                {
                    frmAtributoProducto.setIcon ( !frmAtributoProducto.isIcon () );
                }
            }
        } );
        DesktopPaneIconMoveAdapter moveAdapter1 = new DesktopPaneIconMoveAdapter ();
        btnFrmAtributoProducto.addMouseListener ( moveAdapter1 );
        btnFrmAtributoProducto.addMouseMotionListener ( moveAdapter1 );
        btnFrmAtributoProducto.setBounds ( 95, posY, 120, 75 );
        desktopPane.add(btnFrmAtributoProducto);
        frmAtributoProducto.setLocation(20,20);
        frmAtributoProducto.pack();
        frmAtributoProducto.close();
    }
    
    private void createSimpleFrame ( String titulo, final String mensaje, int posY, final WebDesktopPane desktopPane, Color colorFuente )
    {
        final WebInternalFrame internalFrame = new WebInternalFrame ( titulo, true, true, true, true );
        internalFrame.setFrameIcon ( loadIcon ( "frame.png" ) );

        
        final WebButton internalFrameIcon = new WebButton ( titulo, loadIcon ( "webframe.png" ) );
        internalFrameIcon.setForeground(colorFuente);
        internalFrameIcon.setRolloverDecoratedOnly ( true );
        internalFrameIcon.setHorizontalTextPosition ( WebButton.CENTER );
        internalFrameIcon.setVerticalTextPosition ( WebButton.BOTTOM );
        internalFrameIcon.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( internalFrameIcon.getClientProperty ( DesktopPaneIconMoveAdapter.DRAGGED_MARK ) != null )
                {
                    return;
                }
                if ( internalFrame.isClosed () )
                {
                    if ( internalFrame.getParent () == null )
                    {
                        desktopPane.add ( internalFrame );
                    }
                    internalFrame.open ();
                    internalFrame.setIcon ( false );
                }
                else
                {
                    internalFrame.setIcon ( !internalFrame.isIcon () );
                }
            }
        } );
        DesktopPaneIconMoveAdapter ma1 = new DesktopPaneIconMoveAdapter ();
        internalFrameIcon.addMouseListener ( ma1 );
        internalFrameIcon.addMouseMotionListener ( ma1 );
        internalFrameIcon.setBounds ( 7, posY, 120, 75 );
        desktopPane.add ( internalFrameIcon );
        WebTabbedPane pestañas = new  WebTabbedPane();
        WebPanel panel1 = new WebPanel();
        panel1.add(new WebButton ( titulo, loadIcon ( "webframe.png" ) ) );
        pestañas.addTab("prueba", panel1);
        internalFrame.add(pestañas);
        internalFrame.setBounds ( 25 + 100 + 50, 50, 300, 300 );
        internalFrame.close ();
    }
    
    public WebTabbedPane agregarPestañas(){
        
        
        TexturePainter tp1 = new TexturePainter ( loadIcon ( "bn11.jpg" ) );
        TexturePainter tp2 = new TexturePainter ( loadIcon ( "bn5.jpg" ) );
        TexturePainter tp3 = new TexturePainter ( loadIcon ( "bn10.jpg" ) );
        TexturePainter tp4 = new TexturePainter ( loadIcon ( "bn9.jpg" ) );
        
        pnlPestañas.setPreferredSize ( new Dimension ( 800, 500 ) );
        
        pnlPestañas.addTab ( "Favoritos", desktopPane);
        pnlPestañas.setBackgroundPainterAt ( pnlPestañas.getTabCount () - 1, tp1 );
        pnlPestañas.setSelectedForegroundAt ( pnlPestañas.getTabCount () - 1, Color.WHITE );

        pnlPestañas.addTab ( "Facturas", facturasDesktop );
        pnlPestañas.setBackgroundPainterAt ( pnlPestañas.getTabCount () - 1, tp2 );
        pnlPestañas.setSelectedForegroundAt ( pnlPestañas.getTabCount () - 1, Color.WHITE );

        pnlPestañas.addTab ( "Clientes", clientesDesktop );
        pnlPestañas.setBackgroundPainterAt ( pnlPestañas.getTabCount () - 1, tp3 );
        pnlPestañas.setSelectedForegroundAt ( pnlPestañas.getTabCount () - 1, Color.WHITE );

        pnlPestañas.addTab ( "Productos", productosDesktop);
        pnlPestañas.setBackgroundPainterAt ( pnlPestañas.getTabCount () - 1, tp4 );
        pnlPestañas.setSelectedForegroundAt ( pnlPestañas.getTabCount () - 1, Color.BLACK );

        return pnlPestañas;
    }

}
