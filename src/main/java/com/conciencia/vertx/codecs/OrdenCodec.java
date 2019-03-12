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
        JsonObject jsonToEncode = orden.toJson();
        String jsonToStr = jsonToEncode.encode();
        int length = jsonToStr.getBytes().length;
        buffer.appendInt(length);
        buffer.appendString(jsonToStr);
    }

    @Override
    public Orden decodeFromWire(int position, Buffer buffer) {
        int _pos = position;
        int length = buffer.getInt(_pos);
        String jsonStr = buffer.getString(_pos += 4, _pos += length);
        JsonObject contentJson = new JsonObject(jsonStr);     
        
        return new Orden(contentJson);
    }

    @Override
    public Orden transform(Orden orden) {
        return orden;
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
