package com.conciencia.utilities;

import com.conciencia.controllers.NuevaOrdenController;
import com.conciencia.pojos.Cliente;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.TipoOrden;
import static com.conciencia.vertx.VertxConfig.vertx;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

/**
 * Clase que contiene métodos genéricos que sirven para la operación de Mangiamo.
 * 
 * @author Ernesto Cantu
 * 18 de marzo 2019
 */
public class GeneralUtilities {
    
    /**
     * Método que permite abrir de manera genérica una ventana con un fxml dado.
     * 
     * @param fxml ruta del fxml
     * @param style ruta de la hoja de estilos
     * @param title titulo de la ventana
     */
    public static void abrirVentana(String fxml, String style,String title){
        Stage stage = new Stage();
        try {
            GenericLoader loader = new GenericLoader(fxml,style,title);
            loader.load(stage);
        } catch (Exception ex) {
            Logger.getLogger(GeneralUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
     * Método que permite abrir de manera genérica una ventana con un fxml y un stage
     * dados
     * 
     * @param stage stage provisto para el método
     * @param fxml ruta del fxml
     * @param style ruta de la hoja de estilos
     * @param title titulo de la ventana
     */
    public static void abrirVentana(Stage stage,String fxml, String style,String title){
        try {
            GenericLoader loader = new GenericLoader(fxml,style,title);
            loader.load(stage);
        } catch (Exception ex) {
            Logger.getLogger(GeneralUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
     * Método que crea un objeto orden. Este método NO regresa el objeto órden,
     * más lo pone en el lookup.
     * 
     * @param mesa mesa a asignar a la orden
     * @param nombre nombre de la persona que ordena
     * @param cliente cliente a domicilio
     * @param tipo tipo de orden
     */
    public static void crearOrden(Integer mesa, String nombre, Cliente cliente, TipoOrden tipo){
        vertx.eventBus().send("get_order_num",null,response -> {
            Long numOrden = (Long) response.result().body();
            Orden orden = new Orden();
            orden.setNumeroOrden(numOrden);
            orden.setMesa(mesa);
            orden.setNombre(nombre);
            orden.setCliente(cliente);
            orden.setTipoOrden(tipo);
            LookupClass.current = orden;
        });
    }
      
    /**
     * Método que crea un alert dialog genérico para mostrar un msg.
     * 
     * @param title titulo del input dialog
     * @param headText head text del input dialog
     * @param contentText contenido del input dialog
     * @return dato insertado por el usuario
     */
    public static void mostrarAlertDialog(String title, 
                                    String headText,
                                    String contentText, 
                                    AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    
    /**
     * Método que crea un input text dialog genérico para solicitar un dato de entrada.
     * 
     * @param title titulo del input dialog
     * @param headText head text del input dialog
     * @param contentText contenido del input dialog
     * @return dato insertado por el usuario
     */
    public static Optional<String> mostrarInputDialog(String title, 
                                                    String headText,
                                                        String contentText){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headText);
        dialog.setContentText(contentText);
        return dialog.showAndWait();
    }
    
    
    /**
     * Método que crea un input text dialog genérico para solicitar un dato de entrada.
     * 
     * @param title titulo del input dialog
     * @param headText head text del input dialog
     * @param contentText contenido del input dialog
     * @return dato insertado por el usuario
     */
    public static Optional<Integer> mostrarChoiceDialog(Integer numOfChoices,String title, 
                                                    String headText,
                                                        String contentText){
        List<Integer> choices = new ArrayList<>();
        for(int i = 1; i<= numOfChoices; i++)
            choices.add(i);
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1,choices);
        dialog.setTitle(title);
        dialog.setHeaderText(headText);
        dialog.setContentText(contentText);
        return dialog.showAndWait();
    }
    
    /**
     * Método que permite confirmar un elemento o entidad seleccionado.
     * 
     * @param title título del alert
     * @param headText header del alert
     * @param contentText contenido del alert
     * @param okButtonMsg mensaje a mostrarse en el boton de aceptar
     * @param cancelButtonMsg mensaje a mostrarse en el boton de cancelar
     * @return boton seleccionado
     */
    public static Boolean mostrarConfirmDialog(String title,String headText,
                                                            String contentText,String okButtonMsg,
                                                            String cancelButtonMsg){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headText);
        alert.setContentText(contentText);
        ButtonType ok = new ButtonType(okButtonMsg);
        ButtonType cancel = new ButtonType(cancelButtonMsg);
        alert.getButtonTypes().setAll(ok,cancel);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ok) 
            return true;
        return false;
    }
}
