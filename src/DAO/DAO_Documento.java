/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BDO.BDO_Documento;
import com.alee.laf.optionpane.WebOptionPane;
import contar.Catalogos;
import contar.Conexion;
import java.sql.SQLException;
import java.util.HashMap;
import javax.sql.RowSet;

/**
 *
 * @author Chaz
 */
public class DAO_Documento {

    Conexion conn = new Conexion();

    public String getNoFactura(BDO_Documento documento) {
        String siguiente_no_factura = "";
        siguiente_no_factura = toStringBD(conn.query("SELECT no_factura(?) as numero_factura", new Object[]{ documento.getId_documento()}, new Object[]{"id_doc"}), "numero_factura");
        return siguiente_no_factura;
    }

    public HashMap<String, BDO_Documento> todosDocumentos() {
        HashMap<String, BDO_Documento> documentos;
        documentos = toBDO(conn.query("SELECT * FROM DOCUMENTO", new Object[0], new Object[0]));
        return documentos;
    }

    private String toStringBD(RowSet setDocumento, String columna) {

        try {
            while (setDocumento.next()) {
                return setDocumento.getString(columna);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage() + " " + ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    private HashMap<String, BDO_Documento> toBDO(RowSet setDocumento) {
        HashMap<String, BDO_Documento> documentos = new HashMap<String, BDO_Documento>();
        try {
            while (setDocumento.next()) {
                BDO_Documento actual = new BDO_Documento(setDocumento.getString("id_documento"), setDocumento.getString("nombre_serie"), setDocumento.getInt("correlativo_inicial"), setDocumento.getInt("correlativo_final"), setDocumento.getString("resolucion"));
                documentos.put(actual.basicsToString(), actual);
            }
        } catch (SQLException ex) {
            Catalogos.mostrarMensajeError(ex.getMessage() + " " + ex.getSQLState(), "Error", WebOptionPane.ERROR_MESSAGE);
        }

        return documentos;
    }
}
