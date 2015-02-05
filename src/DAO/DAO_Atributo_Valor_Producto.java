/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Atributo_Valor_Producto;
import com.alee.laf.optionpane.WebOptionPane;
import contar.Catalogos;
import contar.Conexion;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sql.RowSet;

/**
 *
 * @author Chaz
 */
public class DAO_Atributo_Valor_Producto {
    Conexion conn = new Conexion();
    
    public HashMap<String, BDO_Atributo_Valor_Producto> todosAtributosProducto(){
        HashMap<String, BDO_Atributo_Valor_Producto> atributosProducto;
        atributosProducto = toBDO(conn.query("SELECT * FROM ATRIBUTO_VALOR_PRODUCTO", new Object[0], new Object[0]));        
        return atributosProducto;
    }
    
    private HashMap<String, BDO_Atributo_Valor_Producto> toBDO(RowSet setAtributo){
        HashMap<String, BDO_Atributo_Valor_Producto> atributos = new HashMap<String, BDO_Atributo_Valor_Producto>();
        try {
            while(setAtributo.next()){
                BDO_Atributo_Valor_Producto actual = new BDO_Atributo_Valor_Producto(setAtributo.getString("valor"),setAtributo.getString("sku"),setAtributo.getInt("id_atributo_producto"));
                atributos.put(actual.basicsToString(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage()+" "+ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }
        
        return atributos;
    }
    
    private HashMap<String, BDO_Atributo_Valor_Producto> toBDOValor(RowSet setAtributo){
        HashMap<String, BDO_Atributo_Valor_Producto> atributos = new HashMap<String, BDO_Atributo_Valor_Producto>();
        try {
            while(setAtributo.next()){
                BDO_Atributo_Valor_Producto actual = new BDO_Atributo_Valor_Producto(setAtributo.getString("valor"),"",0);
                atributos.put(actual.basicsToString(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage()+" "+ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }
        
        return atributos;
    }
    
    public ArrayList<BDO_Atributo_Valor_Producto> atributosProducto(String sku){
        HashMap<String, BDO_Atributo_Valor_Producto> atributos;
        atributos = toBDO(conn.query("SELECT A.id_atributo_producto, A.nombre, B.valor, B.sku FROM atributo_producto A LEFT JOIN (SELECT D.id_atributo_producto, D.valor, C.sku FROM PRODUCTO C JOIN atributo_valor_producto D ON C.sku = D.sku WHERE C.sku = ?) as B ON A.id_atributo_producto = B.id_atributo_producto ORDER BY A.id_atributo_producto", new Object[]{sku}, new Object[]{sku}));        
        return new ArrayList<BDO_Atributo_Valor_Producto>(atributos.values());
   }
    
    public ArrayList<BDO_Atributo_Valor_Producto> valoresAtributo(int id_atributo_producto){
        HashMap<String, BDO_Atributo_Valor_Producto> valores;
        valores = toBDOValor(conn.query("SELECT DISTINCT valor FROM ATRIBUTO_VALOR_PRODUCTO WHERE id_atributo_producto = ?", new Object[]{id_atributo_producto}, new Object[]{id_atributo_producto}));        
        return new ArrayList<BDO_Atributo_Valor_Producto>(valores.values());
   }
}
