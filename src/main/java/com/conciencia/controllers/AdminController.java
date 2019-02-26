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
    public static BigDecimal COSTO_ENVIO = new BigDecimal("15.0");
    public static Integer MINUTOS_ESPERA = 10;
    public static Integer MINUTOS_ESPERA_MAX = 120;
    public static String DB_URL = "C:/Conciencia/files/db/MangiamoDB.db";
    
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
    private TextField tiempoEsperaMaxTextField;
    @FXML
    private Label tEsperaMaxLabel;
    @FXML
    private TextField baseDatosTextField;
    
    @FXML
    private void guardarGeneral(ActionEvent event) {
        MESAS = Integer.parseInt(noMesaTextField.getText());
        COSTO_ENVIO = new BigDecimal(costoEnvioTextField.getText());
        MINUTOS_ESPERA = Integer.parseInt(tiempoEsperaTextField.getText());
        MINUTOS_ESPERA_MAX = Integer.parseInt(tiempoEsperaMaxTextField.getText());
        DB_URL = baseDatosTextField.getText();
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
        tEsperaMaxLabel.setText("Tiempo\nEspera Max.");
        tiempoEsperaTextField.setText(MINUTOS_ESPERA.toString());
        tiempoEsperaMaxTextField.setText(MINUTOS_ESPERA_MAX.toString());
        baseDatosTextField.setText(DB_URL);
    }    

    

}
