package com.conciencia.pojos;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.conciencia.db.ExpectedResult;

/**
 * Item de menu
 * @author usuario
 */
public class Item extends TreeContainer implements ToJson,ExpectedResult {
    
    /* PROPIEDADES DEL ITEM */
    protected Integer id;
    protected String seccion;
    protected String nombre;
    protected BigDecimal precioUnitario;
    protected Boolean esOrden;
    protected Integer cantidadOrden;
    protected List<Item> relacionados;

    public Item() {
    }

    /**
     * Constructor que permite crear un objeto item a partir de un objeto json
     * @param json objeto Json de entrada
     */
    public Item(JsonObject object) {
        this.id = object.getInteger("id");
        this.seccion = object.getString("seccion");
        this.nombre = object.getString("nombre");
        this.precioUnitario = new BigDecimal(object.getDouble("precioUnitario"));
        this.esOrden = object.getBoolean("esOrden");
        this.cantidadOrden = object.getInteger("cantidadOrden");
        JsonArray rel = object.getJsonArray("relacionados");
        if(rel != null && !rel.isEmpty()){
            relacionados = new ArrayList<>();
            for(Object o:rel){
                relacionados.add(new Item((JsonObject)o));
            }
        }
        
    }

    /* MÉTODOS DE ACCESO */
    
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

    public Boolean getEsOrden() {
        return esOrden;
    }

    public void setEsOrden(Boolean esOrden) {
        this.esOrden = esOrden;
    }

    public Integer getCantidadOrden() {
        return cantidadOrden;
    }

    public void setCantidadOrden(Integer cantidadOrden) {
        this.cantidadOrden = cantidadOrden;
    }
    
    public BigDecimal getTotal(){
        return this.precioUnitario.multiply(new BigDecimal(cantidadOrden));
    }

    public List<Item> getRelacionados() {
        return relacionados;
    }

    public void setRelacionados(List<Item> relacionados) {
        this.relacionados = relacionados;
    }    
    
    /**
     * Método que permite convertir un cliente a Json
     * @return cliente en formato json
     */    
    @Override
    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.put("id", getId());
        json.put("seccion", getSeccion());
        json.put("nombre", getNombre());
        json.put("precioUnitario", getPrecioUnitario());
        json.put("esOrden", getEsOrden());
        json.put("cantidadOrden", getCantidadOrden());
        if(relacionados != null && relacionados.size() > 0){
            JsonArray rel = new JsonArray();
            for(Item i: relacionados){
                rel.add(i.toJson());
            }
            json.put("relacionados", rel);
        }
        return json;
    }

    /**
     * Método que permite extraer de un ResultSet un Item.
     * @param rs 
     */
    @Override
    public void mapResult(ResultSet rs) {
        try{
            setId(rs.getInt("id"));
            setSeccion(rs.getString("seccion"));
            setNombre(rs.getString("nombre"));
            setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
            setEsOrden(rs.getBoolean("es_orden"));
            setCantidadOrden(rs.getInt("cantidad_orden"));
        }catch(SQLException ex){
            //handle exceptions...
        }
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
}
