/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDO;

/**
 *
 * @author Chaz
 */
public class BDO_Precio_Cliente {
    int id_lista_precio;
    String tipo;
    Double descuento;

    public BDO_Precio_Cliente(int id_lista_precio, String tipo, Double descuento) {
        this.id_lista_precio = id_lista_precio;
        this.tipo = tipo;
        this.descuento = descuento;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public int getId_lista_precio() {
        return id_lista_precio;
    }

    public void setId_lista_precio(int id_lista_precio) {
        this.id_lista_precio = id_lista_precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String basicsToString(){
        return this.id_lista_precio + " | " + this.tipo + " ("+this.descuento+"%)";
    }
}
