package cdu;

import cdu.ui.CDUMain;
import cdu.ui.MensajeFlotante;
import cdu.utils.CDULogger;
import cdu.utils.AppUtils;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Clase principal
 *
 * @author cristopher
 */
public class CDU {
    public static void main(String[] args) {
        CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "  CDU v0.0.4");
        CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "Sistema " + AppUtils.systemName);
        
        
        CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "Inicializando...");
        
        CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "Aplicando Metal Look and Feel");
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            CDULogger.imprimirExcepcion(ex);
            CDULogger.imprimirInfo(CDULogger.TipoDeDato.GRAVE, "Error al aplicar");
            CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "  La app se cerrará ahora");
            new MensajeFlotante().mostrarMensaje(CDULogger.getRegistro(), CDULogger.TipoDeDato.GRAVE);
            
            System.exit(1);
        }
        
        CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "Comprobando instalación de Python...");
        if (AppUtils.pythonEstaInstalado()) {
            CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "Python esta instalado");
        } else {
            CDULogger.imprimirInfo(CDULogger.TipoDeDato.GRAVE, "Esta app requiere de Python");
            CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "  La app se cerrará ahora");
            new MensajeFlotante().mostrarMensaje(CDULogger.getRegistro(), CDULogger.TipoDeDato.GRAVE);
            
            System.exit(1);
        }
        
        CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "Copiando el backend...");
        if (AppUtils.copiarBackend()) {
            CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "Se copió el backend a " + AppUtils.rutaDelBackend.getAbsolutePath());
        } else {
            CDULogger.imprimirInfo(CDULogger.TipoDeDato.ERROR, "Fallo al copiar el backend");
            CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "  Verifica la integridad del ejecutable");
            CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "  Verifica los permisos de escritura en " + AppUtils.userHome);
            CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "  La app se cerrará ahora");
            new MensajeFlotante().mostrarMensaje(CDULogger.getRegistro(), CDULogger.TipoDeDato.ERROR);
            
            System.exit(1);
        }
        
        CDULogger.imprimirInfo(CDULogger.TipoDeDato.EMPTY, "");
        CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "Iniciando UI...");
        
        CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "Inicialización exitosa");
        CDULogger.imprimirInfo(CDULogger.TipoDeDato.INFO, "Iniciando ventana principal...");
        
        new CDUMain().setVisible(true);
    }
}
