package com.conciencia.vertx.eventbus;

import com.conciencia.pojos.Cliente;
import com.conciencia.pojos.Usuario;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author Ernesto Cantu
 */
public class EventBusWrapper implements JsonConverter{
    
    private String type;
    private EventBusObject pojo;
    
    public EventBusWrapper(){
    
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EventBusObject getPojo() {
        return pojo;
    }

    public void setPojo(EventBusObject pojo) {
        this.pojo = pojo;
    }
    
    @Override
    public JsonObject toJson(){
        JsonObject json = new JsonObject().
                put("type", type).put("pojo",pojo);
        return json;
    }
    
    @Override
    public void initWithJson(JsonObject json){
        this.type = json.getString("type");
        
        if(this.type.equals("Cliente")){
            this.pojo = new Cliente();
        }
        
        if(this.type.equals("Usuario")){
            this.pojo = new Usuario();
        }
        
        this.pojo.initWithJson(json.getJsonObject("pojo"));
    }
}
