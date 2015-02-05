/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDO;

import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import contar.Catalogos;
import contar.productos.FrmProducto;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Chaz
 */
public class BDO_Atributo_Valor_Producto {

    private String valor, sku;
    private BDO_Atributo_Producto atributo_producto;

    public ImageIcon loadIcon(final String path) {
        return loadIcon(getClass(), path);
    }

    public static ImageIcon loadIcon(final Class nearClass, final String path) {
        try {
            return new ImageIcon(nearClass.getResource("icons/" + path));


        } catch (Exception e) {
            return new ImageIcon();
        }
    }

    public BDO_Atributo_Valor_Producto(String valor, String sku, int atributo_producto) {
        this.valor = valor;
        this.sku = sku;
        this.atributo_producto = Catalogos.buscarAtributoProductoInt(atributo_producto);
    }

    public BDO_Atributo_Producto getAtributo_producto() {
        return atributo_producto;
    }

    public void setAtributo_producto(BDO_Atributo_Producto atributo_producto) {
        this.atributo_producto = atributo_producto;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    public String basicsToString() {
        if (atributo_producto != null) {
            return this.sku + " | " + this.atributo_producto.getId_atributo_producto() + " | " + this.valor;
        } else {
            return String.valueOf(this.hashCode());
        }
    }
    
     public Component panelTag(final FrmProducto ventana) {
        final WebPanel pnlTag = new WebPanel();
        pnlTag.setLayout(new FlowLayout());
        WebLabel lblTitulo = new WebLabel(atributo_producto.getNombre() + ":");
        lblTitulo.setBoldFont(true);
        pnlTag.add(lblTitulo);
        WebLabel lblValor = new WebLabel(valor);
        pnlTag.add(lblValor);

        WebLabel lblIcono = new WebLabel(loadIcon("button-close.png"));
        lblIcono.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.removerDatosBusquedaAvanzada(atributo_producto, valor, pnlTag);
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

    public Component toPanel(int columnasDatos) {
        WebTextField txtValor = new WebTextField();
        txtValor.setColumns(columnasDatos);
        txtValor.setInputPrompt(atributo_producto.getDescripcion());
        txtValor.setHideInputPromptOnFocus(false);
        txtValor.setText(valor);
        txtValor.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                ((JTextField) e.getComponent()).selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                valor = ((JTextField) e.getComponent()).getText().trim();
            }
        });
        WebPanel pnlValor = new WebPanel();
        pnlValor.setLayout(new FlowLayout());
        pnlValor.add(new WebLabel(atributo_producto.getNombre() + ":"));
        pnlValor.add(txtValor);
        TooltipManager.setTooltip(pnlValor, atributo_producto.getDescripcion(), TooltipWay.up);
        TooltipManager.setTooltip(txtValor, atributo_producto.getDescripcion(), TooltipWay.up);

        return pnlValor;

    }
}
