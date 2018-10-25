package com.conciencia.loaders;

/**
 * 
 * @author Ernesto Cantu
 */
public class BuscarClienteLoader extends Loader {
    
    /**
     * el objeto loader para abrir la pantalla de nueva orden
     */
    public static BuscarClienteLoader instance;
    
    /**
     * Método estático para obtener una instancia del loader.
     * @return este objeto loader.
     */
    public static BuscarClienteLoader getInstance(){
        if(instance == null){
            instance = new BuscarClienteLoader();
        }
        return instance;
    }
    
    private BuscarClienteLoader(){
        this.fxmlRoute = "/fxml/BuscaClienteUI.fxml";
        this.styleRoute = "/styles/addbook.css";
        this.title = "Mangiamo";
    }
}
