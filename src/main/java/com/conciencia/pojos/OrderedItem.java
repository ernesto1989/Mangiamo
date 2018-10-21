package com.conciencia.pojos;

import io.vertx.core.json.JsonObject;

/**
 * Item de menu
 * @author usuario
 */
public class OrderedItem implements ToJson{
    
    private Integer persona;
    private Item item;
    private Integer cantidad;

    public OrderedItem() {
    }
    
    public OrderedItem(JsonObject object) {
        this.persona = object.getInteger("persona");
        this.item = new Item(object);
        this.cantidad = object.getInteger("cantidad");
    }

    public Integer getPersona() {
        return persona;
    }

    public void setPersona(Integer persona) {
        this.persona = persona;
    }
    
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }    
    
    @Override
    public JsonObject toJson(){
        JsonObject json = this.item.toJson();
        json.put("cantidad", this.cantidad);
        return json;
    }

    @Override
    public String toString() {
        return this.item.getNombre();
    }    
}
