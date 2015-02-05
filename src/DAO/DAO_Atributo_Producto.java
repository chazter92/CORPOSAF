/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Atributo_Producto;
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
public class DAO_Atributo_Producto {
    Conexion conn = new Conexion();
    
    public HashMap<String, BDO_Atributo_Producto> todosAtributosProducto(){
        HashMap<String, BDO_Atributo_Producto> atributos;
        atributos = toBDO(conn.query("SELECT * FROM ATRIBUTO_PRODUCTO", new Object[0], new Object[0]));        
        return atributos;
    }
    
    private HashMap<String, BDO_Atributo_Producto> toBDO(RowSet setAtributo){
        HashMap<String, BDO_Atributo_Producto> atributos = new HashMap<String, BDO_Atributo_Producto>();
        try {
            while(setAtributo.next()){
                BDO_Atributo_Producto actual = new BDO_Atributo_Producto(setAtributo.getInt("id_atributo_producto"),setAtributo.getString("nombre"),setAtributo.getString("descripcion"));
                atributos.put(actual.basicsToString(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage()+" "+ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }
        
        
        return atributos;
    }
    
    public HashMap<String, BDO_Atributo_Producto> busquedaIdNombre (String buscar) {
        HashMap<String, BDO_Atributo_Producto> atributosProducto = null;
        
        Object[] datos = {"%" + buscar.toLowerCase() + "%", "%" + buscar.toLowerCase() + "%"};
        
        atributosProducto = toBDO(conn.query("SELECT * FROM ATRIBUTO_PRODUCTO "
                + "WHERE LOWER(nombre) LIKE ? UNION "
                + "SELECT * FROM ATRIBUTO_PRODUCTO "
                + "WHERE to_char(id_atributo_producto, '999') LIKE ?", datos, datos));
        return atributosProducto;
    }
    
    
    public void actualizarAtributoProducto(BDO_Atributo_Producto nuevo, BDO_Atributo_Producto antiguo) {
         if (conn.executeQuery("UPDATE ATRIBUTO_PRODUCTO SET "
                 + "nombre = ?,"
                 + "descripcion = ?"
                 + "WHERE id_atributo_producto = ? RETURNING id_atributo_producto",
                 new Object[]{nuevo.getNombre(),nuevo.getDescripcion(),antiguo.getId_atributo_producto()},
                 new Object[]{"nombre","descripcion",1})){
             Catalogos.mostrarNotificacion("Datos modificados correctamente.",  NotificationIcon.information);
         }
    }
    
    public void agregarAtributoProducto(BDO_Atributo_Producto nuevo) {
        if (conn.executeQuery("INSERT INTO ATRIBUTO_PRODUCTO (nombre, descripcion) VALUES (?,?) RETURNING id_atributo_producto",
                new Object[]{nuevo.getNombre(),nuevo.getDescripcion()},
                new Object[]{"nombre","descripcion"})){
            Catalogos.mostrarNotificacion("Atributo " + nuevo.getNombre()+ " agregado correctamente.",  NotificationIcon.information);
        }
    }
}
