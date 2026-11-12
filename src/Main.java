import java.sql.Connection;
import java.sql.SQLException;

import controlador.RestauranteControlador;
import modelo.bd.ConexionBD;
import vista.Ventana;

public class Main {
    public static void main(String[] args) {
        System.out.println("Conectando con Neon...");
        
        try {
            Connection conn = ConexionBD.obtenerConexion();
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("¡ÉXITO! Conectado a PostgreSQL y tablas creadas.");
            }
        } catch (SQLException e) {
            System.err.println("Falló la conexión: " + e.getMessage());
            e.printStackTrace();
        }

        // Inicializar la interfaz gráfica 
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                Ventana miVentana = new Ventana();          
               
                RestauranteControlador controladorPrincipal = new RestauranteControlador(miVentana);
                
                
                miVentana.setVisible(true);
            }
        });
    }
}
