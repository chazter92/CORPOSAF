/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar;

import BDO.BDO_Cliente;
import DAO.DAO_Cliente;
import java.util.ArrayList;

/**
 *
 * @author Chaz
 */
public class Catalogos {
    private static ArrayList<BDO_Cliente> clientes;
    private DAO_Cliente connCliente = new DAO_Cliente();
    
    private void actualizarClientes(){
        clientes = connCliente.todosClientes();        
    }

    public static ArrayList<BDO_Cliente> getClientes() {
        return clientes;
    }
    
    
}
