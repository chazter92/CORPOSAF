/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.factura;

import BDO.BDO_Cliente;
import DAO.DAO_Cliente;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import contar.Catalogos;
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
import validacion.Validar_Vacio;

/**
 *
 * @author Chaz
 */
public class FrmAgregarCliente extends WebDialog {

    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private WebTextField txtNit, txtNombre, txtDireccion, txtEmail;
    private WebButton btnGuardar, btnCancelar;
    private final int columnasDatos = 25;
    private DAO_Cliente connCliente;

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

    public FrmAgregarCliente(String cliente) {
        super();
        createUI(cliente);
    }

    private void createUI(String cliente) {
        setModal(true);
        setTitle("Agregar cliente");
        WebPanel pnlAgregarCliente = new WebPanel();
        pnlAgregarCliente.setLayout(new BoxLayout(pnlAgregarCliente, BoxLayout.Y_AXIS));
        pnlAgregarCliente.add(agregarPanelDatosCliente(cliente));
        pnlAgregarCliente.add(agregarPanelBotones());
        add(pnlAgregarCliente);
        pack();
        setLocationRelativeTo(null);
    }

    private Component agregarPanelDatosCliente(String nit) {
        WebPanel pnlDatosCliente = new WebPanel();
        pnlDatosCliente.setLayout(new BoxLayout(pnlDatosCliente, BoxLayout.Y_AXIS));

        txtNit = new WebTextField();
        txtNit.setColumns(columnasDatos);
        txtNit.setInputPrompt("NIT del cliente");
        txtNit.setHideInputPromptOnFocus(false);
        txtNit.setEditable(false);
        txtNit.setText(nit);
        WebPanel pnlNit = new WebPanel();
        pnlNit.setLayout(new FlowLayout());
        pnlNit.add(new WebLabel(loadIcon("label.png")));
        pnlNit.add(txtNit);
        TooltipManager.setTooltip(pnlNit, "NIT del cliente, Ejemplos: 3456789, 457896k", TooltipWay.up);
        TooltipManager.setTooltip(txtNit, "NIT del cliente, Ejemplos: 3456789, 457896k", TooltipWay.up);
        pnlDatosCliente.add(pnlNit);

        txtNombre = new WebTextField();
        txtNombre.setColumns(columnasDatos);
        txtNombre.setInputPrompt("Nombre completo del cliente");
        txtNombre.setHideInputPromptOnFocus(false);
        txtNombre.addFocusListener(new Validar_Vacio("El nombre completo del cliente "));
        WebPanel pnlNombre = new WebPanel();
        pnlNombre.setLayout(new FlowLayout());
        pnlNombre.add(new WebLabel(loadIcon("user_green.png")));
        pnlNombre.add(txtNombre);
        TooltipManager.setTooltip(pnlNombre, "Ingrese nombre del cliente", TooltipWay.up);
        TooltipManager.setTooltip(txtNombre, "Ingrese nombre del cliente", TooltipWay.up);
        pnlDatosCliente.add(pnlNombre);

        txtDireccion = new WebTextField();
        txtDireccion.setColumns(columnasDatos);
        txtDireccion.setInputPrompt("Dirección domiciliar");
        txtDireccion.setHideInputPromptOnFocus(false);
        txtDireccion.addFocusListener(new Validar_Vacio("La dirección "));
        WebPanel pnlDireccion = new WebPanel();
        pnlDireccion.setLayout(new FlowLayout());
        pnlDireccion.add(new WebLabel(loadIcon("mail_box.png")));
        pnlDireccion.add(txtDireccion);
        TooltipManager.setTooltip(pnlDireccion, "Ingrese dirección del cliente", TooltipWay.up);
        TooltipManager.setTooltip(txtDireccion, "Ingrese dirección del cliente", TooltipWay.up);
        pnlDatosCliente.add(pnlDireccion);

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
        pnlDatosCliente.add(pnlEmail);
        WebScrollPane scrDatos = new WebScrollPane(pnlDatosCliente);
        scrDatos.setBorder(null);
        return scrDatos;
    }

    private Component agregarPanelBotones() {
        WebPanel pnlBotonesDatosIndividuales = new WebPanel();
        pnlBotonesDatosIndividuales.setLayout(new FlowLayout());
        btnGuardar = new WebButton("Guardar", loadIcon("save_new.png"));
        btnCancelar = new WebButton("Cancelar", loadIcon("cancel.png"));

        btnGuardar.addActionListener(new ActionListener() {

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

        pnlBotonesDatosIndividuales.add(btnGuardar);
        pnlBotonesDatosIndividuales.add(btnCancelar);
        return pnlBotonesDatosIndividuales;
    }

    private void guardarCliente() {
        if (validarDatos()) {
            connCliente = new DAO_Cliente();
            BDO_Cliente nuevo = new BDO_Cliente(txtNit.getText().trim(), txtNombre.getText().trim(),
                    txtDireccion.getText().trim(), txtEmail.getText().trim(), txtNombre.getText().trim(),
                    "", 1, 1, null);

            connCliente.agregarCliente(nuevo);
            Catalogos.actualizarClientes();
            this.dispose();
        }
    }
    
    private boolean validarDatos(){
        if (txtNombre.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El campo Nombre no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (txtDireccion.getText().trim().isEmpty()) {
            Catalogos.mostrarMensajeError("El campo direccion no puede estar vacío.", "Advertencia", WebOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void cancelarOperacion(){
        this.dispose();
    }
}
