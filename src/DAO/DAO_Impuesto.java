/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Impuesto;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.managers.notification.NotificationIcon;
import contar.Catalogos;
import contar.Conexion;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sql.RowSet;

/**
 *
 * @author Chaz
 */
public class DAO_Impuesto {

    Conexion conn = new Conexion();

    public HashMap<String, BDO_Impuesto> todosImpuestos() {
        HashMap<String, BDO_Impuesto> impuestos;
        impuestos = toBDO(conn.query("SELECT * FROM IMPUESTO", new Object[0], new Object[0]));
        return impuestos;
    }
    
    public HashMap<String, BDO_Impuesto> todosImpuestosIDP() {
        HashMap<String, BDO_Impuesto> impuestos;
        impuestos = toBDO(conn.query("SELECT * FROM IMPUESTO WHERE es_idp = TRUE", new Object[0], new Object[0]));
        return impuestos;
    }
    
    public HashMap<String, BDO_Impuesto> todosImpuestosAplicaPrecioVenta() {
        HashMap<String, BDO_Impuesto> impuestos;
        impuestos = toBDO(conn.query("SELECT * FROM IMPUESTO WHERE aplica_precio_venta = TRUE", new Object[0], new Object[0]));
        return impuestos;
    }

    private HashMap<String, BDO_Impuesto> toBDO(RowSet setImpuesto) {
        HashMap<String, BDO_Impuesto> impuestos = new HashMap<String, BDO_Impuesto>();
        try {
            while (setImpuesto.next()) {
                BDO_Impuesto actual = new BDO_Impuesto(setImpuesto.getInt("id_impuesto"), setImpuesto.getString("nombre"), setImpuesto.getDouble("valor"), setImpuesto.getBoolean("es_idp"), setImpuesto.getBoolean("aplica_precio_venta"), setImpuesto.getInt("id_empresa"));
                impuestos.put(actual.basicsToString(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage() + " " + ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }

        return impuestos;
    }

    public HashMap<String, BDO_Impuesto> busquedaIdNombre(String buscar) {
        HashMap<String, BDO_Impuesto> impuestos = null;

        Object[] datos = {"%" + buscar.toLowerCase() + "%", "%" + buscar.toLowerCase() + "%"};

        impuestos = toBDO(conn.query("SELECT * FROM IMPUESTO "
                + "WHERE LOWER(nombre) LIKE ? UNION "
                + "SELECT * FROM IMPUESTO "
                + "WHERE to_char(id_impuesto, '999') LIKE ?", datos, datos));
        return impuestos;
    }

    public void actualizarImpuesto(BDO_Impuesto nuevo, BDO_Impuesto antiguo) {
        if (conn.executeQuery("UPDATE IMPUESTO SET "
                + "nombre = ?,"
                + "valor = ?,"
                + "es_idp = ?,"
                + "aplica_precio_venta = ?"
                + "WHERE id_impuesto = ? RETURNING id_impuesto",
                new Object[]{nuevo.getNombre(), nuevo.getValor(), nuevo.isEs_idp(), antiguo.getId_impuesto()},
                new Object[]{"nombre", 0.0, true, 1})) {
            Catalogos.mostrarNotificacion("Datos modificados correctamente.", NotificationIcon.information);
        }
    }

    public void agregarImpuesto(BDO_Impuesto nuevo) {
        if (conn.executeQuery("INSERT INTO IMPUESTO (nombre, valor, es_idp, aplica_precio_venta id_empresa) VALUES (?,?,?,?,?) RETURNING id_impuesto",
                new Object[]{nuevo.getNombre(), nuevo.getValor(), nuevo.isEs_idp(), nuevo.isAplica_precio_venta(), Catalogos.getEmpresa().getId_empresa()},
                new Object[]{"nombre", 0.0, true, true, 1})) {
            Catalogos.mostrarNotificacion("Impuesto " + nuevo.getNombre() + " agregada correctamente.", NotificationIcon.information);
        }
    }

    public BigDecimal getImpuestosPrecioVenta() {
        ArrayList<BDO_Impuesto> impuestoPrecioVenta = new ArrayList(todosImpuestosAplicaPrecioVenta().values());
        BigDecimal precioVenta = new BigDecimal(1.0);
        for(int i=0;i<impuestoPrecioVenta.size();i++){
            precioVenta = precioVenta.multiply(BigDecimal.valueOf((impuestoPrecioVenta.get(i).getValor()/100)+1));
        }
        
        return precioVenta;
    }
}
