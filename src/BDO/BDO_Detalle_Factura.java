/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDO;

import java.math.BigDecimal;

/**
 *
 * @author Chaz
 */
public class BDO_Detalle_Factura {

    int id_detalle_factura;
    String no_factura, id_documento, sku;
    int cantidad;
    String descripcion;
    BigDecimal subtotal;

    public BDO_Detalle_Factura(int id_detalle_factura, String no_factura, String id_documento, String sku, int cantidad, String descripcion, BigDecimal subtotal) {
        this.id_detalle_factura = id_detalle_factura;
        this.no_factura = no_factura;
        this.id_documento = id_documento;
        this.sku = sku;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.subtotal = subtotal;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_detalle_factura() {
        return id_detalle_factura;
    }

    public void setId_detalle_factura(int id_detalle_factura) {
        this.id_detalle_factura = id_detalle_factura;
    }

    public String getId_documento() {
        return id_documento;
    }

    public void setId_documento(String id_documento) {
        this.id_documento = id_documento;
    }

    public String getNo_factura() {
        return no_factura;
    }

    public void setNo_factura(String no_factura) {
        this.no_factura = no_factura;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public String basicsToString(){
        return this.id_detalle_factura + " | " + this.no_factura;
    }
}
