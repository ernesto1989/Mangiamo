package com.conciencia.loaders;

/**
 * Clase que carga la ventana principal de la aplicación.
 * @author Ernesto Cantu
 */
public class LoginLoader extends Loader {
    
    public static LoginLoader instance;
    
    public static LoginLoader getInstance(){
        if(instance == null){
            instance = new LoginLoader();
        }
        return instance;
    }
    
    private LoginLoader(){
        this.fxmlRoute = "/fxml/LogInUI.fxml";
        this.styleRoute = "/styles/addbook.css";
        this.title = "Mangiamo";
    }
}
