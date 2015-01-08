/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author Chaz
 */
public class Conexion {

    PreparedStatement ps;
    String url = "jdbc:postgresql://127.0.0.1:5432/CORPOSAF";
    Connection con;

    public CachedRowSet query(String sql) {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, "consulta", "admin");
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            ResultSet rs = s.executeQuery(sql);

            CachedRowSet crs = new CachedRowSetImpl();
            crs.populate(rs);

            rs.close();
            s.close();
            con.close();

            return crs;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public CachedRowSet executePreparedQuery(Object[] datos) {
        try {
            if (ps != null) {

                for (int i = 0; i < datos.length; i++) {
                    String clase = datos[i].getClass().getSimpleName();

                    if (clase.equalsIgnoreCase("String")) {
                        ps.setString(i+1, datos[i].toString());
                    } else if (clase.equalsIgnoreCase("Integer")) {
                        ps.setInt(i+1, Integer.parseInt(datos[i].toString()));
                    } else if (clase.equalsIgnoreCase("Double")) {
                        ps.setDouble(i+1, Double.parseDouble(datos[i].toString()));
                    } else if (clase.equalsIgnoreCase("Boolean")) {
                        ps.setBoolean(i+1, Boolean.parseBoolean(datos[i].toString()));
                    } else {
                        ps.setString(i+1, datos[i].toString());
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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }
    }
}
