/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Atributo_Valor_Cliente;
import BDO.BDO_Cliente;
import com.alee.laf.optionpane.WebOptionPane;
import contar.Catalogos;
import contar.Conexion;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
            Catalogos.mostrarMensajeError(ex.getMessage() + " " + ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
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
            if (nuevo.getAtributos() != null) {
                for (int i = 0; i < nuevo.getAtributos().size(); i++) {
                    if (nuevo.getAtributos().get(i).getValor() != null) {
                        if (!nuevo.getAtributos().get(i).getValor().toString().trim().isEmpty()) {
                            conn.executeQuery("INSERT INTO ATRIBUTO_VALOR_CLIENTE (valor, nit, id_atributo_cliente) VALUES (?,?,?)",
                                    new Object[]{nuevo.getAtributos().get(i).getValor(), nuevo.getNit(), nuevo.getAtributos().get(i).getAtributo_cliente().getId_atributo_cliente()},
                                    new Object[]{"valor", "nit", 1});

                        }
                    }
                }
            }
            Catalogos.mostrarNotificacion("Cliente " + nuevo.getNit() + " agregado correctamente.", NotificationIcon.information);
        }
    }

    public void actualizarCliente(BDO_Cliente nuevo, BDO_Cliente antiguo) {
        if (conn.executeQuery("UPDATE CLIENTE SET "
                + "nit = ?, nombre = ?, direccion = ?, email = ?, "
                + "nombre_facturar = ?, telefono = ?, id_lista_precio = ?,"
                + "id_estado_cliente = ?, id_empresa = ? "
                + "WHERE nit = ? RETURNING nit", new Object[]{nuevo.getNit(), nuevo.getNombre(), nuevo.getDireccion(),
                    nuevo.getEmail(), nuevo.getNombre_factura(), nuevo.getTelefono(), nuevo.getPrecio(),
                    nuevo.getEstado(), Catalogos.getEmpresa().getId_empresa(), antiguo.getNit()}, new Object[]{"nit", "nombre", "direccion",
                    "email", "nombre_facturar", "telefono", 1, 1, 1, "nit"})) {
            if (nuevo.getAtributos() != null) {
                for (int i = 0; i < nuevo.getAtributos().size(); i++) {
                    if (nuevo.getAtributos().get(i).getValor() != null) {
                        if (!nuevo.getAtributos().get(i).getValor().toString().trim().isEmpty()) {
                            conn.executeQuery("INSERT INTO ATRIBUTO_VALOR_CLIENTE (valor, nit, id_atributo_cliente) VALUES (?,?,?)",
                                    new Object[]{nuevo.getAtributos().get(i).getValor(), nuevo.getNit(), nuevo.getAtributos().get(i).getAtributo_cliente().getId_atributo_cliente()},
                                    new Object[]{"valor", "nit", 1});

                        }
                    }
                }
            }
            Catalogos.mostrarNotificacion("Datos del cliente " + nuevo.getNit() + " modificados correctamente.", NotificationIcon.information);
        }
    }

    public ArrayList<BDO_Cliente> busquedaAvanzada(ArrayList<BDO_Atributo_Valor_Cliente> parametros) {
        HashMap<String, BDO_Cliente> clientes = null;
        String condicion = "";        
        ArrayList objDatos = new ArrayList();
        
        if(parametros.size()>0){
            condicion = "WHERE ";
        }        
        
        for(int i=0;i<parametros.size();i++){
            if(i>0){
                condicion += " AND ";
            }
            condicion += "(A.id_atributo_cliente = ? AND A.valor = ?)";
            objDatos.add(parametros.get(i).getAtributo_cliente().getId_atributo_cliente());
            objDatos.add(parametros.get(i).getValor());
        }
        Object[] datos = objDatos.toArray();

        clientes = toBDO(conn.query("SELECT DISTINCT C.* FROM CLIENTE C LEFT JOIN ATRIBUTO_VALOR_CLIENTE A ON C.nit = A.nit " + condicion, datos, datos));
        return new ArrayList(clientes.values());
    }
}
