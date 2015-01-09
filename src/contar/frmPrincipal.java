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
        createUI();
        show();
    }
    
    
    public void createUI(){
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
        createSimpleFrame("Facturas", "Agregar facturas",25,desktopPane, Color.WHITE);
        //createSimpleFrame("Clientes", "Agregar clientes",125,desktopPane, Color.WHITE);
        createSimpleFrame("Proveedores", "Agregar proveedores",225,desktopPane, Color.WHITE);
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
        crearModuloClientes();
        
        
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

    public ImageIcon loadIcon ( final Class nearClass, final String path )
    {
        final String key = nearClass.getCanonicalName () + ":" + path;
        if ( !iconsCache.containsKey ( key ) )
        {
            iconsCache.put ( key, new ImageIcon ( nearClass.getResource ( "icons/" + path ) ) );
        }
        return iconsCache.get ( key );
    }
    
    private void crearModuloClientes(){
        agregarBtnFrmCliente(125,desktopPane, Color.WHITE);
    }
    
    private void agregarBtnFrmCliente(int posY, final WebDesktopPane desktopPane, Color colorFuente){
        final FrmCliente frmCliente = new FrmCliente();
        final WebButton btnFrmCliente = new WebButton ( "Clientes", loadIcon ( "users_4.png" ) );
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
        internalFrameIcon.setBounds ( 5, posY, 120, 75 );
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
