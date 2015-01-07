/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author Chaz
 */
public class conexion {
    public CachedRowSet Function(String sql)
    {
            try
            {
                    Class.forName("org.postgresql.Driver");
 
                    String url = "jdbc:postgresql://127.0.0.1:5432/CORPOSAF";
                    Connection con = DriverManager.getConnection(url, "consulta","admin");
                    Statement s = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                                      ResultSet.CONCUR_READ_ONLY);
 
                    ResultSet rs= s.executeQuery(sql);
 
                    CachedRowSet crs = new CachedRowSetImpl();
                    crs.populate(rs);
 
                    rs.close();
                    s.close();
                    con.close();
 
                    return crs;
 
            }
            catch(Exception e)
            {
                   System.out.println(e.getMessage());
            }
 
            return null;
    }
 
}
