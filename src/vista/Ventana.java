package vista;

import java.awt.*; //para importar todas las clases de los paquetes
import javax.swing.*;

public class Ventana extends JFrame{

    private JTabbedPane pestagnas;
    private JPanel panelMesero;
    private JPanel panelCocina;

    public Ventana(){
        //Configs básicas
        this.setSize(1000, 1000);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); //Determina que pasa al cerrar la ventana
        this.setTitle("restaurante-sistema");
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.black);

        iniciarPanelMesero();
        iniciarPanelCocina();;

        pestagnas = new JTabbedPane();
        pestagnas.addTab("Panel Mesero", panelMesero);
        pestagnas.addTab("Panel Cocina", panelCocina);
        this.add(pestagnas);
        
        this.setVisible(true);

    }

    private void iniciarPanelMesero(){
        panelMesero= new JPanel();
        panelMesero.add(new JLabel("Módulo del Mesero"));
        panelMesero.add(new JButton("Tomar Pedido"));
    }

    private void iniciarPanelCocina(){
        panelCocina= new JPanel();
        panelCocina.add(new JLabel("Módulo de la cocina"));
        panelCocina.add(new JButton("Siguiente pedido"));
    }


}