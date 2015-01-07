/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar;

import com.alee.extended.layout.ToolbarLayout;
import com.alee.extended.painter.TexturePainter;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.statusbar.WebStatusLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.desktoppane.WebDesktopPane;
import com.alee.laf.desktoppane.WebInternalFrame;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebRadioButtonMenuItem;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


/**
 *
 * @author Chaz
 */
public class frmPrincipal extends WebFrame {
    
    WebDesktopPane desktopPane = new WebDesktopPane ();
    WebPanel contentPane = new WebPanel();    
    WebMenuBar menu = new WebMenuBar();
    WebStatusBar statusBar = new WebStatusBar ();
    WebTabbedPane tabbedPane = new WebTabbedPane ();
    WebDesktopPane facturasDesktop = new WebDesktopPane ();
    WebDesktopPane clientesDesktop = new WebDesktopPane ();
    WebDesktopPane productosDesktop = new WebDesktopPane ();
    public frmPrincipal(){
        super();
        construirGUI();
        setSize(900,600);
        setLocationRelativeTo(null);
        setTitle ( "Contar" );
        setIconImages ( WebLookAndFeel.getImages () );
        setLayout ( new BorderLayout () );
        setContentPane(contentPane);
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        show();
    }
    
    
    public void construirGUI(){
        desktopPane.setOpaque ( false );
        facturasDesktop.setOpaque( false );
        clientesDesktop.setOpaque( false );
        productosDesktop.setOpaque( false ); 
        createSimpleFrame("Facturas", "Agregar facturas",25,desktopPane, Color.WHITE);
        createSimpleFrame("Clientes", "Agregar clientes",125,desktopPane, Color.WHITE);
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
        //setupMenuBar(menu);
        //contentPane.add(menu, BorderLayout.NORTH);
        contentPane.add(getBar(), BorderLayout.SOUTH);
        contentPane.add(pestañas(), BorderLayout.CENTER);
    }
    
    public WebStatusBar getBar(){
        statusBar = new WebStatusBar ();
        statusBar.add ( new WebStatusLabel ( "CONECTADO - Última sincronización 18/12/2014 15:23", loadIcon ( "info.png" ) ) );
        WebMemoryBar memoryBar = new WebMemoryBar ();
        memoryBar.setPreferredWidth ( memoryBar.getPreferredSize ().width + 20 );
        statusBar.add ( memoryBar, ToolbarLayout.END );
        return statusBar;        
    }
    
    public void setBarraInfo(String texto){
        statusBar.removeAll();
        statusBar.add ( new WebStatusLabel ( texto, loadIcon ( "info.png" ) ) );

        WebMemoryBar memoryBar = new WebMemoryBar ();
        memoryBar.setPreferredWidth ( memoryBar.getPreferredSize ().width + 20 );
        statusBar.add ( memoryBar, ToolbarLayout.END );
        statusBar.repaint(); 
    }

    
    public void setupMenuBar ( WebMenuBar menuBar )
    {
        menuBar.add ( new WebMenu ( "Archivo", loadIcon ( "menubar/file.png" ) )
        {
            {
                add ( new WebMenu ( "Nuevo", loadIcon ( "menubar/media.png" ) )
                {
                    {
                        add ( new WebMenuItem ( "Nueva factura", loadIcon ( "menubar/file_image.png" ) )
                        {
                            {
                                setAccelerator ( Hotkey.CTRL_N );
                            }
                        } );
                        add ( new WebMenuItem ( "Nuevo cliente", loadIcon ( "menubar/file_music.png" ) ) );
                        add ( new WebMenuItem ( "Nuevo producto", loadIcon ( "menubar/file_video.png" ) ) );
                        add ( new WebMenuItem ( "Nuevo documento", loadIcon ( "menubar/file_archive.png" ) ) );
                    }
                } );
                addSeparator ();
                add ( new WebMenuItem ( "Abrir", loadIcon ( "menubar/file_doc.png" ) ) );
                addSeparator ();
                add ( new WebMenuItem ( "Cerrar", loadIcon ( "menubar/file_exit.png" ) )
                {
                    {
                        setAccelerator ( Hotkey.ALT_F4 );
                    }
                } );
            }
        } );
        menuBar.add ( new WebMenu ( "Editar", loadIcon ( "menubar/edit.png" ) )
        {
            {
                add ( new WebMenuItem ( "Cortar", loadIcon ( "menubar/edit_cut.png" ) )
                {
                    {
                        setAccelerator ( Hotkey.CTRL_X );
                    }
                } );
                add ( new WebMenuItem ( "Copiar", loadIcon ( "menubar/edit_copy.png" ) )
                {
                    {
                        setAccelerator ( Hotkey.CTRL_C );
                    }
                } );
                add ( new WebMenuItem ( "Pegar", loadIcon ( "menubar/edit_paste.png" ) )
                {
                    {
                        setAccelerator ( Hotkey.CTRL_V );
                        setEnabled ( false );
                    }
                } );
            }
        } );
        menuBar.add ( new WebMenu ( "Documento", loadIcon ( "menubar/states.png" ) )
        {
            {
                final ButtonGroup buttonGroup = new ButtonGroup ();
                add ( new WebRadioButtonMenuItem ( "Serie A", loadIcon ( "menubar/radio1.png" ) )
                {
                    {
                        setAccelerator ( Hotkey.A );
                        setSelected ( true );
                        buttonGroup.add ( this );
                    }
                } );
                add ( new WebRadioButtonMenuItem ( "Serie B", loadIcon ( "menubar/radio2.png" ) )
                {
                    {
                        setAccelerator ( Hotkey.B );
                        buttonGroup.add ( this );
                    }
                } );
                add ( new WebRadioButtonMenuItem ( "Serie C", loadIcon ( "menubar/radio3.png" ) )
                {
                    {
                        setAccelerator ( Hotkey.C );
                        buttonGroup.add ( this );
                    }
                } );
            }
        } );
       
    }
    
    public ImageIcon loadIcon ( final String path )
    {
        return loadIcon ( getClass (), path );
    }

    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon> ();
    public ImageIcon loadIcon ( final Class nearClass, final String path )
    {
        final String key = nearClass.getCanonicalName () + ":" + path;
        if ( !iconsCache.containsKey ( key ) )
        {
            iconsCache.put ( key, new ImageIcon ( nearClass.getResource ( "icons/" + path ) ) );
        }
        return iconsCache.get ( key );
    }
    
    private void createSimpleFrame ( String titulo, final String mensaje, int posY, final WebDesktopPane desktopPane, Color colorFuente )
    {
        final WebInternalFrame internalFrame = new WebInternalFrame ( titulo, true, true, true, true );
        internalFrame.setFrameIcon ( loadIcon ( "frame.png" ) );

        JLabel label = new JLabel ( mensaje, JLabel.CENTER );
        label.setOpaque ( false );
        internalFrame.add ( label );

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
                    //setBarraInfo(mensaje);
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
        WebPanel nuevo = new WebPanel();
        //nuevo.add( new WebButton ( titulo, loadIcon ( "webframe.png" ) ));
        nuevo.add(pestañas());
        internalFrame.setContentPane(nuevo);
        internalFrame.setBounds ( 25 + 100 + 50, 50, 300, 300 );
        internalFrame.close ();
    }
    
    public WebTabbedPane pestañas(){
        
        
        TexturePainter tp1 = new TexturePainter ( loadIcon ( "bn11.jpg" ) );
        TexturePainter tp2 = new TexturePainter ( loadIcon ( "bn5.jpg" ) );
        TexturePainter tp3 = new TexturePainter ( loadIcon ( "bn10.jpg" ) );
        TexturePainter tp4 = new TexturePainter ( loadIcon ( "bn9.jpg" ) );
        
        tabbedPane.setPreferredSize ( new Dimension ( 800, 500 ) );
        
        tabbedPane.addTab ( "Favoritos", desktopPane);
        tabbedPane.setBackgroundPainterAt ( tabbedPane.getTabCount () - 1, tp1 );
        tabbedPane.setSelectedForegroundAt ( tabbedPane.getTabCount () - 1, Color.WHITE );

        tabbedPane.addTab ( "Facturas", facturasDesktop );
        tabbedPane.setBackgroundPainterAt ( tabbedPane.getTabCount () - 1, tp2 );
        tabbedPane.setSelectedForegroundAt ( tabbedPane.getTabCount () - 1, Color.WHITE );

        tabbedPane.addTab ( "Clientes", clientesDesktop );
        tabbedPane.setBackgroundPainterAt ( tabbedPane.getTabCount () - 1, tp3 );
        tabbedPane.setSelectedForegroundAt ( tabbedPane.getTabCount () - 1, Color.WHITE );

        tabbedPane.addTab ( "Productos", productosDesktop);
        tabbedPane.setBackgroundPainterAt ( tabbedPane.getTabCount () - 1, tp4 );
        tabbedPane.setSelectedForegroundAt ( tabbedPane.getTabCount () - 1, Color.BLACK );

        return tabbedPane;
    }

}
