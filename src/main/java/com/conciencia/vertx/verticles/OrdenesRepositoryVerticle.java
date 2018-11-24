package com.conciencia.vertx.verticles;

import com.conciencia.controllers.VisorOrdenCocinaController;
import com.conciencia.datastructures.queue.Queue;
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
        private Long DIA_NUMERO;
        private Map<Long,Orden> ordenes;
        private Queue<Orden> listaEspera;

    /**
     * Método ejecutado al arranque del verticle.
     * 
     * @param startFuture
     * @throws Exception 
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        DIA_NUMERO = Long.parseLong(sdf.format(new Date()));
        
        ordenes = new HashMap<>();
        listaEspera = new Queue();
        vertx.eventBus().consumer("get_order_num", msg-> {
            Long currOrder;
            if(NUMERO_ORDEN < 10)
                currOrder = Long.parseLong(DIA_NUMERO + "00" + NUMERO_ORDEN);
            else
                currOrder = Long.parseLong(DIA_NUMERO + "" + NUMERO_ORDEN);
            msg.reply(currOrder);
        });
        
        vertx.eventBus().consumer("save_order", msg->{
            // <editor-fold defaultstate="colapsed" desc="handler">
            Orden o = (Orden) msg.body();
            NUMERO_ORDEN++;
            ordenes.put(o.getNumeroOrden(), o);
            
            if(VisorOrdenCocinaController.espacioDisponible){
                vertx.eventBus().send("display_order", o);
            }else{
                listaEspera.insert(o);
            }
            
            msg.reply(new JsonObject().put("success", Boolean.TRUE));
            //</editor-fold>
        });
        
        vertx.eventBus().consumer("find_order", msg->{
            // <editor-fold defaultstate="colapsed" desc="handler">
            Long orderNum = (Long) msg.body();
            Long searchOrder;
            if(NUMERO_ORDEN < 10)
                searchOrder = Long.parseLong(DIA_NUMERO + "00" + orderNum);
            else
                searchOrder = Long.parseLong(DIA_NUMERO + "" + orderNum);
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
