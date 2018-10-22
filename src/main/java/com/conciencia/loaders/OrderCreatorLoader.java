package com.conciencia.loaders;

/**
 * Clase que carga la ventana principal de la aplicación.
 * @author Ernesto Cantu
 */
public class OrderCreatorLoader extends Loader {
    
    public static OrderCreatorLoader instance;
    
    public static OrderCreatorLoader getInstance(){
        if(instance == null){
            instance = new OrderCreatorLoader();
        }
        return instance;
    }
    
    private OrderCreatorLoader(){
        this.fxmlRoute = "/fxml/OrderCreatorUI.fxml";
        this.styleRoute = "/styles/addbook.css";
        this.title = "Mangiamo";
    }
}
