package cdu.ui;

import cdu.utils.AppUtils;
import cdu.utils.CDULogger;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

/**
 *
 * @author cristopher
 */
public class CDUMain extends JFrame {

    // Elementos UI
    private final SpringLayout layout = new SpringLayout();
    
    private final JPanel contenedor = new JPanel(layout);
    
    private final JLabel titulo = new JLabel("CDU");
    
    private final BarraDeEspacios barra = new BarraDeEspacios(this);
    
    private final BotonConIcono nuevaDefinicion = new BotonConIcono("Nueva definición", "Nuevo");
    private final BotonConIcono limpiar = new BotonConIcono("Limpiar", "Limpiar");
    private final BotonConIcono copiar = new BotonConIcono("Copiar", "Copiar");
    private final BotonConIcono preferencias = new BotonConIcono("Preferencias", "Ajustes");
    
    public final Preferencias panelDePreferencias = new Preferencias(this);
    
    private final PanelDeNuevasDefiniciones panelDeDefiniciones = new PanelDeNuevasDefiniciones(this);
    
    
    private final JScrollPane contenedorDePanelDeConversiones = new JScrollPane();
    private ArrayList<PanelDeConversiones> panelesDeConversion = new ArrayList<>();
    private PanelDeConversiones panelActivo = new PanelDeConversiones(this);
    // Fin elementos UI
    
    private boolean permitirNuevasDefiniciones = true;
    
    
    public CDUMain() throws HeadlessException {
        initUI();
        
        panelesDeConversion.add(panelActivo);
    }
    
    public final void initUI() {
        this.setSize(new Dimension(1000, 630));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        this.add(contenedor);
        
        
        titulo.setFont(AppUtils.fondoXL);
        
        contenedorDePanelDeConversiones.setPreferredSize(new Dimension(940, 450));
        contenedorDePanelDeConversiones.setBorder(null);
        contenedorDePanelDeConversiones.setViewportView(panelActivo);
        
        nuevaDefinicion.addActionListener((Action) -> {
            if (permitirNuevasDefiniciones) {
                panelDeDefiniciones.setVisible(!panelDeDefiniciones.isVisible());
            } else {
                new MensajeFlotante().mostrarMensaje(CDULogger.getRegistro(), CDULogger.TipoDeDato.ERROR);
            }
        });
        
        limpiar.addActionListener((Action) -> {
            panelActivo.reiniciarPanel();
        });
        
        copiar.addActionListener((Action) -> {
            if (panelActivo.copiarUltimaSalida()) {
                copiar.setText("¡Texto copiado!");
            } else {
                copiar.setText("No hay datos");
            }
            
            new Thread() {
                @Override
                public void run() {
                    try { sleep(2000); } catch (InterruptedException ex) { }
                    copiar.setText("Copiar");
                }
            }.start();
        });
        
        preferencias.addActionListener((Action) -> {
            panelDePreferencias.setVisible(!panelDePreferencias.isVisible());
        });
        
        
        contenedor.add(titulo);
        contenedor.add(barra);
        contenedor.add(contenedorDePanelDeConversiones);
        contenedor.add(nuevaDefinicion);
        contenedor.add(limpiar);
        contenedor.add(copiar);
        contenedor.add(preferencias);
        
        
        layout.putConstraint(SpringLayout.NORTH, titulo, 10, SpringLayout.NORTH, contenedor);
        layout.putConstraint(SpringLayout.EAST, titulo, -30, SpringLayout.EAST, contenedor);
        
        layout.putConstraint(SpringLayout.SOUTH, barra, 0, SpringLayout.NORTH, contenedorDePanelDeConversiones);
        layout.putConstraint(SpringLayout.WEST, barra, 0, SpringLayout.WEST, contenedorDePanelDeConversiones);
        
        layout.putConstraint(SpringLayout.NORTH, contenedorDePanelDeConversiones, 10, SpringLayout.SOUTH, titulo);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, contenedorDePanelDeConversiones, 0, SpringLayout.HORIZONTAL_CENTER, contenedor);
        
        layout.putConstraint(SpringLayout.NORTH, nuevaDefinicion, 10, SpringLayout.SOUTH, contenedorDePanelDeConversiones);
        layout.putConstraint(SpringLayout.WEST, nuevaDefinicion, 30, SpringLayout.WEST, contenedor);
        
        layout.putConstraint(SpringLayout.NORTH, limpiar, 0, SpringLayout.NORTH, nuevaDefinicion);
        layout.putConstraint(SpringLayout.WEST, limpiar, 0, SpringLayout.EAST, nuevaDefinicion);
        
        layout.putConstraint(SpringLayout.NORTH, copiar, 0, SpringLayout.NORTH, nuevaDefinicion);
        layout.putConstraint(SpringLayout.WEST, copiar, 0, SpringLayout.EAST, limpiar);
        
        layout.putConstraint(SpringLayout.NORTH, preferencias, 0, SpringLayout.NORTH, nuevaDefinicion);
        layout.putConstraint(SpringLayout.EAST, preferencias, -30, SpringLayout.EAST, contenedor);
        
        
        actualizarTema();
    }
    
    public void actualizarTema() {
        contenedor.setBackground(AppUtils.APP_BG_COLOR);
        
        titulo.setForeground(AppUtils.APP_FG_COLOR);
        barra.actualizarTema();
        
        contenedorDePanelDeConversiones.setBackground(AppUtils.APP_BG_A_COLOR);
        contenedorDePanelDeConversiones.getVerticalScrollBar().setUI(new ColorScrollBarUI());
        contenedorDePanelDeConversiones.getHorizontalScrollBar().setUI(new ColorScrollBarUI());
        panelActivo.actualizarTema();
        
        nuevaDefinicion.actualizarTema();
        limpiar.actualizarTema();
        copiar.actualizarTema();
        preferencias.actualizarTema();
        
        panelDePreferencias.actualizarTema();
        panelDeDefiniciones.actualizarTema();
    }
    
    public void hacerClickACopiar() {
        copiar.doClick();
    }
    
    public void deshabilitarNuevasDefiniciones() {
        permitirNuevasDefiniciones = false;
    }
    
    public void activarPanel(int indice) {
        panelActivo = panelesDeConversion.get(indice);
        panelActivo.actualizarTema();
        
        contenedorDePanelDeConversiones.setViewportView(panelActivo);
        contenedorDePanelDeConversiones.repaint();
        contenedorDePanelDeConversiones.revalidate();
    }
    
    public void crearPanel() {
        PanelDeConversiones p = new PanelDeConversiones(this);
        panelesDeConversion.add(p);
        panelActivo = p;
        
        contenedorDePanelDeConversiones.setViewportView(p);
        contenedorDePanelDeConversiones.repaint();
        contenedorDePanelDeConversiones.revalidate();
    }
    
    public void eliminarPanel(int indice) {
        panelesDeConversion.remove(indice);
    }
}
