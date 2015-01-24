/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Cliente;
import com.alee.laf.optionpane.WebOptionPane;
import contar.Catalogos;
import contar.Conexion;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.RowSet;
import com.alee.managers.notification.NotificationIcon;

/**
 *
 * @author Chaz
 */
public class DAO_Cliente {

    Conexion conn = new Conexion();

    public HashMap<String, BDO_Cliente> todosClientes() {
        HashMap<String, BDO_Cliente> clientes;
        clientes = toBDO(conn.query("SELECT * FROM CLIENTE", new Object[0], new Object[0]));
        return clientes;
    }

    private HashMap<String, BDO_Cliente> toBDO(RowSet setCliente) {
        HashMap<String, BDO_Cliente> clientes = new HashMap<String, BDO_Cliente>();
        try {
            while (setCliente.next()) {
                BDO_Cliente actual = new BDO_Cliente(setCliente.getString("nit"), setCliente.getString("nombre"), setCliente.getString("direccion"), setCliente.getString("email"), setCliente.getString("nombre_facturar"), setCliente.getString("telefono"), setCliente.getInt("id_lista_precio"), setCliente.getInt("id_estado_cliente"));
                clientes.put(actual.getNit(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage()+" "+ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }

        return clientes;
    }

    public HashMap<String, BDO_Cliente> busquedaNitApellido(String buscar) {
        HashMap<String, BDO_Cliente> clientes = null;
        Object[] datos = {"%" + buscar.toLowerCase() + "%", "%" + buscar.toLowerCase() + "%"};
        
        clientes = toBDO(conn.query("SELECT * FROM CLIENTE "
                + "WHERE LOWER(nombre) LIKE ? UNION "
                + "SELECT * FROM CLIENTE "
                + "WHERE LOWER(nit) LIKE ?", datos, datos));
        return clientes;
    }

    public void agregarCliente(BDO_Cliente nuevo) {
        if (conn.executeQuery("INSERT INTO CLIENTE (nit, nombre, direccion, email, nombre_facturar, telefono, id_lista_precio, id_estado_cliente, id_empresa) "
                + "VALUES (?,?,?,?,?,?,?,?,?) RETURNING nit",
                new Object[]{nuevo.getNit(), nuevo.getNombre(), nuevo.getDireccion(), nuevo.getEmail(), nuevo.getNombre_factura(), nuevo.getTelefono(),
                    nuevo.getPrecio(), nuevo.getEstado(), Catalogos.getEmpresa().getId_empresa()},
                nuevo.getTypes())) {
            Catalogos.mostrarNotificacion("Cliente " + nuevo.getNit() + " agregado correctamente.",  NotificationIcon.information);
        }
    }

    public void actualizarCliente(BDO_Cliente nuevo, BDO_Cliente antiguo) {
        if (conn.executeQuery("UPDATE CLIENTE SET "
                + "nit = ?, nombre = ?, direccion = ?, email = ?, "
                + "nombre_facturar = ?, telefono = ?, id_lista_precio = ?,"
                + "id_estado_cliente = ?, id_empresa = ? "
                + "WHERE nit = ? RETURNING nit", new Object[]{nuevo.getNit(), nuevo.getNombre(), nuevo.getDireccion(),
                    nuevo.getEmail(), nuevo.getNombre_factura(), nuevo.getTelefono(), nuevo.getPrecio(),
                    nuevo.getEstado(), Catalogos.getEmpresa().getId_empresa(), antiguo.getNit()}, new Object[]{"nit","nombre","direccion",
                        "email","nombre_facturar","telefono",1,1,1,"nit"})) {
            Catalogos.mostrarNotificacion("Datos del cliente " + nuevo.getNit() + " modificados correctamente.",  NotificationIcon.information);
        }
    }
}
