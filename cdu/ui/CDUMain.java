package cdu.ui;

import cdu.utils.AppUtils;
import java.awt.Dimension;
import java.awt.HeadlessException;
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
    
    private final JScrollPane contenedorVista = new JScrollPane();
    
    private final BotonConIcono nuevaDefinicion = new BotonConIcono("Nueva definición", "Nuevo");
    private final BotonConIcono limpiar = new BotonConIcono("Limpiar", "Limpiar");
    private final BotonConIcono copiar = new BotonConIcono("Copiar", "Copiar");
    private final BotonConIcono preferencias = new BotonConIcono("Preferencias", "Ajustes");
    
    private final Preferencias panelDePreferencias = new Preferencias(this);
    // Fin elementos UI
    
    
    PanelDeConversiones test = new PanelDeConversiones();
    
    
    public CDUMain() throws HeadlessException {
        initUI();
    }
    
    public final void initUI() {
        this.setSize(new Dimension(1000, 630));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        this.add(contenedor);
        
        
        titulo.setFont(AppUtils.fondoXL);
        
        contenedorVista.setPreferredSize(new Dimension(940, 450));
        contenedorVista.setBorder(null);
        contenedorVista.setViewportView(test);
        
        nuevaDefinicion.addActionListener((Action) -> {
        });
        
        limpiar.addActionListener((Action) -> {
            test.reiniciarPanel();
        });
        
        preferencias.addActionListener((Action) -> {
            panelDePreferencias.setVisible(!panelDePreferencias.isVisible());
        });
        
        
        contenedor.add(titulo);
        contenedor.add(barra);
        contenedor.add(contenedorVista);
        contenedor.add(nuevaDefinicion);
        contenedor.add(limpiar);
        contenedor.add(copiar);
        contenedor.add(preferencias);
        
        
        layout.putConstraint(SpringLayout.NORTH, titulo, 10, SpringLayout.NORTH, contenedor);
        layout.putConstraint(SpringLayout.EAST, titulo, -30, SpringLayout.EAST, contenedor);
        
        layout.putConstraint(SpringLayout.SOUTH, barra, 0, SpringLayout.NORTH, contenedorVista);
        layout.putConstraint(SpringLayout.WEST, barra, 0, SpringLayout.WEST, contenedorVista);
        
        layout.putConstraint(SpringLayout.NORTH, contenedorVista, 10, SpringLayout.SOUTH, titulo);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, contenedorVista, 0, SpringLayout.HORIZONTAL_CENTER, contenedor);
        
        layout.putConstraint(SpringLayout.NORTH, nuevaDefinicion, 10, SpringLayout.SOUTH, contenedorVista);
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
        
        contenedorVista.setBackground(AppUtils.APP_BG_A_COLOR);
        contenedorVista.getVerticalScrollBar().setUI(new ColorScrollBarUI());
        contenedorVista.getHorizontalScrollBar().setUI(new ColorScrollBarUI());
        test.actualizarTema();
        
        nuevaDefinicion.actualizarTema();
        limpiar.actualizarTema();
        copiar.actualizarTema();
        preferencias.actualizarTema();
        
        panelDePreferencias.actualizarTema();
    }
}
