
import controlador.PlatoControlador;
import modelo.entidades.Plato;
import vista.Ventana;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {        
       
        Ventana miVentana = new Ventana();
        
        PlatoControlador controlador = new PlatoControlador();
        ArrayList<Plato> platosCargados = controlador.cargarPlatosDesdeJSON();
        
        String[] nombresDePlatos = new String[platosCargados.size()];
        
        for (int i = 0; i < platosCargados.size(); i++) {
            
            nombresDePlatos[i] = platosCargados.get(i).getNombre();
        }
        
        // poner platos en la lista desplegable
        miVentana.cargarPlatos(nombresDePlatos);
        
        // pendiente de implementar en clase controlador 
        String[] meserosPrueba = {"Nicolás", "Ana", "Carlos"};
        miVentana.cargarMeseros(meserosPrueba); 
        miVentana.cargarMesas(5);              

        //llamada a ventana
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                miVentana.setVisible(true);
            }
        });
    }
}
