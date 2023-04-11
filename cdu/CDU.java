package cdu;

import cdu.ui.CDUMain;
import cdu.ui.MensajeFlotante;
import cdu.utils.CDULogger;
import cdu.utils.AppUtils;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Clase principal
 *
 * @author cristopher
 */
public class CDU {
    public static void main(String[] args) {
        inicializarApp();
        
        new CDUMain().setVisible(true);
    }
    
    public static void inicializarApp() {
        CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "  CDU v0.0.5");
        CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Sistema " + AppUtils.SYSTEM_NAME);
        
        
        CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Inicializando...");
        
        if (GraphicsEnvironment.isHeadless()) {
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.GRAVE, "Ambiente no soportado, este programa requiere de un ambiente gráfico");
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "  La app se cerrará ahora");
            
            System.exit(1);
        }
        
        CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Aplicando Metal Look and Feel");
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("Button.select", Color.LIGHT_GRAY);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            CDULogger.imprimirExcepcion(ex);
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.GRAVE, "Error al aplicar");
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "  La app se cerrará ahora");
            new MensajeFlotante().mostrarMensaje(CDULogger.getRegistro(), CDULogger.TipoDeDato.GRAVE);
            
            System.exit(1);
        }
        
        CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Comprobando instalación de Python...");
        if (AppUtils.pythonEstaInstalado()) {
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Python esta instalado");
        } else {
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.GRAVE, "Esta app requiere de Python");
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "  La app se cerrará ahora");
            new MensajeFlotante().mostrarMensaje(CDULogger.getRegistro(), CDULogger.TipoDeDato.GRAVE);
            
            System.exit(1);
        }
        
        CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Copiando el backend...");
        if (AppUtils.copiarBackend()) {
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Se copió el backend a " + AppUtils.rutaDelBackend.getAbsolutePath());
        } else {
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.ERROR, "Fallo al copiar el backend");
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "  Verifica la integridad del ejecutable");
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "  Verifica los permisos de escritura en " + AppUtils.USER_HOME);
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "  La app se cerrará ahora");
            new MensajeFlotante().mostrarMensaje(CDULogger.getRegistro(), CDULogger.TipoDeDato.ERROR);
            
            System.exit(1);
        }
        
        
        CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Comprobando configuración...");
        AppUtils.ScriptRunner script = AppUtils.ejecutarScript("-m", "man", "-theme", "get");
        script.start();
        while(script.estaEjecutandose())
            try { Thread.sleep(100); } catch (InterruptedException ex) { }
        
        if (script.getCodigoDeSalida() != 0) {
            CDULogger.imprimirMensajeMultilinea(CDULogger.TipoDeDato.UNK, script.getSalida());
        } else if (script.getSalida().equalsIgnoreCase("Dark")) {
            CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "  Aplicando tema oscuro...");
            AppUtils.cambiarTema(true);
        }
        
        
        CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Inicialización exitosa");
        
        CDULogger.imprimirMensaje(CDULogger.TipoDeDato.EMPTY, "");
        CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Iniciando UI...");
        CDULogger.imprimirMensaje(CDULogger.TipoDeDato.INFO, "Iniciando ventana principal...");
    }
}
