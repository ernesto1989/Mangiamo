package com.conciencia.controllers;

import com.conciencia.loaders.NuevoClienteLoader;
import com.conciencia.loaders.CreadorOrdenLoader;
import com.conciencia.lookups.LookupClass;
import com.conciencia.pojos.Cliente;
import com.conciencia.pojos.EstatusOrden;
import com.conciencia.pojos.ItemOrdenado;
import static com.conciencia.vertx.VertxConfig.vertx;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.TipoOrden;
import com.conciencia.vertx.VertxConfig;
import java.math.BigDecimal;
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
    
    /**************************************************************************/
    
    /* METODOS UTILITARIOS*/
    
    /**
     * Método usado para cerrar completamente la aplicación
     */
    private static void cerrarApp(){
        vertx.close();
        System.exit(0);
    }      
    
    /**
     * Método que permite abrir la pantalla para registrar un cliente nuevo
     */
    private void abrirNuevoClienteUI(){
        Platform.runLater(()->{
            Stage ps = new Stage();
            try {
                NuevoClienteLoader.getInstance().load(ps);
            } catch (Exception ex) {
                Logger.getLogger(NuevaOrdenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
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
            orden.setEstatusOrden(EstatusOrden.ESPERA);
            orden.setNumeroOrden(numOrden);
            orden.setEsNueva(true);
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
     * Método que crea un input text dialog genérico para solicitar un dato de entrada.
     * 
     * @param title titulo del input dialog
     * @param headText head text del input dialog
     * @param contentText contenido del input dialog
     * @return dato insertado por el usuario
     */
    private Optional<String> mostrarInputDialog(String title, 
                                                    String headText,
                                                        String contentText){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headText);
        dialog.setContentText(contentText);
        return dialog.showAndWait();
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
                mostrarInputDialog("No. Orden", "Buscar por No. Orden", "No. de Orden:");
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
                        mostrarAlertDialog("Orden no encontrada...", 
                            "No se encontró la orden",
                            "No se encontró la orden. Favor de validar", 
                            AlertType.INFORMATION);
                    });  
                }
            } else{
                Platform.runLater(()->{
                    mostrarAlertDialog("Error en la busqueda", 
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
        abrirNuevoClienteUI();
    }
    
    /**
     * Método que define la acción de salir de la app
     * @param event 
     */
    @FXML
    private void salir(ActionEvent event) {
        cerrarApp();
    }

    /**
     * Método que muestra el acerca de Mangiamo.
     * @param event 
     */
    @FXML
    private void mostrarAcercaDe(ActionEvent event) {
        mostrarAlertDialog("About...", 
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
        while(mesa == null || mesa == 0){
           Optional<String> result = 
                mostrarInputDialog("Orden en Mesa", "Orden en Mesa", "No. de Mesa:");
           try{mesa = Integer.parseInt(result.get());
           }catch(NumberFormatException e){mesa = 0;}
           catch(Exception e){return;}
        }
        crearOrden(mesa, null, null, TipoOrden.MESA);
        abrirCreadorOrdenUI();
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
                mostrarInputDialog("Orden para Llevar", "Orden para Llevar", "Nombre:");
            try{nombreCliente = result.get();
            }catch(Exception e){return;}
        }
        crearOrden(null, nombreCliente, null, TipoOrden.LLEVAR);
        abrirCreadorOrdenUI();
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
                    mostrarInputDialog("Buscar Cliente", "Buscar Cliente", "Teléfono:");
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
                        crearOrden(null, null, c, TipoOrden.DOMICILIO);
                        abrirCreadorOrdenUI();
                    }else{
                        abrirNuevoClienteUI();
                    }
                });      
            }else{
                abrirNuevoClienteUI();
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
            ps.setOnHiding(event-> cerrarApp());
        });
    }        
}
