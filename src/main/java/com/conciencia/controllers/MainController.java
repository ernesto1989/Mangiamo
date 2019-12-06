package com.conciencia.controllers;

import com.conciencia.utilities.GeneralUtilities;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

/**
 * FXML Controller class
 *
 * @author user
 */
public class MainController implements Initializable {

    @FXML
    private MenuItem registrarClienteMenuItem;
    @FXML
    private MenuItem adminMenuItem;
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
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void registrarNuevoCliente(ActionEvent event) {
    }   

    @FXML
    private void abrirModuloAdministracion(ActionEvent event) {
    }

    @FXML
    private void salir(ActionEvent event) {
        GeneralUtilities.cierraMangiamo();
    }

    @FXML
    private void mostrarAcercaDe(ActionEvent event) {
        GeneralUtilities.mostrarAlertDialog("Acerca de...", "Acerca de Mangiamo", 
                "Mangiamo Restaurant Software", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void crearOrdenEnMesa(ActionEvent event) {
    }

    @FXML
    private void crearOrdenParaLlevar(ActionEvent event) {
    }

    @FXML
    private void crearOrdenADomicilio(ActionEvent event) {
    }
    
}
