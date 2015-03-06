/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDO;

/**
 *
 * @author Chaz
 */
public class BDO_Impuesto {
    private int id_impuesto;
    private String nombre;
    private double valor;
    private boolean es_idp;
    private boolean aplica_precio_venta;
    private int id_empresa;

    public BDO_Impuesto(int id_impuesto, String nombre, double valor, boolean es_idp, boolean aplica_precio_venta, int id_empresa) {
        this.id_impuesto = id_impuesto;
        this.nombre = nombre;
        this.valor = valor;
        this.es_idp = es_idp;
        this.id_empresa = id_empresa;
        this.aplica_precio_venta = aplica_precio_venta;
    }

    public boolean isAplica_precio_venta() {
        return aplica_precio_venta;
    }

    public void setAplica_precio_venta(boolean aplica_precio_venta) {
        this.aplica_precio_venta = aplica_precio_venta;
    }

    
    
    public boolean isEs_idp() {
        return es_idp;
    }

    public void setEs_idp(boolean es_idp) {
        this.es_idp = es_idp;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public int getId_impuesto() {
        return id_impuesto;
    }

    public void setId_impuesto(int id_impuesto) {
        this.id_impuesto = id_impuesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    
    public String basicsToString(){
        return this.id_impuesto + " | " + this.nombre + " ("+this.valor+"%)";
    }
    
}
