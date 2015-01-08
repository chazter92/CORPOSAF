/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar;

import com.alee.extended.layout.TableLayout;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.utils.SwingUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Chaz
 */
class FrmLogin extends WebDialog{
    
    WebPanel content;
    WebTextField user;
    WebPasswordField pass;
     public FrmLogin(){
        super();
        construirGUI();
        setSize(300,200);
        setLocationRelativeTo(null);
        setTitle ( "Ingreso de Usuarios" );
        setIconImages ( WebLookAndFeel.getImages () );
        setLayout ( new BorderLayout () );
        setContentPane(content);
        setDefaultCloseOperation ( WebDialog.DISPOSE_ON_CLOSE );
        setResizable ( false );
        setModal(true);
        pack();
        show();
        
    }

    private void construirGUI() {
        WebLabel lblUser = new WebLabel("Usuario");
        setIconImages ( WebLookAndFeel.getImages () );
            
            TableLayout layout = new TableLayout ( new double[][]{ { TableLayout.PREFERRED, TableLayout.FILL },
                    { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED , TableLayout.PREFERRED } } );
            layout.setHGap ( 5 );
            layout.setVGap ( 5 );
            user =  new WebTextField ( 15 );
            pass = new WebPasswordField ( 15 );
            content = new WebPanel ( layout );
            content.setMargin ( 15, 30, 15, 30 );
            content.setOpaque ( false );
            //content.add( new WebLabel ("Ingreso de usuarios", WebLabel.CENTER), "0,0");
            content.add ( new WebLabel ( "Usuario", WebLabel.TRAILING ), "0,1" );
            content.add (user, "1,1" );

            content.add ( new WebLabel ( "Constraseña", WebLabel.TRAILING ), "0,2" );
            content.add ( pass, "1,2" );
            content.setBackground(Color.red);
            content.setOpaque(false);
            WebButton login = new WebButton ( "Ingresar" );
            WebButton cancel = new WebButton ( "Cancelar" );
            ActionListener listener = new ActionListener ()
            {
                @Override
                public void actionPerformed ( ActionEvent e )
                {
                    if(e.getActionCommand().equalsIgnoreCase("Ingresar")){
                        
                        if(user.getText().equals("admin") && pass.getText().equals("password")){
                            WebOptionPane.showMessageDialog ( null, "Datos correctos, bienvenido", "Bienvenido", WebOptionPane.INFORMATION_MESSAGE );
                            dispose();
                            FrmPrincipal principal = new FrmPrincipal();
                        }else{
                            WebOptionPane.showMessageDialog ( null, "Verifique los datos ingresados", "Error", WebOptionPane.ERROR_MESSAGE );
                        }
                        
                    }else{
                        
                        dispose();
                    }
                    
                
                }
            };
            login.addActionListener ( listener );
            cancel.addActionListener ( listener );
            content.add ( new CenterPanel ( new GroupPanel ( 5, login, cancel ) ), "0,3,1,3" );
            SwingUtils.equalizeComponentsWidths ( login, cancel );

            add ( content );

            HotkeyManager.registerHotkey ( this, cancel, Hotkey.ESCAPE );
            HotkeyManager.registerHotkey ( this, login, Hotkey.ENTER );
 
    }
}
