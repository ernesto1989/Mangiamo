package com.conciencia.vertx.verticles;

import com.conciencia.controllers.AdminController;
import static com.conciencia.controllers.AdminController.CANTIDAD_CAJA_CHICA;
import static com.conciencia.controllers.AdminController.DB_URL;
import static com.conciencia.controllers.AdminController.TOTAL;
import com.conciencia.db.DatabaseUtilities;
import com.conciencia.db.impl.SqliteUtilities;
import com.conciencia.pojos.EstatusOrden;
import com.conciencia.pojos.Orden;
import com.conciencia.pojos.TipoOrden;
import com.conciencia.vertx.VertxConfig;
import com.conciencia.vertx.eventbus.EventBusWrapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import java.time.temporal.ChronoUnit;

/**
 * Verticle que funciona como repositorio de órdenes "abiertas"
 * 
 * @author Ernesto Cantu
 */
public class OrdenesRepositoryVerticle extends AbstractVerticle{

    private Long NUMERO_ORDEN = 1L;
    //private Map<Long,Orden> ordenesAbiertas;
    private final String INSERT_ORDER = "INSERT INTO ordenes " +
                                            "(numero_orden, "
            + "mesa, nombre, cliente, tipo_orden, "
            + "total, hora_registro, hora_servicio, "
            + "tiempo_espera, cambio, diferencia_total, repartidor) " +
              "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        

    /**
     * Método ejecutado al arranque del verticle.
     * 
     * @param startFuture
     * @throws Exception 
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().consumer("get_order_num", msg-> {
            msg.reply(NUMERO_ORDEN);                                     
        });
        
        vertx.eventBus().consumer("save_order", msg->{
            // <editor-fold defaultstate="collapsed" desc="handler">
            EventBusWrapper wrapper = (EventBusWrapper) msg.body();
            Orden orden = (Orden)wrapper.getPojo();
            NUMERO_ORDEN++;
            orden.setEstatusOrden(EstatusOrden.COCINA);
            orden.startTimer();
            VertxConfig.vertx.eventBus().send("orders_resume", wrapper);
            msg.reply(new JsonObject().put("success", Boolean.TRUE));
            //</editor-fold>
        });
        
        vertx.eventBus().consumer("close_order", msg->{
            // <editor-fold defaultstate="collapsed" desc="handler">
            EventBusWrapper wrapper = (EventBusWrapper) msg.body();
            Orden o = (Orden)wrapper.getPojo();
            DatabaseUtilities dbConn = new SqliteUtilities(DB_URL);
            
            CANTIDAD_CAJA_CHICA = CANTIDAD_CAJA_CHICA.add(o.getTotal());
            TOTAL++;
            if(o.getTipoOrden().equals(TipoOrden.MESA))
                AdminController.EN_RESTAURANTE++;
            if(o.getTipoOrden().equals(TipoOrden.LLEVAR))
                AdminController.PARA_LLEVAR++;
            if(o.getTipoOrden().equals(TipoOrden.DOMICILIO))
                AdminController.DOMICILIO++;
            
            dbConn.executeInsert(INSERT_ORDER, 
                    o.getNumeroOrden().intValue(),
                    o.getMesa()!= null? o.getMesa():-1,
                    o.getNombre()!=null?o.getNombre():"",
                    o.getCliente()!=null?o.getCliente().getId():-1,
                    o.getTipoOrden().toString(),
                    o.getTotal().doubleValue(),o.getHoraRegistro().toString(),
                    o.getHoraServicio().toString(),
                    ChronoUnit.MINUTES.between(o.getHoraRegistro(), o.getHoraServicio()),
                    o.getCambio().doubleValue(),o.getDiferenciaTotal().doubleValue(),
                    o.getRepartidor()!= null?o.getRepartidor():"");
            
            msg.reply(o);
            //</editor-fold>
        });
    }
    
    @Override
    public void stop() throws Exception {
        System.out.println("Ordenes Repository Verticle undeploy");
    }
}
