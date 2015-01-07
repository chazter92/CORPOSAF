/*
 * Representación en objeto de un registro de la tabla Factura
 */
package BDO;

import java.util.Currency;
import java.util.Date;

/**
 *
 * @author Chaz
 */
public class BDO_Factura {
    int no_factura;
    Date fecha;
    Currency total;
    String id_documento;
    String nit;
    int tipo_pago;
    String voucher;

    public BDO_Factura(int no_factura, Date fecha, Currency total, String id_documento, String nit, int tipo_pago, String voucher) {
        this.no_factura = no_factura;
        this.fecha = fecha;
        this.total = total;
        this.id_documento = id_documento;
        this.nit = nit;
        this.tipo_pago = tipo_pago;
        this.voucher = voucher;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getId_documento() {
        return id_documento;
    }

    public void setId_documento(String id_documento) {
        this.id_documento = id_documento;
    }

    public int getTipo_pago() {
        return tipo_pago;
    }

    public void setTipo_pago(int tipo_pago) {
        this.tipo_pago = tipo_pago;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public int getNo_factura() {
        return no_factura;
    }

    public void setNo_factura(int no_factura) {
        this.no_factura = no_factura;
    }

    public Currency getTotal() {
        return total;
    }

    public void setTotal(Currency total) {
        this.total = total;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }
}
