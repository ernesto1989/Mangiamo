package com.conciencia.controllers;

import com.conciencia.loaders.CreadorOrdenLoader;
import com.conciencia.lookups.LookupClass;
import com.conciencia.pojos.Cliente;
import com.conciencia.pojos.EstatusOrden;
import static com.conciencia.vertx.VertxConfig.vertx;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.TipoOrden;
import com.conciencia.utilities.GeneralUtilities;
import com.conciencia.vertx.VertxConfig;
import java.net.URL;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

/**
 * Controlador de la pantalla NuevaOrdenUI.
 *
 * @author Ernesto Cantú
 */
public class NuevaOrdenController implements Initializable {
    
    /* ELEMENTOS DE LA PANTALLA */

    @FXML
    private MenuItem buscarOrdenMenuItem;
    @FXML
    private MenuItem ordenesPendienteMenuItem;
    @FXML
    private MenuItem registrarClienteMenuItem;
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
    
    private final Integer MESAS = 10;
    
    /**************************************************************************/
    
    /* METODOS INVOCADOS POR JAVAFX */
    
    /**
     * Método que busca una orden con un número dado.
     * 
     * @param event 
     */
    @FXML
    private void buscarOrden(ActionEvent event) {
        Long numeroOrden = null;
        while(numeroOrden == null || numeroOrden == 0){
           Optional<String> result = 
                GeneralUtilities.mostrarInputDialog("No. Orden", "Buscar por No. Orden", "No. de Orden:");
           try{numeroOrden = Long.parseLong(result.get());
           }catch(NumberFormatException e){numeroOrden = 0L;}
           catch(Exception e){return;}
        }
        
        VertxConfig.vertx.eventBus().send("find_order", numeroOrden,response->{
            if(response.succeeded()){
                Orden o = (Orden)response.result().body();
                if(o != null){
                    Platform.runLater(()->{
                        LookupClass.current = o;
                        Stage ps = new Stage();
                        try {
                            CreadorOrdenLoader.getInstance().load(ps);
                        } catch (Exception ex) {
                            Logger.getLogger(NuevaOrdenController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }else{
                    Platform.runLater(()->{
                        GeneralUtilities.mostrarAlertDialog("Orden no encontrada...", 
                            "No se encontró la orden",
                            "No se encontró la orden. Favor de validar", 
                            AlertType.INFORMATION);
                    });  
                }
            } else{
                Platform.runLater(()->{
                    GeneralUtilities.mostrarAlertDialog("Error en la busqueda", 
                        "Error en la busqueda",
                        "Favor de contactar a soporte", 
                        AlertType.ERROR);
                });  
            }
        });
    }
    
    /**
     * Método que será llamado para mostrar la lista de órdenes pendientes.
     * @param event 
     */
    @FXML
    private void verListaOrdenesPendientes(ActionEvent event) {
        System.out.println("ver lista de ordenes pendientes");
    }
    
    /**
     * Método para registrar un nuevo cliente
     * @param event 
     */
    @FXML
    private void registrarNuevoCliente(ActionEvent event) {
        LookupClass.telefono = "";
        GeneralUtilities.abrirNuevoClienteUI();
    }
    
    /**
     * Método que define la acción de salir de la app
     * @param event 
     */
    @FXML
    private void salir(ActionEvent event) {
        vertx.close();
        System.exit(0);
    }

    /**
     * Método que muestra el acerca de Mangiamo.
     * @param event 
     */
    @FXML
    private void mostrarAcercaDe(ActionEvent event) {
        GeneralUtilities.mostrarAlertDialog("About...", 
                "About Mangiamo...",
                "Mangiamo Restaurant Software for JavaFX8", 
                AlertType.INFORMATION);
    }    
    
    /**
     * Metodo ejecutado para crear una orden en mesa
     * @param event 
     */
    @FXML
    private void crearOrdenEnMesa(ActionEvent event) {
        Integer mesa = null;
        Optional<Integer> result = 
            GeneralUtilities.mostrarChoiceDialog(MESAS,"Orden en Mesa", 
                    "Orden en Mesa", "No. de Mesa:");
        mesa = result.get();
        GeneralUtilities.crearOrden(mesa, null, null, TipoOrden.MESA);
        GeneralUtilities.abrirCreadorOrdenUI();
    }
    
    /**
     * Metodo ejecutado para crear una orden para llevar
     * @param event 
     */
    @FXML
    private void crearOrdenParaLlevar(ActionEvent event) {
        String nombreCliente = null;
        while(nombreCliente == null || nombreCliente.isEmpty()){
            Optional<String> result = 
                GeneralUtilities.mostrarInputDialog("Orden para Llevar", "Orden para Llevar", "Nombre:");
            try{nombreCliente = result.get();
            }catch(Exception e){return;}
        }
        GeneralUtilities.crearOrden(null, nombreCliente, null, TipoOrden.LLEVAR);
        GeneralUtilities.abrirCreadorOrdenUI();
    }

    /**
     * Metodo ejecutado para crear una orden a domicilio
     * @param event 
     */
    @FXML
    private void crearOrdenADomicilio(ActionEvent event) {
        String telefono = null;
        while(telefono == null || telefono.isEmpty()){
            Optional<String> result = 
                    GeneralUtilities.mostrarInputDialog("Buscar Cliente", "Buscar Cliente", "Teléfono:");
            try{telefono = result.get();
            }catch(Exception e){return;}
            
        }
        LookupClass.telefono = telefono;
        VertxConfig.vertx.eventBus().send("get_customer",telefono,response->{
            if(response.succeeded()){
                Cliente c = (Cliente) response.result().body();
                Platform.runLater(()->{
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmar Cliente");
                    alert.setHeaderText("Confirmar Cliente");
                    alert.setContentText("Dirección: " + c.toString());
                    ButtonType ok = new ButtonType("Ordenar");
                    ButtonType cancel = new ButtonType("Cancel");
                    alert.getButtonTypes().setAll(ok,cancel);
                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.get() == ok){
                        GeneralUtilities.crearOrden(null, null, c, TipoOrden.DOMICILIO);
                        GeneralUtilities.abrirCreadorOrdenUI();
                    }else{
                        GeneralUtilities.abrirNuevoClienteUI();
                    }
                });      
            }else{
                GeneralUtilities.abrirNuevoClienteUI();
            }
        });
    }
    
    /**
     * Método que inicializa el controlador.Se define la acción a tomar al cerrar la ventana. 
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(()->{
            Stage ps = (Stage)mesaButton.getScene().getWindow();
            ps.setOnHiding(event-> {
                vertx.close();
                System.exit(0);
            });
        });
    }        
}
