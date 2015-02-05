/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Estado_Cliente;
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
public class DAO_Estado_Cliente {
    Conexion conn = new Conexion();
    
    public HashMap<String, BDO_Estado_Cliente> todosEstados(){
        HashMap<String, BDO_Estado_Cliente> estados;
        estados = toBDO(conn.query("SELECT * FROM Estado_CLIENTE", new Object[0], new Object[0]));        
        return estados;
    }
    
    private HashMap<String, BDO_Estado_Cliente> toBDO(RowSet setEstado){
        HashMap<String, BDO_Estado_Cliente> estados = new HashMap<String, BDO_Estado_Cliente>();
        try {
            while(setEstado.next()){
                BDO_Estado_Cliente actual = new BDO_Estado_Cliente(setEstado.getInt("id_estado_cliente"),setEstado.getString("tipo"),setEstado.getBoolean("puede_facturar"));
                estados.put(actual.basicsToString(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage()+" "+ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }
        
        return estados;
    }

    public HashMap<String, BDO_Estado_Cliente> busquedaCodigoTipo(String buscar) {
        HashMap<String, BDO_Estado_Cliente> estadosClientes = null;
        
        Object[] datos = {"%" + buscar.toLowerCase() + "%", "%" + buscar.toLowerCase() + "%"};
        
        estadosClientes = toBDO(conn.query("SELECT * FROM ESTADO_CLIENTE "
                + "WHERE LOWER(tipo) LIKE ? UNION "
                + "SELECT * FROM ESTADO_CLIENTE "
                + "WHERE to_char(id_estado_cliente, '999') LIKE ?", datos, datos));
        return estadosClientes;
    }

    public void actualizarEstado(BDO_Estado_Cliente nuevo, BDO_Estado_Cliente antiguo) {
         if (conn.executeQuery("UPDATE ESTADO_CLIENTE SET "
                 + "tipo = ?,"
                 + "puede_facturar = ?"
                 + "WHERE id_estado_cliente = ? RETURNING id_estado_cliente",
                 new Object[]{nuevo.getTipo(),nuevo.getPuede_facturar(),antiguo.getId_estado_cliente()},
                 new Object[]{"tipo",true,1})){
             Catalogos.mostrarNotificacion("Datos modificados correctamente.",  NotificationIcon.information);
         }
    }

    public void agregarEstado(BDO_Estado_Cliente nuevo) {
        if (conn.executeQuery("INSERT INTO ESTADO_CLIENTE (tipo, puede_facturar) VALUES (?,?) RETURNING id_estado_cliente",
                new Object[]{nuevo.getTipo(),nuevo.getPuede_facturar()},
                new Object[]{"tipo",true})){
            Catalogos.mostrarNotificacion("Estado " + nuevo.getTipo() + " agregado correctamente.",  NotificationIcon.information);
        }
    }
}
