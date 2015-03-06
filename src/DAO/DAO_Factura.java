/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Factura;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.managers.notification.NotificationIcon;
import contar.Catalogos;
import contar.Conexion;
import java.sql.SQLException;
import java.util.HashMap;
import javax.sql.RowSet;

/**
 *
 * @author Chaz
 */
public class DAO_Factura {
    Conexion conn = new Conexion();

    public HashMap<String, BDO_Factura> todasFacturas() {
        HashMap<String, BDO_Factura> clientes;
        clientes = toBDO(conn.query("SELECT * FROM Fractura", new Object[0], new Object[0]));
        return clientes;
    }
    
     private HashMap<String, BDO_Factura> toBDO(RowSet setFactura) {
        HashMap<String, BDO_Factura> facturas = new HashMap<String, BDO_Factura>();
        try {
            while (setFactura.next()) {
                BDO_Factura actual = new BDO_Factura(setFactura.getString("no_factura"), setFactura.getDate("fecha"), setFactura.getDouble("total"), setFactura.getString("id_documento"), setFactura.getString("nit"), setFactura.getInt("id_precio_venta"), setFactura.getString("voucher"));
                facturas.put(actual.getNit(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage() + " " + ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }

        return facturas;
    }
     
      public void agregarFactura(BDO_Factura nuevo) {
        if (conn.executeQuery("INSERT INTO FACTURA (no_factura, fecha, total, id_documento, nit, id_precio_venta, voucher) "
                + "VALUES (?,?,?,?,?,?,?) RETURNING no_factura",
                new Object[]{nuevo.getNo_factura(), nuevo.getFecha(), nuevo.getTotal(), nuevo.getId_documento(), nuevo.getNit(), nuevo.getTipo_pago(),
                    nuevo.getVoucher()},
                new Object[]{"no_factura", java.util.Date.class, 1.0, "id_doc","nit",1, "voucher"})) {
            
            Catalogos.mostrarNotificacion("Factura " + nuevo.getNo_factura() + " agregada correctamente.", NotificationIcon.information);
        }
    }

}
