package com.conciencia.vertx.eventbus;

import io.vertx.core.json.JsonObject;

/**
 * Interfase que definir la conversión de un objeto a Json y vice versa
 * 
 * @author Ernesto Cantú
 */
public interface JsonConverter {
      
    public JsonObject toJson();
    
    public void initWithJson(JsonObject json);
}
