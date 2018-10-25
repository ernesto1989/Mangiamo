package com.conciencia.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class BuscarClienteController implements Initializable {

    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private TextField customerSearchTextField;
    @FXML
    private Button buscarClienteButton;
    @FXML
    private TextField nombreTextField;
    @FXML
    private TextField telTextfield;
    @FXML
    private TextField DirTextField;
    
    @FXML
    private void executeClose(ActionEvent event) {
    }

    @FXML
    private void executeAbout(ActionEvent event) {
    }

    @FXML
    private void searchCustomer(ActionEvent event) {
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    
    
}
