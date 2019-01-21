package com.conciencia.pojos;

import io.vertx.core.json.JsonObject;
import java.math.BigDecimal;

/**
 * Item de menu ordenado.
 * @author usuario
 */
public class ItemOrdenado implements ToJson{
    
    private Integer persona;
    private Integer idItem;
    private String descripcion;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal total;
    private Boolean esOrden;
    private Integer numRelacionados = 0;
    private Boolean servido = false;

    public ItemOrdenado() {
    }
    
    public ItemOrdenado(JsonObject object) {
        this.persona = object.getInteger("persona");
        this.idItem = object.getInteger("idItem");
        this.descripcion = object.getString("descripcion");
        this.cantidad = object.getInteger("cantidad");
        this.precioUnitario = new BigDecimal(object.getDouble("precioUnitario"));
        this.total = new BigDecimal(object.getDouble("total"));
        this.esOrden = object.getBoolean("esOrden");
        this.numRelacionados = object.getInteger("numRelacionados");
        this.servido = object.getBoolean("servido");
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

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Boolean getEsOrden() {
        return esOrden;
    }

    public void setEsOrden(Boolean esOrden) {
        this.esOrden = esOrden;
    }
    
    public Integer getNumRelacionados() {
        return numRelacionados;
    }

    public void setNumRelacionados(Integer numRelacionados) {
        this.numRelacionados = numRelacionados;
    }

    public Boolean getServido() {
        return servido;
    }

    public void setServido(Boolean servido) {
        this.servido = servido;
    }

    @Override
    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.put("persona",this.persona);
        json.put("idItem",this.idItem);
        json.put("descripcion",this.descripcion);
        json.put("cantidad", this.cantidad);
        json.put("precioUnitario",this.precioUnitario);
        json.put("total", this.total);
        json.put("esOrden",this.esOrden);
        json.put("numRelacionados",this.numRelacionados);
        json.put("servido", this.servido);
        return json;
    }
    
    public String print(){
        System.out.println(descripcion + " length: " + descripcion.length());
        StringBuilder s = new StringBuilder();
        int size = descripcion.length();
        s.append(descripcion);
        for(int i = size;i<=24;i++)
            s.append(" ");
        s.append(cantidad.toString());
        for(int i = 24;i<=35;i++)
            s.append(" ");
        s.append(total.toString());
        return s.toString();
    }

    @Override
    public String toString() {
        return this.getDescripcion();
    }    
}
