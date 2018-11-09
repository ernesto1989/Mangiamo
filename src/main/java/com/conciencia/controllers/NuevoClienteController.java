package com.conciencia.controllers;

import com.conciencia.loaders.CreadorOrdenLoader;
import com.conciencia.lookups.OrdenLookup;
import com.conciencia.pojos.Cliente;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.OrderType;
import com.conciencia.vertx.VertxConfig;
import java.net.URL;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class NuevoClienteController implements Initializable {

    @FXML
    private TextField nombreTextField;
    @FXML
    private TextField telTextfield;
    @FXML
    private TextField dirTextField;
    @FXML
    private Button guardarClienteButton;
    @FXML
    private Button crearOrdenButton;
    
    private Cliente cliente;
    
    /**
     * Método que crea el objeto Orden segun el tipo de orden a crear y carga la
     * pantalla para agregar elementos del menu a la orden
     * @param type Tipo de Orden Seleccionado
     */    
    private void crearOrden(Orden orden){
        Stage ps = new Stage();
        OrdenLookup.current = orden;
        try {
            CreadorOrdenLoader.getInstance().load(ps);
        } catch (Exception ex) {
            Logger.getLogger(NuevaOrdenController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @FXML
    private void agregarCliente(ActionEvent event) {
        String nombre = nombreTextField.getText();
        String telefono = telTextfield.getText();
        String dirección = dirTextField.getText();
        
        Cliente c = new Cliente();
        c.setNombre(nombre);
        c.setTelefono(telefono);
        c.setDireccion(dirección);
        
        VertxConfig.vertx.eventBus().send("save_customer",c,response->{
            if(response.succeeded()){
                int result = (int) response.result().body();
                if(c.getId() != null && c.getId() == result){
                    Platform.runLater(()->{
                        Alert alert = new Alert(AlertType.CONFIRMATION);
                        alert.setTitle("Registro de Cliente");
                        alert.setHeaderText("Cliente registrado exitosamente");
                        alert.setContentText("El cliente se ha registrado exitosamente.");
                        alert.showAndWait();
                        crearOrdenButton.setDisable(false);
                        cliente = c;
                    });
                }else{
                    Platform.runLater(()->{
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error en registro de cliente");
                        alert.setHeaderText("Contacte al administrador");
                        alert.setContentText("Contacte al administrador");
                        alert.showAndWait();
                    }); 
                }
            }else{
                String mensajeError = response.cause().getMessage();
                Platform.runLater(()->{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error en registro de cliente");
                    alert.setHeaderText(mensajeError);
                    alert.setContentText(mensajeError);
                    alert.showAndWait();
                }); 
            }
        });
        
    }
    
    @FXML
    private void crearOrden(ActionEvent event) {
        Orden orden = new Orden();
        orden.setCliente(cliente);
        orden.setOrderType(OrderType.DOMICILIO);
        crearOrden(orden);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        crearOrdenButton.setDisable(true);
    }    
}
