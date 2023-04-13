package cdu.ui;

import cdu.utils.AppUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

/**
 *
 * @author cristopher
 */
public class BarraDeEspacios extends JPanel {
    private final CDUMain contenedor;
    
    private int ultimoEspacioAgregado = 1;
    
    // Elementos UI
    private final SpringLayout layout = new SpringLayout();
    
    private final ArrayList<Espacio> espacios = new ArrayList<>();
    private final BotonConIcono agregarEspacio = new BotonConIcono("Nuevo", true);
    // Fin elementos UI
    
    
    public BarraDeEspacios(CDUMain contenedor) {
        this.contenedor = contenedor;
        
        initUI();
    }
    
    public final void initUI() {
        this.setPreferredSize(new Dimension(850, 22));
        this.setLayout(layout);
        
        
        agregarEspacio.addActionListener((Action) -> {
            agregarEspacio();
        });
        
        espacios.add(new Espacio(1));
        espacios.get(0).setActivo(true);
        
        
        this.add(espacios.get(0));
        this.add(agregarEspacio);
        
        
        layout.putConstraint(SpringLayout.NORTH, espacios.get(0), 0, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, espacios.get(0), 0, SpringLayout.WEST, this);
        
        layout.putConstraint(SpringLayout.NORTH, agregarEspacio, 0, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, agregarEspacio, 0, SpringLayout.EAST, espacios.get(0));
    }
    
    public void actualizarTema() {
        this.setBackground(AppUtils.APP_BG_COLOR);
        
        agregarEspacio.actualizarTema();
        
        for (Espacio e : espacios)
            e.actualizarTema();
    }
    
    private void activarEspacio(int indice) {
        for (Espacio e : espacios) {
            e.setActivo(false);
            e.actualizarTema();
        }
        
        espacios.get(indice).setActivo(true);
        espacios.get(indice).actualizarTema();
        
        contenedor.activarPanel(indice);
    }
    
    private void agregarEspacio() {
        espacios.add(new Espacio(ultimoEspacioAgregado + 1));
        
        this.add(espacios.get(ultimoEspacioAgregado));
        layout.putConstraint(SpringLayout.WEST, agregarEspacio, 0, SpringLayout.EAST, espacios.get(ultimoEspacioAgregado));
        
        if (ultimoEspacioAgregado != 0) {
            layout.putConstraint(SpringLayout.NORTH, espacios.get(ultimoEspacioAgregado), 0, SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.WEST, espacios.get(ultimoEspacioAgregado), 0, SpringLayout.EAST, espacios.get(ultimoEspacioAgregado - 1));
        } else {
            layout.putConstraint(SpringLayout.NORTH, espacios.get(0), 0, SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.WEST, espacios.get(0), 0, SpringLayout.WEST, this);
        }
        
        contenedor.crearPanel();
        activarEspacio(ultimoEspacioAgregado);
        ultimoEspacioAgregado++;
        
        this.revalidate();
        this.repaint();
        
        agregarEspacio.setVisible(ultimoEspacioAgregado < 4);
    }
    
    private void desocuparEspacio(int indice) {
        Espacio e = espacios.remove(indice);
        
        this.remove(e);
        
        for (int i = indice; i < espacios.size(); i++) {
            espacios.get(i).setNumero(i + 1);
        }
        
        contenedor.eliminarPanel(indice);
        
        ultimoEspacioAgregado--;
        
        if (ultimoEspacioAgregado == 0) {
            agregarEspacio();
            return;
        }
        
        if (indice == 0) {
            layout.putConstraint(SpringLayout.NORTH, espacios.get(0), 0, SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.WEST, espacios.get(0), 0, SpringLayout.WEST, this);
        }
        
        layout.putConstraint(SpringLayout.WEST, agregarEspacio, 0, SpringLayout.EAST, espacios.get(ultimoEspacioAgregado - 1));
        activarEspacio(ultimoEspacioAgregado - 1);
        
        this.revalidate();
        this.repaint();
        
        agregarEspacio.setVisible(ultimoEspacioAgregado < 4);
    }
    
    private class Espacio extends JPanel {
        private boolean activo = false;
        private int numero = -1;
        private final JLabel etiqueta;
        private final BotonConIcono botonDeEliminar = new BotonConIcono("Cruz", false);
        private final SpringLayout layout = new SpringLayout();

        public Espacio(int numero) {
            this.numero = numero;
            this.etiqueta = new JLabel("Espacio " + numero);
            
            initUI();
        }
        
        public final void initUI() {
            this.setPreferredSize(new Dimension(170, 22));
            this.setLayout(layout);
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    activarEspacio(numero - 1);
                }
            });
            
            this.etiqueta.setFont(AppUtils.fondoEstandar);
            
            botonDeEliminar.addActionListener((Action) -> {
                desocuparEspacio(numero - 1);
            });
            
            
            this.add(etiqueta);
            this.add(botonDeEliminar);
            
            
            this.layout.putConstraint(SpringLayout.VERTICAL_CENTER, etiqueta, 0, SpringLayout.VERTICAL_CENTER, this);
            this.layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, etiqueta, 0, SpringLayout.HORIZONTAL_CENTER, this);
            
            this.layout.putConstraint(SpringLayout.VERTICAL_CENTER, botonDeEliminar, 0, SpringLayout.VERTICAL_CENTER, this);
            this.layout.putConstraint(SpringLayout.EAST, botonDeEliminar, 0, SpringLayout.EAST, this);
        }
        
        public void actualizarTema() {
            if (activo) {
                if (AppUtils.usarTemaOscuro)
                    this.setBackground(Color.GRAY);
                else
                    this.setBackground(Color.LIGHT_GRAY);
            } else {
                this.setBackground(AppUtils.APP_BG_A_COLOR);
            }
            
            etiqueta.setForeground(AppUtils.APP_FG_COLOR);
            botonDeEliminar.actualizarTema();
        }
        
        public void setNumero(int numero) {
            this.numero = numero;
            etiqueta.setText("Espacio " + numero);
        }

        public int getNumero() {
            return numero;
        }

        public void setActivo(boolean activo) {
            this.activo = activo;
        }
    }
}
