/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Categoria_Producto;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.managers.notification.NotificationIcon;
import contar.Catalogos;
import contar.Conexion;
import java.sql.SQLException;
import java.util.HashMap;
import javax.sql.RowSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Chaz
 */
public class DAO_Categoria_Producto {
    Conexion conn = new Conexion();
    
    public HashMap<String, BDO_Categoria_Producto> todasCategorias(){
        HashMap<String, BDO_Categoria_Producto> estados;
        estados = toBDO(conn.query("SELECT * FROM CATEGORIA", new Object[0], new Object[0]));        
        return estados;
    }
    
    private HashMap<String, BDO_Categoria_Producto> toBDO(RowSet setCategoria){
        HashMap<String, BDO_Categoria_Producto> categorias = new HashMap<String, BDO_Categoria_Producto>();
        try {
            while(setCategoria.next()){
                BDO_Categoria_Producto actual = new BDO_Categoria_Producto(setCategoria.getInt("id_categoria"),setCategoria.getString("nombre"),setCategoria.getDouble("margen_ganancia"));
                categorias.put(actual.basicsToString(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage()+" "+ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }
        
        return categorias;
    }

    public HashMap<String, BDO_Categoria_Producto> busquedaCodigoTipo(String buscar) {
        HashMap<String, BDO_Categoria_Producto> categorias = null;
        
        Object[] datos = {"%" + buscar.toLowerCase() + "%", "%" + buscar.toLowerCase() + "%"};
        
        categorias = toBDO(conn.query("SELECT * FROM CATEGORIA "
                + "WHERE LOWER(nombre) LIKE ? UNION "
                + "SELECT * FROM CATEGORIA "
                + "WHERE to_char(id_categoria, '999') LIKE ?", datos, datos));
        return categorias;
    }

    public void actualizarCategoria(BDO_Categoria_Producto nuevo, BDO_Categoria_Producto antiguo) {
         if (conn.executeQuery("UPDATE CATEGORIA SET "
                 + "nombre = ?,"
                 + "margen_ganancia = ?"
                 + "WHERE id_categoria = ? RETURNING id_categoria",
                 new Object[]{nuevo.getNombre(),nuevo.getMargen_ganancia(),antiguo.getId_categoria()},
                 new Object[]{"tipo",true,1})){
             Catalogos.mostrarNotificacion("Datos modificados correctamente.",  NotificationIcon.information);
         }
    }

    public void agregarCategoria(BDO_Categoria_Producto nuevo) {
        if (conn.executeQuery("INSERT INTO CATEGORIA (nombre, margen_ganancia) VALUES (?,?) RETURNING id_categoria",
                new Object[]{nuevo.getNombre(),nuevo.getMargen_ganancia()},
                new Object[]{"nombre",0.0})){
            Catalogos.mostrarNotificacion("Estado " + nuevo.getNombre() + " agregado correctamente.",  NotificationIcon.information);
        }
    }
}
