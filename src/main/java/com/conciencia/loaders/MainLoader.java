package com.conciencia.loaders;

/**
 * Clase que carga la ventana principal de la aplicación.
 * @author Ernesto Cantu
 */
public class MainLoader extends Loader {
    
    public static MainLoader instance;
    
    public static MainLoader getInstance(){
        if(instance == null){
            instance = new MainLoader();
        }
        return instance;
    }
    
    private MainLoader(){
        this.fxmlRoute = "/fxml/MainUI.fxml";
        this.styleRoute = "/styles/addbook.css";
        this.title = "Mangiamo";
    }
}
