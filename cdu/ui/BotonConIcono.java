package cdu.ui;

import cdu.utils.AppUtils;
import cdu.utils.CDULogger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

/**
 * Botones con iconos pre-hechos
 *
 * @author cristopher
 */
public class BotonConIcono extends JButton {
    private static int iconosNoCargados = 0;
    
    private final String nombreDelIcono;
    
    private int ancho = 30;
    private int altura = 30;
    
    // Elementos UI
    private final SpringLayout layout = new SpringLayout();
    
    private final JLabel icono;
    private final JLabel texto;
    // Fin elementos UI
    

    public BotonConIcono(String texto, String nombreDelIcono) {
        this.nombreDelIcono = nombreDelIcono;
        
        this.icono = new JLabel(obtenerIcono());
        this.texto = new JLabel(texto);
        
        initUI();
    }
    
    public BotonConIcono(String nombreDelIcono, boolean opaco) {
        this.nombreDelIcono = nombreDelIcono;
        
        this.icono = new JLabel(obtenerIcono());
        this.texto = null;
        
        ancho = 13;
        altura = 13;
        
        this.setOpaque(opaco);
        if (!opaco)
            this.setBackground(new Color(0, 0, 0, 0));
        
        initUI();
    }
    
    public final void initUI() {
        if (texto == null) {
            this.setPreferredSize(new Dimension(22, 22));
        } else {
            this.setPreferredSize(new Dimension(120, 80));
        }
        
        this.setLayout(layout);
        this.setBorder(null);
        this.setFocusPainted(false);
        
        this.add(icono);
        
        if (texto != null) {
            this.add(texto);
            
            texto.setFont(AppUtils.fondoEstandar);
            
            layout.putConstraint(SpringLayout.NORTH, icono, 10, SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, icono, 0, SpringLayout.HORIZONTAL_CENTER, this);

            layout.putConstraint(SpringLayout.SOUTH, texto, -10, SpringLayout.SOUTH, this);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, texto, 0, SpringLayout.HORIZONTAL_CENTER, this);
        } else {
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, icono, 0, SpringLayout.VERTICAL_CENTER, this);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, icono, 0, SpringLayout.HORIZONTAL_CENTER, this);
        }
    }
    
    public void actualizarTema() {
        if (this.isOpaque())
            this.setBackground(AppUtils.APP_BG_COLOR);
        
        if (texto != null)
            texto.setForeground(AppUtils.APP_FG_COLOR);
        
        icono.setIcon(obtenerIcono());
    }
    
    
    private ImageIcon obtenerIcono() {
        String ruta = "cdu/assets/l" + nombreDelIcono + ".png";
        
        if (AppUtils.usarTemaOscuro)
            ruta = "cdu/assets/d" + nombreDelIcono + ".png";
        
        
        BufferedImage icon = null;
        Image resizedImage = null;
        
        URL rutaInterna = ClassLoader.getSystemClassLoader().getResource(ruta);
        
        try {
            icon = ImageIO.read(rutaInterna);
        } catch (IllegalArgumentException | IOException ex) {
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.ERROR, "Error al cargar el icono \"" + nombreDelIcono + "\"");
            CDULogger.imprimirExcepcion(ex);
            
            iconosNoCargados++;
            if (iconosNoCargados == 3) {
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Varios iconos no pudieron cargarse");
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.ERROR, "  Esta copia de CDU podría estar dañanada");
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "  La app se cerrará ahora");
                
                new MensajeFlotante().mostrarMensaje(CDULogger.getRegistro(), CDULogger.TipoDeDato.GRAVE);
            }
        }
        
        if (icon != null) {
            resizedImage = icon.getScaledInstance(ancho, altura, Image.SCALE_SMOOTH);
        }
        
        if (resizedImage == null) {
            return null;
        }
        
        return new ImageIcon(resizedImage);
    }
}
