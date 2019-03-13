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
    private MenuItem adminMenuItem;
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
    
    /**
     * Método para inicializar las columnas de la tabla de elementos ordenados.
     */
    private void initCols(){
        ordenParaColumn.setCellValueFactory(new PropertyValueFactory<>("ordenPara"));
        ordenTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        estausColumn.setCellValueFactory(new PropertyValueFactory<>("estatusOrden"));
        obsColumn.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
    }
   
    /**
     * Método que inicializa el evento del grid de ordenes
     */
    private void initTableEvent(){
        ordenesTable.setOnMouseClicked(evt->{
            if(evt.getClickCount() == 2){
                Orden selected = ordenesTable.getSelectionModel().getSelectedItem();
                LookupClass.current = selected;
                GeneralUtilities.abrirCreadorOrdenUI();
            }
        });
    }
    
    /**
     * Método que inicializa servicio de event bus para operar sobre el grid de órdenes
     */
    private void initGridListener(){
        vertx.eventBus().consumer("orders_resume", msg->{
            // <editor-fold defaultstate="colapsed" desc="handler">
            Orden o = (Orden) msg.body();
            Platform.runLater(()->{
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
        GeneralUtilities.abrirNuevoClienteUI();
    }
    
    /**
     * Método que abre la ventana para iniciar sesión en la administración de la aplicación
     * @param event 
     */
    @FXML
    private void abrirModuloAdministracion(ActionEvent event) {
        GeneralUtilities.abrirLoginUI();
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
                EventBusWrapper wrapper = (EventBusWrapper) response.result().body();
                Cliente c = (Cliente)wrapper.getPojo();
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
        initCols();
        initTableEvent();
        initGridListener();        
        Platform.runLater(()->{
            Stage ps = (Stage)mesaButton.getScene().getWindow();
            ps.setOnHiding(event-> {
                vertx.close();
                System.exit(0);
            });
        });
    }        
}
