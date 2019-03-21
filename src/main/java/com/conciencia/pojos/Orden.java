package com.conciencia.pojos;

import com.conciencia.utilities.GeneralUtilities;
import com.conciencia.vertx.VertxConfig;
import com.conciencia.vertx.eventbus.EventBusObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Clase que representa una orden realizada por un cliente.
 * 
 * @author Ernesto Cantú
 */
public class Orden extends EventBusObject {
    
    public static String TYPE = "ORDEN";
    
    /* PROPIEDADES DEL CLIENTE */
    
    //número de orden
    private Long numeroOrden;
    
    //mesa a la que va la orden. Puede ser null
    private Integer mesa;
    
    //nombre del cliente que pidió para llevar. Puede ser null
    private String nombre;
    
    //cliente que pidió por teléfono
    private Cliente cliente;
    
    //tipo de órden (Mesa,Llevar,Domicilio)
    private TipoOrden tipoOrden;
    
    //Permite conocer si la órden es nueva o no. Para uso del creador de órdenes.
    private boolean esNueva = true;
    
    //Estatus que permite saber dónde está ubicada la órden. 
    private EstatusOrden estatusOrden;
    
    //El total de la cuenta
    private BigDecimal total;
    
    //Permite saber si la órden ya fue pagada
    private Boolean pagado = false;
    
    //Elementos ordenados
    private List<ItemOrdenado> orderedItems;
    
    //La hora en que la orden fue tomada
    private LocalTime horaRegistro;
    
    //La hora en que la orden se sirvió
    private LocalTime horaServicio;
    
    //Diferencia entre hora de registro y servicio
    private Integer tiempoEspera;
    
    //Cambio, para las órdenes a domicilio
    private BigDecimal cambio = BigDecimal.ZERO;
    
    //Elemento agregado para guardar la diferencia con el total cuando el envío es a domicilio.
    private BigDecimal diferenciaTotal = BigDecimal.ZERO;
    
    //el repartidor que llevó la orden
    private String repartidor;

    /* CONSTRUCTORES */
    public Orden() {
    }

     /* MÉTODOS DE ACCESO */
    
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
    
    public TipoOrden getTipoOrden() {
        return tipoOrden;
    }

    public void setTipoOrden(TipoOrden tipoOrden) {
        this.tipoOrden = tipoOrden;
    }
    
    public boolean isEsNueva() {
        return esNueva;
    }

    public void setEsNueva(boolean esNueva) {
        this.esNueva = esNueva;
    }

    public EstatusOrden getEstatusOrden() {
        return estatusOrden;
    }

    public void setEstatusOrden(EstatusOrden estatusOrden) {
        this.estatusOrden = estatusOrden;
    }    

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Boolean getPagado() {
        return pagado;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    public List<ItemOrdenado> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<ItemOrdenado> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public LocalTime getHoraRegistro() {
        return horaRegistro;
    }

    public void setHoraRegistro(LocalTime horaRegistro) {
        this.horaRegistro = horaRegistro;
    }
    
    public LocalTime getHoraServicio() {
        return horaServicio;
    }

    public void setHoraServicio(LocalTime horaServicio) {
        this.horaServicio = horaServicio;
    }

    public Integer getTiempoEspera() {
        return tiempoEspera;
    }

    public void setTiempoEspera(Integer tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }
    
    public BigDecimal getCambio() {
        return cambio;
    }

    public void setCambio(BigDecimal cambio) {
        this.cambio = cambio;
    }

    public BigDecimal getDiferenciaTotal() {
        return diferenciaTotal;
    }

    public void setDiferenciaTotal(BigDecimal diferenciaTotal) {
        this.diferenciaTotal = diferenciaTotal;
    }

    public String getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(String repartidor) {
        this.repartidor = repartidor;
    }
    
    
     /* MÉTODOS DE APOYO A LA CLASE */
    
    /**
     * Método de apoyo que permite obtener una lista de Elementos Ordenados a
     * partir de un objeto Json.
     * 
     * @param obj
     * @return 
     */
    private List<ItemOrdenado> getItems(JsonObject obj){
        List<ItemOrdenado> items = new ArrayList<>();
        JsonArray itemsJson = obj.getJsonArray("items");
        for(Object i : itemsJson){
            items.add(new ItemOrdenado((JsonObject)i));
        }
        return items;
    }
    
    /**
     * Método usado por la pantalla principal para asignar el valor "Orden Para"
     * en el resumen de órdenes.
     * @return 
     */
    public String getOrdenPara(){
        return this.toString();
    }
    
    public String getObservaciones(){
        if(this.tipoOrden == TipoOrden.DOMICILIO) return "Cambio: " + this.cambio.toString();
        if(this.pagado) return "PAGADA";
        return "NO PAGADA";
    }
    
    /**
     * Timer de tempo de espera para la orden
     */
    public void startTimer(){
        Integer tiempoMili = this.tiempoEspera * 60000;
        VertxConfig.vertx.setTimer(tiempoMili, event->{
            if(getEstatusOrden() == EstatusOrden.COCINA){
                Platform.runLater(()->{
                    GeneralUtilities.mostrarAlertDialog("Orden con tiempo de espera alto", 
                            "Orden con tiempo de espera alto", "La orden " + this.toString() + 
                                    " tiene mucho tiempo en espera", Alert.AlertType.WARNING);
                });
                startTimer();
            }
            
        });
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        
        obj.put("tipo", this.getTipoOrden().toString());
        obj.put("estatus", this.getEstatusOrden().toString());
        obj.put("numeroOrden", this.getNumeroOrden());
        obj.put("mesa", this.getMesa());
        obj.put("nombre", this.getNombre());
        obj.put("id",this.getCliente().getId());
        obj.put("nombre", this.getCliente().getNombre());
        obj.put("telefono", this.getCliente().getTelefono());
        obj.put("calle",this.getCliente().getCalle());
        obj.put("numero",this.getCliente().getNumero());
        obj.put("colonia",this.getCliente().getColonia());
        obj.put("eCalle1",this.getCliente().geteCalle1());
        obj.put("eCalle2",this.getCliente().geteCalle2());
        obj.put("total", this.getTotal());
        obj.put("pagado",this.getPagado());
        obj.put("esNueva",this.esNueva);
        obj.put("tiempoEspera",this.tiempoEspera);
        obj.put("horaRegistro",this.horaRegistro.toString());
        obj.put("horaServicio",this.horaServicio.toString());
        obj.put("cambio",this.cambio.toString());
        obj.put("diferenciaTotal", this.diferenciaTotal);
        obj.put("repartidor",this.repartidor);
        
        JsonArray a = new JsonArray();

        for(ItemOrdenado i:this.getOrderedItems()){
            a.add(i.toJson());
        }
        obj.put("items", a);
        return obj;
    }
    
    @Override
    public void initWithJson(JsonObject json){
        this.tipoOrden = TipoOrden.getTipo(json.getString("tipo"));
        this.estatusOrden = estatusOrden.getStatus(json.getString("estatus"));
        this.numeroOrden =json.getLong("numeroOrden");
        this.mesa = json.getInteger("mesa");
        this.nombre = json.getString("nombre");
        this.cliente = new Cliente();
        this.cliente.initWithJson(json);
        this.total = new BigDecimal(json.getDouble("total"));
        this.pagado = json.getBoolean("pagado");
        this.orderedItems = getItems(json);
        this.esNueva = json.getBoolean("esNueva");
        this.tiempoEspera = json.getInteger("tiempoEspera");
        this.horaRegistro = LocalTime.parse(json.getString("horaRegistro"));
        this.horaServicio = LocalTime.parse(json.getString("horaServicio"));
        this.cambio = new BigDecimal(json.getDouble("cambio"));
        this.diferenciaTotal = new BigDecimal(json.getDouble("diferenciaTotal"));
        this.repartidor = json.getString("repartidor");
    }
    
    /**
     * Método que permite saber qué tipo de objeto es una orden en tiempo
     * de ejecución.
     * 
     * @return ORDEN. Para uso de bus de eventos.
     */
    @Override
    public String getType(){
        return Orden.TYPE;
    }

    @Override
    public String toString() {
        if(getTipoOrden() == TipoOrden.MESA){
            return "MESA " + getMesa().toString();
        }if(getTipoOrden() == TipoOrden.LLEVAR){
            return getNombre();
        }if(getTipoOrden() == TipoOrden.DOMICILIO){
            return getCliente().toString();
        }
        return "";
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.numeroOrden);
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
        final Orden other = (Orden) obj;
        if (!Objects.equals(this.numeroOrden, other.numeroOrden)) {
            return false;
        }
        return true;
    }
    
    
}
