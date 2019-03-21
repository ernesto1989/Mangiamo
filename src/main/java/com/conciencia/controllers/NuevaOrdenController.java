package com.conciencia.controllers;

import com.conciencia.utilities.LookupClass;
import com.conciencia.pojos.Cliente;
import com.conciencia.pojos.EstatusOrden;
import com.conciencia.vertx.eventbus.EventBusWrapper;
import static com.conciencia.vertx.VertxConfig.vertx;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.TipoOrden;
import com.conciencia.utilities.GeneralUtilities;
import com.conciencia.vertx.VertxConfig;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Controlador de la pantalla NuevaOrdenUI.
 *
 * @author Ernesto Cantú
 */
public class NuevaOrdenController implements Initializable {
    
    /* ELEMENTOS DE LA PANTALLA */

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
    @FXML
    private TableView<Orden> ordenesTable;
    @FXML
    private TableColumn<Orden, String> ordenParaColumn;
    @FXML
    private TableColumn<Orden, BigDecimal> ordenTotalColumn;
    @FXML
    private TableColumn<Orden, EstatusOrden> estausColumn;
    @FXML
    private TableColumn<Orden, String> obsColumn;
    
    /* METODOS DE INICIO DE PANTALLA */
    
    /**
     * Método para inicializar las columnas de la tabla de órdenes abiertas.
     */
    private void inicializaColumnas(){
        ordenParaColumn.setCellValueFactory(new PropertyValueFactory<>("ordenPara"));
        ordenTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        estausColumn.setCellValueFactory(new PropertyValueFactory<>("estatusOrden"));
        obsColumn.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
    }
   
    /**
     * Método que inicializa el evento de la tabla de órdenes.
     */
    private void inicializaListenerTablaOrdenes(){
        ordenesTable.setOnMouseClicked(evt->{
            if(evt.getClickCount() == 2){
                Orden selected = ordenesTable.getSelectionModel().getSelectedItem();
                LookupClass.current = selected;
                GeneralUtilities.abrirVentana("/fxml/CreadorOrdenUI.fxml"
                        ,"/styles/addbook.css", "Mangiamo");
            }else
                return;
        });
    }
    
    /**
     * Método que inicializa servicio de event bus para operar sobre el grid de órdenes
     */
    private void inicializaResumenOrdenesConsumer(){
        vertx.eventBus().consumer("orders_resume", msg->{
            // <editor-fold defaultstate="collapsed" desc="handler">
            EventBusWrapper wrapper = (EventBusWrapper)msg.body();
            Orden o = (Orden) wrapper.getPojo();
            Platform.runLater(()->{
                // si la órden es nueva, la agrego a la tabla de órdenes
                if(o.isEsNueva()){ 
                    this.ordenesTable.getItems().add(o);
                    o.setEsNueva(false);
                }else{
                    if(o.getEstatusOrden() != EstatusOrden.CERRADA)
                        this.ordenesTable.refresh();
                    else
                        this.ordenesTable.getItems().remove(o);
                }
            });
            //</editor-fold>
        });
    }
    
    /**************************************************************************/
    
    /* METODOS INVOCADOS POR JAVAFX */
    
    /**
     * Método para registrar un nuevo cliente
     * @param event 
     */
    @FXML
    private void registrarNuevoCliente(ActionEvent event) {
        LookupClass.telefono = "";
        GeneralUtilities.abrirVentana("/fxml/NuevoClienteUI.fxml"
                        ,"/styles/addbook.css", "Mangiamo");
    }
    
    /**
     * Método que abre la ventana para iniciar sesión en la administración de la aplicación
     * @param event 
     */
    @FXML
    private void abrirModuloAdministracion(ActionEvent event) {
        GeneralUtilities.abrirVentana("/fxml/LogInUI.fxml"
                        ,"/styles/addbook.css", "Mangiamo");
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
            GeneralUtilities.mostrarChoiceDialog(AdminController.MESAS,"Orden en Mesa", 
                    "Orden en Mesa", "No. de Mesa:");
        try{
            mesa = result.get();
        }catch(Exception e){}
        if(mesa == null)
            return;
        GeneralUtilities.crearOrden(mesa, null, null, TipoOrden.MESA);
        GeneralUtilities.abrirVentana("/fxml/CreadorOrdenUI.fxml"
                        ,"/styles/addbook.css", "Mangiamo");
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
        GeneralUtilities.abrirVentana("/fxml/CreadorOrdenUI.fxml"
                        ,"/styles/addbook.css", "Mangiamo");
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
                EventBusWrapper wrapper = (EventBusWrapper) response.result().body();
                Cliente c = (Cliente)wrapper.getPojo();
                Platform.runLater(()->{
                    Boolean confirmaCliente = 
                        GeneralUtilities.mostrarConfirmDialog("Confirmar Cliente", 
                            "Confirmar Cliente", "Dirección: " + c.toString(), 
                                "Ordenar", "Cancel");
                    if(confirmaCliente){
                        GeneralUtilities.crearOrden(null, null, c, TipoOrden.DOMICILIO);
                        GeneralUtilities.abrirVentana("/fxml/CreadorOrdenUI.fxml"
                        ,"/styles/addbook.css", "Mangiamo");
                    }else{
                        LookupClass.telefono = null;
                        GeneralUtilities.mostrarAlertDialog("Telefono registrado", "Telefono registrado", 
                                "El telefono ya está registrado con otra dirección", AlertType.WARNING);
                        GeneralUtilities.abrirVentana("/fxml/NuevoClienteUI.fxml"
                        ,"/styles/addbook.css", "Mangiamo");
                    }
                });      
            }else{
                Platform.runLater(()->{
                    GeneralUtilities.abrirVentana("/fxml/NuevoClienteUI.fxml"
                        ,"/styles/addbook.css", "Mangiamo");
                });
                
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
        inicializaColumnas();
        inicializaListenerTablaOrdenes();
        inicializaResumenOrdenesConsumer();        
        Platform.runLater(()->{
            Stage ps = (Stage)mesaButton.getScene().getWindow();
            ps.setOnHiding(event-> {
                vertx.close();
                System.exit(0);
            });
        });
    }        
}
