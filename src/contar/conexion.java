/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar;

import com.alee.laf.optionpane.WebOptionPane;
import com.sun.rowset.CachedRowSetImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.rowset.CachedRowSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Chaz
 */
public class Conexion {

    String url = "jdbc:postgresql://127.0.0.1:5432/CORPOSAF";
    Connection con;
    /*
    public CachedRowSet query(String sql) {
    try {
    Class.forName("org.postgresql.Driver");
    con = DriverManager.getConnection(url, "consulta", "admin");
    Statement ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
    ResultSet.CONCUR_READ_ONLY);
    
    ResultSet rs = ps.executeQuery(sql);
    
    CachedRowSet crs = new CachedRowSetImpl();
    crs.populate(rs);
    
    rs.close();
    ps.close();
    con.close();
    
    return crs;
    
    } catch (Exception e) {
    mostrarError(e.getMessage());
    }
    
    return null;
    }
     */

    public CachedRowSet query(String sql, Object[] datos, Object[] tipos) {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, "consulta", "admin");
            PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            for (int i = 0; i < datos.length; i++) {
                String clase = tipos[i].getClass().getSimpleName();
                if (datos[i] == null) {
                    int sqlType = java.sql.Types.INTEGER;
                    if (clase.equalsIgnoreCase("String")) {
                        sqlType = java.sql.Types.VARCHAR;
                    } else if (clase.equalsIgnoreCase("Integer")) {
                        sqlType = java.sql.Types.INTEGER;
                    } else if (clase.equalsIgnoreCase("Doble")) {
                        sqlType = java.sql.Types.NUMERIC;
                    } else if (clase.equalsIgnoreCase("Boolean")) {
                        sqlType = java.sql.Types.BOOLEAN;
                    }

                    ps.setNull(i + 1, sqlType);
                } else {
                    if (clase.equalsIgnoreCase("String")) {
                        ps.setString(i + 1, datos[i].toString());
                    } else if (clase.equalsIgnoreCase("Integer")) {
                        ps.setInt(i + 1, Integer.parseInt(datos[i].toString()));
                    } else if (clase.equalsIgnoreCase("Double")) {
                        ps.setDouble(i + 1, Double.parseDouble(datos[i].toString()));
                    } else if (clase.equalsIgnoreCase("Boolean")) {
                        ps.setBoolean(i + 1, Boolean.parseBoolean(datos[i].toString()));
                    } else {
                        ps.setString(i + 1, datos[i].toString());
                    }
                }
            }

            ResultSet rs = ps.executeQuery();

            CachedRowSet crs = new CachedRowSetImpl();
            crs.populate(rs);

            rs.close();
            ps.close();
            con.close();

            return crs;

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }

        return null;
    }

    public Boolean executeQuery(String sql, Object[] datos, Object[] tipos) {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, "consulta", "admin");
            PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            for (int i = 0; i < datos.length; i++) {
                String clase = tipos[i].getClass().getSimpleName();
                if (datos[i] == null) {
                    int sqlType = java.sql.Types.INTEGER;
                    if (clase.equalsIgnoreCase("String")) {
                        sqlType = java.sql.Types.VARCHAR;
                    } else if (clase.equalsIgnoreCase("Integer")) {
                        sqlType = java.sql.Types.INTEGER;
                    } else if (clase.equalsIgnoreCase("Doble")) {
                        sqlType = java.sql.Types.NUMERIC;
                    } else if (clase.equalsIgnoreCase("Boolean")) {
                        sqlType = java.sql.Types.BOOLEAN;
                    }

                    ps.setNull(i + 1, sqlType);
                } else {
                    if (clase.equalsIgnoreCase("String")) {
                        ps.setString(i + 1, datos[i].toString());
                    } else if (clase.equalsIgnoreCase("Integer")) {
                        ps.setInt(i + 1, Integer.parseInt(datos[i].toString()));
                    } else if (clase.equalsIgnoreCase("Double")) {
                        ps.setDouble(i + 1, Double.parseDouble(datos[i].toString()));
                    } else if (clase.equalsIgnoreCase("Boolean")) {
                        ps.setBoolean(i + 1, Boolean.parseBoolean(datos[i].toString()));
                    } else {
                        ps.setString(i + 1, datos[i].toString());
                    }
                }
            }
            ps.executeQuery();

            ps.close();
            con.close();

            return true;

        } catch (Exception e) {
            if(!e.getMessage().equalsIgnoreCase("No results were returned by the query.")){
                mostrarError(e.getMessage());
            }            
        }

        return false;
    }
    /*
    public CachedRowSet executePreparedQuery(Object[] datos) {
    try {
    if (ps != null) {
    
    for (int i = 0; i < datos.length; i++) {
    String clase = datos[i].getClass().getSimpleName();
    
    if (clase.equalsIgnoreCase("String")) {
    ps.setString(i + 1, datos[i].toString());
    } else if (clase.equalsIgnoreCase("Integer")) {
    ps.setInt(i + 1, Integer.parseInt(datos[i].toString()));
    } else if (clase.equalsIgnoreCase("Double")) {
    ps.setDouble(i + 1, Double.parseDouble(datos[i].toString()));
    } else if (clase.equalsIgnoreCase("Boolean")) {
    ps.setBoolean(i + 1, Boolean.parseBoolean(datos[i].toString()));
    } else {
    ps.setString(i + 1, datos[i].toString());
    }
    }
    
    ResultSet rs = ps.executeQuery();
    
    CachedRowSet crs = new CachedRowSetImpl();
    crs.populate(rs);
    
    rs.close();
    ps.close();
    ps = null;
    con.close();
    
    return crs;
    }
    
    } catch (Exception e) {
    mostrarError(e.getMessage());
    }
    
    return null;
    }
    
    public void preparedQuery(String string) {
    try {
    Class.forName("org.postgresql.Driver");
    con = DriverManager.getConnection(url, "consulta", "admin");
    
    
    ps = con.prepareStatement(string, ResultSet.TYPE_SCROLL_SENSITIVE,
    ResultSet.CONCUR_READ_ONLY);
    
    
    } catch (Exception e) {
    mostrarError(e.getMessage());
    }
    }
     */

    private void mostrarError(String message) {
        Catalogos.mostrarMensajeError(message, "Error", WebOptionPane.ERROR_MESSAGE);
    }
}
