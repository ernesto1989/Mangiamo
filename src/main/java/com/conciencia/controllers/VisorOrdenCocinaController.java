/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conciencia.controllers;

import com.conciencia.pojos.EstatusOrden;
import com.conciencia.pojos.ItemOrdenado;
import com.conciencia.pojos.Orden;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import static com.conciencia.vertx.VertxConfig.vertx;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Ernesto Cantu
 */
public class VisorOrdenCocinaController implements Initializable {

    @FXML
    private TextField numOrden1TextField;
    @FXML
    private TextField tipoOrden1Textfield;
    @FXML
    private TableView<ItemOrdenado> resumeTable1;
    @FXML
    private TableColumn<ItemOrdenado, String> descripcionColumn1;
    @FXML
    private TableColumn<ItemOrdenado, Integer> cantidadColumn1;
    @FXML
    private TextField numOrden2TextField;
    @FXML
    private TextField tipoOrden2Textfield;
    @FXML
    private TableView<ItemOrdenado> resumeTable2;
    @FXML
    private TableColumn<ItemOrdenado, String> descripcionColumn2;
    @FXML
    private TableColumn<ItemOrdenado, Integer> cantidadColumn2;
    @FXML
    private TextField numOrden3TextField;
    @FXML
    private TextField tipoOrden3Textfield;
    @FXML
    private TableView<ItemOrdenado> resumeTable3;
    @FXML
    private TableColumn<ItemOrdenado, String> descripcionColumn3;
    @FXML
    private TableColumn<ItemOrdenado, Integer> cantidadColumn3;
    
    public static Boolean espacioDisponible;
    
    private Orden o1, o2, o3;
    
    private Integer mostrarEn = 1;
    
    private void initCols(){
        descripcionColumn1.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        cantidadColumn1.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        descripcionColumn2.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        cantidadColumn2.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        descripcionColumn3.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        cantidadColumn3.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    }
    
    private void mostrarOrden(Orden o){
        if(mostrarEn == 1){
            numOrden1TextField.setText(o.getNumeroOrden().toString());
            tipoOrden1Textfield.setText(o.getTipoOrden().toString());
            resumeTable1.getItems().setAll(o.getOrderedItems());
        }
        
        if(mostrarEn == 2){
            numOrden2TextField.setText(o.getNumeroOrden().toString());
            tipoOrden2Textfield.setText(o.getTipoOrden().toString());
            resumeTable2.getItems().setAll(o.getOrderedItems());
        }
        
        if(mostrarEn == 3){
            numOrden3TextField.setText(o.getNumeroOrden().toString());
            tipoOrden3Textfield.setText(o.getTipoOrden().toString());
            resumeTable3.getItems().setAll(o.getOrderedItems());
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCols();
        espacioDisponible = true;
        vertx.eventBus().consumer("display_order", msg-> {
            Orden o = (Orden) msg.body();
            o.setEstatusOrden(EstatusOrden.COCINA);
            mostrarOrden(o);
            mostrarEn++;
            if(mostrarEn > 3){
                espacioDisponible = false;
            }            
        });
    }    


    @FXML
    private void finalizarOrden1(ActionEvent event) {
    }

    @FXML
    private void finalizarOrden2(ActionEvent event) {
    }

    @FXML
    private void finalizarOrden3(ActionEvent event) {
    }
    
}
