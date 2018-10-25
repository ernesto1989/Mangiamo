package com.conciencia.controllers;

import com.conciencia.loaders.BuscarClienteLoader;
import com.conciencia.loaders.OrderCreatorLoader;
import com.conciencia.lookups.OrdenLookup;
import static com.conciencia.vertx.VertxConfig.vertx;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.OrderType;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

/**
 * Controlador de la pantalla NuevaOrdenFXML
 *
 * @author usuario
 */
public class NuevaOrdenController implements Initializable {

    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private MenuItem pedidosOrdenesMenuItem;
    @FXML
    private Button mesaButton;
    @FXML
    private Button llevaButton;
    @FXML
    private Button domicilioButton;
    
    /**
     * Método que será llamado cuando se de click al boton de cerrar de la pantalla
     */
    public static void cerrarApp(){
        vertx.close();
        System.exit(0);
    }  
    
    /**
     * Método que define el tipo de orden a crear
     * @param type Tipo de Orden Seleccionado
     */    
    private void crearOrden(OrderType type){
        Stage ps = new Stage();
        Orden orden = new Orden();
        orden.setOrderType(type);
        OrdenLookup.current = orden;
        try {
            OrderCreatorLoader.getInstance().load(ps);
        } catch (Exception ex) {
            Logger.getLogger(NuevaOrdenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * Boton para crear orden en mesa
     * @param event 
     */
    @FXML
    private void crearOrdenEnMesa(ActionEvent event) {
        //Integer mesa = 
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Orden en Mesa");
        dialog.setHeaderText("Orden en Mesa");
        dialog.setContentText("No. de Mesa:");
        
        Optional<String> result = dialog.showAndWait();
        Integer mesa = Integer.parseInt(result.get());
        if (result.isPresent()){
            System.out.println("Mesa " + mesa);
        }
        //crearOrden(OrderType.MESA);
    }
    
    /**
     * Boton para crear orden para llevar
     * @param event 
     */
    @FXML
    private void crearOrdenParaLlevar(ActionEvent event) {
        //nombreCliente = 
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Orden para llevar");
        dialog.setHeaderText("Orden para llevar");
        dialog.setContentText("Nombre:");
        
        Optional<String> result = dialog.showAndWait();
        String nombre = result.get();
        if (result.isPresent()){
            System.out.println("nombre " + nombre);
        }
        crearOrden(OrderType.LLEVAR);
    }

    /**
     * Boton para crear orden a domicilio
     * @param event 
     */
    @FXML
    private void crearOrdenADomicilio(ActionEvent event) {
        Stage ps = new Stage();
        try {
            BuscarClienteLoader.getInstance().load(ps);
            //crearOrden(OrderType.DOMICILIO);
        } catch (Exception ex) {
            Logger.getLogger(NuevaOrdenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que define la acción de salir de la app
     * @param event 
     */
    @FXML
    private void salir(ActionEvent event) {
        cerrarApp();
    }

    /**
     * Método que muestra el acerca de Mangiamo.
     * @param event 
     */
    @FXML
    private void mostrarAcercaDe(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About...");
        alert.setHeaderText("About Mangiamo...");
        alert.setContentText("Mangiamo Restaurant Software for JavaFX8");
        alert.showAndWait();
    }
    
    /**
     * Método que será llamado para mostrar la lista de órdenes pendientes.
     * @param event 
     */
    @FXML
    private void verListaOrdenesPendientes(ActionEvent event) {
        System.out.println("ver lista de ordenes pendientes");
    }

    /**
     * Método que inicializa el controlador. 
     * 
     * Se define la acción a tomar al cerrar la ventana.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(()->{
            Stage ps = (Stage)mesaButton.getScene().getWindow();
            ps.setOnHiding(event-> cerrarApp());
        });
    }        
}
