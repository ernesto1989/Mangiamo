package com.conciencia.loaders;

/**
 * 
 * @author Ernesto Cantu
 */
public class NuevoClienteLoader extends Loader {
    
    /**
     * el objeto loader para abrir la pantalla de nueva orden
     */
    public static NuevoClienteLoader instance;
    
    /**
     * Método estático para obtener una instancia del loader.
     * @return este objeto loader.
     */
    public static NuevoClienteLoader getInstance(){
        if(instance == null){
            instance = new NuevoClienteLoader();
        }
        return instance;
    }
    
    private NuevoClienteLoader(){
        this.fxmlRoute = "/fxml/NuevoClienteUI.fxml";
        this.styleRoute = "/styles/addbook.css";
        this.title = "Mangiamo";
    }
}
