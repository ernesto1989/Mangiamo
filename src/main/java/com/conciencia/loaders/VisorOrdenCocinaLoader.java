package com.conciencia.loaders;

/**
 * Clase que carga la ventana principal de la aplicación.
 * @author Ernesto Cantu
 */
public class VisorOrdenCocinaLoader extends Loader {
    
    public static VisorOrdenCocinaLoader instance;
    
    public static VisorOrdenCocinaLoader getInstance(){
        if(instance == null){
            instance = new VisorOrdenCocinaLoader();
        }
        return instance;
    }
    
    private VisorOrdenCocinaLoader(){
        this.fxmlRoute = "/fxml/VisorOrdenCocinaUI.fxml";
        this.styleRoute = "/styles/addbook.css";
        this.title = "Mangiamo";
    }
}
