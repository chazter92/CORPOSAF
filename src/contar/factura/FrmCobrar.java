/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.factura;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.text.WebTextField;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import contar.Catalogos;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import validacion.Validar_Double;

/**
 *
 * @author Chaz
 */
public class FrmCobrar extends WebDialog {

    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    static final Toolkit t = Toolkit.getDefaultToolkit();
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private WebPanel pnlEfectivo, pnlTarjeta;
    private WebTabbedPane pnlPestañasCobrar;
    private WebTextField txtEfectivo, txtVoucher;
    private WebLabel lblTotal, lblCambio;
    private WebButton btnGuardarEfectivo, btnCancelarEfectivo, btnGuardarTarjeta, btnCancelarTarjeta;
    private int columnasDatos;

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

    public FrmCobrar(Component frmFactura, int columnasDatos, BigDecimal total, String noFactura) {
        super();
        this.columnasDatos = columnasDatos;
        createUI(frmFactura, total, noFactura);
    }

    private void createUI(Component frmFactura, BigDecimal total, String noFactura) {
        setModal(true);
        setTitle("Agregar cliente");
        pnlEfectivo = new WebPanel();
        pnlTarjeta = new WebPanel();
        pnlPestañasCobrar = new WebTabbedPane();
        agregarPestañaEfectivo(total, noFactura);
        agregarPestañaTarjeta(total, noFactura);
        agregarPestañasCobrar();
        add(pnlPestañasCobrar);
        setLocationRelativeTo(frmFactura);
        pack();
        this.setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
    }

    private void agregarPestañasCobrar() {
        pnlPestañasCobrar.addTab("Efectivo", loadIcon("money.png"), pnlEfectivo);
        pnlPestañasCobrar.addTab("Tarjeta de crédito", loadIcon("creditcards.png"), pnlTarjeta);

    }

    private void agregarPestañaEfectivo(final BigDecimal total, String noFactura) {
        pnlEfectivo.setLayout(new BoxLayout(pnlEfectivo, BoxLayout.Y_AXIS));
        WebPanel pnlTotalPagar = new WebPanel();
        pnlTotalPagar.setLayout(new FlowLayout());
        WebLabel lblTituloTotal = new WebLabel("Total a pagar:");
        lblTituloTotal.setFontSize(15);
        pnlTotalPagar.add(lblTituloTotal);
        lblTotal = new WebLabel("Q. " + total.toString());
        lblTotal.setFontSize(17);
        lblTotal.setForeground(Color.BLUE);
        pnlTotalPagar.add(lblTotal);
        pnlEfectivo.add(pnlTotalPagar);

        WebPanel pnlDatosEfectivo = new WebPanel();
        pnlDatosEfectivo.setLayout(new FlowLayout());

        txtEfectivo = new WebTextField();
        txtEfectivo.addFocusListener(new Validar_Double());
        txtEfectivo.setColumns(columnasDatos);
        txtEfectivo.setInputPrompt("Efectivo");
        txtEfectivo.setHideInputPromptOnFocus(false);
        txtEfectivo.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!txtEfectivo.getText().isEmpty()) {
                    if (txtEfectivo.getText().matches("^\\d+(\\.\\d+)?")) {
                        BigDecimal efectivo = new BigDecimal(txtEfectivo.getText());
                        BigDecimal resta = efectivo.subtract(total);
                        lblCambio.setText("Q. " + resta);
                        if(resta.compareTo(BigDecimal.ZERO)<0){
                            lblCambio.setForeground(Color.RED);
                        }else{
                            lblCambio.setForeground(Color.GREEN.darker());
                        }
                    }


                }
            }
        });
        txtEfectivo.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent ev) {
                String ingresado = txtEfectivo.getText().trim();
                if (!ingresado.isEmpty()) {
                    if (!valirdarDouble(ingresado)) {
                        Catalogos.mostrarMensajeError("Cifra no válida. Ingrese un número. Ejemplo: 4, 7.96", "Error", WebOptionPane.ERROR_MESSAGE);
                        txtEfectivo.requestFocus();
                    }
                }else{
                    Catalogos.mostrarMensajeError("Ingrese la cantidad en efectivo cancelada por el cliente", "Error", WebOptionPane.ERROR_MESSAGE);
                    txtEfectivo.requestFocus();
                }
            }

            public boolean valirdarDouble(String ingresado) {
                if (ingresado.matches("^\\d+(\\.\\d+)?")) {
                    return true;
                }
                return false;
            }
        });
        TooltipManager.setTooltip(pnlDatosEfectivo, "Ingrese la cantidad de dinero en efectivo", TooltipWay.up);
        TooltipManager.setTooltip(txtEfectivo, "Ingrese la cantidad de dinero en efectivo", TooltipWay.up);

        pnlDatosEfectivo.add(new WebLabel(loadIcon("money_dollar.png")));
        pnlDatosEfectivo.add(txtEfectivo);

        pnlEfectivo.add(pnlDatosEfectivo);

        WebPanel pnlDatosCambio = new WebPanel();
        pnlDatosCambio.setLayout(new FlowLayout());
        WebLabel lblTituloCambio = new WebLabel("Cambio: ");
        lblTituloCambio.setFontSize(16);

        pnlDatosCambio.add(lblTituloCambio);
        lblCambio = new WebLabel("-");
        lblCambio.setFontSize(16);
        lblCambio.setForeground(Color.GREEN.darker());
        pnlDatosCambio.add(lblCambio);

        pnlEfectivo.add(pnlDatosCambio);

        WebPanel pnlBotones = new WebPanel();
        pnlBotones.setLayout(new FlowLayout());
        btnGuardarEfectivo = new WebButton("Guardar", loadIcon("save_new.png"));
        btnGuardarEfectivo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnCancelarEfectivo = new WebButton("Cancelar", loadIcon("cancel.png"));
        btnCancelarEfectivo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        pnlBotones.add(btnGuardarEfectivo);
        pnlBotones.add(btnCancelarEfectivo);

        pnlEfectivo.add(pnlBotones);
        pnlEfectivo.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
    }

    private void agregarPestañaTarjeta(BigDecimal total, String noFactura) {
        pnlTarjeta.setLayout(new BoxLayout(pnlTarjeta, BoxLayout.Y_AXIS));
        WebPanel pnlTotalPagar = new WebPanel();
        pnlTotalPagar.setLayout(new FlowLayout());
        WebLabel lblTituloTotal = new WebLabel("Total a pagar:");
        lblTituloTotal.setFontSize(15);
        pnlTotalPagar.add(lblTituloTotal);
        lblTotal = new WebLabel("Q. " + total.toString());
        lblTotal.setFontSize(17);
        lblTotal.setForeground(Color.BLUE);
        pnlTotalPagar.add(lblTotal);
        pnlTarjeta.add(pnlTotalPagar);

        WebPanel pnlDatosVoucher = new WebPanel();
        pnlDatosVoucher.setLayout(new FlowLayout());

        txtVoucher = new WebTextField();
        txtVoucher.addFocusListener(new Validar_Double());
        txtVoucher.setColumns(columnasDatos);
        txtVoucher.setInputPrompt("No. Voucher");
        txtVoucher.setHideInputPromptOnFocus(false);
        TooltipManager.setTooltip(pnlDatosVoucher, "Ingrese el voucher", TooltipWay.up);
        TooltipManager.setTooltip(txtVoucher, "Ingrese el voucher", TooltipWay.up);

        pnlDatosVoucher.add(new WebLabel(loadIcon("label.png")));
        pnlDatosVoucher.add(txtVoucher);

        pnlTarjeta.add(pnlDatosVoucher);

        WebPanel pnlBotones = new WebPanel();
        pnlBotones.setLayout(new FlowLayout());
        btnGuardarTarjeta = new WebButton("Guardar", loadIcon("save_new.png"));
        btnGuardarTarjeta.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnCancelarTarjeta = new WebButton("Cancelar", loadIcon("cancel.png"));
        btnCancelarTarjeta.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });



        pnlBotones.add(btnGuardarTarjeta);
        pnlBotones.add(btnCancelarTarjeta);

        pnlTarjeta.add(pnlBotones);
        pnlTarjeta.setBorder(new EmptyBorder((int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025), (int) (screenSize.height * 0.025), (int) (screenSize.width * 0.025)));
    }
}
