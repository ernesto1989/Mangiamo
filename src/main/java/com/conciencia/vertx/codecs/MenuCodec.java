package com.conciencia.vertx.codecs;

import com.conciencia.pojos.Menu;
import com.conciencia.pojos.Section;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author Ernesto Cantu
 */
public class MenuCodec implements MessageCodec<Menu, Menu> {

    @Override
    public void encodeToWire(Buffer buffer, Menu menu) {
        // Easiest ways is using JSON object
        JsonArray jsonMenu = new JsonArray();
        JsonObject jsonToEncode;
        
        for(Section section: menu){
            jsonToEncode = section.toJson();            
            jsonMenu.add(jsonToEncode);
        }
        
        // Encode object to string
        String jsonToStr = jsonMenu.encode();

        // Length of JSON: is NOT characters count
        int length = jsonToStr.getBytes().length;

        // Write data into given buffer
        buffer.appendInt(length);
        buffer.appendString(jsonToStr);
    }

    @Override
    public Menu decodeFromWire(int position, Buffer buffer) {
        // My custom message starting from this *position* of buffer
        int _pos = position;

        // Length of JSON
        int length = buffer.getInt(_pos);

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        String jsonStr = buffer.getString(_pos += 4, _pos += length);
        JsonArray contentJson = new JsonArray(jsonStr);
        Menu menu = new Menu();
        for(Object obj:contentJson){
            menu.add(new Section((JsonObject) obj));
        }
        
        return menu;
    }

    @Override
    public Menu transform(Menu menu) {
        // If a message is sent *locally* across the event bus.
        // This example sends message just as is
        return menu;
    }

    @Override
    public String name() {
        // Each codec must have a unique name.
        // This is used to identify a codec when sending a message and for unregistering codecs.
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        // Always -1
        return -1;
    }
}
