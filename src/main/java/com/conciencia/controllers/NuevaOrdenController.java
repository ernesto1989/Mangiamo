package com.conciencia.controllers;

import com.conciencia.loaders.NuevoClienteLoader;
import com.conciencia.loaders.OrderCreatorLoader;
import com.conciencia.lookups.OrdenLookup;
import com.conciencia.pojos.Cliente;
import static com.conciencia.vertx.VertxConfig.vertx;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.OrderType;
import com.conciencia.vertx.VertxConfig;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

/**
 * Controlador de la pantalla NuevaOrdenUI.
 *
 * @author Ernesto Cantú
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
     * Método usado para cerrar completamente la aplicación
     */
    public static void cerrarApp(){
        vertx.close();
        System.exit(0);
    }  
    @FXML
    private MenuItem registrarClienteMenuItem;
    
    /**
     * Método que permite abrir la pantalla para registrar un cliente nuevo
     */
    private void registrarCliente(){
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
     * Método que crea el objeto Orden segun el tipo de orden a crear y carga la
     * pantalla para agregar elementos del menu a la orden
     * @param type Tipo de Orden Seleccionado
     */    
    private void crearOrden(Orden orden){
        Stage ps = new Stage();
        OrdenLookup.current = orden;
        try {
            OrderCreatorLoader.getInstance().load(ps);
        } catch (Exception ex) {
            Logger.getLogger(NuevaOrdenController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
     * Método que crea un imput text dialog genérico para solicitar el dato
     * de entrada requerido por cada tipo de orden.
     * 
     * @param title titulo del input dialog
     * @param headText head text del input dialog
     * @param contentText contenido del input dialog
     * @return dato insertado por el usuario
     */
    private Optional<String> solicitarDatoEntrada(String title, 
                                                    String headText,
                                                        String contentText){
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headText);
        dialog.setContentText(contentText);
        return dialog.showAndWait();
    }
    
    /**
     * Metodo ejecutado para crear una orden en mesa
     * @param event 
     */
    @FXML
    private void crearOrdenEnMesa(ActionEvent event) {
        Integer mesa = null;
        while(mesa == null || mesa == 0){
           Optional<String> result = solicitarDatoEntrada("Orden en Mesa", 
                                                          "Orden en Mesa", 
                                                          "No. de Mesa:");
           try{mesa = Integer.parseInt(result.get());
           }catch(NumberFormatException e){mesa = 0;}
           catch(Exception e){return;}
        }
        
        Orden orden = new Orden();
        orden.setMesa(mesa);
        orden.setOrderType(OrderType.MESA);
        crearOrden(orden);
    }
    
    /**
     * Metodo ejecutado para crear una orden para llevar
     * @param event 
     */
    @FXML
    private void crearOrdenParaLlevar(ActionEvent event) {
        String nombreCliente = null;
        while(nombreCliente == null || nombreCliente.isEmpty()){
            Optional<String> result = solicitarDatoEntrada("Orden para Llevar", 
                                                           "Orden para Llevar", 
                                                           "Nombre:");
            try{nombreCliente = result.get();
            }catch(Exception e){return;}
        }
        Orden orden = new Orden();
        orden.setNombre(nombreCliente);
        orden.setOrderType(OrderType.LLEVAR);
        crearOrden(orden);
    }

    /**
     * Metodo ejecutado para crear una orden a domicilio
     * @param event 
     */
    @FXML
    private void crearOrdenADomicilio(ActionEvent event) {
        String telefono = null;
        while(telefono == null || telefono.isEmpty()){
            Optional<String> result = solicitarDatoEntrada("Buscar Cliente", 
                                                           "Buscar Cliente", 
                                                           "Teléfono:");
            try{telefono = result.get();
            }catch(Exception e){return;}
            
        }
        
        VertxConfig.vertx.eventBus().send("get_customer",telefono,response->{
            if(response.succeeded()){
                Cliente c = (Cliente) response.result().body();
                Platform.runLater(()->{
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmar Cliente");
                    alert.setHeaderText("Confirmar Cliente");
                    alert.setContentText("Dirección: " + c.getDireccion());
                    ButtonType ok = new ButtonType("Ordenar");
                    ButtonType cancel = new ButtonType("Cancel");

                    alert.getButtonTypes().setAll(ok,cancel);

                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.get() == ok){
                        Orden orden = new Orden();
                        orden.setCliente(c);
                        orden.setOrderType(OrderType.DOMICILIO);
                        crearOrden(orden);
                    }else{
                        registrarCliente();
                    }
                });      
            }else{
                registrarCliente();
            }
        });
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
     * Método para registrar un nuevo cliente
     * @param event 
     */
    @FXML
    private void registrarNuevoCliente(ActionEvent event) {
        registrarCliente();
    }

    /**
     * Método que inicializa el controlador.Se define la acción a tomar al cerrar la ventana. 
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(()->{
            Stage ps = (Stage)mesaButton.getScene().getWindow();
            ps.setOnHiding(event-> cerrarApp());
        });
    }        
}
