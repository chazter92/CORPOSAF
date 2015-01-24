/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Atributo_Valor_Cliente;
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
public class DAO_Atributo_Valor_Cliente {
    
    Conexion conn = new Conexion();
    
    public HashMap<String, BDO_Atributo_Valor_Cliente> todosAtributos(){
        HashMap<String, BDO_Atributo_Valor_Cliente> atributos;
        atributos = toBDO(conn.query("SELECT * FROM ATRIBUTO_VALOR_CLIENTE", new Object[0], new Object[0]));        
        return atributos;
    }

   private HashMap<String, BDO_Atributo_Valor_Cliente> toBDO(RowSet setAtributo){
        HashMap<String, BDO_Atributo_Valor_Cliente> atributos = new HashMap<String, BDO_Atributo_Valor_Cliente>();
        try {
            while(setAtributo.next()){
                BDO_Atributo_Valor_Cliente actual = new BDO_Atributo_Valor_Cliente(setAtributo.getString("valor"),setAtributo.getString("nit"),setAtributo.getInt("id_atributo_cliente"));
                atributos.put(actual.basicsToString(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage()+" "+ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }
        
        return atributos;
    }
   
   public ArrayList<BDO_Atributo_Valor_Cliente> atributosCliente(String nit){
        HashMap<String, BDO_Atributo_Valor_Cliente> atributos;
        atributos = toBDO(conn.query("SELECT A.nombre, B.* FROM atributo_cliente A LEFT JOIN (SELECT D.id_atributo_cliente, D.valor, C.nit FROM cliente C JOIN atributo_valor_cliente D ON C.nit = D.nit WHERE C.nit = ?) as B ON A.id_atributo_cliente = B.id_atributo_cliente ", new Object[]{nit}, new Object[]{nit}));        
        return new ArrayList<BDO_Atributo_Valor_Cliente>(atributos.values());
   } 
}
