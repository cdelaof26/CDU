package cdu.ui;

import cdu.utils.AppUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;

/**
 *
 * @author cristopher
 */
public class PanelDeConversiones extends JPanel {
    // Elementos UI
    private final SpringLayout layout = new SpringLayout();
    private final JLabel ingresaUnDato = new JLabel("<html><p align='center'>Ingresa un dato<br>a convertir</p></html>");
    
    /**
     * 0 - Controlador principal<br>
     * 1 - Numerador de unidades in<br>
     * 2 - Denominador de unidades in<br>
     * 3 - Numerador de unidades out<br>
     * 4 - Denominador de unidades out<br>
     */
    private final TextFieldUnformated [] entradas = new TextFieldUnformated[5];
    private final JLabel flechaDivisora = new JLabel(" -> ");
    private final Divisor [] divisores = new Divisor[2];
    
    private final JLabel resultadoSimple = new JLabel();
    
    private final ArrayList<JLabel> resultadoComplejo = new ArrayList<>();
    // Fin elementos UI
    
    private final PanelDeConversiones panel = this;
    
    public PanelDeConversiones() {
        initUI();
    }
    
    public final void initUI() {
        this.setPreferredSize(new Dimension(940, 450));
        this.setLayout(layout);
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                prepararEntradaDeDatos();
            }
        });
        
        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("TAB"), "Tab");
        this.getActionMap().put("Tab", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 1; i < 5; i++)
                    if (entradas[i].getText().isEmpty()) {
                        entradas[i].requestFocus();
                        return;
                    }
                
                for (int i = 0; i < 5; i++)
                    if (entradas[i].isFocusOwner())
                        if (i + 1 < 5)
                            entradas[i + 1].requestFocus();
                        else
                            entradas[0].requestFocus();
            }
        });
        

        flechaDivisora.setFont(AppUtils.fondoXL);
        flechaDivisora.setVisible(false);
        
        divisores[0] = new Divisor();
        divisores[1] = new Divisor();
        
        this.add(flechaDivisora);
        this.add(divisores[0]);
        this.add(divisores[1]);
        
        
        entradas[0] = new TextFieldUnformated(true);
        entradas[0].setVisible(false);
        entradas[0].setHorizontalAlignment(JTextField.RIGHT);
        this.add(entradas[0]);
        
        
        for (int i = 1; i < 5; i++) {
            entradas[i] = new TextFieldUnformated(false);
            entradas[i].setVisible(false);
            entradas[i].setHorizontalAlignment(JTextField.CENTER);
            
            this.add(entradas[i]);
        }
        
        ingresaUnDato.setFont(new Font(AppUtils.NOMBRE_DE_LA_FUENTE, Font.PLAIN, 24));
        ingresaUnDato.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                prepararEntradaDeDatos();
            }
        });
        
        resultadoSimple.setFont(AppUtils.fondoXL);
        
        
        this.add(ingresaUnDato);
        this.add(resultadoSimple);
        
        
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, ingresaUnDato, 0, SpringLayout.VERTICAL_CENTER, this);
        layout.putConstraint(SpringLayout.WEST, ingresaUnDato, 50, SpringLayout.WEST, this);
        
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, entradas[0], 0, SpringLayout.VERTICAL_CENTER, ingresaUnDato);
        layout.putConstraint(SpringLayout.WEST, entradas[0], 20, SpringLayout.WEST, ingresaUnDato);
        
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, divisores[0], 0, SpringLayout.VERTICAL_CENTER, ingresaUnDato);
        layout.putConstraint(SpringLayout.WEST, divisores[0], 10, SpringLayout.EAST, entradas[0]);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, flechaDivisora, 0, SpringLayout.VERTICAL_CENTER, ingresaUnDato);
        layout.putConstraint(SpringLayout.WEST, flechaDivisora, 10, SpringLayout.EAST, divisores[0]);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, divisores[1], 0, SpringLayout.VERTICAL_CENTER, ingresaUnDato);
        layout.putConstraint(SpringLayout.WEST, divisores[1], 10, SpringLayout.EAST, flechaDivisora);
        
        layout.putConstraint(SpringLayout.SOUTH, entradas[1], 0, SpringLayout.NORTH, divisores[0]);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, entradas[1], 0, SpringLayout.HORIZONTAL_CENTER, divisores[0]);
        layout.putConstraint(SpringLayout.NORTH, entradas[2], 0, SpringLayout.SOUTH, divisores[0]);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, entradas[2], 0, SpringLayout.HORIZONTAL_CENTER, divisores[0]);
        
        layout.putConstraint(SpringLayout.SOUTH, entradas[3], 0, SpringLayout.NORTH, divisores[1]);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, entradas[3], 0, SpringLayout.HORIZONTAL_CENTER, divisores[1]);
        layout.putConstraint(SpringLayout.NORTH, entradas[4], 0, SpringLayout.SOUTH, divisores[1]);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, entradas[4], 0, SpringLayout.HORIZONTAL_CENTER, divisores[1]);
        
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, resultadoSimple, 0, SpringLayout.VERTICAL_CENTER, ingresaUnDato);
        layout.putConstraint(SpringLayout.WEST, resultadoSimple, 5, SpringLayout.EAST, entradas[0]);
    }
    
    public void actualizarTema() {
        this.setBackground(AppUtils.APP_BG_A_COLOR);
        
        
        flechaDivisora.setForeground(AppUtils.APP_FG_COLOR);
        for (JLabel d : divisores)
            d.setForeground(AppUtils.APP_FG_COLOR);
        
        
        for (TextFieldUnformated t : entradas) {
            t.setBackground(AppUtils.APP_BG_A_COLOR);
            t.setForeground(AppUtils.APP_FG_COLOR);
        }
        
        if (AppUtils.usarTemaOscuro) {
            ingresaUnDato.setForeground(Color.LIGHT_GRAY);
        } else {
            ingresaUnDato.setForeground(Color.GRAY);
        }
        
        resultadoSimple.setForeground(AppUtils.APP_FG_COLOR);
    }
    
    public void prepararEntradaDeDatos() {
        ingresaUnDato.setVisible(false);
        entradas[0].setVisible(true);
        entradas[0].requestFocus();
    }
    
    public void reiniciarPanel() {
        for (TextFieldUnformated t : entradas)
            t.setText("");
        
        ingresaUnDato.setVisible(true);
        
        for (TextFieldUnformated t : entradas)
            t.setVisible(false);
        
        flechaDivisora.setVisible(false);
        for (Divisor d : divisores)
            d.setVisible(false);
        
        resultadoSimple.setText("");
        
        this.setPreferredSize(new Dimension(940, 450));
    }
    
    private class TextFieldUnformated extends JTextField {
        @Override
        public boolean isManagingFocus() {
            return true;
        }
        
        public TextFieldUnformated(boolean entradaPrincipal) {
            this.setBackground(AppUtils.APP_BG_A_COLOR);
            this.setCaretColor(AppUtils.APP_FG_COLOR);
            this.setSelectedTextColor(AppUtils.APP_FG_COLOR);
            this.setSelectionColor(Color.GRAY);
            this.setBorder(null);
            this.setFont(AppUtils.fondoXL);
            
            
            
            int [] dimensiones = AppUtils.obtenerLongitudDeTexto("000", AppUtils.fondoXL);
            
            this.setPreferredSize(new Dimension(dimensiones[0], dimensiones[1]));
            
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && getText().isEmpty()) {
                        int i = entradas.length - 1;
                        while (i > -1) {
                            if (entradas[i].isFocusOwner())
                                if (i - 1 > -1) {
                                    entradas[i - 1].requestFocus();
                                    return;
                                }
                            i--;
                        }
                    }
                }
                
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyChar() == '/' && entradaPrincipal && !entradas[1].isVisible()) {
                        // Solo la entrada principal [0], puede poner las otras dos
                        
                        entradas[0].setText(entradas[0].getText().replace("/", ""));
                        
                        String texto = entradas[0].getText();
                        String textoSinLetras = texto.replaceAll("[a-zA-Z]", "");
                        
                        if (!texto.equals(textoSinLetras)) {
                            entradas[0].setText(textoSinLetras);
                            entradas[1].setText(texto.replace(textoSinLetras, ""));
                        }
                        
                        
                        entradas[1].setVisible(true);
                        entradas[2].setVisible(true);
                        entradas[3].setVisible(true);
                        entradas[4].setVisible(true);
                        
                        entradas[1].requestFocus();
                        flechaDivisora.setVisible(true);
                        for (Divisor d : divisores)
                            d.setVisible(true);
                        
                        layout.putConstraint(SpringLayout.VERTICAL_CENTER, resultadoSimple, 0, SpringLayout.VERTICAL_CENTER, ingresaUnDato);
                        layout.putConstraint(SpringLayout.WEST, resultadoSimple, 10, SpringLayout.EAST, divisores[1]);
                    } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && !entradaPrincipal && entradas[1].isVisible()) {
                        if (entradas[1].getText().isEmpty() && entradas[2].getText().isEmpty()) {
                            entradas[0].requestFocus();
                            
                            entradas[1].setVisible(false);
                            entradas[2].setVisible(false);
                            entradas[3].setVisible(false);
                            entradas[4].setVisible(false);
                            
                            flechaDivisora.setVisible(false);
                            for (Divisor d : divisores)
                                d.setVisible(false);
                            
                            layout.putConstraint(SpringLayout.VERTICAL_CENTER, resultadoSimple, 0, SpringLayout.VERTICAL_CENTER, ingresaUnDato);
                            layout.putConstraint(SpringLayout.WEST, resultadoSimple, 5, SpringLayout.EAST, entradas[0]);
                        }
                    }
                    
                    setPreferredSize(new Dimension(AppUtils.obtenerLongitudDeTexto(getText() + "__", AppUtils.fondoXL)[0], getPreferredSize().height));
                    if (divisores[0].isVisible()) {
                        int longitudEntrada1 = entradas[1].getPreferredSize().width;
                        int longitudEntrada2 = entradas[2].getPreferredSize().width;
                        
                        int longitudEntrada3 = entradas[3].getPreferredSize().width;
                        int longitudEntrada4 = entradas[4].getPreferredSize().width;
                        
                        if (longitudEntrada1 > longitudEntrada2)
                            divisores[0].setPreferredSize(new Dimension(longitudEntrada1, 3));
                        else
                            divisores[0].setPreferredSize(new Dimension(longitudEntrada2, 3));
                        
                        if (longitudEntrada3 > longitudEntrada4)
                            divisores[1].setPreferredSize(new Dimension(longitudEntrada3, 3));
                        else
                            divisores[1].setPreferredSize(new Dimension(longitudEntrada4, 3));
                    }
                    
                    new CalculadorDeConversiones().start();
                    
                    panel.revalidate();
                    panel.repaint();
                }
            });
        }
    }
    
    class CalculadorDeConversiones extends Thread {
        @Override
        public void run() {
            String [] datos;
            
            if (!entradas[1].isVisible())
                datos = entradas[0].getText().replace(" ", "").split("->");
            else
                datos = (entradas[0].getText() + entradas[1].getText() + "/" + entradas[2].getText() + "->" + entradas[3].getText() + "/" + entradas[4].getText()).replace(" ", "").split("->");
            
//            System.out.println("got " + Arrays.toString(datos));
            
            if (datos.length != 2) {
                resultadoSimple.setText("Entrada inválida");
                resultadoSimple.setForeground(Color.red);
                return;
            }
            
            String unidadDeEntrada = datos[0].replaceAll("^-?\\d*?.?\\d+(?=[a-zA-Z])", "");
            String valor = datos[0].replace(unidadDeEntrada, "");
            String unidadDeSalida = datos[1];
            
            if (datos[0].equals("") || unidadDeEntrada.equals("") || valor.equals("") || unidadDeSalida.equals("")) {
                resultadoSimple.setText("Entrada inválida");
                resultadoSimple.setForeground(Color.red);
                return;
            }
            
            AppUtils.ScriptRunner script = AppUtils.ejecutarScript("-m", "conv", "-in", valor, "-in_u", unidadDeEntrada, "-out_u", unidadDeSalida);
            script.start();
            
            while(script.estaEjecutandose())
                try { sleep(1); } catch (InterruptedException ex) { }
            
            String salida = script.getSalida();
//            System.out.println("out " + salida);
//            System.out.println("exit " + script.getCodigoDeSalida());
            
            if (script.getCodigoDeSalida() != 0) {
                if (salida.equalsIgnoreCase("NO_DATA")) {
                    resultadoSimple.setForeground(Color.MAGENTA);
                    resultadoSimple.setText("Unidad desconocida");  
                } else if (salida.equalsIgnoreCase("INVALID_IN_OUT_U")) {
                    resultadoSimple.setForeground(Color.ORANGE);
                    resultadoSimple.setText("Unidades incompletas");
                } else {
                    resultadoSimple.setForeground(Color.red);
                    resultadoSimple.setText("Entrada inválida");
                }
                
                return;
            }
            
            resultadoSimple.setForeground(AppUtils.APP_FG_COLOR);
            if (entradas[0].getText().isEmpty()) {
                resultadoSimple.setText("");
                return;
            }
            
            if (salida.replace("->", "").length() == salida.length()) {
                salida = "= " + salida;
            } else if (!salida.contains("/")) {
                salida = "= " + salida;
            }
            
            resultadoSimple.setText(salida);
            
            int sumaDeLargos = resultadoSimple.getPreferredSize().width + entradas[0].getPreferredSize().width + 145;
            if (divisores[0].isVisible())
                sumaDeLargos += divisores[0].getPreferredSize().width + flechaDivisora.getPreferredSize().width + divisores[1].getPreferredSize().width;
            
            if (sumaDeLargos > 940)
                panel.setPreferredSize(new Dimension(sumaDeLargos, 440));
            else
                panel.setPreferredSize(new Dimension(sumaDeLargos, 450));
        }
    }
    
    private class Divisor extends JLabel {
        public Divisor() {
            initUI();
        }
        
        private void initUI() {
            this.setFont(AppUtils.fondoXL);
            this.setVisible(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(AppUtils.APP_FG_COLOR);
            g.fillRect(0, 0, getPreferredSize().width, 3);
        }
    }
}
