package com.conciencia.main;

import com.conciencia.loaders.MainLoader;
import com.conciencia.vertx.VertxConfig;
import io.vertx.core.Vertx;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * LibraryManager FX App.
 * 
 * 1.- Se arranca y configura vertx
 * 2.- Se inicia el Javafx API.
 * 3.- Se muestra la pantalla principal
 * 
 * @author Ernesto Cantu
 */
public class MainApp extends Application {
    
    /* Instancia de vertx que controla la aplicación */
    public static Vertx vertx;

    /**
     * Método ejecutado por JavaFX para iniciar la aplicación.
     * @param primaryStage
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        VertxConfig.config();
        MainLoader.getInstance().load(primaryStage);        
    }
    
    /**
     * Método main. Punto de entrada a la aplicación
     * @param args argumentos de entrada pasados por consola
     */
    public static void main(String[] args) {
       launch(args);
    }
}
