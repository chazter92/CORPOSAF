/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Precio_Cliente;
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
public class DAO_Precio_Cliente {
    Conexion conn = new Conexion();
    
    public HashMap<String, BDO_Precio_Cliente> todosPrecios(){
        HashMap<String, BDO_Precio_Cliente> precios;
        precios = toBDO(conn.query("SELECT * FROM PRECIO_CLIENTE", new Object[0], new Object[0]));        
        return precios;
    }
    
    private HashMap<String, BDO_Precio_Cliente> toBDO(RowSet setPrecio){
        HashMap<String, BDO_Precio_Cliente> precios = new HashMap<String, BDO_Precio_Cliente>();
        try {
            while(setPrecio.next()){
                BDO_Precio_Cliente actual = new BDO_Precio_Cliente(setPrecio.getInt("id_lista_precio"),setPrecio.getString("nombre"),setPrecio.getDouble("descuento"));
                precios.put(actual.basicsToString(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage()+" "+ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }
        
        return precios;
    }
    
    public HashMap<String, BDO_Precio_Cliente> busquedaCodigoTipo(String buscar) {
        HashMap<String, BDO_Precio_Cliente> preciosClientes = null;
        
        Object[] datos = {"%" + buscar.toLowerCase() + "%", "%" + buscar.toLowerCase() + "%"};
        
        preciosClientes = toBDO(conn.query("SELECT * FROM PRECIO_CLIENTE "
                + "WHERE LOWER(nombre) LIKE ? UNION "
                + "SELECT * FROM PRECIO_CLIENTE "
                + "WHERE to_char(id_lista_precio, '999') LIKE ?", datos, datos));
        return preciosClientes;
    }
    
    public void actualizarPrecio(BDO_Precio_Cliente nuevo, BDO_Precio_Cliente antiguo) {
         if (conn.executeQuery("UPDATE PRECIO_CLIENTE SET "
                 + "nombre = ?,"
                 + "descuento = ?"
                 + "WHERE id_lista_precio = ? RETURNING id_lista_precio",
                 new Object[]{nuevo.getTipo(),nuevo.getDescuento(),antiguo.getId_lista_precio()},
                 new Object[]{"nombre",0.0,1})){
             Catalogos.mostrarNotificacion("Datos modificados correctamente.",  NotificationIcon.information);
         }
    }

    public void agregarPrecio(BDO_Precio_Cliente nuevo) {
        if (conn.executeQuery("INSERT INTO PRECIO_CLIENTE (nombre, descuento) VALUES (?,?) RETURNING id_lista_precio",
                new Object[]{nuevo.getTipo(),nuevo.getDescuento()},
                new Object[]{"tipo",0.0})){
            Catalogos.mostrarNotificacion("Lista de precios " + nuevo.getTipo() + " agregada correctamente.",  NotificationIcon.information);
        }
    }
}
