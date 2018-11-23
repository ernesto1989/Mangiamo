package com.conciencia.vertx.verticles;

import com.conciencia.controllers.VisorOrdenCocinaController;
import com.conciencia.pojos.Orden;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import java.text.SimpleDateFormat;
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
        
        ordenes = new HashMap<>();
        vertx.eventBus().consumer("get_order_num", msg-> {
//            Long currOrder;
//            String today = sdf.format(new Date());
//            if(NUMERO_ORDEN < 10)
//                currOrder = Long.parseLong(today + "00" + NUMERO_ORDEN);
//            else
//                currOrder = Long.parseLong(today + NUMERO_ORDEN);
            msg.reply(NUMERO_ORDEN);
        });
        
        vertx.eventBus().consumer("save_order", msg->{
            // <editor-fold defaultstate="colapsed" desc="handler">
            Orden o = (Orden) msg.body();
            NUMERO_ORDEN++;
            ordenes.put(o.getNumeroOrden(), o);
            
            if(VisorOrdenCocinaController.espacioDisponible){
                vertx.eventBus().send("display_order", o);
            }
            
            msg.reply(new JsonObject().put("success", Boolean.TRUE));
            //</editor-fold>
        });
        
        vertx.eventBus().consumer("find_order", msg->{
            // <editor-fold defaultstate="colapsed" desc="handler">
            Long orderNum = (Long) msg.body();
//            Long searchOrder;
//            String today = sdf.format(new Date());
//            if(orderNum < 10)
//                searchOrder = Long.parseLong(today + "00" + orderNum);
//            else
//                searchOrder = Long.parseLong(today + orderNum);
            Orden o = ordenes.get(orderNum);
            msg.reply(o);
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
    
    @Override
    public void stop() throws Exception {
        System.out.println("Ordenes Repository Verticle undeploy");
    }
}
