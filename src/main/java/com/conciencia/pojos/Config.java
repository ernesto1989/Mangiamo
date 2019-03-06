package com.conciencia.pojos;

import com.conciencia.controllers.AdminController;
import com.conciencia.db.SpectedResult;
import io.vertx.core.json.JsonObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase utilitaria que permite interactuar con la tabla de parámetros.
 *
 * @author Ernesto Cantu
 */
public class Config implements ToJson,SpectedResult {

    private int id;
    private String parametro;
    private String valor;

    public Config() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    
    
    @Override
    public void mapResult(ResultSet rs) {
        try {
            this.id = rs.getInt("id");
            this.parametro = rs.getString("parametro");
            this.valor = rs.getString("valor");
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.put("id", id);
        object.put("parametro",parametro);
        object.put("valor",valor);
        return object;
    }
}
