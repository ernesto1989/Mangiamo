package com.conciencia.pojos;

import com.conciencia.vertx.eventbus.EventBusObject;
import io.vertx.core.json.JsonObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.conciencia.db.ExpectedResult;

/**
 * Clase que representa a un usuario del sistema.
 * 
 * @author Ernesto Cantú
 */
public class Usuario extends EventBusObject implements ExpectedResult {
    
    /* PROPIEDADES DEL CLIENTE */
    private Integer id;
    private String nombre;
    private String user;
    private String password;

    public Usuario() {
    }
    
    /**
     * Constructor que permite crear un objeto cliente a partir de un objeto json
     * @param json objeto Json de entrada
     */
    public Usuario(JsonObject json) {
        this.id = json.getInteger("id");
        this.nombre = json.getString("nombre");
        this.user = json.getString("user");
        this.password = json.getString("password");
    }   

    /* MÉTODOS DE ACCESO */
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Método que permite convertir un cliente a Json
     * @return cliente en formato json
     */
    @Override
    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.put("id",getId());
        json.put("nombre", getNombre());
        json.put("user", getUser());
        json.put("password", getPassword());
        return json;
    }
    
    @Override
    public void initWithJson(JsonObject json){
        this.id = json.getInteger("id");
        this.nombre = json.getString("nombre");
        this.user = json.getString("user");
        this.password = json.getString("password");
        json.put("type","Usuario");
    }
    
    /**
     * Método que permite extraer de un ResultSet un cliente.
     * @param rs 
     */
    @Override
    public void mapResult(ResultSet rs) {
        try{
            setId(rs.getInt("id"));
            setNombre(rs.getString("nombre"));
            setUser(rs.getString("user"));
            setPassword(rs.getString("password"));
        }catch(SQLException ex){
            //handle exceptions...
        }
    }    
}
