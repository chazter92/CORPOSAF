/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Detalle_Factura;
import com.alee.laf.optionpane.WebOptionPane;
import contar.Catalogos;
import contar.Conexion;
import java.sql.SQLException;
import java.util.HashMap;
import javax.sql.RowSet;

/**
 *
 * @author Chaz
 */
public class DAO_Detalle_Factura {
    Conexion conn = new Conexion();
    
    public HashMap<String, BDO_Detalle_Factura> todosDetalles(String no_factura){
        HashMap<String, BDO_Detalle_Factura> detalles;
        detalles = toBDO(conn.query("SELECT * FROM DETALLE_FACTURA WHERE no_factura = ?", new Object[]{no_factura}, new Object[]{"no_factura"}));        
        return detalles;
    }
    
    public HashMap<String, BDO_Detalle_Factura> todosDetalles(){
        HashMap<String, BDO_Detalle_Factura> detalles;
        detalles = toBDO(conn.query("SELECT * FROM DETALLE_FACTURA", new Object[0], new Object[0]));        
        return detalles;
    }
    
    
    private HashMap<String, BDO_Detalle_Factura> toBDO(RowSet setAtributo){
        HashMap<String, BDO_Detalle_Factura> detalles = new HashMap<String, BDO_Detalle_Factura>();
        try {
            while(setAtributo.next()){
                BDO_Detalle_Factura actual = new BDO_Detalle_Factura(setAtributo.getInt("id_detalle_factura"),setAtributo.getString("no_factura"),setAtributo.getString("id_documento"),setAtributo.getString("sku"),setAtributo.getInt("cantidad"), setAtributo.getString("descripcion"), setAtributo.getBigDecimal("subtotal"));
                detalles.put(actual.basicsToString(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage()+" "+ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }
        
        return detalles;
    }
    
    
}
