package com.conciencia.pojos;

import com.conciencia.db.SpectedResult;
import io.vertx.core.json.JsonObject;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Item de menu
 * @author usuario
 */
public class Item extends TreeContainer implements ToJson,SpectedResult {
    
    private Integer id;
    private String seccion;
    private String nombre;
    private BigDecimal precioUnitario;

    public Item() {
    }

    public Item(JsonObject object) {
        this.id = object.getInteger("id");
        this.seccion = object.getString("seccion");
        this.nombre = object.getString("nombre");
        this.precioUnitario = new BigDecimal(object.getDouble("precioUnitario"));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    @Override
    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.put("id", getId());
        json.put("seccion", getSeccion());
        json.put("nombre", getNombre());
        json.put("precioUnitario", getPrecioUnitario());
        return json;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.seccion);
        hash = 79 * hash + Objects.hashCode(this.nombre);
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
        final Item other = (Item) obj;
        if (!Objects.equals(this.seccion, other.seccion)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public void mapResult(ResultSet rs) {
        try{
            setId(rs.getInt("id"));
            setSeccion(rs.getString("seccion"));
            setNombre(rs.getString("nombre"));
            setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
            
        }catch(SQLException ex){
            //handle exceptions...
        }
    }
    
}
