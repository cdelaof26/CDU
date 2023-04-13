package cdu.ui;

import cdu.utils.AppUtils;
import cdu.utils.CDULogger;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

/**
 *
 * @author cristopher
 */
public class Preferencias extends JDialog {
    private final CDUMain contenedor;
    
    // Elementos UI
    private final SpringLayout layout = new SpringLayout();
    private final JPanel contenedorPrincipal = new JPanel(layout);
    
    private final JLabel titulo = new JLabel("Preferencias");
    
    private final JLabel temaActivo = new JLabel("El modo " + (!AppUtils.usarTemaOscuro ? "diurno" : "oscuro") + " esta activo");
    private final JButton cambiarTema = new JButton("Cambiar tema");
    
    private final JLabel etiquetaDefiniciones = new JLabel("Definiciones agregadas");
    
    private final SpringLayout layoutDeDefiniciones = new SpringLayout();
    private final JPanel panelDeDefiniciones = new JPanel(layoutDeDefiniciones);
    private final JScrollPane contenedorDeDefiniciones = new JScrollPane();
    
    private final JButton traerRegistro = new JButton("Traer registro");
    private final JButton eliminarDatos = new JButton("Eliminar todos los datos de configuración");
    private final JButton aceptarCambios = new JButton("Aceptar");
    // Fin elementos UI
    
    private boolean desinstalar = false;
    private boolean confirmarDesinstalacion = false;
    
    private ArrayList<DefinicionGrafica> definiciones = new ArrayList<>();
    
    
    public Preferencias(CDUMain contenedor) {
        this.contenedor = contenedor;
        
        initUI();
    }
    
    public final void initUI() {
        this.setSize(new Dimension(500, 325));
        this.setLocationRelativeTo(null);
        this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        this.setResizable(false);
        
        this.add(contenedorPrincipal);
        
        
        titulo.setFont(AppUtils.fondoXL);
        
        temaActivo.setFont(AppUtils.fondoEstandar);
        
        cambiarTema.setPreferredSize(new Dimension(250, 22));
        cambiarTema.setBorder(null);
        cambiarTema.setFont(AppUtils.fondoEstandar);
        cambiarTema.setFocusPainted(false);
        cambiarTema.addActionListener((Action) -> {
            AppUtils.cambiarTema(false);
            actualizarTema();
            contenedor.actualizarTema();
            temaActivo.setText("El modo " + (!AppUtils.usarTemaOscuro ? "diurno" : "oscuro") + " esta activo");
        });
        
        
        etiquetaDefiniciones.setFont(AppUtils.fondoEstandar);
        contenedorDeDefiniciones.setViewportView(panelDeDefiniciones);
        contenedorDeDefiniciones.setPreferredSize(new Dimension(450, 115));
        contenedorDeDefiniciones.setBorder(null);
        listarDefiniciones();
        
        
        traerRegistro.setPreferredSize(new Dimension(120, 22));
        traerRegistro.setBorder(null);
        traerRegistro.setFont(AppUtils.fondoEstandar);
        traerRegistro.setFocusPainted(false);
        traerRegistro.addActionListener((Action) -> {
            new MensajeFlotante().mostrarMensaje(CDULogger.getRegistro(), CDULogger.TipoDeDato.INFO);
        });
        
        eliminarDatos.setPreferredSize(new Dimension(450, 22));
        eliminarDatos.setBorder(null);
        eliminarDatos.setFont(AppUtils.fondoEstandar);
        eliminarDatos.setFocusPainted(false);
        eliminarDatos.addActionListener((Action) -> {
            if (!desinstalar) {
                desinstalar = true;
                eliminarDatos.setText("CDU se cerrará, presiona de nuevo para confirmar (5s)");
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Iniciando secuencia de desinstalación...");
                new Thread() {
                    @Override
                    public void run() {
                        CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Esperando confirmación...");
                        for (int i = 4; i >= 0; i--) {
                            if (confirmarDesinstalacion)
                                break;
                            
                            try { sleep(1000); } catch (InterruptedException ex) { }
                            eliminarDatos.setText("CDU se cerrará, presiona de nuevo para confirmar (" + i + "s)");
                        }
                        
                        if (confirmarDesinstalacion) {
                            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Ejecutando procedimiento...");
                            AppUtils.ScriptRunner script = AppUtils.ejecutarScript("-m", "man", "-rm", "all_data");
                            script.start();
                            while(script.estaEjecutandose())
                                try { Thread.sleep(100); } catch (InterruptedException ex) { }
                            
                            if (script.getCodigoDeSalida() != 0) {
                                CDULogger.imprimirMensajeMultilinea(CDULogger.TipoDeDato.UNK, script.getSalida());
                                
                                new MensajeFlotante().mostrarMensaje("Ocurrio un error inesperado, intenta de nuevo", CDULogger.TipoDeDato.ERROR);
                                return;
                            }
                            
                            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Ejecución terminada");
                            new MensajeFlotante().mostrarMensaje("Se han eliminado los datos de CDU, presiona aceptar\npara salir\n\n    Bye bye!", CDULogger.TipoDeDato.INFO);
                            
                            System.exit(0);
                        }
                        
                        CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Proceso cancelado");
                        eliminarDatos.setText("Eliminar todos los datos de configuración");
                        desinstalar = false;
                        confirmarDesinstalacion = false;
                    }
                }.start();
                return;
            }
            if (desinstalar) {
                confirmarDesinstalacion = true;
            }
        });
        
        aceptarCambios.setPreferredSize(new Dimension(120, 22));
        aceptarCambios.setBorder(null);
        aceptarCambios.setFont(AppUtils.fondoEstandar);
        aceptarCambios.setFocusPainted(false);
        aceptarCambios.addActionListener((Action) -> {
            this.setVisible(false);
        });
        
        
        contenedorPrincipal.add(titulo);
        contenedorPrincipal.add(temaActivo);
        contenedorPrincipal.add(cambiarTema);
        contenedorPrincipal.add(etiquetaDefiniciones);
        contenedorPrincipal.add(contenedorDeDefiniciones);
        contenedorPrincipal.add(traerRegistro);
        contenedorPrincipal.add(eliminarDatos);
        contenedorPrincipal.add(aceptarCambios);
        
        
        
        layout.putConstraint(SpringLayout.NORTH, titulo, 10, SpringLayout.NORTH, contenedorPrincipal);
        layout.putConstraint(SpringLayout.WEST, titulo, 25, SpringLayout.WEST, contenedorPrincipal);
        
        layout.putConstraint(SpringLayout.NORTH, temaActivo, 10, SpringLayout.SOUTH, titulo);
        layout.putConstraint(SpringLayout.WEST, temaActivo, 0, SpringLayout.WEST, titulo);
        
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, cambiarTema, 0, SpringLayout.VERTICAL_CENTER, temaActivo);
        layout.putConstraint(SpringLayout.EAST, cambiarTema, -25, SpringLayout.EAST, contenedorPrincipal);
        
        
        layout.putConstraint(SpringLayout.NORTH, etiquetaDefiniciones, 10, SpringLayout.SOUTH, temaActivo);
        layout.putConstraint(SpringLayout.WEST, etiquetaDefiniciones, 0, SpringLayout.WEST, titulo);
        layout.putConstraint(SpringLayout.NORTH, contenedorDeDefiniciones, 10, SpringLayout.SOUTH, etiquetaDefiniciones);
        layout.putConstraint(SpringLayout.WEST, contenedorDeDefiniciones, 0, SpringLayout.WEST, titulo);
        
        
        layout.putConstraint(SpringLayout.SOUTH, traerRegistro, -15, SpringLayout.SOUTH, contenedorPrincipal);
        layout.putConstraint(SpringLayout.WEST, traerRegistro, 25, SpringLayout.WEST, contenedorPrincipal);
        
        layout.putConstraint(SpringLayout.SOUTH, eliminarDatos, -10, SpringLayout.NORTH, aceptarCambios);
        layout.putConstraint(SpringLayout.WEST, eliminarDatos, 25, SpringLayout.WEST, contenedorPrincipal);
        
        layout.putConstraint(SpringLayout.SOUTH, aceptarCambios, -15, SpringLayout.SOUTH, contenedorPrincipal);
        layout.putConstraint(SpringLayout.EAST, aceptarCambios, -25, SpringLayout.EAST, contenedorPrincipal);
    }
    
    public void actualizarTema() {
        contenedorPrincipal.setBackground(AppUtils.APP_BG_COLOR);
        
        titulo.setForeground(AppUtils.APP_FG_COLOR);
        temaActivo.setForeground(AppUtils.APP_FG_COLOR);
        cambiarTema.setForeground(AppUtils.APP_FG_COLOR);
        cambiarTema.setBackground(AppUtils.APP_BG_A_COLOR);
        
        
        etiquetaDefiniciones.setForeground(AppUtils.APP_FG_COLOR);
        contenedorDeDefiniciones.getVerticalScrollBar().setUI(new ColorScrollBarUI());
        contenedorDeDefiniciones.getHorizontalScrollBar().setUI(new ColorScrollBarUI());
        panelDeDefiniciones.setBackground(AppUtils.APP_BG_A_COLOR);
        for (DefinicionGrafica d : definiciones) {
            d.actualizarTema();
        }
        
        
        traerRegistro.setBackground(AppUtils.APP_BG_A_COLOR);
        traerRegistro.setForeground(AppUtils.APP_FG_COLOR);
        
        eliminarDatos.setBackground(AppUtils.APP_BG_A_COLOR);
        eliminarDatos.setForeground(AppUtils.APP_FG_COLOR);
        
        aceptarCambios.setBackground(AppUtils.APP_BG_A_COLOR);
        aceptarCambios.setForeground(AppUtils.APP_FG_COLOR);
    }

    public void listarDefiniciones() {
        new CargadorDeDefiniciones().start();
    }
    
    private class CargadorDeDefiniciones extends Thread {
        @Override
        public void run() {
            if (!definiciones.isEmpty())
                for (DefinicionGrafica d : definiciones) {
                    panelDeDefiniciones.remove(d);
                }
            
            definiciones = new ArrayList<>();
            panelDeDefiniciones.revalidate();
            panelDeDefiniciones.repaint();
            
            AppUtils.ScriptRunner script = AppUtils.ejecutarScript("-m", "get");
            script.start();
            
            while(script.estaEjecutandose())
                try { sleep(100); } catch (InterruptedException ex) { }
            
            String [] definicionesEncontradas = script.getSalida().split("\n");
            
            boolean longitudEstandar = 22 * definicionesEncontradas.length < 115;
            
            for (int i = 0; i < definicionesEncontradas.length; i++)
                if (!definicionesEncontradas[i].equals(""))
                    definiciones.add(new DefinicionGrafica(definicionesEncontradas[i], i, longitudEstandar));
            
            if (!definiciones.isEmpty()) {
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Se encontraron " + definiciones.size() + " definiciones");
                    
                for (int i = 0; i < definiciones.size(); i++) {
                    DefinicionGrafica d = definiciones.get(i);
                    
                    panelDeDefiniciones.add(d);
                    
                    if (i == 0)
                        layoutDeDefiniciones.putConstraint(SpringLayout.NORTH, d, 0, SpringLayout.NORTH, panelDeDefiniciones);
                    else
                        layoutDeDefiniciones.putConstraint(SpringLayout.NORTH, d, 0, SpringLayout.SOUTH, definiciones.get(i - 1));
                    
                    layoutDeDefiniciones.putConstraint(SpringLayout.WEST, d, 0, SpringLayout.WEST, panelDeDefiniciones);
                }
                
                panelDeDefiniciones.setPreferredSize(new Dimension(panelDeDefiniciones.getPreferredSize().width, 22 * definiciones.size()));
                panelDeDefiniciones.revalidate();
                panelDeDefiniciones.repaint();
            } else {
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "No se encontraron definiciones");
            }
        }
    }
    
    public void eliminarDefinicion(int id) {
        for (DefinicionGrafica d : definiciones) {
            d.deshabilitar();
        }
        
        new EliminadorDeDefiniciones(id).start();
    }
    
    private class EliminadorDeDefiniciones extends Thread {
        private final int id;

        public EliminadorDeDefiniciones(int id) {
            this.id = id;
        }
        
        @Override
        public void run() {
            AppUtils.ScriptRunner script = AppUtils.ejecutarScript("-m", "man", "-rm", "definition", "-id", "" + id);
            script.start();
            
            while(script.estaEjecutandose())
                try { sleep(100); } catch (InterruptedException ex) { }
            
            if (script.getCodigoDeSalida() == 0) {
                listarDefiniciones();
            } else {
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Error al eliminar definición");
            }
        }
    }
    
    private class DefinicionGrafica extends JPanel {
        private final SpringLayout layout = new SpringLayout();
        private final int indice;
        private final JLabel etiqueta;
        private final BotonConIcono botonDeEliminar;
        private final int longitud;

        public DefinicionGrafica(String datos, int indice, boolean longitudEstandar) {
            this.indice = indice;
            this.etiqueta = new JLabel(datos);
            this.botonDeEliminar = new BotonConIcono("Cruz", false);
            
            if (longitudEstandar) {
                longitud = 450;
            } else {
                longitud = 440;
            }
            
            initUI();
        }
        
        public final void initUI() {
            this.setLayout(this.layout);
            
            etiqueta.setFont(AppUtils.fondoEstandar);
            botonDeEliminar.addActionListener((Action) -> {
                eliminarDefinicion(indice);
            });
            
            
            this.add(etiqueta);
            this.add(botonDeEliminar);
            this.setPreferredSize(new Dimension(longitud, 22));
            
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(AppUtils.APP_BG_A_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(AppUtils.APP_BG_COLOR);
                }
            });
            
            this.layout.putConstraint(SpringLayout.VERTICAL_CENTER, etiqueta, 0, SpringLayout.VERTICAL_CENTER, this);
            this.layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, etiqueta, 0, SpringLayout.HORIZONTAL_CENTER, this);
            
            this.layout.putConstraint(SpringLayout.NORTH, botonDeEliminar, 0, SpringLayout.NORTH, this);
            this.layout.putConstraint(SpringLayout.EAST, botonDeEliminar, 0, SpringLayout.EAST, this);
            
            actualizarTema();
        }
        
        public void actualizarTema() {
            this.setBackground(AppUtils.APP_BG_COLOR);
            etiqueta.setForeground(AppUtils.APP_FG_COLOR);
            botonDeEliminar.actualizarTema();
        }
        
        public void deshabilitar() {
            this.botonDeEliminar.setEnabled(false);
        }
    }
}
