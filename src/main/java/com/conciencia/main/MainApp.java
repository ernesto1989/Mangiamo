package com.conciencia.main;

import com.conciencia.loaders.NuevaOrdenLoader;
import com.conciencia.vertx.VertxConfig;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Mangiamo Restaurant Software. Clase principal de arranque
 * 
 * 1.- Se arranca y configura vertx
 * 2.- Se inicia el Javafx API.
 * 3.- Se muestra la pantalla principal
 * 
 * @author Ernesto Cantu Valle
 * Conciencia 2018
 * ernesto.cantu1989@live.com
 * Versión 0.9
 * Octubre 10
 */
public class MainApp extends Application {
    
    /**
     * Método ejecutado por JavaFX para iniciar la aplicación.
     * @param primaryStage
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        VertxConfig.config();
        NuevaOrdenLoader.getInstance().load(primaryStage);        
    }
    
    /**
     * Método main. Punto de entrada a la aplicación
     * @param args argumentos de entrada pasados por consola
     */
    public static void main(String[] args) {
       launch(args);
    }
}