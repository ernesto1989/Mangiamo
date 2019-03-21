/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conciencia.controllers;

import com.conciencia.utilities.LookupClass;
import com.conciencia.pojos.EstatusOrden;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.TipoOrden;
import com.conciencia.utilities.GeneralUtilities;
import com.conciencia.vertx.VertxConfig;
import com.conciencia.vertx.eventbus.EventBusWrapper;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ernesto Cantu
 */
public class CuentaController implements Initializable {

    Orden o;
    @FXML
    private ToggleGroup tipoPago;
    @FXML
    private TextField porPagarTextField;
    @FXML
    private TextField importeTextField;
    @FXML
    private TextField cambioTextField;
    @FXML
    private RadioButton efectivoRadioButton;
    @FXML
    private RadioButton tarjetaRadioButton;
    @FXML
    private Button pagarButton;
    
    @FXML
    private void pagarCuenta(ActionEvent event) {
        EventBusWrapper wrapper = new EventBusWrapper();
        wrapper.setType(Orden.TYPE);
        wrapper.setPojo(this.o);
        this.o.setPagado(true);
        if(this.o.getTipoOrden() == TipoOrden.MESA){
            this.o.setEstatusOrden(EstatusOrden.CERRADA);
            VertxConfig.vertx.eventBus().send("close_order", wrapper);
        }
        else{
            this.o.setEstatusOrden(EstatusOrden.COCINA);
            if(this.o.getTipoOrden() == TipoOrden.DOMICILIO)
                this.o.setCambio(new BigDecimal(cambioTextField.getText()));
        }
        VertxConfig.vertx.eventBus().send("orders_resume", wrapper);
        //proceso de agregar dinero a la caja
        //guardar orden en bd
        Platform.runLater(()->{
            GeneralUtilities.mostrarAlertDialog("Orden Pagada",
               "Orden pagada",
               "Se ha cerrado la orden con número " + this.o.getNumeroOrden()
               , Alert.AlertType.CONFIRMATION);
            Stage ps = (Stage)porPagarTextField.getScene().getWindow();
            ps.close();
        });
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.o = LookupClass.toBill;
        LookupClass.toBill = null;
        porPagarTextField.setText(o.getTotal().toString());
        this.efectivoRadioButton.setSelected(true);
        if(this.o.getTipoOrden() == TipoOrden.DOMICILIO)
            pagarButton.setText("Guardar");
        else
            pagarButton.setText("Pagar");
        importeTextField.textProperty().addListener(l->{
            if(efectivoRadioButton.isSelected()){
                Double aPagar = new Double(porPagarTextField.getText());
                Double importe = new Double(importeTextField.getText());
                Double cambio = importe - aPagar;
                cambioTextField.setText(cambio.toString());
            }
        });
        this.tarjetaRadioButton.selectedProperty().addListener(l->{
            if(this.tarjetaRadioButton.isSelected()){
                this.importeTextField.setText("");
                this.importeTextField.setDisable(true);
                this.cambioTextField.setText("");
                this.cambioTextField.setDisable(true);
            }
            else{
                this.importeTextField.setDisable(false);
                this.cambioTextField.setText("");
                this.cambioTextField.setText("");
                this.cambioTextField.setDisable(false);
            }
        });   
    }    
}
