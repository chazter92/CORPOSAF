/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Estado_Producto;
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
public class DAO_Estado_Producto {
    Conexion conn = new Conexion();
    
    public HashMap<String, BDO_Estado_Producto> todosEstados(){
        HashMap<String, BDO_Estado_Producto> estados;
        estados = toBDO(conn.query("SELECT * FROM ESTADO_PRODUCTO", new Object[0], new Object[0]));        
        return estados;
    }
    
    private HashMap<String, BDO_Estado_Producto> toBDO(RowSet setEstado){
        HashMap<String, BDO_Estado_Producto> estados = new HashMap<String, BDO_Estado_Producto>();
        try {
            while(setEstado.next()){
                BDO_Estado_Producto actual = new BDO_Estado_Producto(setEstado.getInt("id_estado_producto"),setEstado.getString("estado"),setEstado.getBoolean("puede_facturar"));
                estados.put(actual.basicsToString(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage()+" "+ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }
        
        return estados;
    }

    public HashMap<String, BDO_Estado_Producto> busquedaCodigoTipo(String buscar) {
        HashMap<String, BDO_Estado_Producto> estadosProductos = null;
        
        Object[] datos = {"%" + buscar.toLowerCase() + "%", "%" + buscar.toLowerCase() + "%"};
        
        estadosProductos = toBDO(conn.query("SELECT * FROM ESTADO_PRODUCTO "
                + "WHERE LOWER(estado) LIKE ? UNION "
                + "SELECT * FROM ESTADO_PRODUCTO "
                + "WHERE to_char(id_estado_producto, '999') LIKE ?", datos, datos));
        return estadosProductos;
    }

    public void actualizarEstado(BDO_Estado_Producto nuevo, BDO_Estado_Producto antiguo) {
         if (conn.executeQuery("UPDATE ESTADO_PRODUCTO SET "
                 + "estado = ?,"
                 + "puede_facturar = ?"
                 + "WHERE id_estado_Producto = ? RETURNING id_estado_Producto",
                 new Object[]{nuevo.getTipo(),nuevo.getPuede_facturar(),antiguo.getId_estado_producto()},
                 new Object[]{"tipo",true,1})){
             Catalogos.mostrarNotificacion("Datos modificados correctamente.",  NotificationIcon.information);
         }
    }

    public void agregarEstado(BDO_Estado_Producto nuevo) {
        if (conn.executeQuery("INSERT INTO ESTADO_PRODUCTO (estado, puede_facturar) VALUES (?,?) RETURNING id_estado_producto",
                new Object[]{nuevo.getTipo(),nuevo.getPuede_facturar()},
                new Object[]{"tipo",true})){
            Catalogos.mostrarNotificacion("Estado " + nuevo.getTipo() + " agregado correctamente.",  NotificationIcon.information);
        }
    }
}
