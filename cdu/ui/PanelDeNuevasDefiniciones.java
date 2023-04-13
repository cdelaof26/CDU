package cdu.ui;

import cdu.utils.AppUtils;
import cdu.utils.CDULogger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 *
 * @author cristopher
 */
public class PanelDeNuevasDefiniciones extends JDialog {
    
    
    // Elementos UI
    private final CDUMain contenedor;
    
    private final SpringLayout layout = new SpringLayout();
    private final JPanel contenedorPrincipal = new JPanel(layout);
    
    private final JLabel titulo = new JLabel("Nueva definción");
    
    private final JLabel etiquetaTipoDeUnidad = new JLabel("Selecciona el tipo de unidad");
    private final SpringLayout layoutDeTipos = new SpringLayout();
    private final JPanel panelDeTipos = new JPanel(layoutDeTipos);
    private final JScrollPane contenedorDeTipos = new JScrollPane();
    private final ArrayList<TipoGrafico> tiposDisponibles = new ArrayList<>();
    
    private final JLabel etiquetaDatosDeUnidad = new JLabel("Datos de la nueva unidad");
    private final SpringLayout layoutPanelDeDatos = new SpringLayout();
    private final JPanel panelDeDatos = new JPanel(layoutPanelDeDatos);
    private final JLabel mensajeDeEstado = new JLabel();
    
    private final JTextField [] entradas = new JTextField[2];
    private final JLabel flechaDivisora = new JLabel(" -> ");
    
    private final JButton salir = new JButton("Salir");
    private final JButton agregar = new JButton("Agregar");
    
    // Fin elementos UI
    
    private String tipoDeUnidadSeleccionado = "";
    
    
    public PanelDeNuevasDefiniciones(CDUMain contenedor) {
        this.contenedor = contenedor;
        
        initUI();
    }
    
    public final void initUI() {
        this.setSize(new Dimension(500, 330));
        this.setLocationRelativeTo(null);
        this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        this.setResizable(false);
        
        this.add(contenedorPrincipal);
        
        
        titulo.setFont(AppUtils.fondoXL);
        
        etiquetaTipoDeUnidad.setFont(AppUtils.fondoEstandar);
        
        contenedorDeTipos.setViewportView(panelDeTipos);
        contenedorDeTipos.setPreferredSize(new Dimension(200, 170));
        contenedorDeTipos.setBorder(null);
        cargarTiposDeDatos();
        
        
        
        etiquetaDatosDeUnidad.setFont(AppUtils.fondoEstandar);
        
        for (int i = 0; i < 2; i++) {
            entradas[i] = new JTextField();
            
            entradas[i].setBorder(null);
            entradas[i].setFont(AppUtils.fondoES);
            entradas[i].setPreferredSize(new Dimension(90, 30));
            entradas[i].setHorizontalAlignment(JTextField.CENTER);
            entradas[i].setBackground(Color.LIGHT_GRAY);
            entradas[i].setSelectionColor(Color.GRAY);
            entradas[i].setSelectedTextColor(Color.WHITE);
            entradas[i].setForeground(Color.BLACK);
            entradas[i].setCaretColor(Color.BLACK);
            entradas[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER)
                        agregar.doClick();
                }
            });
            
            panelDeDatos.add(entradas[i]);
        }
        
        layoutPanelDeDatos.putConstraint(SpringLayout.VERTICAL_CENTER, flechaDivisora, 0, SpringLayout.VERTICAL_CENTER, panelDeDatos);
        layoutPanelDeDatos.putConstraint(SpringLayout.HORIZONTAL_CENTER, flechaDivisora, 0, SpringLayout.HORIZONTAL_CENTER, panelDeDatos);
        layoutPanelDeDatos.putConstraint(SpringLayout.VERTICAL_CENTER, entradas[0], 0, SpringLayout.VERTICAL_CENTER, flechaDivisora);
        layoutPanelDeDatos.putConstraint(SpringLayout.EAST, entradas[0], 0, SpringLayout.WEST, flechaDivisora);
        layoutPanelDeDatos.putConstraint(SpringLayout.VERTICAL_CENTER, entradas[1], 0, SpringLayout.VERTICAL_CENTER, flechaDivisora);
        layoutPanelDeDatos.putConstraint(SpringLayout.WEST, entradas[1], 0, SpringLayout.EAST, flechaDivisora);
        
        layoutPanelDeDatos.putConstraint(SpringLayout.SOUTH, mensajeDeEstado, -10, SpringLayout.SOUTH, panelDeDatos);
        layoutPanelDeDatos.putConstraint(SpringLayout.HORIZONTAL_CENTER, mensajeDeEstado, 0, SpringLayout.HORIZONTAL_CENTER, panelDeDatos);
        
        flechaDivisora.setFont(AppUtils.fondoES);
        
        panelDeDatos.add(flechaDivisora);
        panelDeDatos.add(mensajeDeEstado);
        
        mensajeDeEstado.setFont(AppUtils.fondoEstandar);
        
        panelDeDatos.setBorder(null);
        panelDeDatos.setPreferredSize(new Dimension(240, 170));

        
        
        salir.setPreferredSize(new Dimension(120, 22));
        salir.setBorder(null);
        salir.setFont(AppUtils.fondoEstandar);
        salir.setFocusPainted(false);
        salir.addActionListener((Action) -> {
            setVisible(false);
        });
        
        agregar.setPreferredSize(new Dimension(120, 22));
        agregar.setBorder(null);
        agregar.setFont(AppUtils.fondoEstandar);
        agregar.setFocusPainted(false);
        agregar.addActionListener((Action) -> {
            new GuardadorDeDeficion().start();
        });
        
        
        contenedorPrincipal.add(titulo);
        contenedorPrincipal.add(etiquetaTipoDeUnidad);
        contenedorPrincipal.add(contenedorDeTipos);
        contenedorPrincipal.add(etiquetaDatosDeUnidad);
        contenedorPrincipal.add(panelDeDatos);
        contenedorPrincipal.add(salir);
        contenedorPrincipal.add(agregar);
        
        
        layout.putConstraint(SpringLayout.NORTH, titulo, 10, SpringLayout.NORTH, contenedorPrincipal);
        layout.putConstraint(SpringLayout.WEST, titulo, 25, SpringLayout.WEST, contenedorPrincipal);
        
        layout.putConstraint(SpringLayout.NORTH, etiquetaTipoDeUnidad, 15, SpringLayout.SOUTH, titulo);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, etiquetaTipoDeUnidad, 0, SpringLayout.HORIZONTAL_CENTER, contenedorDeTipos);
        layout.putConstraint(SpringLayout.NORTH, contenedorDeTipos, 10, SpringLayout.SOUTH, etiquetaTipoDeUnidad);
        layout.putConstraint(SpringLayout.WEST, contenedorDeTipos, 0, SpringLayout.WEST, titulo);
        
        
        layout.putConstraint(SpringLayout.NORTH, etiquetaDatosDeUnidad, 0, SpringLayout.NORTH, etiquetaTipoDeUnidad);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, etiquetaDatosDeUnidad, 0, SpringLayout.HORIZONTAL_CENTER, panelDeDatos);
        layout.putConstraint(SpringLayout.NORTH, panelDeDatos, 0, SpringLayout.NORTH, contenedorDeTipos);
        layout.putConstraint(SpringLayout.EAST, panelDeDatos, -25, SpringLayout.EAST, contenedorPrincipal);
        
        
        layout.putConstraint(SpringLayout.SOUTH, salir, -15, SpringLayout.SOUTH, contenedorPrincipal);
        layout.putConstraint(SpringLayout.WEST, salir, 25, SpringLayout.WEST, contenedorPrincipal);
        
        layout.putConstraint(SpringLayout.SOUTH, agregar, -15, SpringLayout.SOUTH, contenedorPrincipal);
        layout.putConstraint(SpringLayout.EAST, agregar, -25, SpringLayout.EAST, contenedorPrincipal);
    }
    
    public void actualizarTema() {
        contenedorPrincipal.setBackground(AppUtils.APP_BG_COLOR);
        
        titulo.setForeground(AppUtils.APP_FG_COLOR);
        
        etiquetaTipoDeUnidad.setForeground(AppUtils.APP_FG_COLOR);
        contenedorDeTipos.getVerticalScrollBar().setUI(new ColorScrollBarUI());
        contenedorDeTipos.getHorizontalScrollBar().setUI(new ColorScrollBarUI());
        
        panelDeTipos.setBackground(AppUtils.APP_BG_A_COLOR);
        for (TipoGrafico t : tiposDisponibles)
            t.actualizarTema();
        
        etiquetaDatosDeUnidad.setForeground(AppUtils.APP_FG_COLOR);
        
        flechaDivisora.setForeground(AppUtils.APP_FG_COLOR);
        
        panelDeDatos.setBackground(AppUtils.APP_BG_A_COLOR);
        
        mensajeDeEstado.setForeground(AppUtils.APP_FG_COLOR);
        
        salir.setBackground(AppUtils.APP_BG_A_COLOR);
        salir.setForeground(AppUtils.APP_FG_COLOR);
        
        agregar.setBackground(AppUtils.APP_BG_A_COLOR);
        agregar.setForeground(AppUtils.APP_FG_COLOR);
    }
    
    private void cargarTiposDeDatos() {
        new CargadorDeTipos().start();
    }
    
    private class CargadorDeTipos extends Thread {
        @Override
        public void run() {
            AppUtils.ScriptRunner script = AppUtils.ejecutarScript("-m", "get_t");
            script.start();
            
            while(script.estaEjecutandose())
                try { sleep(100); } catch (InterruptedException ex) { }
            
            String [] tiposEncontrados = script.getSalida().split("\n");
            
            boolean longitudEstandar = 22 * tiposDisponibles.size() < 170;
            
            for (String tiposEncontrado : tiposEncontrados)
                if (!tiposEncontrado.equals(""))
                    tiposDisponibles.add(new TipoGrafico(tiposEncontrado, longitudEstandar));
            
            if (!tiposDisponibles.isEmpty()) {
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Se encontraron " + tiposEncontrados.length + " tipos de datos");
                
                for (int i = 0; i < tiposDisponibles.size(); i++) {
                    TipoGrafico t = tiposDisponibles.get(i);
                    
                    panelDeTipos.add(t);

                    if (i == 0)
                        layoutDeTipos.putConstraint(SpringLayout.NORTH, t, 0, SpringLayout.NORTH, panelDeTipos);
                    else
                        layoutDeTipos.putConstraint(SpringLayout.NORTH, t, 0, SpringLayout.SOUTH, tiposDisponibles.get(i - 1));

                    layoutDeTipos.putConstraint(SpringLayout.WEST, t, 0, SpringLayout.WEST, panelDeTipos);
                }
                
                panelDeTipos.setPreferredSize(new Dimension(panelDeTipos.getPreferredSize().width, 22 * tiposDisponibles.size()));
                panelDeTipos.revalidate();
                panelDeTipos.repaint();
            } else {
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.GRAVE, "No se encontraron tipos de datos");
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "  No será posible agregar definiciones...");
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "  Esta copia de CDU podría estar incompleta");
                
                contenedor.deshabilitarNuevasDefiniciones();
                
                new MensajeFlotante().mostrarMensaje(CDULogger.getRegistro(), CDULogger.TipoDeDato.INFO);
            }
        }
    }
    
    private void deseleccionarTipos() {
        for (TipoGrafico t : tiposDisponibles)
            t.setSelecionado(false);
    }
    
    private class TipoGrafico extends JPanel {
        private final SpringLayout layout = new SpringLayout();
        private final JLabel etiqueta;
        private boolean selecionado = false;
        private final int longitud;
        
        public TipoGrafico(String datos, boolean longitudEstandar) {
            this.etiqueta = new JLabel(datos);
            
            if (longitudEstandar)
                longitud = 200;
            else
                longitud = 190;
            
            initUI();
        }
        
        public final void initUI() {
            this.setLayout(this.layout);
            
            etiqueta.setFont(AppUtils.fondoEstandar);
            
            this.add(etiqueta);
            this.setPreferredSize(new Dimension(longitud, 22));
            
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!selecionado)
                        setBackground(AppUtils.APP_BG_A_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!selecionado)
                        setBackground(AppUtils.APP_BG_COLOR);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!selecionado) {
                        deseleccionarTipos();
                        
                        selecionado = true;
                        setBackground(AppUtils.APP_BG_A_COLOR);
                        tipoDeUnidadSeleccionado = etiqueta.getText();
                    }
                }
            });
            
            this.layout.putConstraint(SpringLayout.VERTICAL_CENTER, etiqueta, 0, SpringLayout.VERTICAL_CENTER, this);
            this.layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, etiqueta, 0, SpringLayout.HORIZONTAL_CENTER, this);
            
            actualizarTema();
        }
        
        public void actualizarTema() {
            etiqueta.setForeground(AppUtils.APP_FG_COLOR);
            
            if (!selecionado)
                this.setBackground(AppUtils.APP_BG_COLOR);
            else
                this.setBackground(AppUtils.APP_BG_A_COLOR);
        }

        public void setSelecionado(boolean selecionado) {
            this.selecionado = selecionado;
            actualizarTema();
        }
    }

    private class GuardadorDeDeficion extends Thread {
        @Override
        public void run() {
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.DEBUG, "Attempt to create a new definition");
            
            mensajeDeEstado.setText("");
            
            if (tipoDeUnidadSeleccionado.equals("")) {
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.DEBUG, "User didn't select an unit");
                
                mensajeDeEstado.setText("Selecciona un tipo de unidad");
                return;
            }
            
            if (entradas[0].getText().isEmpty()) {
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.DEBUG, "User didn't fill input data");
                
                mensajeDeEstado.setText("No hay datos de entrada");
                return;
            }
            
            if (entradas[1].getText().isEmpty()) {
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.DEBUG, "User didn't fill output data");
                
                mensajeDeEstado.setText("No hay datos de salida");
                return;
            }
            
            String datos = tipoDeUnidadSeleccionado + ";" + entradas[0].getText() + "=" + entradas[1].getText();
            datos = datos.replace("-", "~");
            
            AppUtils.ScriptRunner script = AppUtils.ejecutarScript("-m", "man", "-add", datos);
            script.start();
            
            while(script.estaEjecutandose())
                try { sleep(100); } catch (InterruptedException ex) { }
            
            String salida = script.getSalida();
            
            if (script.getCodigoDeSalida() != 0) {
                if (salida.equals("MISSING_DATA") || salida.equals("INVALID_INPUT"))
                    mensajeDeEstado.setText("Entrada incompleta");
                if (salida.equals("UNK_TYPE"))
                    mensajeDeEstado.setText("El tipo seleccionado no se encontró");
                if (salida.equals("INVALID_INPUT_DATA"))
                    mensajeDeEstado.setText("Los datos de entrada están incompletos");
                if (salida.equals("INVALID_OUTPUT_DATA"))
                    mensajeDeEstado.setText("Los datos de salida están incompletos");
                
                return;
            }
            
            mensajeDeEstado.setText("¡Unidad agregada!");
            
            entradas[0].setText("");
            entradas[1].setText("");
            deseleccionarTipos();
            tipoDeUnidadSeleccionado = "";
            
            contenedor.panelDePreferencias.listarDefiniciones();
            try { sleep(3000); } catch (InterruptedException ex) { }
            mensajeDeEstado.setText("");
        }
    }
}
