package com.conciencia.pojos;

import com.conciencia.pojos.interfaces.ToJson;
import com.conciencia.pojos.enums.EstatusOrden;
import com.conciencia.pojos.enums.TipoOrden;
import com.conciencia.utilities.GeneralUtilities;
import com.conciencia.vertx.VertxConfig;
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
 *
 * @author usuario
 */
public class Orden implements ToJson {
    private TipoOrden orderType;
    private EstatusOrden estatusOrden;
    private Long numeroOrden;
    private Integer mesa;
    private String nombre;
    private Cliente cliente;
    private BigDecimal total;
    private Boolean pagado = false;
    private List<ItemOrdenado> orderedItems;
    private LocalTime horaRegistro;
    private LocalTime horaServicio;
    private boolean esNueva;
    private Integer tiempoEspera;
    private BigDecimal cambio = BigDecimal.ZERO;
    private BigDecimal diferenciaTotal = BigDecimal.ZERO;
    private String repartidor;

    public Orden() {
    }
    
    public Orden(JsonObject obj) {
        this.orderType = TipoOrden.getTipo(obj.getString("tipo"));
        this.estatusOrden = estatusOrden.getStatus(obj.getString("estatus"));
        this.numeroOrden =obj.getLong("numeroOrden");
        this.mesa = obj.getInteger("mesa");
        this.nombre = obj.getString("nombre");
        this.cliente = new Cliente(obj);
        this.total = new BigDecimal(obj.getDouble("total"));
        this.pagado = obj.getBoolean("pagado");
        this.orderedItems = getItems(obj);
        this.esNueva = obj.getBoolean("esNueva");
        this.tiempoEspera = obj.getInteger("tiempoEspera");
        this.horaRegistro = LocalTime.parse(obj.getString("horaRegistro"));
        this.horaServicio = LocalTime.parse(obj.getString("horaServicio"));
        this.cambio = new BigDecimal(obj.getDouble("cambio"));
        this.diferenciaTotal = new BigDecimal(obj.getDouble("diferenciaTotal"));
        this.repartidor = obj.getString("repartidor");
    }

    private List<ItemOrdenado> getItems(JsonObject obj){
        List<ItemOrdenado> items = new ArrayList<>();
        JsonArray itemsJson = obj.getJsonArray("items");
        for(Object i : itemsJson){
            items.add(new ItemOrdenado((JsonObject)i));
        }
        return items;
    }
    
    public TipoOrden getTipoOrden() {
        return orderType;
    }

    public void setTipoOrden(TipoOrden orderType) {
        this.orderType = orderType;
    }

    public EstatusOrden getEstatusOrden() {
        return estatusOrden;
    }

    public void setEstatusOrden(EstatusOrden estatusOrden) {
        this.estatusOrden = estatusOrden;
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

    public boolean isEsNueva() {
        return esNueva;
    }

    public void setEsNueva(boolean esNueva) {
        this.esNueva = esNueva;
    }

    public Integer getTiempoEspera() {
        return tiempoEspera;
    }

    public void setTiempoEspera(Integer tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }
    
    public LocalTime getHoraServicio() {
        return horaServicio;
    }

    public void setHoraServicio(LocalTime horaServicio) {
        this.horaServicio = horaServicio;
    }
    
    public String getOrdenPara(){
        return this.toString();
    }
    
    public String getObservaciones(){
        if(this.orderType == TipoOrden.DOMICILIO) return "Cambio: " + this.cambio.toString();
        if(this.pagado) return "PAGADA";
        return "NO PAGADA";
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
    
    /**
     * Cada 20 minutos
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
