package com.conciencia.controllers;

import com.conciencia.lookups.OrdenLookup;
import com.conciencia.pojos.Cliente;
import com.conciencia.pojos.Item;
import com.conciencia.pojos.Menu;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.OrderType;
import com.conciencia.pojos.OrderedItem;
import com.conciencia.pojos.Section;
import com.conciencia.pojos.TreeContainer;
import static com.conciencia.vertx.VertxConfig.vertx;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    private TableColumn<Item, String> nameColumn;
    @FXML
    private TableColumn<Item, BigDecimal> precioColumn;
    @FXML
    private TableView<Item> resumeTable;
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
    
    private Orden orden;
    
    private int persona = 1;
    
    
    private void initCols(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
    }
    
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
    
    private void setTreeViewEvent(){
        menuTree.setOnMouseClicked(e->{
            if(e.getClickCount() == 2){
                if(menuTree.getSelectionModel().getSelectedItem().getValue() instanceof Section)
                    return;
                Item item = (Item) (menuTree.getSelectionModel().getSelectedItem().getValue());
                resumeTable.getItems().add(item);
                
                BigDecimal currentTotal = new BigDecimal(totalTextBox.getText());
                totalTextBox.setText(currentTotal.add(item.getPrecioUnitario()).toString());
            }
        });
    }
    
    private void setTableEvent(){
        resumeTable.setOnMouseClicked(e->{
            int selected = resumeTable.getSelectionModel().getSelectedIndex();
            Item selectedItem = resumeTable.getSelectionModel().getSelectedItem();
            BigDecimal currentTotal = new BigDecimal(totalTextBox.getText());
            totalTextBox.setText(currentTotal.subtract(selectedItem.getPrecioUnitario()).toString());
            resumeTable.getItems().remove(selected);
        });
    }
    
    private void getMenu() {
        vertx.eventBus().send("get_menu",null,response -> {
            Menu menu = (Menu) response.result().body();
            Platform.runLater(()->{
                createTree(menu);
            });
        });
    }
    
    private String generateTicket(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        List<Item> orderedItems = resumeTable.getItems();
        StringBuilder builder = new StringBuilder();
        builder.append("*********Tacos Güerononón**************\n" +
                       "*********Fecha:"+dateFormat.format(new Date())+"**************\n");
                       //"*********Orden No. ######**************");
        
        for(Item item:orderedItems){
            builder.append(item.getNombre() + " - " + item.getPrecioUnitario()+"\n");
        }
        builder.append("Total: " + totalTextBox.getText() + "\n");
        builder.append("*********Gracias por su compra*********");
        return builder.toString();
    }
        
    
    @FXML
    private void executeClose(ActionEvent event) {
        return;
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
    private void saveOrder(ActionEvent event) {
        List<Item> selected = resumeTable.getItems();
        OrderedItem item;
        List<OrderedItem> ordered = new ArrayList<>();
        for(Item i: selected){
            item = new OrderedItem();
            item.setPersona(1);
            item.setCantidad(1);
            item.setItem(i);
            ordered.add(item);
        }
        Orden o = new Orden();
        o.setNombre("Un Nombre");
        o.setOrderType(OrderType.LLEVAR);
        o.setOrderedItems(ordered);
        System.out.println(o);
    }


    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCols();
        getMenu();
        setTreeViewEvent();
        setTableEvent();
        
        vertx.eventBus().send("get_order_num",null,response -> {
            Long numOrden = (long) response.result().body();
            Platform.runLater(()->{
                numOrdenTextField.setText(numOrden.toString());
            });
        });
        
        Platform.runLater(()->{
            Stage ps = (Stage)mainAnchor.getScene().getWindow();
            ps.setOnHiding(event-> ps.hide());
            this.orden = OrdenLookup.current;
            tipoOrdenTextfield.setText(this.orden.getOrderType().toString());
            
            if(this.orden.getOrderType() == OrderType.MESA){
                descripcionTextField.setText("MESA: " + this.orden.getMesa().toString());
            }
            
            if(this.orden.getOrderType() == OrderType.LLEVAR){
                descripcionTextField.setText(this.orden.getNombre().toString());
            }
            
            if(this.orden.getOrderType() == OrderType.DOMICILIO){
                descripcionTextField.setText(this.orden.getCliente().toString());
            }
            descripcionTextField.setTooltip(new Tooltip(descripcionTextField.getText()));
            
        });
    }        

    
    
}
