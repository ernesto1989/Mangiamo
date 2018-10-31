package com.conciencia.pojos;

import com.conciencia.db.SpectedResult;
import io.vertx.core.json.JsonObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Clase que representa a un cliente del restaurante.
 * @author Ernesto Cantú
 */
public class Cliente implements ToJson, SpectedResult {
    
    /* PROPIEDADES DEL CLIENTE */
    private Integer id;
    private String nombre;
    private String telefono;
    private String direccion;

    public Cliente() {
    }
    
    /**
     * Constructor que permite crear un objeto cliente a partir de un objeto json
     * @param json objeto Json de entrada
     */
    public Cliente(JsonObject json) {
        this.id = json.getInteger("id");
        this.nombre = json.getString("nombre");
        this.telefono = json.getString("telefono");
        this.direccion = json.getString("direccion");
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
        json.put("telefono", getTelefono());
        json.put("direccion", getDireccion());
        return json;
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
            setTelefono(rs.getString("telefono"));
            setDireccion(rs.getString("direccion"));
        }catch(SQLException ex){
            //handle exceptions...
        }
    }

    @Override
    public String toString() {
        return "Customer{" + "nombre=" + nombre + ", telefono=" + telefono + ", direccion=" + direccion + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.nombre);
        hash = 89 * hash + Objects.hashCode(this.telefono);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cliente other = (Cliente) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.telefono, other.telefono)) {
            return false;
        }
        return true;
    }
    
    
}
