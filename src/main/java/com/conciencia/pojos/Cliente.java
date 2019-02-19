package com.conciencia.pojos;

import com.conciencia.db.SpectedResult;
import io.vertx.core.json.JsonObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Clase que representa a un cliente del restaurante.
 * 
 * @author Ernesto Cantú
 */
public class Cliente implements ToJson, SpectedResult {
    
    /* PROPIEDADES DEL CLIENTE */
    private Integer id;
    private String nombre;
    private String telefono;
    private String calle;
    private String numero;
    private String colonia;
    private String eCalle1;
    private String eCalle2;

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
        this.calle = json.getString("calle");
        this.numero = json.getString("numero");
        this.colonia = json.getString("colonia");
        this.eCalle1 = json.getString("eCalle1");
        this.eCalle2 = json.getString("eCalle2");
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

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String geteCalle1() {
        return eCalle1;
    }

    public void seteCalle1(String eCalle1) {
        this.eCalle1 = eCalle1;
    }

    public String geteCalle2() {
        return eCalle2;
    }

    public void seteCalle2(String eCalle2) {
        this.eCalle2 = eCalle2;
    }  
    
    public boolean hayDatosFaltantes(){
        if(nombre == null || telefono == null || calle == null || 
                numero == null || colonia == null || eCalle1 == null || eCalle2 == null)
            return true;
        return false;
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
        json.put("calle",getCalle());
        json.put("numero",getNumero());
        json.put("colonia",getColonia());
        json.put("eCalle1",geteCalle1());
        json.put("eCalle2",geteCalle2());
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
            setCalle(rs.getString("calle"));
            setNumero(rs.getString("numero"));
            setColonia(rs.getString("colonia"));
            seteCalle1(rs.getString("ecalle1"));
            seteCalle2(rs.getString("ecalle2"));
        }catch(SQLException ex){
            //handle exceptions...
        }
    }

    @Override
    public String toString() {
        return calle + " " + numero + "," + colonia;
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
