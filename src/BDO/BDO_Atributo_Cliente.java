/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDO;

/**
 *
 * @author Chaz
 */
public class BDO_Atributo_Cliente {
    int id_atributo_cliente;
    String nombre, descripcion;

    public BDO_Atributo_Cliente(int id_atributo_cliente, String nombre, String descripcion) {
        this.id_atributo_cliente = id_atributo_cliente;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_atributo_cliente() {
        return id_atributo_cliente;
    }

    public void setId_atributo_cliente(int id_atributo_cliente) {
        this.id_atributo_cliente = id_atributo_cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String basicsToString() {
        return this.id_atributo_cliente + " | " + this.nombre;
    }
}
