package com.conciencia.controllers;

import com.conciencia.utilities.GeneralUtilities;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ernesto Cantu
 */
public class LogInController implements Initializable {

    @FXML
    private TextField userTextField;
    @FXML
    private PasswordField passwordTextField;
    
    private final String IN_MEMORY_USER = "admin";
    private final String IN_MEMORY_PASS = "admin";

    @FXML
    private void login(ActionEvent event) {
        String userName = userTextField.getText();
        String pass = passwordTextField.getText();
        
        if(userName.equals(IN_MEMORY_USER) && pass.equals(IN_MEMORY_PASS)){
            Stage ps = (Stage)userTextField.getScene().getWindow();
            ps.close();
            GeneralUtilities.abrirAdminUI();
        }else{
            GeneralUtilities.mostrarAlertDialog("Acceso Restringido", 
                    "Acceso Restringido", 
                    "Se requiere permisos para acceder a esta sección", 
                    Alert.AlertType.ERROR);
        }
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

}
