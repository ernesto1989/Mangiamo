package com.conciencia.loaders;

/**
 * Clase que carga la ventana principal de la aplicación.
 * 
 * Se define como pantalla principal el menú principal para:
 *  1.- Selección de un tipo de orden a crear
 *  2.- Mostrar lista de pedidos activos
 * 
 * @author Ernesto Cantu
 */
public class NuevaOrdenLoader extends Loader {
    
    public static NuevaOrdenLoader instance;
    
    public static NuevaOrdenLoader getInstance(){
        if(instance == null){
            instance = new NuevaOrdenLoader();
        }
        return instance;
    }
    
    private NuevaOrdenLoader(){
        this.fxmlRoute = "/fxml/NuevaOrdenUI.fxml";
        this.styleRoute = "/styles/addbook.css";
        this.title = "Mangiamo";
    }
}
