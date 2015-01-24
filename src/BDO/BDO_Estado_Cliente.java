/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BDO;

/**
 *
 * @author Chaz
 */
public class BDO_Estado_Cliente {
    int id_estado_cliente;
    String tipo;
    Boolean puede_facturar;

    public BDO_Estado_Cliente(int id_estado_cliente, String tipo, Boolean puede_facturar) {
        this.id_estado_cliente = id_estado_cliente;
        this.tipo = tipo;
        this.puede_facturar = puede_facturar;
    }

    public int getId_estado_cliente() {
        return id_estado_cliente;
    }

    public void setId_estado_cliente(int id_estado_cliente) {
        this.id_estado_cliente = id_estado_cliente;
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
        return this.id_estado_cliente + " | " + this.tipo;
    }
}
