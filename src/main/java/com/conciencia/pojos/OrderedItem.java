package com.conciencia.pojos;

import io.vertx.core.json.JsonObject;
import java.math.BigDecimal;

/**
 * Item de menu
 * @author usuario
 */
public class OrderedItem implements ToJson{
    
    private Integer persona;
    private Integer idItem;
    private String descripcion;
    private Integer cantidad;
    private BigDecimal total;

    public OrderedItem() {
    }
    
    public OrderedItem(JsonObject object) {
        this.persona = object.getInteger("persona");
        this.idItem = object.getInteger("idItem");
        this.descripcion = object.getString("descripcion");
        this.cantidad = object.getInteger("cantidad");
        new BigDecimal(object.getDouble("total"));
    }

    public Integer getPersona() {
        return persona;
    }

    public void setPersona(Integer persona) {
        this.persona = persona;
    }

    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    
    
    @Override
    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.put("persona",this.persona);
        json.put("idItem",this.idItem);
        json.put("descripcion",this.descripcion);
        json.put("cantidad", this.cantidad);
        json.put("total", this.total);
        return json;
    }

    @Override
    public String toString() {
        return this.getDescripcion();
    }    
}
