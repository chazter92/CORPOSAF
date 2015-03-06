/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Empresa;
import contar.Conexion;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.RowSet;

/**
 *
 * @author Chaz
 */
public class DAO_Empresa {
    Conexion conn = new Conexion();
    public HashMap<String, BDO_Empresa> todosEmpresas(){
        HashMap<String, BDO_Empresa> empresas;
        empresas = toBDO(conn.query("SELECT * FROM EMPRESA", new Object[0], new Object[0]));   
        return empresas;
    }
    
    private HashMap<String, BDO_Empresa> toBDO(RowSet setEmpresa){
        HashMap<String, BDO_Empresa> empresas = new HashMap<String, BDO_Empresa>();
        try {
            while(setEmpresa.next()){
                BDO_Empresa actual = new BDO_Empresa(setEmpresa.getInt("id_empresa"),setEmpresa.getString("nit"),setEmpresa.getString("nombre_comercial"),setEmpresa.getString("razon_social"),setEmpresa.getString("direccion"),setEmpresa.getString("telefono"),setEmpresa.getString("direccion_web"),setEmpresa.getInt("id_moneda"));
                empresas.put(actual.basicsToString(), actual);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return empresas;
    }
}
