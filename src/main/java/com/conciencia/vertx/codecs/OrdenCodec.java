package com.conciencia.vertx.codecs;

import com.conciencia.pojos.Orden;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * Clase que permite transmitir un objeto Customer a traves del event bus
 * 
 * @author Ernesto Cantu
 */
public class OrdenCodec implements MessageCodec<Orden, Orden> {

    @Override
    public void encodeToWire(Buffer buffer, Orden orden) {
        // Easiest ways is using JSON object
        JsonObject jsonToEncode = orden.toJson();

        // Encode object to string
        String jsonToStr = jsonToEncode.encode();

        // Length of JSON: is NOT characters count
        int length = jsonToStr.getBytes().length;

        // Write data into given buffer
        buffer.appendInt(length);
        buffer.appendString(jsonToStr);
    }

    @Override
    public Orden decodeFromWire(int position, Buffer buffer) {
        // My custom message starting from this *position* of buffer
        int _pos = position;

        // Length of JSON
        int length = buffer.getInt(_pos);

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        String jsonStr = buffer.getString(_pos += 4, _pos += length);
        JsonObject contentJson = new JsonObject(jsonStr);     
        
        return new Orden(contentJson);
    }

    @Override
    public Orden transform(Orden orden) {
        // If a message is sent *locally* across the event bus.
        // This example sends message just as is
        return orden;
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
