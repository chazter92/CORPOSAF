/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Atributo_Cliente;
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
public class DAO_Atributo_Cliente {
    
    Conexion conn = new Conexion();
    
    public HashMap<String, BDO_Atributo_Cliente> todosAtributos(){
        HashMap<String, BDO_Atributo_Cliente> atributos;
        atributos = toBDO(conn.query("SELECT * FROM ATRIBUTO_CLIENTE", new Object[0], new Object[0]));        
        return atributos;
    }
    
    private HashMap<String, BDO_Atributo_Cliente> toBDO(RowSet setAtributo){
        HashMap<String, BDO_Atributo_Cliente> atributos = new HashMap<String, BDO_Atributo_Cliente>();
        try {
            while(setAtributo.next()){
                BDO_Atributo_Cliente actual = new BDO_Atributo_Cliente(setAtributo.getInt("id_atributo_cliente"),setAtributo.getString("nombre"),setAtributo.getString("descripcion"));
                atributos.put(actual.basicsToString(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage()+" "+ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }
        
        return atributos;
    }
    
    public HashMap<String, BDO_Atributo_Cliente> busquedaIdNombre (String buscar) {
        HashMap<String, BDO_Atributo_Cliente> atributosCliente = null;
        
        Object[] datos = {"%" + buscar.toLowerCase() + "%", "%" + buscar.toLowerCase() + "%"};
        
        atributosCliente = toBDO(conn.query("SELECT * FROM ATRIBUTO_CLIENTE "
                + "WHERE LOWER(nombre) LIKE ? UNION "
                + "SELECT * FROM ATRIBUTO_CLIENTE "
                + "WHERE to_char(id_atributo_cliente, '999') LIKE ?", datos, datos));
        return atributosCliente;
    }
    
    public void actualizarAtributoCliente(BDO_Atributo_Cliente nuevo, BDO_Atributo_Cliente antiguo) {
         if (conn.executeQuery("UPDATE ATRIBUTO_CLIENTE SET "
                 + "nombre = ?,"
                 + "descripcion = ?"
                 + "WHERE id_atributo_cliente = ? RETURNING id_atributo_cliente",
                 new Object[]{nuevo.getNombre(),nuevo.getDescripcion(),antiguo.getId_atributo_cliente()},
                 new Object[]{"nombre","descripcion",1})){
             Catalogos.mostrarNotificacion("Datos modificados correctamente.",  NotificationIcon.information);
         }
    }
    
    public void agregarAtributoCliente(BDO_Atributo_Cliente nuevo) {
        if (conn.executeQuery("INSERT INTO ATRIBUTO_CLIENTE (nombre, descripcion) VALUES (?,?) RETURNING id_atributo_cliente",
                new Object[]{nuevo.getNombre(),nuevo.getDescripcion()},
                new Object[]{"nombre","descripcion"})){
            Catalogos.mostrarNotificacion("Atributo " + nuevo.getNombre()+ " agregado correctamente.",  NotificationIcon.information);
        }
    }
}
