package bd;

import java.sql.Connection;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "";
    private static final String USUARIO = "";
    private static final String CLAVE = "";
    private static Connection conexion = null;

    private ConexionBD() {
    }

    public static Connection obtenerConexion() throws SQLException {
        // TODO: Implementar conexión
        return null;
    }

    private static void inicializarTablas() {
        // TODO: Implementar creación de tablas
    }
}