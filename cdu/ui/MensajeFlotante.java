package cdu.ui;

import cdu.utils.CDULogger;
import cdu.utils.AppUtils;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

/**
 *
 * @author cristopher
 */
public class MensajeFlotante extends JDialog {
    private String mensaje;
    private CDULogger.TipoDeDato tipoDeMensaje;
    
    // Elementos UI
    
    private final SpringLayout layout = new SpringLayout();
    
    private final JPanel contenedor = new JPanel(layout);
    
    private final JLabel titulo = new JLabel();
    private final JScrollPane cuerpoDelMensaje = new JScrollPane();
    private final JTextArea texto = new JTextArea();
    
    private final JButton aceptar = new JButton("Aceptar");
    // Fin elementos UI
    
    public void initUI() {
        this.setSize(new Dimension(500, 330));
        this.setLocationRelativeTo(null);
        this.add(contenedor);
        this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        
        titulo.setFont(new Font(titulo.getFont().getFontName(), Font.BOLD, 32));
        titulo.setText(tipoDeMensaje.name());
        
        texto.setText(mensaje);
        texto.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        texto.setEditable(false);
        
        cuerpoDelMensaje.setViewportView(texto);
        cuerpoDelMensaje.setPreferredSize(new Dimension(450, 190));
        cuerpoDelMensaje.setBorder(null);
        
        aceptar.setPreferredSize(new Dimension(120, 22));
        aceptar.setBorder(null);
        aceptar.setFocusPainted(false);
        aceptar.addActionListener((Action) -> {
            setVisible(false);
        });
        
        
        contenedor.add(titulo);
        contenedor.add(cuerpoDelMensaje);
        contenedor.add(aceptar);
        
        
        layout.putConstraint(SpringLayout.NORTH, titulo, 15, SpringLayout.NORTH, contenedor);
        layout.putConstraint(SpringLayout.WEST, titulo, 20, SpringLayout.WEST, contenedor);
        
        layout.putConstraint(SpringLayout.NORTH, cuerpoDelMensaje, 15, SpringLayout.SOUTH, titulo);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, cuerpoDelMensaje, 0, SpringLayout.HORIZONTAL_CENTER, contenedor);
        
        layout.putConstraint(SpringLayout.NORTH, cuerpoDelMensaje, 15, SpringLayout.SOUTH, titulo);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, cuerpoDelMensaje, 0, SpringLayout.HORIZONTAL_CENTER, contenedor);
        
        layout.putConstraint(SpringLayout.NORTH, aceptar, 10, SpringLayout.SOUTH, cuerpoDelMensaje);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, aceptar, 0, SpringLayout.HORIZONTAL_CENTER, contenedor);
        
        actualizarTema();
    }
    
    public void actualizarTema() {
        contenedor.setBackground(AppUtils.APP_BG_COLOR);
        texto.setBackground(AppUtils.APP_BG_A_COLOR);
        
        cuerpoDelMensaje.setBackground(AppUtils.APP_BG_A_COLOR);
        cuerpoDelMensaje.getVerticalScrollBar().setUI(new ColorScrollBarUI());
        cuerpoDelMensaje.getHorizontalScrollBar().setUI(new ColorScrollBarUI());
        
        aceptar.setBackground(AppUtils.APP_BG_A_COLOR);
        
        titulo.setForeground(AppUtils.APP_FG_COLOR);
        texto.setForeground(AppUtils.APP_FG_COLOR);
    }
    
    public void mostrarMensaje(String mensaje, CDULogger.TipoDeDato tipoDeMensaje) {
        this.mensaje = mensaje;
        this.tipoDeMensaje = tipoDeMensaje;
        
        initUI();
        
        this.setVisible(true);
        
        dispose();
    }
}
