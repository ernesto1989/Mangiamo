package com.conciencia.controllers;

import com.conciencia.loaders.CuentaLoader;
import com.conciencia.lookups.LookupClass;
import com.conciencia.pojos.EstatusOrden;
import com.conciencia.pojos.Item;
import com.conciencia.pojos.Menu;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.TipoOrden;
import com.conciencia.pojos.ItemOrdenado;
import com.conciencia.pojos.Seccion;
import com.conciencia.pojos.TreeContainer;
import com.conciencia.utilities.GeneralUtilities;
import com.conciencia.utilities.PrintableClass;
import com.conciencia.vertx.VertxConfig;
import static com.conciencia.vertx.VertxConfig.vertx;
import io.vertx.core.json.JsonObject;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controlador de pantalla CreadorOrdenUI.
 *
 * @author Ernesto Cantu
 */
public class CreadorOrdenController implements Initializable {

    /* ELEMENTOS DE LA PANTALLA */
    
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private TableColumn<ItemOrdenado, String> descripcionColumn;
    @FXML
    private TableColumn<ItemOrdenado,Integer> cantidadColumn;
    @FXML
    private TableColumn<ItemOrdenado, BigDecimal> totalColumn;
    @FXML
    private TableView<ItemOrdenado> resumeTable;
    @FXML
    private TextField totalTextBox;
    @FXML
    private TreeView<TreeContainer> menuTree;
    @FXML
    private Button saveOrderButton;
    @FXML
    private TextField descripcionTextField;
    @FXML
    private TextField cantidadTextField;
    @FXML
    private Button incrementoButton;
    @FXML
    private Button decrementoButton;
    @FXML
    private Button modificarButton;
    @FXML
    private TextField horaOrdenTextfield;
    @FXML
    private Label horaLabel;
    @FXML
    private Button billOrderButton;
    @FXML
    private CheckBox pagadoCheckbox;
    @FXML
    private Button deliverOrderButton;
    @FXML
    private ComboBox<Integer> tiempoEstimadoCombo;
    
    /* OBJETOS DE LA CLASE */
    
    private Orden orden;
    
    private ItemOrdenado selected;
    
    private int persona = 1;
    
    private int idItem = 1;
    
    private BigDecimal curSubtotal = new BigDecimal("0.0");
    
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");    
    
    
    /**************************************************************************/
    
    private void getOrder(){
        this.orden = LookupClass.current;
        LookupClass.current = null; //limpio la orden que se quedó "preseteada"
    }
    
    /**
     * Método que inicializa los headers de la pantalla de creación de órdenes.
     */
    private void initOrderHeaders(){
        descripcionTextField.setText(this.orden.toString());
        horaLabel.setVisible(!this.orden.isEsNueva());
        horaOrdenTextfield.setVisible(!this.orden.isEsNueva());
        if(!this.orden.isEsNueva())
            horaOrdenTextfield.setText(this.orden.getHoraRegistro().format(dtf));
        descripcionTextField.setTooltip(new Tooltip(descripcionTextField.getText()));
        ObservableList<Integer> ol = FXCollections.observableArrayList();
        for(int i = AdminController.MINUTOS_ESPERA; i<=AdminController.MINUTOS_ESPERA_MAX;i+=5){
            ol.add(i);
        }
        tiempoEstimadoCombo.setItems(ol);
        if(this.orden.isEsNueva()){
            tiempoEstimadoCombo.setValue(AdminController.MINUTOS_ESPERA);
        }else{
            tiempoEstimadoCombo.setValue(orden.getTiempoEspera());
        }
       
        
    }
    
    /**
     * Método para inicializar las columnas de la tabla de elementos ordenados.
     */
    private void initCols(){
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        cantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
    }
    
    /**
     * Método que se ejecuta cuando se selecciona un elemento del menu
     */
    private void setTreeViewEvent(){
        menuTree.setOnMouseClicked(e->{
            cantidadTextField.setText("");
            incrementoButton.setDisable(true);
            decrementoButton.setDisable(true);
            modificarButton.setDisable(true);
            if(this.orden.getEstatusOrden() != null && this.orden.getEstatusOrden().equals(EstatusOrden.CERRADA))
                return;
            if(e.getClickCount() == 2){
                if(menuTree.getSelectionModel().getSelectedItem().getValue() instanceof Seccion)
                    return;
                Item item = (Item) (menuTree.getSelectionModel().getSelectedItem().getValue());
                ItemOrdenado o = addItem(item);
                
                for(Item i:item.getRelacionados()){
                    addItem(i);
                    o.setNumRelacionados(o.getNumRelacionados() + 1);
                }
            }
        });
    }
    
    /**
     * Método que se ejecuta cuando se selecciona un elemento de la lista de elementos ordenados
     */
    private void setTableEvent(){
        resumeTable.setOnMouseClicked(e->{
            selected = resumeTable.getSelectionModel().getSelectedItem();
            if(selected.getIdItem() == 0) return;
            if(e.getClickCount() == 1) {
                selected = resumeTable.getSelectionModel().getSelectedItem();
                if(selected.getEsOrden()){
                    cantidadTextField.setText(selected.getCantidad().toString());
                    incrementoButton.setDisable(false);
                    decrementoButton.setDisable(false);
                    modificarButton.setDisable(false);
                }else{
                    cantidadTextField.setText("");
                    incrementoButton.setDisable(true);
                    decrementoButton.setDisable(true);
                    modificarButton.setDisable(true);
                }
            }
            if(e.getClickCount() == 2){
                int selected = resumeTable.getSelectionModel().getSelectedIndex();
                ItemOrdenado o = resumeTable.getSelectionModel().getSelectedItem();
                ItemOrdenado selectedItem = resumeTable.getSelectionModel().getSelectedItem();
                resumeTable.getItems().remove(selected);
                if(o.getEsOrden()){
                    cantidadTextField.setText("");
                    incrementoButton.setDisable(true);
                    decrementoButton.setDisable(true);
                    modificarButton.setDisable(true);
                }
                for(int i = selected; i< selected + o.getNumRelacionados();i++){
                    if(resumeTable.getItems().size() > 0)
                        resumeTable.getItems().remove(i);
                    else
                        break;
                }
            }
            setCurrentTotal();
        });
    }
    
    /**
     * Método que trae el menú desde la bd
     */
    private void getMenu() {
        vertx.eventBus().send("get_menu",null,response -> {
            Menu menu = (Menu) response.result().body();
            Platform.runLater(()->{
                createTree(menu);
            });
        });
    }
    
    /**
     * Método que inicializa el menú en la pantalla
     * @param menu 
     */
    private void createTree(Menu menu){
        Seccion root = new Seccion();
        root.setNombre("Menu");
        TreeItem<TreeContainer> rootNode = new TreeItem<>(root);
        TreeItem<TreeContainer> node;
        TreeItem<TreeContainer> leaf;
        for(Seccion s:menu){
            node = new TreeItem<>(s);
            node.setExpanded(true);
            for(Item i:s.getItems()){
                leaf = new TreeItem<>(i);
                node.getChildren().add(leaf);
            }
            rootNode.getChildren().add(node);
        }
        rootNode.setExpanded(true);
        menuTree.setRoot(rootNode);
        menuTree.setShowRoot(false);
    }
    
    /**
     * Método que inicializa a los elementos de la pantalla
     */
    private void initElements(){
        incrementoButton.setDisable(true);
        decrementoButton.setDisable(true);
        modificarButton.setDisable(true);
        pagadoCheckbox.setDisable(true);
        if(this.orden.getOrderedItems() != null)
            resumeTable.getItems().addAll(this.orden.getOrderedItems());
        
        if(this.orden.isEsNueva() && this.orden.getTipoOrden()== TipoOrden.DOMICILIO){
            ItemOrdenado envio = new ItemOrdenado();
            envio.setPersona(1);
            envio.setDescripcion("Envío");
            envio.setCantidad(1);
            envio.setTotal(AdminController.COSTO_ENVIO);
            envio.setIdItem(0);  
            resumeTable.getItems().add(envio);
            setCurrentTotal();
        }
        
        if(!this.orden.isEsNueva()){
            //saveOrderButton.setDisable(true);
            pagadoCheckbox.setVisible(true);
            pagadoCheckbox.setSelected(orden.getPagado());
            pagadoCheckbox.setOnAction(e->{
                pagadoCheckbox.setSelected(this.orden.getPagado());
            });

            totalTextBox.setText(orden.getTotal().toString());
        }else{
            pagadoCheckbox.setVisible(false);
        }
        
        if(this.orden.getEstatusOrden() != null && 
                this.orden.getEstatusOrden().equals(EstatusOrden.CERRADA)){
            saveOrderButton.setDisable(true);
            billOrderButton.setDisable(true);
            deliverOrderButton.setDisable(true);
        }
    }
    
    
    /* METODOS UTILITARIOS*/
    
    private void printOrder(){
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintableClass p = new PrintableClass(this.orden);
        job.setPrintable(p);
        boolean doPrint = job.printDialog();
        
        if (doPrint) {
            try {
                job.print();
            } catch (PrinterException e) {
                GeneralUtilities.mostrarAlertDialog("Error de Impresion",
                        "Error de Impresion" , "Error de Impresion", Alert.AlertType.ERROR);
            }
        }
    }   
    
    /**
     * Método que establece el total para la orden actual
     */
    private void setCurrentTotal(){
        BigDecimal currentTotal = BigDecimal.ZERO;
        for(ItemOrdenado o : resumeTable.getItems()){
            currentTotal = currentTotal.add(o.getTotal());
        }
        totalTextBox.setText(currentTotal.toString());
    }
    
    /**
     * Agrega el item seleccionado como un Item Ordenado a la lista de pedidos
     * @param item
     * @return 
     */
    private ItemOrdenado addItem(Item item){
        ItemOrdenado oItem = new ItemOrdenado();
        oItem.setIdItem(idItem++);
        oItem.setPersona(this.persona);
        oItem.setDescripcion(item.getNombre());
        oItem.setPrecioUnitario(item.getPrecioUnitario());
        oItem.setTotal(item.getTotal());
        if(item.getEsOrden()){
            oItem.setCantidad(item.getCantidadOrden());
        }else{
            oItem.setCantidad(1);
        }
        oItem.setEsOrden(item.getEsOrden());
        resumeTable.getItems().add(oItem);
        curSubtotal = curSubtotal.add(oItem.getTotal());
        BigDecimal currentTotal = new BigDecimal(totalTextBox.getText());
        setCurrentTotal();
        return oItem;
    }
    
    /**************************************************************************/
    
    /* METODOS INVOCADOS POR JAVAFX */
    
    /**
     * Método que cierra la ventana
     * @param event 
     */
    @FXML
    private void executeClose(ActionEvent event) {
        Stage ps = (Stage)mainAnchor.getScene().getWindow();
        ps.close();
    }

    /**
     * Método que muestra el about
     * @param event 
     */
    @FXML
    private void executeAbout(ActionEvent event) {
         GeneralUtilities.mostrarAlertDialog("About...", 
                "About Mangiamo...",
                "Mangiamo Restaurant Software for JavaFX8", 
                Alert.AlertType.INFORMATION);
    }
    
    /**
     * Método que modifica la cantidad de un objeto tipo orden.
     * @param event 
     */
    @FXML
    private void incrementarCantidad(ActionEvent event) {
        Integer cantidadActual = Integer.parseInt(cantidadTextField.getText());
        Integer cantidadNueva = cantidadActual+1;
        cantidadTextField.setText(cantidadNueva.toString());
    }

    /**
     * Método que modifica la cantidad de un objeto tipo orden.
     * @param event 
     */
    @FXML
    private void decrementarCantidad(ActionEvent event) {
        Integer cantidadActual = Integer.parseInt(cantidadTextField.getText());
        if(cantidadActual==1)
            return;
        Integer cantidadNueva = cantidadActual-1;
        cantidadTextField.setText(cantidadNueva.toString());
    }
    
    /**
     * Método que guarda la cantidad de un objeto tipo orden.
     * @param event 
     */
    @FXML
    private void modificarCantidad(ActionEvent event) {
        selected.setCantidad(Integer.parseInt(cantidadTextField.getText()));
        BigDecimal cantidad = new BigDecimal(selected.getCantidad().doubleValue());
        BigDecimal total = selected.getPrecioUnitario().multiply(cantidad);
        selected.setTotal(total);
        setCurrentTotal();
        resumeTable.refresh();
        cantidadTextField.setText("");
        incrementoButton.setDisable(true);
        decrementoButton.setDisable(true);
        modificarButton.setDisable(true);
        
    }
    
    /**
     * Método que guarda la orden en el repositorio de órdenes
     * @param event 
     */
    @FXML
    private void saveOrder(ActionEvent event) {
        List<ItemOrdenado> items = resumeTable.getItems();
        this.orden.setOrderedItems(items);
        this.orden.setTotal(new BigDecimal(totalTextBox.getText()));
        this.orden.setTiempoEspera(tiempoEstimadoCombo.getValue());
        if(this.orden.isEsNueva()){
            this.orden.setHoraRegistro(LocalTime.now());
        }
        vertx.eventBus().send("save_order", this.orden,result->{
            if(result.succeeded()){
                Boolean stored = ((JsonObject)result.result().body()).getBoolean("success");
                if(stored){
                    Platform.runLater(()->{
//                        Stage ps = (Stage)mainAnchor.getScene().getWindow();
//                        ps.close();
                        GeneralUtilities.mostrarAlertDialog("Orden Creada",
                            "Orden Creada",
                            "Orden creada con número " + this.orden.getNumeroOrden()
                            , Alert.AlertType.CONFIRMATION);
                        VertxConfig.vertx.eventBus().send("orders_resume", this.orden);
                    });
                }
            }else{
                System.out.println("error!!!");
            }
        });
        //printOrder();
    }
    
    @FXML
    private void billOrder(ActionEvent event) {
        LookupClass.toBill = this.orden;
        Stage s = new Stage();
        try {
            CuentaLoader.getInstance().load(s);
        } catch (Exception ex) {
            Logger.getLogger(CreadorOrdenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void deliverOrder(ActionEvent event) {
        for(ItemOrdenado io: this.orden.getOrderedItems()){
            io.setServido(Boolean.TRUE);
        }
        this.orden.setEstatusOrden(EstatusOrden.SERVIDA);
        this.orden.setHoraServicio(LocalTime.now());
        VertxConfig.vertx.eventBus().send("orders_resume", this.orden);
    }    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getOrder();
        initOrderHeaders();
        initCols();
        setTreeViewEvent();
        setTableEvent();
        getMenu();
        initElements();
        
        Platform.runLater(()->{
            Stage ps = (Stage)mainAnchor.getScene().getWindow();
            ps.setOnHiding(event-> ps.close());           
        });
    }        
}
