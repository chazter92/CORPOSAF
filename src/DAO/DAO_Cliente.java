/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Cliente;
import contar.Conexion;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.RowSet;

/**
 *
 * @author Chaz
 */
public class DAO_Cliente {
    Conexion conn = new Conexion();
    
    public ArrayList<BDO_Cliente> todosClientes(){
        ArrayList<BDO_Cliente> clientes;
        clientes = toBDO(conn.query("SELECT * FROM CLIENTE"));        
        return clientes;
    }
    
    private ArrayList<BDO_Cliente> toBDO(RowSet setCliente){
        ArrayList<BDO_Cliente> clientes = new ArrayList<BDO_Cliente>();
        try {
            while(setCliente.next()){
                BDO_Cliente actual = new BDO_Cliente(setCliente.getString("nit"),setCliente.getString("nombre"),setCliente.getString("direccion"),setCliente.getString("email"), setCliente.getString("nombre_factura"),setCliente.getString("telefono"));
                clientes.add(actual);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return clientes;
    }

    public ArrayList<BDO_Cliente> busquedaNitApellido(String buscar) {
        ArrayList<BDO_Cliente> clientes = null;
        conn.preparedQuery("SELECT * FROM CLIENTE WHERE LOWER(nombre) LIKE ? UNION SELECT * FROM CLIENTE WHERE LOWER(nit) LIKE ?");
        Object[] datos = {"%"+buscar.toLowerCase()+"%", "%"+buscar.toLowerCase()+"%"};
        clientes = toBDO(conn.executePreparedQuery(datos));        
        return clientes;
    }
}
