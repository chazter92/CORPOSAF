/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDO;

/**
 *
 * @author Chaz
 */
public class BDO_Categoria_Producto {
    int id_categoria;
    String nombre;
    double margen_ganancia;

    public BDO_Categoria_Producto(int id_categoria, String nombre, double margen_ganancia) {
        this.id_categoria = id_categoria;
        this.nombre = nombre;
        this.margen_ganancia = margen_ganancia;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria_producto(int id_categoria_producto) {
        this.id_categoria = id_categoria_producto;
    }

    public double getMargen_ganancia() {
        return margen_ganancia;
    }

    public void setMargen_ganancia(double margen_ganancia) {
        this.margen_ganancia = margen_ganancia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String basicsToString() {
        return id_categoria+ " | " + nombre;
    }
    
    
}
