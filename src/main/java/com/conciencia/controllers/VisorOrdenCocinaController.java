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
import javafx.application.Platform;
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
    
    private Boolean hayEn1, hayEn2, hayEn3;
    
    //private Integer mostrarEn = 1;
    
    private void initCols(){
        descripcionColumn1.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        cantidadColumn1.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        descripcionColumn2.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        cantidadColumn2.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        descripcionColumn3.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        cantidadColumn3.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    }
    
    private void eliminarOrden(Integer eliminarEn){
        if(eliminarEn == 1){
            numOrden1TextField.setText("");
            tipoOrden1Textfield.setText("");
            resumeTable1.getItems().removeAll(o1.getOrderedItems());
            if(hayEn2){
                hayEn2 = false;
                eliminarOrden(2);
                mostrarOrden(o2, 1);
            }
            if(hayEn3){
                hayEn3 = false;
                eliminarOrden(3);
                mostrarOrden(o3, 2);
            }
        }
        if(eliminarEn == 2){
            numOrden2TextField.setText("");
            tipoOrden2Textfield.setText("");
            resumeTable2.getItems().removeAll(o2.getOrderedItems());
            if(hayEn3){
                hayEn3 = false;
                eliminarOrden(3);
                mostrarOrden(o3, 2);
            }
        }
        if(eliminarEn == 3){
            numOrden3TextField.setText("");
            tipoOrden3Textfield.setText("");
            resumeTable3.getItems().removeAll(o3.getOrderedItems());
            vertx.eventBus().consumer("get_next_order", msg-> {
                Orden o = (Orden) msg.body();
                Platform.runLater(()->{
                    mostrarOrden(o, 3);
                });
            });
        }
    }
    
    private void mostrarOrden(Orden o,Integer mostrarEn){
        if(mostrarEn == 1){
            o1 = o;
            numOrden1TextField.setText(o.getNumeroOrden().toString());
            tipoOrden1Textfield.setText(o.getTipoOrden().toString());
            resumeTable1.getItems().setAll(o.getOrderedItems());
            hayEn1 = true;
        }
        
        if(mostrarEn == 2){
            o2 = o;
            numOrden2TextField.setText(o.getNumeroOrden().toString());
            tipoOrden2Textfield.setText(o.getTipoOrden().toString());
            resumeTable2.getItems().setAll(o.getOrderedItems());
            hayEn2 = true;
        }
        
        if(mostrarEn == 3){
            o3 = o;
            numOrden3TextField.setText(o.getNumeroOrden().toString());
            tipoOrden3Textfield.setText(o.getTipoOrden().toString());
            resumeTable3.getItems().setAll(o.getOrderedItems());
            hayEn3 = true;
        }
    }
    
    @FXML
    private void finalizarOrden1(ActionEvent event) {
        hayEn1 = false;
        o1.setEstatusOrden(EstatusOrden.ENTREGADA);
        eliminarOrden(1);
    }

    @FXML
    private void finalizarOrden2(ActionEvent event) {
        hayEn2 = false;
        o2.setEstatusOrden(EstatusOrden.ENTREGADA);
        eliminarOrden(2);
    }

    @FXML
    private void finalizarOrden3(ActionEvent event) {
        hayEn3 = false;
        o3.setEstatusOrden(EstatusOrden.ENTREGADA);
        eliminarOrden(3);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCols();
        espacioDisponible = true;
        hayEn1 = false;
        hayEn2 = false;
        hayEn3 = false;
        vertx.eventBus().consumer("display_order", msg-> {
            Orden o = (Orden) msg.body();
            o.setEstatusOrden(EstatusOrden.COCINA);
            if(!hayEn1){
                mostrarOrden(o,1);
                return;
            }else{
                if(!hayEn2){
                    mostrarOrden(o,2);
                    return;
                }else{
                    if(!hayEn3){
                        mostrarOrden(o, 3);
                        return;
                    }
                }
            }
            if(hayEn1 && hayEn2 && hayEn3){
                espacioDisponible = false;
            }            
        });
    }    
}
