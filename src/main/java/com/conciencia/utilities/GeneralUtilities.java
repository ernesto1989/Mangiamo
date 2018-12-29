package com.conciencia.utilities;

import com.conciencia.controllers.NuevaOrdenController;
import com.conciencia.loaders.CreadorOrdenLoader;
import com.conciencia.loaders.NuevoClienteLoader;
import com.conciencia.lookups.LookupClass;
import com.conciencia.pojos.Cliente;
import com.conciencia.pojos.EstatusOrden;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.TipoOrden;
import static com.conciencia.vertx.VertxConfig.vertx;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

/**
 *
 * @author Ernesto Cantu
 */
public class GeneralUtilities {
    
    /**
     * Método que crea un objeto orden. Este método NO regresa el objeto órden,
     * más lo pone en el lookup.
     * 
     * @param mesa mesa a asignar a la orden
     * @param nombre nombre de la persona que ordena
     * @param c cliente a domicilio
     * @param tipo tipo de orden
     */
    public static void crearOrden(Integer mesa, String nombre, Cliente c, TipoOrden tipo){
        vertx.eventBus().send("get_order_num",null,response -> {
            Long numOrden = (Long) response.result().body();
            Orden orden = new Orden();
            orden.setMesa(mesa);
            orden.setNombre(nombre);
            orden.setCliente(c);
            orden.setTipoOrden(tipo);
            orden.setNumeroOrden(numOrden);
            orden.setEsNueva(true);
            orden.setEstatusOrden(EstatusOrden.ESPERA);
            LookupClass.current = orden;
        });
    }
    
    /**
     * Método que crea el objeto Orden segun el tipo de orden a crear y carga la
     * pantalla para agregar elementos del menu a la orden
     * @param type Tipo de Orden Seleccionado
     */    
    public static void abrirCreadorOrdenUI(){
        Stage ps = new Stage();
        try {
            CreadorOrdenLoader.getInstance().load(ps);
        } catch (Exception ex) {
            Logger.getLogger(NuevaOrdenController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
     * Método que permite abrir la pantalla para registrar un cliente nuevo
     */
    public static void abrirNuevoClienteUI(){
        Platform.runLater(()->{
            Stage ps = new Stage();
            try {
                NuevoClienteLoader.getInstance().load(ps);
            } catch (Exception ex) {
                Logger.getLogger(NuevaOrdenController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
}
