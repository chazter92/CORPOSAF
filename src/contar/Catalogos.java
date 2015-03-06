/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar;

import DAO.DAO_Atributo_Producto;
import BDO.BDO_Atributo_Cliente;
import BDO.BDO_Atributo_Producto;
import BDO.BDO_Categoria_Producto;
import BDO.BDO_Cliente;
import BDO.BDO_Detalle_Factura;
import BDO.BDO_Documento;
import BDO.BDO_Empresa;
import BDO.BDO_Estado_Cliente;
import BDO.BDO_Estado_Producto;
import BDO.BDO_Impuesto;
import BDO.BDO_Precio_Cliente;
import BDO.BDO_Tipo_Pago_Venta;
import BDO.BDO_Producto;
import DAO.DAO_Atributo_Cliente;
import DAO.DAO_Categoria_Producto;
import DAO.DAO_Cliente;
import DAO.DAO_Detalle_Factura;
import DAO.DAO_Documento;
import DAO.DAO_Empresa;
import DAO.DAO_Estado_Cliente;
import DAO.DAO_Estado_Producto;
import DAO.DAO_Impuesto;
import DAO.DAO_Precio_Cliente;
import DAO.DAO_Precio_Venta;
import DAO.DAO_Producto;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotificationPopup;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 *
 * @author Chaz
 */
public class Catalogos {

    private static Map<String, BDO_Cliente> clientes = new HashMap<String, BDO_Cliente>();
    private static Map<String, BDO_Cliente> clientesActivos = new HashMap<String, BDO_Cliente>();
    private static Map<String, BDO_Precio_Cliente> precios_cliente = new HashMap<String, BDO_Precio_Cliente>();
    private static Map<String, BDO_Estado_Cliente> estados_cliente = new HashMap<String, BDO_Estado_Cliente>();
    private static Map<String, BDO_Categoria_Producto> categorias_producto = new HashMap<String, BDO_Categoria_Producto>();
    private static Map<String, BDO_Atributo_Cliente> atributos_cliente = new HashMap<String, BDO_Atributo_Cliente>();
    private static Map<String, BDO_Impuesto> impuestos = new HashMap<String, BDO_Impuesto>();
    private static Map<String, BDO_Producto> productos = new HashMap<String, BDO_Producto>();
    private static Map<String, BDO_Producto> productosActivos = new HashMap<String, BDO_Producto>();
    private static Map<String, BDO_Atributo_Producto> atributos_producto = new HashMap<String, BDO_Atributo_Producto>();
    private static Map<String, BDO_Estado_Producto> estados_producto = new HashMap<String, BDO_Estado_Producto>();
    private static Map<String, BDO_Tipo_Pago_Venta> tipo_pago_venta = new HashMap<String, BDO_Tipo_Pago_Venta>();
    private static Map<String, BDO_Documento> documentos = new HashMap<String, BDO_Documento>();
    private static Map<String, BDO_Detalle_Factura> detalles = new HashMap<String, BDO_Detalle_Factura>();
    private static BDO_Empresa empresa;

    static void cargarCatologos() {
        actualizarEmpresas();
        actualizarDocumentos();
        actualizarAtributosCliente();
        actualizarPreciosClientes();
        actualizarEstadosClientes();
        actualizarClientes();
        actualizarClientesActivos();
        actualizarCategoriasProducto();
        actualizarImpuestos();
        actualizarProductos();
        actualizarProductosActivos();
        actualizarAtributosProducto();
        actualizarEstadosProducto();
        actualizarTiposPagoVenta();
        actualizarDetallesFactura();
    }
    private static DAO_Cliente connCliente = new DAO_Cliente();
    private static DAO_Precio_Cliente connPreciosCliente = new DAO_Precio_Cliente();
    private static DAO_Estado_Cliente connEstadosCliente = new DAO_Estado_Cliente();
    private static DAO_Empresa connEmpresa = new DAO_Empresa();
    private static DAO_Categoria_Producto connCategoria = new DAO_Categoria_Producto();
    private static DAO_Atributo_Cliente connAtributoCliente = new DAO_Atributo_Cliente();
    private static DAO_Impuesto connImpuesto = new DAO_Impuesto();
    private static DAO_Producto connProducto = new DAO_Producto();
    private static DAO_Atributo_Producto connAtributoProducto = new DAO_Atributo_Producto();
    private static DAO_Estado_Producto connEstadoProducto = new DAO_Estado_Producto();
    private static DAO_Precio_Venta connPrecioVenta = new DAO_Precio_Venta();
    private static DAO_Documento connDocumento = new DAO_Documento();
    private static DAO_Detalle_Factura connDetalle = new DAO_Detalle_Factura();

    public static BDO_Detalle_Factura buscarDetalle(String clave) {
        if (detalles.containsKey(clave)) {
            return detalles.get(clave);
        }
        return null;
    }

    public static BDO_Documento buscarDocumento(String clave) {
        if (documentos.containsKey(clave)) {
            return documentos.get(clave);
        }
        return null;
    }

    public static BDO_Tipo_Pago_Venta buscarTipoPagoVenta(String clave) {
        if (tipo_pago_venta.containsKey(clave)) {
            return tipo_pago_venta.get(clave);
        }
        return null;
    }

    public static BDO_Estado_Producto buscarEstadoProducto(String clave) {
        if (estados_producto.containsKey(clave)) {
            return estados_producto.get(clave);
        }
        return null;
    }

    public static BDO_Atributo_Producto buscarAtributoProducto(String clave) {
        if (atributos_producto.containsKey(clave)) {
            return atributos_producto.get(clave);
        }
        return null;
    }

    public static BDO_Producto buscarProducto(String clave) {
        if (productos.containsKey(clave)) {
            return productos.get(clave);
        }
        return null;
    }
    
    public static BDO_Producto buscarProductoActivo(String clave) {
        if (productosActivos.containsKey(clave)) {
            return productosActivos.get(clave);
        }
        return null;
    }

    public static BDO_Impuesto buscarImpuesto(String clave) {
        if (impuestos.containsKey(clave)) {
            return impuestos.get(clave);
        }
        return null;
    }

    public static BDO_Cliente buscarCliente(String nit) {
        if (clientes.containsKey(nit)) {
            return clientes.get(nit);
        }
        return null;
    }
    
    public static BDO_Cliente buscarClienteActivo(String nit) {
        if (clientesActivos.containsKey(nit)) {
            return clientesActivos.get(nit);
        }
        return null;
    }

    public static BDO_Precio_Cliente buscarPrecioCliente(String clave) {
        if (precios_cliente.containsKey(clave)) {
            return precios_cliente.get(clave);
        }
        return null;
    }

    public static BDO_Atributo_Cliente buscarAtributoCliente(String clave) {
        if (atributos_cliente.containsKey(clave)) {
            return atributos_cliente.get(clave);
        }
        return null;
    }

    public static BDO_Estado_Cliente buscarEstadoCliente(String clave) {
        if (estados_cliente.containsKey(clave)) {
            return estados_cliente.get(clave);
        }
        return null;
    }

    public static BDO_Categoria_Producto buscarCategoriaProducto(String clave) {
        if (categorias_producto.containsKey(clave)) {
            return categorias_producto.get(clave);
        }
        return null;
    }

    public static void actualizarDetallesFactura() {
        detalles = connDetalle.todosDetalles();
    }

    public static void actualizarDocumentos() {
        documentos = connDocumento.todosDocumentos();
    }

    public static void actualizarTiposPagoVenta() {
        tipo_pago_venta = connPrecioVenta.todosPreciosVenta();
    }

    public static void actualizarEstadosProducto() {
        estados_producto = connEstadoProducto.todosEstados();
    }

    public static void actualizarAtributosProducto() {
        atributos_producto = connAtributoProducto.todosAtributosProducto();
    }

    public static void actualizarProductosActivos() {
        productosActivos = connProducto.productosActivos();
    }
    
    public static void actualizarProductos() {
        productos = connProducto.todosProductos();
    }

    public static void actualizarImpuestos() {
        impuestos = connImpuesto.todosImpuestos();
    }

    public static void actualizarClientes() {
        clientes = connCliente.todosClientes();
    }
    
    public static void actualizarClientesActivos() {
        clientesActivos = connCliente.clientesActivos();
    }

    public static void actualizarPreciosClientes() {
        precios_cliente = connPreciosCliente.todosPrecios();
    }

    public static void actualizarAtributosCliente() {
        atributos_cliente = connAtributoCliente.todosAtributos();
    }

    public static void actualizarEstadosClientes() {
        estados_cliente = connEstadosCliente.todosEstados();
    }

    public static void actualizarCategoriasProducto() {
        categorias_producto = connCategoria.todasCategorias();
    }

    public static void actualizarEmpresas() {
        ArrayList<BDO_Empresa> empresas = new ArrayList(connEmpresa.todosEmpresas().values());
        empresa = empresas.get(0);
    }

    public static ArrayList<BDO_Detalle_Factura> getDetalleFactura() {
        if (detalles.values() == null) {
            return null;
        }
        return new ArrayList(detalles.values());
    }

    public static ArrayList<BDO_Documento> getDocumentos() {
        return new ArrayList(documentos.values());
    }

    public static ArrayList<BDO_Tipo_Pago_Venta> getTiposPagoVenta() {
        return new ArrayList(tipo_pago_venta.values());
    }

    public static ArrayList<BDO_Estado_Producto> getEstados_Producto() {
        return new ArrayList(estados_producto.values());
    }

    public static ArrayList<BDO_Atributo_Producto> getAtributosProducto() {
        return new ArrayList(atributos_producto.values());
    }

    public static ArrayList<BDO_Producto> getProductos() {
        return new ArrayList(productos.values());
    }
    

    public static ArrayList<BDO_Producto> getProductosActivos() {
        return new ArrayList(productosActivos.values());
    }
    
    public static ArrayList<BDO_Impuesto> getImpuestos() {
        return new ArrayList(impuestos.values());
    }

    public static ArrayList<BDO_Impuesto> getImpuestosIDP() {
        return new ArrayList(connImpuesto.todosImpuestosIDP().values());
    }

    public static ArrayList<BDO_Cliente> getClientes() {
        return new ArrayList(clientes.values());
    }

    public static ArrayList<BDO_Cliente> getClientesActivos() {
        return new ArrayList(clientesActivos.values());
    }
    
    public static ArrayList<BDO_Precio_Cliente> getPrecios_cliente() {
        return new ArrayList(precios_cliente.values());
    }

    public static ArrayList<BDO_Atributo_Cliente> getAtributos_cliente() {
        return new ArrayList(atributos_cliente.values());
    }

    public static ArrayList< BDO_Estado_Cliente> getEstados_cliente() {
        return new ArrayList(estados_cliente.values());
    }

    public static ArrayList< BDO_Categoria_Producto> getCategorias_Producto() {
        return new ArrayList(categorias_producto.values());
    }

    public static BDO_Empresa getEmpresa() {
        return empresa;
    }

    public static BDO_Documento buscarDocumentoInt(String id) {
        ArrayList<BDO_Documento> documento = new ArrayList(documentos.values());
        for (int i = 0; i < documento.size(); i++) {
            if (documento.get(i).getId_documento().equals(id)) {
                return documento.get(i);
            }
        }
        return null;
    }

    public static BDO_Impuesto buscarImpuestoInt(int id) {
        ArrayList<BDO_Impuesto> impuesto = new ArrayList(impuestos.values());
        for (int i = 0; i < impuesto.size(); i++) {
            if (impuesto.get(i).getId_impuesto() == id) {
                return impuesto.get(i);
            }
        }
        return null;
    }

    public static BDO_Precio_Cliente buscarPrecioClienteInt(int id) {
        ArrayList<BDO_Precio_Cliente> precios = new ArrayList(precios_cliente.values());
        for (int i = 0; i < precios.size(); i++) {
            if (precios.get(i).getId_lista_precio() == id) {
                return precios.get(i);
            }
        }
        return null;
    }

    public static BDO_Atributo_Producto buscarAtributoProductoInt(int id) {
        ArrayList<BDO_Atributo_Producto> atributos_productos = new ArrayList(atributos_producto.values());
        for (int i = 0; i < atributos_productos.size(); i++) {
            if (atributos_productos.get(i).getId_atributo_producto() == id) {
                return atributos_productos.get(i);
            }
        }
        return null;
    }

    public static BDO_Atributo_Cliente buscarAtributoClienteInt(int id) {
        ArrayList<BDO_Atributo_Cliente> atributos = new ArrayList(atributos_cliente.values());
        for (int i = 0; i < atributos.size(); i++) {
            if (atributos.get(i).getId_atributo_cliente() == id) {
                return atributos.get(i);
            }
        }
        return null;
    }

    public static BDO_Estado_Producto buscarEstadoProductoInt(int id) {
        ArrayList<BDO_Estado_Producto> estados = new ArrayList(estados_producto.values());
        for (int i = 0; i < estados.size(); i++) {
            if (estados.get(i).getId_estado_producto() == id) {
                return estados.get(i);
            }
        }
        return null;
    }

    public static BDO_Estado_Cliente buscarEstadoClienteInt(int id) {
        ArrayList<BDO_Estado_Cliente> estados = new ArrayList(estados_cliente.values());
        for (int i = 0; i < estados.size(); i++) {
            if (estados.get(i).getId_estado_cliente() == id) {
                return estados.get(i);
            }
        }
        return null;
    }

    public static BDO_Categoria_Producto buscarCategoriaProductoInt(int id) {
        ArrayList<BDO_Categoria_Producto> categorias = new ArrayList(categorias_producto.values());
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getId_categoria() == id) {
                return categorias.get(i);
            }
        }
        return null;
    }

    public static BDO_Tipo_Pago_Venta buscarTipoPagoVentaInt(int id) {
        ArrayList<BDO_Tipo_Pago_Venta> precios = new ArrayList(tipo_pago_venta.values());
        for (int i = 0; i < precios.size(); i++) {
            if (precios.get(i).getId_tipo_pago() == id) {
                return precios.get(i);
            }
        }
        return null;
    }

    public static void mostrarNotificacion(String notificacion, NotificationIcon icono) {
        WebNotificationPopup notificationPopup = new WebNotificationPopup();
        notificationPopup.setIcon(icono);
        notificationPopup.setDisplayTime(5000);

        notificationPopup.setContent(new GroupPanel(new WebLabel(notificacion)));
        NotificationManager.showNotification(notificationPopup);

    }

    public static void mostrarMensajeError(String mensaje, String titulo, int icon) {
        WebOptionPane.showMessageDialog(null, mensaje, titulo, icon);
    }

    public static String getFechaCorta() {
        Locale l = new Locale("es", "GT");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Guatemala"), l);
        return cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
    }

    public static String getFechaLarga() {
        Locale l = new Locale("es", "GT");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Guatemala"), l);
        return getDiaSemana(cal.get(Calendar.DAY_OF_WEEK)) + " " + cal.get(Calendar.DATE) + " de " + getNombreMes(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.YEAR);
    }

    private static String getDiaSemana(int dia) {
        String diaSemana = "";
        switch (dia) {
            case Calendar.SUNDAY:
                diaSemana = "Domingo";
                break;
            case Calendar.MONDAY:
                diaSemana = "Lunes";
                break;
            case Calendar.TUESDAY:
                diaSemana = "Martes";
                break;
            case Calendar.WEDNESDAY:
                diaSemana = "Miércoles";
                break;
            case Calendar.THURSDAY:
                diaSemana = "Jueves";
                break;
            case Calendar.FRIDAY:
                diaSemana = "Viernes";
                break;
            case Calendar.SATURDAY:
                diaSemana = "Sábado";
                break;
        }
        return diaSemana;
    }

    private static String getNombreMes(int mes) {
        String nombreMes = "";
        switch (mes) {
            case Calendar.JANUARY:
                nombreMes = "enero";
                break;
            case Calendar.FEBRUARY:
                nombreMes = "febrero";
                break;
            case Calendar.MARCH:
                nombreMes = "marzo";
                break;
            case Calendar.APRIL:
                nombreMes = "abril";
                break;
            case Calendar.MAY:
                nombreMes = "mayo";
                break;
            case Calendar.JUNE:
                nombreMes = "junio";
                break;
            case Calendar.JULY:
                nombreMes = "julio";
                break;
            case Calendar.AUGUST:
                nombreMes = "agosto";
                break;
            case Calendar.SEPTEMBER:
                nombreMes = "septiembre";
                break;
            case Calendar.OCTOBER:
                nombreMes = "octubre";
                break;
            case Calendar.NOVEMBER:
                nombreMes = "november";
                break;
            case Calendar.DECEMBER:
                nombreMes = "diciembre";
                break;
        }

        return nombreMes;
    }

    public static String getNuevoNoFactura(BDO_Documento documento) {
        return connDocumento.getNoFactura(documento);
    }
    
    public static BigDecimal getPrecioVenta(String sku){
        
        BDO_Producto producto = buscarProducto(sku);
        BigDecimal precio = producto.getCosto();
        precio = precio.multiply(connImpuesto.getImpuestosPrecioVenta());
        precio = precio.multiply(BigDecimal.valueOf((buscarCategoriaProductoInt(producto.getId_categoria()).getMargen_ganancia()/100)+1));
        
        return precio;
    }
}
