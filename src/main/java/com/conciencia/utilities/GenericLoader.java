package com.conciencia.utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase abstracta que define como cargar un stage.
 * @author Ernesto Cantu
 */
public class GenericLoader {
    
    /**
     * Atributos necesarios para cargar un fxml.
     */
    private String fxmlRoute,styleRoute,title;
    
    public GenericLoader(String fxmlRoute,String styleRoute,String title){
        this.fxmlRoute = fxmlRoute;
        this.styleRoute = styleRoute;
        this.title = title;
    }
        
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
