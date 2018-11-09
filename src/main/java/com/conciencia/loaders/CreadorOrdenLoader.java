package com.conciencia.loaders;

/**
 * Clase que carga la ventana principal de la aplicación.
 * @author Ernesto Cantu
 */
public class CreadorOrdenLoader extends Loader {
    
    public static CreadorOrdenLoader instance;
    
    public static CreadorOrdenLoader getInstance(){
        if(instance == null){
            instance = new CreadorOrdenLoader();
        }
        return instance;
    }
    
    private CreadorOrdenLoader(){
        this.fxmlRoute = "/fxml/CreadorOrdenUI.fxml";
        this.styleRoute = "/styles/addbook.css";
        this.title = "Mangiamo";
    }
}
