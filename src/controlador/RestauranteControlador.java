package controlador;

import vista.Ventana;
import modelo.entidades.Factura;
import modelo.entidades.Pedido;
import modelo.entidades.Plato;
import modelo.estructuras.Cola;
import modelo.estructuras.Pila;
import java.util.ArrayList;

public class RestauranteControlador {

    private Ventana ventanaPrincipal;
    private PanelMeseroControlador meseroControlador;
    private PlatoControlador platoControlador;

    // Estructuras compartidas del sistema
    private Cola<Pedido> colaCocina;
    private Pila<Factura> historialFacturas;

    public RestauranteControlador(Ventana ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.platoControlador = new PlatoControlador();
        this.colaCocina = new Cola<>();
        this.historialFacturas = new Pila<>();
        
        inicializarDatosBase();
    }

    private void inicializarDatosBase() {
        // Cargar los meseros y mesas estáticos
        String[] meseros = {"Nicolás", "María", "Juan"};
        ventanaPrincipal.cargarMeseros(meseros);
        ventanaPrincipal.cargarMesas(5);
        
        ArrayList<Plato> platosBD = platoControlador.cargarPlatosDesdeJSON();
        
        String[] nombresPlatos = new String[platosBD.size()];
        for (int i = 0; i < platosBD.size(); i++) {
            nombresPlatos[i] = platosBD.get(i).getNombre();
        }
        
        // Poblar lista de platos en la vista
        ventanaPrincipal.cargarPlatos(nombresPlatos);        
        
        this.meseroControlador = new PanelMeseroControlador(
            this.ventanaPrincipal, 
            platosBD, 
            colaCocina, 
            historialFacturas
        );
    }
}