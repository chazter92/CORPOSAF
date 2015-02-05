/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDO;

/**
 *
 * @author Chaz
 */
public class BDO_Estado_Producto {
    int id_estado_producto;
    String tipo;
    Boolean puede_facturar;

    public BDO_Estado_Producto(int id_estado_producto, String tipo, Boolean puede_facturar) {
        this.id_estado_producto = id_estado_producto;
        this.tipo = tipo;
        this.puede_facturar = puede_facturar;
    }

    public int getId_estado_producto() {
        return id_estado_producto;
    }

    public void setId_estado_producto(int id_estado_producto) {
        this.id_estado_producto = id_estado_producto;
    }

    public Boolean getPuede_facturar() {
        return puede_facturar;
    }

    public void setPuede_facturar(Boolean puede_facturar) {
        this.puede_facturar = puede_facturar;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String basicsToString(){
        return this.id_estado_producto + " | " + this.tipo;
    }
}
