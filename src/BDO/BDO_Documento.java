/*
 * Representación en objeto de un registro de la tabla Documento
 */
package BDO;

/**
 *
 * @author Chaz
 */
public class BDO_Documento {
    String id_documento;
    String nombre_serie;
    int correlativo_inicial;
    int correlativo_final;
    String resolucion;

    public BDO_Documento(String id_documento, String nombre_serie, int correlativo_inicial, int correlativo_final, String resolucion) {
        this.id_documento = id_documento;
        this.nombre_serie = nombre_serie;
        this.correlativo_inicial = correlativo_inicial;
        this.correlativo_final = correlativo_final;
        this.resolucion = resolucion;
    }

    public int getCorrelativo_final() {
        return correlativo_final;
    }

    public void setCorrelativo_final(int correlativo_final) {
        this.correlativo_final = correlativo_final;
    }

    public int getCorrelativo_inicial() {
        return correlativo_inicial;
    }

    public void setCorrelativo_inicial(int correlativo_inicial) {
        this.correlativo_inicial = correlativo_inicial;
    }

    public String getId_documento() {
        return id_documento;
    }

    public void setId_documento(String id_documento) {
        this.id_documento = id_documento;
    }

    public String getNombre_serie() {
        return nombre_serie;
    }

    public void setNombre_serie(String nombre_serie) {
        this.nombre_serie = nombre_serie;
    }

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }
}
