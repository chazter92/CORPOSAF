/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Atributo_Valor_Producto;
import BDO.BDO_Categoria_Producto;
import BDO.BDO_Estado_Producto;
import BDO.BDO_Impuesto;
import BDO.BDO_Producto;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.managers.notification.NotificationIcon;
import contar.Catalogos;
import contar.Conexion;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.sql.RowSet;
import javax.swing.ImageIcon;

/**
 *
 * @author Chaz
 */
public class DAO_Producto {

    Conexion conn = new Conexion();

    private Image convertirImagen(byte[] bytes) throws IOException {
        if (bytes != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            Iterator readers = ImageIO.getImageReadersByFormatName("jpeg");
            ImageReader reader = (ImageReader) readers.next();
            Object source = bis;
            ImageInputStream iis = ImageIO.createImageInputStream(source);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            return reader.read(0, param);
        }
        return null;
    }

    public HashMap<String, BDO_Producto> todosProductos() {
        HashMap<String, BDO_Producto> productos;
        productos = toBDO(conn.query("SELECT * FROM PRODUCTO", new Object[0], new Object[0]));
        return productos;
    }
    
    public HashMap<String, BDO_Producto> productosActivos() {
        HashMap<String, BDO_Producto> productos;
        productos = toBDO(conn.query("SELECT * FROM producto WHERE id_estado_producto IN"
                + " (SELECT id_estado_producto FROM estado_producto WHERE puede_facturar = TRUE)", new Object[0], new Object[0]));
        return productos;
    }

    private HashMap<String, BDO_Producto> toBDO(RowSet setProducto) {
        HashMap<String, BDO_Producto> productos = new HashMap<String, BDO_Producto>();
        try {
            while (setProducto.next()) {
                BDO_Producto actual = new BDO_Producto(setProducto.getString("sku"), setProducto.getString("concepto"), setProducto.getString("serie"), setProducto.getString("lote"), setProducto.getBigDecimal("precio_costo"), setProducto.getInt("id_estado_producto"), setProducto.getInt("id_categoria"), setProducto.getInt("id_empresa"), setProducto.getInt("id_impuesto_idp"), setProducto.getBoolean("afecto_iva"), setProducto.getBoolean("es_producto"), null);
                try {
                    actual.setFoto(convertirImagen(setProducto.getBytes("foto")));
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
                productos.put(actual.getSku(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage() + " " + ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }

        return productos;
    }

    public HashMap<String, BDO_Producto> busquedaSKUConcepto(String buscar) {
        HashMap<String, BDO_Producto> productos = null;
        Object[] datos = {"%" + buscar.toLowerCase() + "%", "%" + buscar.toLowerCase() + "%"};

        productos = toBDO(conn.query("SELECT * FROM PRODUCTO "
                + "WHERE LOWER(concepto) LIKE ? UNION "
                + "SELECT * FROM PRODUCTO "
                + "WHERE LOWER(sku) LIKE ?", datos, datos));
        return productos;
    }

    public HashMap<String, BDO_Producto> busquedaSKUConceptoActivo(String buscar) {
        HashMap<String, BDO_Producto> productos = null;
        Object[] datos = {"%" + buscar.toLowerCase() + "%", "%" + buscar.toLowerCase() + "%"};

        productos = toBDO(conn.query("SELECT * FROM PRODUCTO "
                + "WHERE LOWER(concepto) LIKE ? AND id_estado_producto IN "
                + "(SELECT id_estado_producto FROM estado_producto WHERE puede_facturar = TRUE)"
                + "UNION SELECT * FROM PRODUCTO "
                + "WHERE LOWER(sku) LIKE ? AND id_estado_producto IN"
                + "(SELECT id_estado_producto FROM estado_producto WHERE puede_facturar = TRUE)", datos, datos));
        return productos;
    }
    
    public void agregarProducto(BDO_Producto nuevo) {
        
        
        String condicionImpuesto = "";
        String valueImpuesto = "";
        Object[] datos = new Object[]{};
        Object[] tiposDatos = new Object[]{};
        if (nuevo.getId_impuesto_idp() > 0) {
            condicionImpuesto = "id_impuesto_idp,";
            valueImpuesto = "?,";
            datos = new Object[]{nuevo.getSku(),nuevo.getCosto(), nuevo.getConcepto(), nuevo.getId_estado_producto(),
                nuevo.getId_categoria(), Catalogos.getEmpresa().getId_empresa(), nuevo.getSerie_producto(), nuevo.isAfecto_iva(),
                nuevo.getFoto(), nuevo.getId_impuesto_idp(), nuevo.isEs_producto()};

            tiposDatos = new Object[]{"sku",new BigDecimal(0.0), "concepto", 1, 1, 1, "serie", true, new ImageIcon(), 1, true};
        } else {
            condicionImpuesto = "";
            valueImpuesto = "";
            datos = new Object[]{nuevo.getSku(),nuevo.getCosto(), nuevo.getConcepto(), nuevo.getId_estado_producto(),
                nuevo.getId_categoria(), Catalogos.getEmpresa().getId_empresa(), nuevo.getSerie_producto(), nuevo.isAfecto_iva(),
                nuevo.getFoto(), nuevo.isEs_producto()};

            tiposDatos = new Object[]{"sku",new BigDecimal(0.0), "concepto", 1, 1, 1, "serie", true, new ImageIcon(), true};
        }

        
        if (conn.executeQuery("INSERT INTO PRODUCTO (sku, precio_costo, concepto, id_estado_producto, id_categoria, id_empresa, serie, afecto_iva, foto, "+condicionImpuesto+" es_producto) "
                + "VALUES (?,?,?,?,?,?,?,?,"+valueImpuesto+"?,?) RETURNING sku",
                datos,tiposDatos)) {
            if (nuevo.getAtributos() != null) {
                for (int i = 0; i < nuevo.getAtributos().size(); i++) {
                    if (nuevo.getAtributos().get(i).getValor() != null) {
                        if (!nuevo.getAtributos().get(i).getValor().toString().trim().isEmpty()) {
                            conn.executeQuery("INSERT INTO ATRIBUTO_VALOR_PRODUCTO (valor, sku, id_atributo_producto) VALUES (?,?,?)",
                                    new Object[]{nuevo.getAtributos().get(i).getValor(), nuevo.getSku(), nuevo.getAtributos().get(i).getAtributo_producto().getId_atributo_producto()},
                                    new Object[]{"valor", "nit", 1});

                        }
                    }
                }
            }
            Catalogos.mostrarNotificacion("Producto " + nuevo.getSku() + " agregado correctamente.", NotificationIcon.information);
        }
    }

    public void actualizarProducto(BDO_Producto nuevo, BDO_Producto antiguo) {

        String condicionImpuesto = "";
        Object[] datos = new Object[]{};
        Object[] tiposDatos = new Object[]{};
        if (nuevo.getId_impuesto_idp() > 0) {
            condicionImpuesto = "id_impuesto_idp = ?,";
            datos = new Object[]{nuevo.getCosto(), nuevo.getConcepto(), nuevo.getId_estado_producto(),
                nuevo.getId_categoria(), Catalogos.getEmpresa().getId_empresa(), nuevo.getSerie_producto(), nuevo.isAfecto_iva(),
                nuevo.getFoto(), nuevo.getId_impuesto_idp(), nuevo.isEs_producto(), antiguo.getSku()};

            tiposDatos = new Object[]{new BigDecimal(0.0), "concepto", 1, 1, 1, "serie", true, new ImageIcon(), 1, true, "sku"};
        } else {
            condicionImpuesto = "";
            datos = new Object[]{nuevo.getCosto(), nuevo.getConcepto(), nuevo.getId_estado_producto(),
                nuevo.getId_categoria(), Catalogos.getEmpresa().getId_empresa(), nuevo.getSerie_producto(), nuevo.isAfecto_iva(),
                nuevo.getFoto(), nuevo.isEs_producto(), antiguo.getSku()};

            tiposDatos = new Object[]{new BigDecimal(0.0), "concepto", 1, 1, 1, "serie", true, new ImageIcon(), true, "sku"};
        }

        if (conn.executeQuery("UPDATE PRODUCTO SET "
                + "precio_costo = ?, concepto = ?, id_estado_producto = ?, "
                + "id_categoria = ?, id_empresa = ?, serie = ?,"
                + "afecto_iva = ?, foto = ?, " + condicionImpuesto + "  es_producto = ?"
                + "WHERE sku = ? RETURNING sku",datos,tiposDatos)) {
            if (nuevo.getAtributos() != null) {
                for (int i = 0; i < nuevo.getAtributos().size(); i++) {
                    if (nuevo.getAtributos().get(i).getValor() != null) {
                        if (!nuevo.getAtributos().get(i).getValor().toString().trim().isEmpty()) {
                            conn.executeQuery("INSERT INTO ATRIBUTO_VALOR_PRODUCTO (valor, sku, id_atributo_producto) VALUES (?,?,?)",
                                    new Object[]{nuevo.getAtributos().get(i).getValor(), nuevo.getSku(), nuevo.getAtributos().get(i).getAtributo_producto().getId_atributo_producto()},
                                    new Object[]{"valor", "sku", 1});

                        }
                    }
                }
            }
            Catalogos.mostrarNotificacion("Datos del producto " + nuevo.getSku() + " modificados correctamente.", NotificationIcon.information);
        }
    }

    public ArrayList<BDO_Producto> busquedaAvanzada(ArrayList<BDO_Atributo_Valor_Producto> parametros, BDO_Categoria_Producto categoria, BDO_Estado_Producto estado, BDO_Impuesto impuesto) {
        HashMap<String, BDO_Producto> Productos = null;
        String condicion = "";
        ArrayList objDatos = new ArrayList();

        if (parametros.size() > 0 || categoria != null || estado != null) {
            condicion = "WHERE ";
        }
        int i = 0;
        for (i = 0; i < parametros.size(); i++) {
            if (i > 0) {
                condicion += " AND ";
            }
            condicion += "(A.id_atributo_producto = ? AND A.valor = ?)";
            objDatos.add(parametros.get(i).getAtributo_producto().getId_atributo_producto());
            objDatos.add(parametros.get(i).getValor());
        }

        if (categoria != null) {
            if (i > 0) {
                condicion += " AND ";
            }
            condicion += "C.id_categoria = ?";
            objDatos.add(categoria.getId_categoria());
            i++;
        }

        if (estado != null) {
            if (i > 0) {
                condicion += " AND ";
            }
            condicion += "C.id_estado_producto = ?";
            objDatos.add(estado.getId_estado_producto());
            i++;
        }

        if (impuesto != null) {
            if (i > 0) {
                condicion += " AND ";
            }
            condicion += "C.id_impuesto_idp = ?";
            objDatos.add(impuesto.getId_impuesto());
        }

        Object[] datos = objDatos.toArray();

        Productos = toBDO(conn.query("SELECT DISTINCT C.* FROM PRODUCTO C LEFT JOIN ATRIBUTO_VALOR_PRODUCTO A ON C.sku = A.sku " + condicion, datos, datos));
        return new ArrayList(Productos.values());
    }
}
