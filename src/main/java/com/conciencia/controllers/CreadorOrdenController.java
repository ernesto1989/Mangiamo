package com.conciencia.controllers;

import com.conciencia.lookups.LookupClass;
import com.conciencia.pojos.Item;
import com.conciencia.pojos.Menu;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.OrderType;
import com.conciencia.pojos.OrderedItem;
import com.conciencia.pojos.Section;
import com.conciencia.pojos.TreeContainer;
import static com.conciencia.vertx.VertxConfig.vertx;
import io.vertx.core.json.JsonObject;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
 * FXML Controller class
 *
 * @author usuario
 */
public class CreadorOrdenController implements Initializable {

    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private TableColumn<OrderedItem, String> descripcionColumn;
    @FXML
    private TableColumn<OrderedItem, Integer> personaColumn;
    @FXML
    private TableColumn<OrderedItem,Integer> cantidadColumn;
    @FXML
    private TableColumn<OrderedItem, BigDecimal> totalColumn;
    @FXML
    private TableView<OrderedItem> resumeTable;
    @FXML
    private TextField totalTextBox;
    @FXML
    private TreeView<TreeContainer> menuTree;
    @FXML
    private Button saveOrderButton;
    @FXML
    private TextField tipoOrdenTextfield;
    @FXML
    private TextField numOrdenTextField;
    @FXML
    private TextField descripcionTextField;
    @FXML
    private Button subTotalButton;
    @FXML
    private CheckBox pagadoCheckBox;
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
    
    private Orden orden;
    
    private OrderedItem selected;
    
    private int persona = 1;
    
    private BigDecimal curSubtotal = new BigDecimal("0.0");
    
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
    
    
    private void setCurrentTotal(){
        BigDecimal currentTotal = BigDecimal.ZERO;
        for(OrderedItem o : resumeTable.getItems()){
            currentTotal = currentTotal.add(o.getTotal());
        }
        totalTextBox.setText(currentTotal.toString());
    }
    
    /**
     * Método que inicializa los headers de la pantalla de creación de órdenes.
     */
    private void initOrderHeaders(){
        this.orden = LookupClass.current;
        tipoOrdenTextfield.setText(this.orden.getOrderType().toString());

        if(this.orden.getOrderType() == OrderType.MESA){
            descripcionTextField.setText("MESA: " + this.orden.getMesa().toString());
        }if(this.orden.getOrderType() == OrderType.LLEVAR){
            descripcionTextField.setText(this.orden.getNombre().toString());
        }if(this.orden.getOrderType() == OrderType.DOMICILIO){
            descripcionTextField.setText(this.orden.getCliente().toString());
        }
        numOrdenTextField.setText(this.orden.getNumeroOrden().toString());
        if(!this.orden.isEsNueva()){
            horaLabel.setVisible(true);
            horaOrdenTextfield.setVisible(true);
            horaOrdenTextfield.setText(this.orden.getHoraRegistro().format(dtf));
        }else{
            horaLabel.setVisible(false);
            horaOrdenTextfield.setVisible(false);
        }
        descripcionTextField.setTooltip(new Tooltip(descripcionTextField.getText()));
    }
    
    /**
     * Método para inicializar las columnas de la tabla de elementos ordenados.
     */
    private void initCols(){
        personaColumn.setCellValueFactory(new PropertyValueFactory<>("persona"));
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        cantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
    }
    
    /**
     * Método que inicializa el menú en la pantalla
     * @param menu 
     */
    private void createTree(Menu menu){
        Section root = new Section();
        root.setNombre("Menu");
        TreeItem<TreeContainer> rootNode = new TreeItem<>(root);
        TreeItem<TreeContainer> node;
        TreeItem<TreeContainer> leaf;
        for(Section s:menu){
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
    
    private OrderedItem addItem(Item item){
        OrderedItem oItem = new OrderedItem();
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
    
    /**
     * Método que se ejecuta cuando se selecciona un elemento del menu
     */
    private void setTreeViewEvent(){
        menuTree.setOnMouseClicked(e->{
            if(e.getClickCount() == 2){
                if(menuTree.getSelectionModel().getSelectedItem().getValue() instanceof Section)
                    return;
                Item item = (Item) (menuTree.getSelectionModel().getSelectedItem().getValue());
                OrderedItem o = addItem(item);
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
                OrderedItem o = resumeTable.getSelectionModel().getSelectedItem();
                OrderedItem selectedItem = resumeTable.getSelectionModel().getSelectedItem();
                BigDecimal currentTotal = new BigDecimal(totalTextBox.getText());
                totalTextBox.setText(currentTotal.subtract(selectedItem.getTotal()).toString());
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
    
    @FXML
    private void executeClose(ActionEvent event) {
        Stage ps = (Stage)mainAnchor.getScene().getWindow();
        ps.close();
    }

    @FXML
    private void executeAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About...");
        alert.setHeaderText("About Mangiamo...");
        alert.setContentText("Mangiamo Restaurant Software for JavaFX8");
        alert.showAndWait();
    }
    
    @FXML
    private void generarSubtotal(ActionEvent event) {
//        OrderedItem oItem = new OrderedItem();
//        oItem.setPersona(this.persona);
//        oItem.setDescripcion("SUBTOTAL");
//        resumeTable.getItems().add(oItem);
//        oItem.setTotal(curSubtotal);
//        curSubtotal = new BigDecimal("0");
//        this.persona++;
    }
    
    @FXML
    private void saveOrder(ActionEvent event) {
        this.orden.setPagado(this.pagadoCheckBox.isSelected());
        List<OrderedItem> items = resumeTable.getItems();
        this.orden.setOrderedItems(items);
        if(this.orden.isEsNueva()){
            this.orden.setHoraRegistro(LocalTime.now());
            this.orden.setEsNueva(false);
        }
        vertx.eventBus().send("save_order", this.orden,result->{
            if(result.succeeded()){
                Boolean stored = ((JsonObject)result.result().body()).getBoolean("success");
                if(stored){
                    System.out.println("Orden generada");
                    Platform.runLater(()->{
                        Stage ps = (Stage)mainAnchor.getScene().getWindow();
                        ps.close();
                    });
                }
            }else{
                System.out.println("error!!!");
            }
        });
    }
    
    @FXML
    private void incrementarCantidad(ActionEvent event) {
        Integer cantidadActual = Integer.parseInt(cantidadTextField.getText());
        Integer cantidadNueva = cantidadActual+1;
        cantidadTextField.setText(cantidadNueva.toString());
    }

    @FXML
    private void decrementarCantidad(ActionEvent event) {
        Integer cantidadActual = Integer.parseInt(cantidadTextField.getText());
        if(cantidadActual==1)
            return;
        Integer cantidadNueva = cantidadActual-1;
        cantidadTextField.setText(cantidadNueva.toString());
    }
    
    @FXML
    private void modificarCantidad(ActionEvent event) {
        selected.setCantidad(Integer.parseInt(cantidadTextField.getText()));
        BigDecimal cantidad = new BigDecimal(selected.getCantidad().doubleValue());
        BigDecimal total = selected.getPrecioUnitario().multiply(cantidad);
        selected.setTotal(total);
        setCurrentTotal();
        resumeTable.refresh();
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initOrderHeaders();
        initCols();
        setTreeViewEvent();
        setTableEvent();
        getMenu();
        incrementoButton.setDisable(true);
        decrementoButton.setDisable(true);
        modificarButton.setDisable(true);
        if(this.orden.getOrderedItems() != null)
            resumeTable.getItems().addAll(this.orden.getOrderedItems());
        
        Platform.runLater(()->{
            Stage ps = (Stage)mainAnchor.getScene().getWindow();
            ps.setOnHiding(event-> ps.close());           
        });
        subTotalButton.setVisible(false);
    }        
}
