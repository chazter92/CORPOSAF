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
import BDO.BDO_Empresa;
import BDO.BDO_Estado_Cliente;
import BDO.BDO_Estado_Producto;
import BDO.BDO_Impuesto;
import BDO.BDO_Precio_Cliente;
import BDO.BDO_Producto;
import DAO.DAO_Atributo_Cliente;
import DAO.DAO_Categoria_Producto;
import DAO.DAO_Cliente;
import DAO.DAO_Empresa;
import DAO.DAO_Estado_Cliente;
import DAO.DAO_Estado_Producto;
import DAO.DAO_Impuesto;
import DAO.DAO_Precio_Cliente;
import DAO.DAO_Producto;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotificationPopup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Chaz
 */
public class Catalogos {

    private static Map<String, BDO_Cliente> clientes = new HashMap<String, BDO_Cliente>();
    private static Map<String, BDO_Precio_Cliente> precios_cliente = new HashMap<String, BDO_Precio_Cliente>();
    private static Map<String, BDO_Estado_Cliente> estados_cliente = new HashMap<String, BDO_Estado_Cliente>();
    private static Map<String, BDO_Categoria_Producto> categorias_producto = new HashMap<String, BDO_Categoria_Producto>();
    private static Map<String, BDO_Atributo_Cliente> atributos_cliente = new HashMap<String, BDO_Atributo_Cliente>();
    private static Map<String, BDO_Impuesto> impuestos = new HashMap<String, BDO_Impuesto>();
    private static Map<String, BDO_Producto> productos = new HashMap<String, BDO_Producto>();
    private static Map<String, BDO_Atributo_Producto> atributos_producto = new HashMap<String, BDO_Atributo_Producto>();
    private static Map<String, BDO_Estado_Producto> estados_producto = new HashMap<String, BDO_Estado_Producto>();
    private static BDO_Empresa empresa;
    

    static void cargarCatologos() {
        actualizarEmpresas();
        actualizarAtributosCliente();
        actualizarPreciosClientes();
        actualizarEstadosClientes();
        actualizarClientes();        
        actualizarCategoriasProducto();
        actualizarImpuestos();
        actualizarProductos();
        actualizarAtributosProducto();
        actualizarEstadosProducto();
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

    
    public static void actualizarEstadosProducto() {
        estados_producto = connEstadoProducto.todosEstados();
    }
    
    public static void actualizarAtributosProducto() {
        atributos_producto = connAtributoProducto.todosAtributosProducto();
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

    public static ArrayList<BDO_Estado_Producto> getEstados_Producto() {
        return new ArrayList(estados_producto.values());
    }
    
    public static ArrayList<BDO_Atributo_Producto> getAtributosProducto() {
        return new ArrayList(atributos_producto.values());
    }
    
    public static ArrayList<BDO_Producto> getProductos() {
        return new ArrayList(productos.values());
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
}
