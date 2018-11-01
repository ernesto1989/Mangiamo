package com.conciencia.pojos;

import io.vertx.core.json.JsonObject;

/**
 * Interfase que permite convertir un objeto a Json
 * @author Ernesto Cantú
 */
interface ToJson {
    
    public JsonObject toJson();
}
