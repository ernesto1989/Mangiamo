package com.conciencia.vertx.eventbus;


/**
 *
 * @author Ernesto Cantu
 */
public abstract class EventBusObject implements JsonConverter{
    
    public abstract String getType();
}
