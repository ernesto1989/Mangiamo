package com.conciencia.pojos;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author usuario
 */
public class Orden implements ToJson {
    private OrderType orderType;
    private Long numeroOrden;
    private Integer mesa;
    private String nombre;
    private Cliente cliente;
    private Boolean pagado;
    List<OrderedItem> orderedItems;
    private LocalTime horaRegistro;
    private boolean esNueva;

    public Orden() {
    }
    
    public Orden(JsonObject obj) {
        this.orderType = OrderType.getType(obj.getString("tipo"));
        this.numeroOrden =obj.getLong("numeroOrden");
        this.mesa = obj.getInteger("mesa");
        this.nombre = obj.getString("nombre");
        this.cliente = new Cliente(obj);
        this.pagado = obj.getBoolean("pagado");
        this.orderedItems = getItems(obj);
        this.esNueva = obj.getBoolean("esNueva");
    }

    private List<OrderedItem> getItems(JsonObject obj){
        List<OrderedItem> items = new ArrayList<>();
        JsonArray itemsJson = obj.getJsonArray("items");
        for(Object i : itemsJson){
            items.add(new OrderedItem((JsonObject)i));
        }
        return items;
    }
    
    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Long getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(Long numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public Integer getMesa() {
        return mesa;
    }

    public void setMesa(Integer mesa) {
        this.mesa = mesa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Boolean getPagado() {
        return pagado;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    public List<OrderedItem> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<OrderedItem> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public LocalTime getHoraRegistro() {
        return horaRegistro;
    }

    public void setHoraRegistro(LocalTime horaRegistro) {
        this.horaRegistro = horaRegistro;
    }

    public boolean isEsNueva() {
        return esNueva;
    }

    public void setEsNueva(boolean esNueva) {
        this.esNueva = esNueva;
    }
    
    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        
        obj.put("tipo", this.getOrderType().toString());
        obj.put("numeroOrden", this.getNumeroOrden());
        obj.put("mesa", this.getMesa());
        obj.put("nombre", this.getNombre());
        obj.put("id",this.getCliente().getId());
        obj.put("nombre", this.getCliente().getNombre());
        obj.put("telefono", this.getCliente().getTelefono());
        obj.put("direccion", this.getCliente().getDireccion());
        obj.put("pagado",this.getPagado());
        obj.put("esNueva",this.esNueva);
        
        JsonArray a = new JsonArray();

        for(OrderedItem i:this.getOrderedItems()){
            a.add(i.toJson());
        }
        obj.put("items", a);
        return obj;
    }

    @Override
    public String toString() {
        return "Orden{" + "orderType=" + orderType + ", numeroOrden=" + numeroOrden + ", mesa=" + mesa + ", nombre=" + nombre + ", cliente=" + cliente + ", pagado=" + pagado + ", orderedItems=" + orderedItems + '}';
    }
}
