package com.conciencia.controllers;

import com.conciencia.utilities.GeneralUtilities;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Ernesto Cantu
 */
public class AdminController implements Initializable {
    
    public static Integer MESAS = 10;
    public static BigDecimal COSTO_ENVIO = new BigDecimal("12.0");
    public static Integer MINUTOS_ESPERA = 20;
    
    @FXML
    private TextField noMesaTextField;
    @FXML
    private TextField costoEnvioTextField;
    @FXML
    private Button guardarButton;
    @FXML
    private Label tEsperaLabel;
    @FXML
    private TextField tiempoEsperaTextField;
   
    
    @FXML
    private void guardarGeneral(ActionEvent event) {
        MESAS = Integer.parseInt(noMesaTextField.getText());
        COSTO_ENVIO = new BigDecimal(costoEnvioTextField.getText());
        MINUTOS_ESPERA = Integer.parseInt(tiempoEsperaTextField.getText());
        GeneralUtilities.mostrarAlertDialog("Administración", "Administración General", "Configuración Guardada", Alert.AlertType.INFORMATION);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        noMesaTextField.setText(MESAS.toString());
        costoEnvioTextField.setText(COSTO_ENVIO.toEngineeringString());
        tEsperaLabel.setText("Tiempo\nEspera");
        tiempoEsperaTextField.setText(MINUTOS_ESPERA.toString());
    }    

    

}
