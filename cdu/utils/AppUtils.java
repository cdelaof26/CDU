package cdu.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

/**
 * Esta clase contiene utilidades para el manejo de la app
 * 
 * @author cristopher
 */
public class AppUtils {
    public static final String SYSTEM_NAME = System.getProperty("os.name"); // Nombre del sistema
    public static final String SYSTEM_ANCHOR = (SYSTEM_NAME.charAt(0) == 'W' || SYSTEM_NAME.charAt(0) == 'w') ? "\\" : "/"; // Separador de rutas
    
    public static final String USER_HOME = System.getProperty("user.home");  // Ruta de los datos del usuario
    
    public static File rutaDelBackend = new File(unirRutas(USER_HOME, ".cdu_data"));
    
    public static String [] DIRECTORIOS_DE_PYTHON = {"herramientas"};
    public static String [] ARCHIVOS_PY_EN_HERRAMIENTAS = {"__init__.py", "backend_api.py", "conversiones.py", "datos_del_app.py", "recuadro.py", "utilidades.py"};
    public static String [] ARCHIVOS_EN_CDUBACKEND = {"main.py"};
    
    public static final String COMANDO_DE_PYTHON = SYSTEM_ANCHOR.equals("\\") ? "python" : "python3";
    
    
    public static boolean usarTemaOscuro = false;
    
    public static final Color LIGHT_BG = Color.WHITE;
    public static final Color LIGHT_BG_A = new Color(238, 238, 238);
    public static final Color LIGHT_FG = Color.BLACK;
    
    public static final Color DARK_BG = Color.BLACK;
    public static final Color DARK_BG_A = new Color(30, 30, 30);
    public static final Color DARK_FG = Color.WHITE;
    
    
    public static Color APP_BG_COLOR = LIGHT_BG;
    public static Color APP_BG_A_COLOR = LIGHT_BG_A;
    public static Color APP_FG_COLOR = LIGHT_FG;
    
    public static final String NOMBRE_DE_LA_FUENTE = SYSTEM_ANCHOR.equals("\\") ? "Arial" : SYSTEM_NAME.charAt(0) == 'M' ? "Helvetica Neue" : "";
    
    public static Font fondoEstandar = new Font(NOMBRE_DE_LA_FUENTE, Font.PLAIN, 13);
    public static Font fondoES = new Font(NOMBRE_DE_LA_FUENTE, Font.PLAIN, 18);
    public static Font fondoXL = new Font(NOMBRE_DE_LA_FUENTE, Font.BOLD, 24);
    public static Font fondoXXL = new Font(NOMBRE_DE_LA_FUENTE, Font.BOLD, 32);
    
    private static final AffineTransform affinetransform = new AffineTransform();     
    private static final FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
    
    
    public static String unirRutas(String base, String ... rutas) {
        for (String ruta : rutas) {
            if (base.endsWith(SYSTEM_ANCHOR))
                base += ruta;
            else
                base += SYSTEM_ANCHOR + ruta;
        }
        
        CDULogger.imprimirMensaje(CDULogger.TipoDeDato.DEBUG, "JPR " + base);
        
        return base;
    }
    
    public static String leerArchivo(InputStream inputStream) throws IOException, NullPointerException {
        String datos = "";
        
        InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader buffReader = new BufferedReader(isr);

        String linea = buffReader.readLine();
        
        while (linea != null) {
            datos += linea + "\n";
            linea = buffReader.readLine();
        }
        
        try { buffReader.close(); } catch (IOException ex) { }
        
        return datos;
    }
    
    public static boolean escribirArchivo(File archivo, String datos) {
        FileWriter fileWriter = null;
        
        boolean operacionExitosa = false;
        
        try {
            fileWriter = new FileWriter(archivo);
            fileWriter.write(datos);
            
            operacionExitosa = true;
        } catch (IOException ex) {
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.ERROR, "Error al escribir " + archivo.getAbsolutePath());
            CDULogger.imprimirExcepcion(ex);
        }
        
        if (fileWriter != null) {
            try { fileWriter.close(); } catch (IOException ex) { }
        }
        
        return operacionExitosa;
    }
    
    
    
    public static boolean copiarArchivoEmpaquetado(String rutaDelPaquete, String rutaDeDestino, String nombre) {
        File destino = new File(unirRutas(rutaDeDestino, nombre));
        
        InputStream origen = ClassLoader.getSystemResourceAsStream(rutaDelPaquete);

        String datos;
        try {
            datos = leerArchivo(origen);
            
            return escribirArchivo(destino, datos);
        } catch (IOException | NullPointerException ex) {
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.ERROR, "Error al leer archivo en flujo de datos (buffered: " + rutaDelPaquete + ")");
            CDULogger.imprimirExcepcion(ex);
        }
        
        return false;
    }
    
    public static boolean copiarBackend() {
        boolean operacionExitosa = true;
        
        if (!rutaDelBackend.exists()) {
            rutaDelBackend.mkdir();
        }
            
        for (String directorio : DIRECTORIOS_DE_PYTHON) {
            File subdirectorio = new File(unirRutas(rutaDelBackend.getAbsolutePath(), directorio));
            if (!subdirectorio.exists()) {
                subdirectorio.mkdir();
            }
        }

        for (String archivo : ARCHIVOS_EN_CDUBACKEND) {
            operacionExitosa = operacionExitosa && copiarArchivoEmpaquetado("cdubackend/" + archivo, rutaDelBackend.getAbsolutePath(), archivo);
        }

        for (String archivo : ARCHIVOS_PY_EN_HERRAMIENTAS) {
            operacionExitosa = operacionExitosa && copiarArchivoEmpaquetado("cdubackend/herramientas/" + archivo, rutaDelBackend.getAbsolutePath(), unirRutas("herramientas", archivo));
        }
        
        return operacionExitosa;
    }
    
    
    public static boolean pythonEstaInstalado() {
        try {
            Process process = Runtime.getRuntime().exec(COMANDO_DE_PYTHON);
            process.destroy();
            return true;
        } catch (IOException e) { }
        
        return false;
    }
    
    public static int [] obtenerLongitudDeTexto(String texto, Font fondo) {
        Rectangle2D r2D = fondo.getStringBounds(texto, frc);
        
        return new int []{(int) r2D.getWidth(), (int) r2D.getHeight()};
    }
    
    public static void cambiarTema(boolean soloUI) {
        usarTemaOscuro = !usarTemaOscuro;
        
        if (usarTemaOscuro) {
            APP_BG_COLOR = DARK_BG;
            APP_BG_A_COLOR = DARK_BG_A;
            APP_FG_COLOR = DARK_FG;
            UIManager.put("Button.select", Color.GRAY);
        } else {
            APP_BG_COLOR = LIGHT_BG;
            APP_BG_A_COLOR = LIGHT_BG_A;
            APP_FG_COLOR = LIGHT_FG;
            UIManager.put("Button.select", Color.LIGHT_GRAY);
        }
        
        if (!soloUI)
            ejecutarScript("-m", "man", "-theme", "toggle").start();
    }
    
    public static ScriptRunner ejecutarScript(String ... args) {
        return new ScriptRunner(args);
    }
    
    /**
     * Esta clase es para ejecutar scripts en python con hilos, se recomienda<br>
     * el uso con el método {@link ejecutarScript} 
     */
    public static class ScriptRunner extends Thread {
        private final String [] args;
        private String salida = "";
        private int codigoDeSalida = -1;
        private boolean ejecutando = true;
        
        private Process proceso;

        public ScriptRunner(String [] args) {
            this.args = args;
        }
        
        @Override
        public void run() {
            ejecutando = true;
            
            String comando = "";
            try {
                comando = COMANDO_DE_PYTHON + " main.py " + concatenarArgumentos();
                proceso = Runtime.getRuntime().exec(comando, null, rutaDelBackend);
                
                while (proceso.isAlive()) {
                    try {
                        leerDatosDeSalidaDelProceso();
                    } catch (InterruptedException | IOException | NullPointerException ex) {}
                    
                    try { sleep(100); } catch (InterruptedException ex) { }
                }
                
                codigoDeSalida = proceso.waitFor();
            } catch (IOException | InterruptedException ex) {
                CDULogger.imprimirMensaje(CDULogger.TipoDeDato.ERROR, "Error al ejecutar script");
                CDULogger.imprimirExcepcion(ex);
            }
            
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.DEBUG, "Command \"" + comando + "\" ended with code " + codigoDeSalida);
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.DEBUG, " cwd \"" + rutaDelBackend.getAbsolutePath() + "\"");
            CDULogger.imprimirMensajeMultilinea(CDULogger.TipoDeDato.DEBUG, "  Output\n" + salida);
            
            ejecutando = false;
        }
        
        private void leerDatosDeSalidaDelProceso() throws InterruptedException, IOException, NullPointerException {
            int timeout = 0;
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
                while (!reader.ready()) {
                    try { sleep(100); } catch (InterruptedException ex) { }
                    timeout++;
                    
                    if (!reader.ready() && timeout == 5) {
                        throw new InterruptedException("Tiempo de espera terminado");
                    }
                }
                
                String linea = reader.readLine();
                while(linea != null) {
                    salida += linea + "\n";
                    linea = reader.readLine();
                }
            }
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getErrorStream()))) {
                while (!reader.ready()) {
                    try { sleep(100); } catch (InterruptedException ex) { }
                    timeout++;
                    
                    if (!reader.ready() && timeout == 5) {
                        throw new InterruptedException("Tiempo de espera terminado");
                    }
                }
                
                String linea = reader.readLine();
                while(linea != null) {
                    salida += linea + "\n";
                    linea = reader.readLine();
                }
            }
        }
        
        private void terminarProceso() {
            if (proceso != null && proceso.isAlive())
                proceso.destroy();
        }
        
        private String concatenarArgumentos() {
            String cadena = "";
            for (String arg : args)
                cadena += arg + " ";
            
            return cadena;
        }

        public String getSalida() {
            if (!salida.isEmpty())
                return (String) salida.subSequence(0, salida.length() - 1);
            
            return salida;
        }

        public int getCodigoDeSalida() {
            return codigoDeSalida;
        }

        public boolean estaEjecutandose() {
            return ejecutando;
        }
    }
}
