package com.conciencia.pojos;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.conciencia.db.ExpectedResult;

/**
 * Seccion de menu con sus items.
 * @author usuario
 */
public class Seccion extends TreeContainer implements ToJson,ExpectedResult {
    
    private String nombre;
    private List<Item> items;

    public Seccion() {
    }
    
    public Seccion(JsonObject object){
        this.nombre = object.getString("nombre");
        List<Item> items = new ArrayList();
        for(Object json:object.getJsonArray("items")){
            items.add(new Item((JsonObject)json));
        }
        this.setItems(items);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String section) {
        this.nombre = section;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
    
    @Override
    public void mapResult(ResultSet rs) {
        try{
            setNombre(rs.getString("seccion"));
        }catch(SQLException ex){
            //handle exceptions...
        }
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonArray jsonItems = new JsonArray();
        
        json.put("nombre", getNombre());
        for(Item i: items){
            jsonItems.add(i.toJson());
        }
        json.put("items", jsonItems);
        return json;
    }
}
