package com.conciencia.controllers;

import com.conciencia.loaders.CreadorOrdenLoader;
import com.conciencia.lookups.LookupClass;
import com.conciencia.pojos.Cliente;
import com.conciencia.pojos.EstatusOrden;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.TipoOrden;
import com.conciencia.vertx.VertxConfig;
import static com.conciencia.vertx.VertxConfig.vertx;
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
    
    /* METODOS UTILITARIOS*/
    
    /**
     * Método que crea un objeto orden. Este método NO regresa el objeto órden,
     * más lo pone en el lookup.
     * 
     * @param mesa mesa a asignar a la orden
     * @param nombre nombre de la persona que ordena
     * @param c cliente a domicilio
     * @param tipo tipo de orden
     */
    private void crearOrden(Integer mesa, String nombre, Cliente c, TipoOrden tipo){
        vertx.eventBus().send("get_order_num",null,response -> {
            Long numOrden = (Long) response.result().body();
            Orden orden = new Orden();
            orden.setMesa(mesa);
            orden.setNombre(nombre);
            orden.setCliente(c);
            orden.setTipoOrden(tipo);
            orden.setNumeroOrden(numOrden);
            orden.setEsNueva(true);
            orden.setEstatusOrden(EstatusOrden.ESPERA);
            LookupClass.current = orden;
        });
    }
    
    /**
     * Método que crea el objeto Orden segun el tipo de orden a crear y carga la
     * pantalla para agregar elementos del menu a la orden
     * @param type Tipo de Orden Seleccionado
     */    
    private void abrirCreadorOrdenUI(){
        Stage ps = new Stage();
        try {
            CreadorOrdenLoader.getInstance().load(ps);
        } catch (Exception ex) {
            Logger.getLogger(NuevaOrdenController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
     * Método que crea un alert dialog genérico para mostrar un msg.
     * 
     * @param title titulo del input dialog
     * @param headText head text del input dialog
     * @param contentText contenido del input dialog
     * @return dato insertado por el usuario
     */
    private void mostrarAlertDialog(String title, 
                                    String headText,
                                    String contentText, 
                                    AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    
    /**************************************************************************/
    
    /* METODOS INVOCADOS POR JAVAFX */
    
    @FXML
    private void agregarCliente(ActionEvent event) {
        String nombre = nombreTextField.getText();
        String telefono = telTextfield.getText();
        String calle = calleTextField.getText();
        String no = noTextField.getText();
        String colonia = coloniaTextField.getText();
        String eCalle1 = ec1TextField.getText();
        String eCalle2 = ec2TextField.getText();
        
        cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        cliente.setCalle(calle);
        cliente.setNumero(no);
        cliente.setColonia(colonia);
        cliente.seteCalle1(eCalle1);
        cliente.seteCalle2(eCalle2);
        
        VertxConfig.vertx.eventBus().send("save_customer",cliente,response->{
            if(response.succeeded()){
                int result = (int) response.result().body();
                if(cliente.getId() != null && cliente.getId() == result){
                    Platform.runLater(()->{
                        mostrarAlertDialog("Registro de Cliente",
                                "Cliente registrado exitosamente",
                                "El cliente se ha registrado exitosamente.",
                                AlertType.CONFIRMATION);
                        crearOrdenButton.setDisable(false);
                        cliente = cliente;
                    });
                }else{
                    Platform.runLater(()->{
                        mostrarAlertDialog("Error en registro de cliente",
                                "Contacte al administrador",
                                "Contacte al administrador",
                                AlertType.ERROR);
                    }); 
                }
            }else{
                String mensajeError = response.cause().getMessage();
                Platform.runLater(()->{
                    mostrarAlertDialog("Error en registro de cliente",
                                mensajeError,
                                mensajeError,
                                AlertType.ERROR);
                }); 
            }
        });
        
    }
    
    @FXML
    private void crearOrden(ActionEvent event) {
        crearOrden(null, null, cliente, TipoOrden.DOMICILIO);
        abrirCreadorOrdenUI();
        Button b = (Button)event.getSource();
        Stage s = (Stage)b.getScene().getWindow();
        s.close();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(LookupClass.telefono != null && LookupClass.telefono != "")
            telTextfield.setText(LookupClass.telefono);
        crearOrdenButton.setDisable(true);
    }    
}
