package com.conciencia.loaders;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase abstracta que define como cargar un stage.
 * @author Ernesto Cantu
 */
public abstract class Loader {
    
    protected String fxmlRoute,styleRoute,title;
        
    /**
     * Método que cargar un FXML y generar el controlador necesario.
     * 
     * @param stage
     * @throws Exception 
     */
    public void load(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(fxmlRoute));        
        Scene scene = new Scene(root);        
        scene.getStylesheets().add(styleRoute);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
