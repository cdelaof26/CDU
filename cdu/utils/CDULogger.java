package cdu.utils;

/**
 *
 * @author cristopher
 */
public class CDULogger {
    public enum TipoDeDato {
        EMPTY, GRAVE, ERROR, INFO, TRACE
    }
    
    private static String registro = "";
    
    public static void imprimirInfo(TipoDeDato tipo, String mensaje) {
        switch(tipo) {
            case EMPTY:
                mensaje = "";
            break;
            case GRAVE:
                mensaje = "[GRAVE] " + mensaje;
            break;
            case ERROR:
                mensaje = "[ERROR] " + mensaje;
            break;
            case INFO:
                mensaje = "[INFO]  " + mensaje;
            break;
            case TRACE:
                mensaje = "[TRACE]     " + mensaje;
            break;
        }
        
        System.out.println(mensaje);
        registro += mensaje + "\n";
    }

    public static void imprimirExcepcion(Exception e) {
        imprimirInfo(TipoDeDato.ERROR, e.getMessage());
        for (StackTraceElement trace : e.getStackTrace())
            imprimirInfo(TipoDeDato.TRACE, trace.toString());
    }
    
    public static String getRegistro() {
        return registro;
    }
}
