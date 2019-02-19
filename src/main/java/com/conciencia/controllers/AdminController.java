package com.conciencia.controllers;

import com.conciencia.utilities.GeneralUtilities;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Ernesto Cantu
 */
public class AdminController implements Initializable {
    
    public static Integer MESAS = 10;
    @FXML
    private TextField noMesaTextField;
    @FXML
    private Button guardarButton;
    
    @FXML
    private void guardarGeneral(ActionEvent event) {
        MESAS = Integer.parseInt(noMesaTextField.getText());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        noMesaTextField.setText(MESAS.toString());
    }    

    

}
