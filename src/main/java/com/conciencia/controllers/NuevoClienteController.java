package com.conciencia.controllers;

import com.conciencia.pojos.Cliente;
import com.conciencia.utilities.GeneralUtilities;
import com.conciencia.vertx.VertxConfig;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador de la pantalla NuevoClienteUI.
 *
 * @author usuario
 */
public class NuevoClienteController implements Initializable {
    
    /* ELEMENTOS DE LA PANTALLA */

    @FXML
    private TextField nombreTextField;
    @FXML
    private TextField telTextfield;
    @FXML
    private TextField calleTextField;
    @FXML
    private TextField noTextField;
    @FXML
    private TextField coloniaTextField;
    @FXML
    private TextField ec1TextField;
    @FXML
    private TextField ec2TextField;
    @FXML
    private Button guardarClienteButton;
    @FXML
    private Button crearOrdenButton;
    
    private Cliente cliente;
    
    /**************************************************************************/
    
    /* METODOS INVOCADOS POR JAVAFX */
    
    /**
     * Método que crea el cliente a partir de los datos brindados en la pantalla
     */
    private void creaCliente(){
        String nombre = !nombreTextField.getText().isEmpty()?nombreTextField.getText():null;
        String telefono = !telTextfield.getText().isEmpty()?telTextfield.getText():null;
        String calle = !calleTextField.getText().isEmpty()?calleTextField.getText():null;
        String no = !noTextField.getText().isEmpty()?noTextField.getText():null;
        String colonia = !coloniaTextField.getText().isEmpty()?coloniaTextField.getText():null;
        String eCalle1 = !ec1TextField.getText().isEmpty()?ec1TextField.getText():null;
        String eCalle2 = !ec2TextField.getText().isEmpty()?ec2TextField.getText():null;
    
        cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        cliente.setCalle(calle);
        cliente.setNumero(no);
        cliente.setColonia(colonia);
        cliente.seteCalle1(eCalle1);
        cliente.seteCalle2(eCalle2);
    }
    
    /**
     * Método que crea un cliente y lo envía al método de guardado correspondiente
     * @param event 
     */
    @FXML
    private void agregarCliente(ActionEvent event) {
        creaCliente();
        
    }
    
    @FXML
    private void crearOrden(ActionEvent event) {
       
        Button b = (Button)event.getSource();
        Stage s = (Stage)b.getScene().getWindow();
        s.close();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        crearOrdenButton.setDisable(true);
    }    
}
