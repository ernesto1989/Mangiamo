package com.conciencia.loaders;

/**
 * 
 * @author Ernesto Cantu
 */
public class AdminLoader extends Loader {
    
    /**
     * el objeto loader para abrir la pantalla de nueva orden
     */
    public static AdminLoader instance;
    
    /**
     * Método estático para obtener una instancia del loader.
     * @return este objeto loader.
     */
    public static AdminLoader getInstance(){
        if(instance == null){
            instance = new AdminLoader();
        }
        return instance;
    }
    
    /**
     * Constructor que define los campos default del Loader
     */
    private AdminLoader(){
        this.fxmlRoute = "/fxml/AdminUI.fxml";
        this.styleRoute = "/styles/addbook.css";
        this.title = "Mangiamo";
    }
}
