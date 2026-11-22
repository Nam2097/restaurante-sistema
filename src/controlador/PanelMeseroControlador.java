package controlador;

import vista.Ventana;
import modelo.entidades.Factura;
import modelo.entidades.Pedido;
import modelo.entidades.Plato;
import modelo.estructuras.Cola;
import modelo.estructuras.Pila;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class PanelMeseroControlador {

    private Ventana vista;
    private ArrayList<Plato> listaPlatosDisponibles;
    private ArrayList<Plato> platosOrdenActual;
    private Cola<Pedido> colaCocina;
    private Pila<Factura> historialFacturas;

    public PanelMeseroControlador(
            Ventana vista, 
            ArrayList<Plato> listaPlatosDisponibles,
            Cola<Pedido> colaCocina,
            Pila<Factura> historialFacturas) {
        this.vista = vista;
        this.listaPlatosDisponibles = listaPlatosDisponibles;
        this.platosOrdenActual = new ArrayList<>();
        this.colaCocina = colaCocina;
        this.historialFacturas = historialFacturas;
        
        inicializarEventos();
    }

    private void inicializarEventos() {
        // Evento para añadir plato (+)
        vista.alAgregarPlato(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarPlato();
            }
        });

        // Evento Quitar plato de la lista
        vista.alQuitarPedido(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int posicionEliminada = Integer.parseInt(e.getActionCommand());
                quitarPlato(posicionEliminada);
            }
        });

        // Evento para reiniciar la mesa
        vista.alReiniciarMesa(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarMesa();
            }
        });

        // Evento para generar factura y enviar pedido a cocina
        vista.alGenerarFactura(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarFactura();
            }
        });

        // Evento para revertir la última factura generada (LIFO)
        vista.alRevertirFactura(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                revertirFactura();
            }
        });
    }

    private void agregarPlato() {
        String nombreSeleccionado = vista.obtenerPlatoSeleccionado();        
        
        if (nombreSeleccionado == null || nombreSeleccionado.equals("Escoja el plato")) {
            return;
        }

        Plato platoObj = buscarPlatoPorNombre(nombreSeleccionado);
        if (platoObj != null) {
            platosOrdenActual.add(platoObj);
            vista.agregarPedidoMesero(platoObj.getNombre(), String.format("%.2f", platoObj.getPrecio()));
            recalcularTotal();
        }
    }

    private void quitarPlato(int posicion) {
        if (posicion >= 0 && posicion < platosOrdenActual.size()) {
            platosOrdenActual.remove(posicion);
            recalcularTotal();
        }
    }

    private void recalcularTotal() {
        double total = 0.0;        
        for (Plato plato : platosOrdenActual) {
            total += plato.getPrecio();
        }       
        vista.actualizarTotal(String.format("%.2f", total));
    }

    private void reiniciarMesa() {
        platosOrdenActual.clear();       
        vista.limpiarPedidosMesero();        
        vista.actualizarTotal("0.00");
    }

    private void generarFactura() {
        if (platosOrdenActual.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Debe agregar al menos un plato para generar la factura.");
            return;
        }

        String mesaTexto = vista.obtenerMesaSeleccionada();
        int numeroMesa = Integer.parseInt(mesaTexto);

        ArrayList<Pedido> ordenFactura = new ArrayList<>();
        double totalFactura = 0.0;

        for (Plato plato : platosOrdenActual) {
            // 1. Crear entidad Pedido
            Pedido nuevoPedido = new Pedido();
            nuevoPedido.setNumeroMesa(numeroMesa);
            nuevoPedido.setPlato(plato);

            ordenFactura.add(nuevoPedido);
            totalFactura += plato.getPrecio();

            // 2. Encolar en estructura Cola para cocina y mostrar en el Panel Cocina
            colaCocina.encolar(nuevoPedido);
            vista.agregarPedidoCocina(plato.getNombre(), mesaTexto);
        }

        // 3. Crear y apilar la Factura en el historial
        Factura nuevaFactura = new Factura();
        nuevaFactura.setNumeroMesa(numeroMesa);
        nuevaFactura.setTotalPagado(totalFactura);
        nuevaFactura.setOrden(ordenFactura);
        historialFacturas.push(nuevaFactura);

        // 4. Limpiar el panel del mesero para un nuevo pedido
        reiniciarMesa();
    }

    private void revertirFactura() {
        if (historialFacturas.estaVacia()) {
            JOptionPane.showMessageDialog(vista, "No hay facturas recientes para revertir.");
            return;
        }

        // 1. Extraer la última factura de la pila (LIFO)
        Factura ultimaFactura = historialFacturas.pop();
        ArrayList<Pedido> pedidosFactura = ultimaFactura.getOrden();

        // 2. Filtrar la cola de cocina para quitar los pedidos pendientes de esta factura
        Cola<Pedido> colaAuxiliar = new Cola<>();
        int pedidosCanceladosCocina = 0;

        while (!colaCocina.estaVacia()) {
            Pedido actual = colaCocina.desencolar();
            if (pedidosFactura != null && pedidosFactura.contains(actual)) {
                pedidosCanceladosCocina++;
            } else {
                colaAuxiliar.encolar(actual);
            }
        }

        // 3. Restaurar colaCocina y actualizar visualmente el Panel Cocina
        vista.limpiarPedidosCocina();
        while (!colaAuxiliar.estaVacia()) {
            Pedido restante = colaAuxiliar.desencolar();
            colaCocina.encolar(restante);
            vista.agregarPedidoCocina(
                restante.getPlato().getNombre(),
                String.valueOf(restante.getNumeroMesa())
            );
        }

        // 4. Devolver los platos al Panel Mesero para permitir su edición
        reiniciarMesa();
        if (pedidosFactura != null) {
            for (Pedido pedido : pedidosFactura) {
                Plato plato = pedido.getPlato();
                if (plato != null) {
                    platosOrdenActual.add(plato);
                    vista.agregarPedidoMesero(
                        plato.getNombre(),
                        String.format("%.2f", plato.getPrecio())
                    );
                }
            }
        }
        recalcularTotal();

        JOptionPane.showMessageDialog(
            vista,
            "Factura de la Mesa " + ultimaFactura.getNumeroMesa()
                + " ($" + String.format("%.2f", ultimaFactura.getTotalPagado()) + ") revertida.\n"
                + "Pedidos retirados de cocina: " + pedidosCanceladosCocina
                + ". Puede modificar la orden o pulsar 'Reiniciar Mesa'."
        );
    }

    private Plato buscarPlatoPorNombre(String nombrePlato) {    
        for (Plato plato : listaPlatosDisponibles) {
            if (plato.getNombre().equalsIgnoreCase(nombrePlato)) {
                return plato; 
            }
        }
        return null; 
    }
}