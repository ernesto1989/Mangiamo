package com.conciencia.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;



/**
 * Vérticle que contiene todas las operaciones permitidas con el repositorio 
 * de clientes.
 * 
 * @author Ernesto Cantu
 */
public class CustomersDatabaseVerticle extends AbstractVerticle{
    
    
    /**
     * Método sobreescrito del verticle.
     * 
     * 1.- Se crea el objeto de conexión a bd
     * 2.- Sse crea el consumidor de mensajes para buscar clientes
     * 3.- Sse crea el consumidor de mensajes para crear clientes
     * 
     * @param startFuture
     * @throws Exception 
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().consumer("get_customer", msg->{
            
        });
        
        vertx.eventBus().consumer("save_customer",msg->{
            
        });
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Customers Verticle undeploy");
    }
    
    
}
