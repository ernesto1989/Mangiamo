package com.conciencia.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Ernesto Cantu
 */
public class OrdenesRepositoryVerticle extends AbstractVerticle{

        private Integer NUMERO_ORDEN = 1;

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
        vertx.eventBus().consumer("get_order_num", msg-> {
            Long currOrder;
            if(NUMERO_ORDEN < 10)
                currOrder = Long.parseLong(today + "00" + NUMERO_ORDEN);
            else
                currOrder = Long.parseLong(today + NUMERO_ORDEN);
            msg.reply(currOrder);
        });
    }
}
