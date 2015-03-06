/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDO;

/**
 *
 * @author Chaz
 */
public class BDO_Tipo_Pago_Venta {
    private int id_tipo_pago;
    private String nombre;

    public BDO_Tipo_Pago_Venta(int id_tipo_pago, String nombre) {
        this.id_tipo_pago = id_tipo_pago;
        this.nombre = nombre;
    }

    public int getId_tipo_pago() {
        return id_tipo_pago;
    }

    public void setId_tipo_pago(int id_tipo_pago) {
        this.id_tipo_pago = id_tipo_pago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String basicsToString(){
        return this.id_tipo_pago + " | " + this.nombre;
    }
}
