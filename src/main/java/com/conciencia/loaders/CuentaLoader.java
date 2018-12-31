package com.conciencia.loaders;

/**
 * 
 * @author Ernesto Cantu
 */
public class CuentaLoader extends Loader {
    
    /**
     * el objeto loader para abrir la pantalla de nueva orden
     */
    public static CuentaLoader instance;
    
    /**
     * Método estático para obtener una instancia del loader.
     * @return este objeto loader.
     */
    public static CuentaLoader getInstance(){
        if(instance == null){
            instance = new CuentaLoader();
        }
        return instance;
    }
    
    /**
     * Constructor que define los campos default del Loader
     */
    private CuentaLoader(){
        this.fxmlRoute = "/fxml/CuentaUI.fxml";
        this.styleRoute = "/styles/addbook.css";
        this.title = "Mangiamo";
    }
}
