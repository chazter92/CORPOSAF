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
    String nit, nombre_comercial, razon_social, direccion, telefono, direccion_web;
    int id_moneda;

    public BDO_Empresa(int id_empresa, String nit, String nombre_comercial, String razon_social, String direccion, String telefono, String direccion_web, int id_moneda) {
        this.id_empresa = id_empresa;
        this.nit = nit;
        this.nombre_comercial = nombre_comercial;
        this.razon_social = razon_social;
        this.direccion = direccion;
        this.telefono = telefono;
        this.direccion_web = direccion_web;
        this.id_moneda = id_moneda;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccion_web() {
        return direccion_web;
    }

    public void setDireccion_web(String direccion_web) {
        this.direccion_web = direccion_web;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String basicsToString(){
        return this.nit + " | " + this.nombre_comercial;
    }
}
