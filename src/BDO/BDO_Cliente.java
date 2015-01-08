/*
 * Representación en objeto de un registro de la tabla Cliente
 */
package BDO;

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

    public BDO_Cliente(String nit, String nombre, String direccion, String email, String nombre_factura, String telefono) {
        this.nit = nit;
        this.nombre = nombre;
        this.direccion = direccion;
        this.email = email;
        this.nombre_factura = nombre_factura;
        this.telefono = telefono;
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
   
}
