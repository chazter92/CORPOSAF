/*
 * Representación en objeto de un registro de la tabla Producto
 */
package BDO;

import java.util.Currency;

/**
 *
 * @author Chaz
 */
public class BDO_Producto {
    String SKU;
    Currency precio_compra;
    String concepto;
    int estado_producto;
    int tipo;
    int categoria;

    public BDO_Producto(String SKU, Currency precio_compra, String concepto, int estado_producto, int tipo, int categoria) {
        this.SKU = SKU;
        this.precio_compra = precio_compra;
        this.concepto = concepto;
        this.estado_producto = estado_producto;
        this.tipo = tipo;
        this.categoria = categoria;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
    
    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public int getEstado_producto() {
        return estado_producto;
    }

    public void setEstado_producto(int estado_producto) {
        this.estado_producto = estado_producto;
    }

    public Currency getPrecio_compra() {
        return precio_compra;
    }

    public void setPrecio_compra(Currency precio_compra) {
        this.precio_compra = precio_compra;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
