/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDO;

/**
 *
 * @author Chaz
 */
public class BDO_Empresa {
    int id_empresa;
    String nit, nombre_comercial, razon_social, direccion;
    Double iva, super_idp, diesel_idp, regular_idp,kerosen_idp;
    int id_moneda;

    public BDO_Empresa(int id_empresa, String nit, String nombre_comercial, String razon_social, String direccion, Double iva, Double super_idp, Double diesel_idp, Double regular_idp, Double kerosen_idp, int id_moneda) {
        this.id_empresa = id_empresa;
        this.nit = nit;
        this.nombre_comercial = nombre_comercial;
        this.razon_social = razon_social;
        this.direccion = direccion;
        this.iva = iva;
        this.super_idp = super_idp;
        this.diesel_idp = diesel_idp;
        this.regular_idp = regular_idp;
        this.kerosen_idp = kerosen_idp;
        this.id_moneda = id_moneda;
    }

    public Double getDiesel_idp() {
        return diesel_idp;
    }

    public void setDiesel_idp(Double diesel_idp) {
        this.diesel_idp = diesel_idp;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public int getId_moneda() {
        return id_moneda;
    }

    public void setId_moneda(int id_moneda) {
        this.id_moneda = id_moneda;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getKerosen_idp() {
        return kerosen_idp;
    }

    public void setKerosen_idp(Double kerosen_idp) {
        this.kerosen_idp = kerosen_idp;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre_comercial() {
        return nombre_comercial;
    }

    public void setNombre_comercial(String nombre_comercial) {
        this.nombre_comercial = nombre_comercial;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public Double getRegular_idp() {
        return regular_idp;
    }

    public void setRegular_idp(Double regular_idp) {
        this.regular_idp = regular_idp;
    }

    public Double getSuper_idp() {
        return super_idp;
    }

    public void setSuper_idp(Double super_idp) {
        this.super_idp = super_idp;
    }
    
    public String basicsToString(){
        return this.nit + " | " + this.nombre_comercial;
    }
}
