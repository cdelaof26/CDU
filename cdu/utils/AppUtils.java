package cdu.utils;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Esta clase contiene utilidades y constantes para el manejo de la app
 * 
 * @author cristopher
 */
public class AppUtils {
    public static String systemName = System.getProperty("os.name"); // Nombre del sistema
    public static final String systemAnchor = (systemName.charAt(0) == 'W' || systemName.charAt(0) == 'w') ? "\\" : "/"; // Separador de rutas
    
    public static final String userHome = System.getProperty("user.home");  // Ruta de los datos del usuario
    
    public static File rutaDelBackend = new File(unirRutas(userHome, ".cdu_data"));
    
    public static String [] DIRECTORIOS_DE_PYTHON = {"herramientas"};
    public static String [] ARCHIVOS_PY_EN_HERRAMIENTAS = {"__init__.py", "backend_api.py", "conversiones.py", "datos_del_app.py", "recuadro.py", "utilidades.py"};
    public static String [] ARCHIVOS_EN_CDUBACKEND = {"main.py"};
    
    public static String comandoDePython = "python3";
    
    
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
    
    
    public static String unirRutas(String base, String ... rutas) {
        for (String ruta : rutas) {
            if (base.endsWith(systemAnchor))
                base += ruta;
            else
                base += systemAnchor + ruta;
        }
        
        return base;
    }
    
    public static String leerArchivo(InputStream inputStream) {
        String datos = "";
        
        FileReader fileReader = null;
        BufferedReader buffReader = null;
        
        try {
            buffReader = new BufferedReader(new InputStreamReader(inputStream));
            
            String linea = buffReader.readLine();
            
            while (linea != null) {
                datos += linea + "\n";
                linea = buffReader.readLine();
            }
        } catch (IOException | NullPointerException ex) {
            CDULogger.imprimirInfo(CDULogger.TipoDeDato.ERROR, "Error al leer archivo en flujo de datos (buffered)");
            CDULogger.imprimirExcepcion(ex);
        }
        
        if (fileReader != null) {
            try { fileReader.close(); } catch (IOException ex) { }
        }
        if (buffReader != null) {
            try { buffReader.close(); } catch (IOException ex) { }
        }
        
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
            CDULogger.imprimirInfo(CDULogger.TipoDeDato.ERROR, "Error al escribir " + archivo.getAbsolutePath());
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

        return escribirArchivo(destino, leerArchivo(origen));
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
            operacionExitosa = operacionExitosa && copiarArchivoEmpaquetado(unirRutas("cdubackend", archivo), rutaDelBackend.getAbsolutePath(), archivo);
        }

        for (String archivo : ARCHIVOS_PY_EN_HERRAMIENTAS) {
            operacionExitosa = operacionExitosa && copiarArchivoEmpaquetado(unirRutas("cdubackend", "herramientas", archivo), rutaDelBackend.getAbsolutePath(), unirRutas("herramientas", archivo));
        }
        
        return operacionExitosa;
    }
    
    
    public static boolean pythonEstaInstalado() {
        try {
            Process process = Runtime.getRuntime().exec("python3");
            process.destroy();
            return true;
        } catch (IOException e) { }
        
        try {
            Process process = Runtime.getRuntime().exec("python");
            process.destroy();
            
            comandoDePython = "python";
            return true;
        } catch (IOException e) { }
        
        return false;
    }
    
    public static void cambiarTema() {
        usarTemaOscuro = !usarTemaOscuro;
        
        if (usarTemaOscuro) {
            APP_BG_COLOR = DARK_BG;
            APP_BG_A_COLOR = DARK_BG_A;
            APP_FG_COLOR = DARK_FG;
        } else {
            APP_BG_COLOR = LIGHT_BG;
            APP_BG_A_COLOR = LIGHT_BG_A;
            APP_FG_COLOR = LIGHT_FG;
        }
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

        public ScriptRunner(String [] args) {
            this.args = args;
        }
        
        @Override
        public void run() {
            try {
                String comando = comandoDePython + " main.py " + concatenarArgumentos();
                
                Process proceso = Runtime.getRuntime().exec(comando, null, rutaDelBackend);
                
                codigoDeSalida = proceso.waitFor();
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
                
                String linea = reader.readLine();
                while(linea != null) {
                    salida += linea + "\n";
                    linea = reader.readLine();
                }
            } catch (IOException | InterruptedException ex) {
                CDULogger.imprimirInfo(CDULogger.TipoDeDato.ERROR, "Error al ejecutar script");
                CDULogger.imprimirExcepcion(ex);
            }
        }
        
        private String concatenarArgumentos() {
            String cadena = "";
            for (String arg : args)
                cadena += arg + " ";
            
            return cadena;
        }

        public String getSalida() {
            return salida;
        }

        public int getCodigoDeSalida() {
            return codigoDeSalida;
        }
    }
}