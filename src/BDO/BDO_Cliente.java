 /*
 * Representación en objeto de un registro de la tabla Cliente
 */
package BDO;

import DAO.DAO_Atributo_Valor_Cliente;
import java.util.ArrayList;

/**
 *
 * @author Chaz
 */
public class BDO_Cliente {
    String nit;
    String nombre;
    String direccion;
    String email;
    String nombre_factura;
    String telefono;
    int precio;
    int estado;
    ArrayList<BDO_Atributo_Valor_Cliente> atributos;
    DAO_Atributo_Valor_Cliente connAtributoValorCliente = new DAO_Atributo_Valor_Cliente();

    public BDO_Cliente(String nit, String nombre, String direccion, String email, String nombre_factura, String telefono, int precio, int estado) {
        this.nit = nit;
        this.nombre = nombre;
        this.direccion = direccion;
        this.email = email;
        this.nombre_factura = nombre_factura;
        this.telefono = telefono;
        this.precio = precio;
        this.estado = estado;
        //this.atributos = connAtributoValorCliente.atributosCliente(nit);
    }

    public ArrayList<BDO_Atributo_Valor_Cliente> getAtributos() {
        return atributos;
    }

    public void setAtributos(ArrayList<BDO_Atributo_Valor_Cliente> atributos) {
        this.atributos = atributos;
    }
    
    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre_factura() {
        return nombre_factura;
    }

    public void setNombre_factura(String nombre_factura) {
        this.nombre_factura = nombre_factura;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String basicsToString(){
        return nit + "  | " + nombre;
    }

    public Object[] getTypes() {
        return new Object[] {"nit","nombre","direccion","email","nombrefacturar","telefono",1,1,1};
    }
   
}
