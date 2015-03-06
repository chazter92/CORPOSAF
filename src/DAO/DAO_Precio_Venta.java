/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Tipo_Pago_Venta;
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
public class DAO_Precio_Venta {
    Conexion conn = new Conexion();

    public HashMap<String, BDO_Tipo_Pago_Venta> todosPreciosVenta() {
        HashMap<String, BDO_Tipo_Pago_Venta> precios;
        precios = toBDO(conn.query("SELECT * FROM PRECIO_VENTA", new Object[0], new Object[0]));
        return precios;
    }
    
    private HashMap<String, BDO_Tipo_Pago_Venta> toBDO(RowSet setPrecio) {
        HashMap<String, BDO_Tipo_Pago_Venta> precios = new HashMap<String, BDO_Tipo_Pago_Venta>();
        try {
            while (setPrecio.next()) {
                BDO_Tipo_Pago_Venta actual = new BDO_Tipo_Pago_Venta(setPrecio.getInt("id_precio_venta"), setPrecio.getString("nombre"));
                precios.put(actual.basicsToString(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage() + " " + ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }

        return precios;
    }
    
    public HashMap<String, BDO_Tipo_Pago_Venta> busquedaIdNombre(String buscar) {
        HashMap<String, BDO_Tipo_Pago_Venta> precios = null;

        Object[] datos = {"%" + buscar.toLowerCase() + "%", "%" + buscar.toLowerCase() + "%"};

        precios = toBDO(conn.query("SELECT * FROM TIPO_PAGO_VENTA "
                + "WHERE LOWER(nombre) LIKE ? UNION "
                + "SELECT * FROM TIPO_PAGO_VENTA "
                + "WHERE to_char(id_tipo_pago, '999') LIKE ?", datos, datos));
        return precios;
    }
    
    public void actualizarPrecioVenta(BDO_Tipo_Pago_Venta nuevo, BDO_Tipo_Pago_Venta antiguo) {
        if (conn.executeQuery("UPDATE TIPO_PAGO_VENTA SET nombre = ?"
                + "WHERE id_tipo_pago = ? RETURNING id_tippo_pago",
                new Object[]{nuevo.getNombre(), antiguo.getId_tipo_pago()},
                new Object[]{"nombre",  1})) {
            Catalogos.mostrarNotificacion("Datos modificados correctamente.", NotificationIcon.information);
        }
    }
    
    public void agregarPrecioVenta(BDO_Tipo_Pago_Venta nuevo) {
        if (conn.executeQuery("INSERT INTO TIPO_PAGO_VENTA (id_tipo_pago, nombre) VALUES (?,?) RETURNING id_tipo_pago",
                new Object[]{nuevo.getId_tipo_pago(), nuevo.getNombre()},
                new Object[]{1,  "nombre"})) {
            Catalogos.mostrarNotificacion("Precio de venta " + nuevo.getNombre() + " agregada correctamente.", NotificationIcon.information);
        }
    }
}
