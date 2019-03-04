package com.conciencia.vertx.verticles;

import com.conciencia.controllers.AdminController;
import com.conciencia.db.DatabaseUtilities;
import com.conciencia.db.impl.SqliteUtilities;
import com.conciencia.pojos.EstatusOrden;
import com.conciencia.pojos.Orden;
import com.conciencia.vertx.VertxConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Verticle que funciona como repositorio de órdenes "abiertas"
 * 
 * @author Ernesto Cantu
 */
public class OrdenesRepositoryVerticle extends AbstractVerticle{

    private Long NUMERO_ORDEN = 1L;
    private Map<Long,Orden> ordenesAbiertas;
    private final String INSERT_ORDER = "INSERT INTO ordenes "
            + "(numero_orden, tipo_orden, orden_para, cliente, hora_registro, tiempo_espera, hora_cierre,diferencia_mins) "
            + "values (?,?,?,?,?,?,?,?)";
        

    /**
     * Método ejecutado al arranque del verticle.
     * 
     * @param startFuture
     * @throws Exception 
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        ordenesAbiertas = new HashMap<>();
        vertx.eventBus().consumer("get_order_num", msg-> {
            msg.reply(NUMERO_ORDEN);
        });
        
        vertx.eventBus().consumer("save_order", msg->{
            // <editor-fold defaultstate="colapsed" desc="handler">
            Orden o = (Orden) msg.body();
            NUMERO_ORDEN++;
            ordenesAbiertas.put(o.getNumeroOrden(), o);
            o.setEstatusOrden(EstatusOrden.COCINA);
            o.startTimer();
            
            msg.reply(new JsonObject().put("success", Boolean.TRUE));
            //</editor-fold>
        });
        
        vertx.eventBus().consumer("close_order", msg->{
            // <editor-fold defaultstate="colapsed" desc="handler">
            Orden o = (Orden) msg.body();
            ordenesAbiertas.remove(o.getNumeroOrden());
            DatabaseUtilities dbConn = new SqliteUtilities(AdminController.DB_URL);
            dbConn.executeInsert(INSERT_ORDER, 
                    o.getNumeroOrden(),
                    o.getTipoOrden().toString(),
                    o.toString(),
                    o.getCliente() != null? o.getCliente().getId():-1,
                    o.getHoraRegistro().toString(),
                    o.getTiempoEspera(),
                    o.getHoraServicio().toString(),
                    ChronoUnit.MINUTES.between(o.getHoraRegistro(), o.getHoraServicio()));
            
            msg.reply(o);
            //</editor-fold>
        });
    }
    
    @Override
    public void stop() throws Exception {
        System.out.println("Ordenes Repository Verticle undeploy");
    }
}
