/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conciencia.controllers;

import com.conciencia.lookups.LookupClass;
import com.conciencia.pojos.EstatusOrden;
import com.conciencia.pojos.Orden;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private void pagarCuenta(ActionEvent event) {
        this.o.setPagado(true);
        this.o.setEstatusOrden(EstatusOrden.CERRADA);
        //proceso de agregar dinero a la caja
        //guardar orden en bd
        Platform.runLater(()->{
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
