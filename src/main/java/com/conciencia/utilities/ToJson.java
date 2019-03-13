package com.conciencia.utilities;

import io.vertx.core.json.JsonObject;

/**
 * Interfase que definir la conversión de un objeto a Json.
 * 
 * @author Ernesto Cantú
 */
public interface ToJson {
    
    /**
     * Método que permite define como un objeto se convierte en Json
     * @return 
     */
    public JsonObject toJson();
}
