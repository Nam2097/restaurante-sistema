package controlador;

import vista.Ventana;
import modelo.entidades.Pedido;
import modelo.estructuras.Cola;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelCocinaControlador {

    private Ventana vista;
    private Cola<Pedido> colaCocina;

    public PanelCocinaControlador(Ventana vista, Cola<Pedido> colaCocina) {
        this.vista = vista;
        this.colaCocina = colaCocina;

        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.alCompletarPedido(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ventana envía el comando con formato "nombrePlato;numeroMesa"
                String comando = e.getActionCommand();
                int separador = comando.lastIndexOf(";");

                if (separador != -1) {
                    String nombrePlato = comando.substring(0, separador);
                    int numeroMesa = Integer.parseInt(comando.substring(separador + 1));
                    completarPedido(nombrePlato, numeroMesa);
                }
            }
        });
    }

    private void completarPedido(String nombrePlato, int numeroMesa) {
        if (colaCocina.estaVacia()) {
            return;
        }

        // Si se completa el primer elemento, sale directo por FIFO.
        // Si el cocinero completa otro de la lista, se mantiene el orden de los demás.
        Cola<Pedido> colaAuxiliar = new Cola<>();
        boolean eliminado = false;

        while (!colaCocina.estaVacia()) {
            Pedido actual = colaCocina.desencolar();

            if (!eliminado
                    && actual.getNumeroMesa() == numeroMesa
                    && actual.getPlato().getNombre().equalsIgnoreCase(nombrePlato)) {
                eliminado = true;
                System.out.println("Pedido completado y eliminado de la cola -> Mesa "
                        + numeroMesa + ": " + nombrePlato);
            } else {
                colaAuxiliar.encolar(actual);
            }
        }

        while (!colaAuxiliar.estaVacia()) {
            colaCocina.encolar(colaAuxiliar.desencolar());
        }
    }
}