package cdu.utils;

/**
 * Utilidad básica para imprimir mensajes
 * 
 * @author cristopher
 */
public class CDULogger {
    public enum TipoDeDato {
        DEBUG, UNK, EMPTY, GRAVE, ERROR, INFO, TRACE
    }
    
    private static String registro = "";
    
    public static boolean printDebugData = false;
    
    public static void imprimirMensaje(TipoDeDato tipo, String mensaje) {
        switch(tipo) {
            case DEBUG:
                if (!printDebugData)
                    return;
                mensaje = "[DEBUG] " + mensaje + " ";
            break;
            case UNK:
                mensaje = "[UNK]   " + mensaje + " ";
            break;
            case EMPTY:
                mensaje = "";
            break;
            case GRAVE:
                mensaje = "[GRAVE] " + mensaje + " ";
            break;
            case ERROR:
                mensaje = "[ERROR] " + mensaje + " ";
            break;
            case INFO:
                mensaje = "[INFO]  " + mensaje + " ";
            break;
            case TRACE:
                mensaje = "[TRACE]     " + mensaje + " ";
            break;
        }
        
        System.out.println(mensaje);
        registro += mensaje + "\n";
    }

    public static void imprimirExcepcion(Exception e) {
        imprimirMensaje(TipoDeDato.ERROR, e.getMessage());
        for (StackTraceElement trace : e.getStackTrace())
            imprimirMensaje(TipoDeDato.TRACE, trace.toString());
    }
    
    public static void imprimirMensajeMultilinea(TipoDeDato tipo, String mensaje) {
        for (String linea : mensaje.split("\n"))
            imprimirMensaje(tipo, linea);
    }
    
    public static String getRegistro() {
        return registro;
    }
}
