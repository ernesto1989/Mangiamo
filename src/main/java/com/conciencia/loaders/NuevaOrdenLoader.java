package com.conciencia.loaders;

/**
 * Clase que carga la ventana de inicio de la aplicación.
 * 
 * Se define como pantalla principal el menú principal para:
 *  1.- Selección de un tipo de orden a crear
 *  2.- Mostrar lista de pedidos activos
 * 
 * @author Ernesto Cantu
 */
public class NuevaOrdenLoader extends Loader {
    
    /**
     * el objeto loader para abrir la pantalla de nueva orden
     */
    public static NuevaOrdenLoader instance;
    
    /**
     * Método estático para obtener una instancia del loader.
     * @return este objeto loader.
     */
    public static NuevaOrdenLoader getInstance(){
        if(instance == null){
            instance = new NuevaOrdenLoader();
        }
        return instance;
    }
    
    /**
     * Constructor que define los campos default del Loader
     */
    private NuevaOrdenLoader(){
        this.fxmlRoute = "/fxml/NuevaOrdenUI.fxml";
        this.styleRoute = "/styles/addbook.css";
        this.title = "Mangiamo";
    }
}
