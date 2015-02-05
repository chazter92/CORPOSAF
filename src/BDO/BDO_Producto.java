/*
 * Representación en objeto de un registro de la tabla Producto
 */
package BDO;

import DAO.DAO_Atributo_Valor_Producto;
import java.awt.Image;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author Chaz
 */
public class BDO_Producto {

    String sku, concepto, serie_producto;
    BigDecimal costo;
    int id_estado_producto, id_categoria, id_empresa, id_impuesto_idp;
    boolean afecto_iva, es_producto;
    Image foto;
    ArrayList<BDO_Atributo_Valor_Producto> atributos;
    DAO_Atributo_Valor_Producto connAtributoValorProducto = new DAO_Atributo_Valor_Producto();
    
    public BDO_Producto(String sku, String concepto, String serie_producto, BigDecimal costo, int id_estado_producto, int id_categoria, int id_empresa, int id_impuesto_idp, boolean afecto_iva, boolean es_producto, Image foto) {
        this.sku = sku;
        this.concepto = concepto;
        this.serie_producto = serie_producto;
        this.costo = costo;
        this.id_estado_producto = id_estado_producto;
        this.id_categoria = id_categoria;
        this.id_empresa = id_empresa;
        this.id_impuesto_idp = id_impuesto_idp;
        this.afecto_iva = afecto_iva;
        this.es_producto = es_producto;
        this.foto = foto;
    }
    
    public ArrayList<BDO_Atributo_Valor_Producto> getAtributos() {
        return atributos;
    }

    public void setAtributos(ArrayList<BDO_Atributo_Valor_Producto> atributos) {
        this.atributos = atributos;
    }

    public boolean isAfecto_iva() {
        return afecto_iva;
    }

    public void setAfecto_iva(boolean afecto_iva) {
        this.afecto_iva = afecto_iva;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public boolean isEs_producto() {
        return es_producto;
    }

    public void setEs_producto(boolean es_producto) {
        this.es_producto = es_producto;
    }

    public Image getFoto() {
        return foto;
    }

    public void setFoto(Image foto) {
        this.foto = foto;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public int getId_estado_producto() {
        return id_estado_producto;
    }

    public void setId_estado_producto(int id_estado_producto) {
        this.id_estado_producto = id_estado_producto;
    }

    public int getId_impuesto_idp() {
        return id_impuesto_idp;
    }

    public void setId_impuesto_idp(int id_impuesto_idp) {
        this.id_impuesto_idp = id_impuesto_idp;
    }

    public String getSerie_producto() {
        return serie_producto;
    }

    public void setSerie_producto(String serie_producto) {
        this.serie_producto = serie_producto;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String basicsToString() {
        return this.sku + " | " + this.concepto;
    }

    public Object[] getTypes() {
        return new Object[]{"sku", new BigDecimal(0.0), "concepto", 1, 1, 1, "serie",
                    true, new ImageIcon(), 1, true};
    }
    
    public void cargarAtributos(){
        this.atributos = connAtributoValorProducto.atributosProducto(sku);
    }
}
