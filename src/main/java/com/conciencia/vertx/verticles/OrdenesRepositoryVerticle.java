package com.conciencia.vertx.verticles;

import com.conciencia.pojos.Orden;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
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

        private Integer NUMERO_ORDEN = 1;
        private Map<Long,Orden> ordenes;

    /**
     * Método ejecutado al arranque del verticle.
     * 
     * @param startFuture
     * @throws Exception 
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(new Date());
        ordenes = new HashMap<>();
        vertx.eventBus().consumer("get_order_num", msg-> {
            Long currOrder;
            if(NUMERO_ORDEN < 10)
                currOrder = Long.parseLong(today + "00" + NUMERO_ORDEN);
            else
                currOrder = Long.parseLong(today + NUMERO_ORDEN);
            msg.reply(currOrder);
        });
        
        vertx.eventBus().consumer("save_order", msg->{
            // <editor-fold defaultstate="colapsed" desc="handler">
            Orden o = (Orden) msg.body();
            NUMERO_ORDEN++;
            ordenes.put(o.getNumeroOrden(), o);
            msg.reply(new JsonObject().put("success", Boolean.TRUE));
            //</editor-fold>
        });
        
        vertx.setPeriodic(/*300000*/10000, hndlr->{
            for(Orden o:ordenes.values()){
//                LocalTime ahora = LocalTime.now();
//                long minutos = ChronoUnit.SECONDS.between(o.getHoraRegistro(),ahora);
//                System.out.println(minutos);
            }
        });
    }
}
