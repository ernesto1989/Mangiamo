/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conciencia.controllers;

import static com.conciencia.controllers.OrderCreatorController.closeApp;
import static com.conciencia.main.MainApp.vertx;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.OrderType;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class NuevaOrdenController implements Initializable {

    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
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
    
    private void crearOrden(OrderType type){
        Orden orden = new Orden();
        orden.setOrderType(type);
        //CrearOrdenLoader.load();
        System.out.println(type.toString());
    }
    
    @FXML
    private void crearOrdenEnMesa(ActionEvent event) {
        crearOrden(OrderType.MESA);
    }
    
    @FXML
    private void crearOrdenParaLlevar(ActionEvent event) {
        crearOrden(OrderType.LLEVAR);
    }

    @FXML
    private void crearOrdenADomicilio(ActionEvent event) {
        crearOrden(OrderType.DOMICILIO);
    }

    @FXML
    private void salir(ActionEvent event) {
        cerrarApp();
    }

    @FXML
    private void mostrarAcercaDe(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About...");
        alert.setHeaderText("About Mangiamo...");
        alert.setContentText("Mangiamo Restaurant Software for JavaFX8");
        alert.showAndWait();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(()->{
            Stage ps = (Stage)mesaButton.getScene().getWindow();
            ps.setOnHiding(event-> closeApp());
        });
    }    
}
