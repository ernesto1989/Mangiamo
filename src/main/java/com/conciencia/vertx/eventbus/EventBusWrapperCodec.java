package com.conciencia.vertx.eventbus;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * Clase que permite transmitir un objeto Cliente a traves del event bus
 * 
 * @author Ernesto Cantu
 */
public class EventBusWrapperCodec implements MessageCodec<EventBusWrapper, EventBusWrapper> {

    
    @Override
    public void encodeToWire(Buffer buffer, EventBusWrapper obj) {
        JsonObject jsonToEncode = obj.toJson();
        String jsonToStr = jsonToEncode.encode();
        int length = jsonToStr.getBytes().length;
        buffer.appendInt(length);
        buffer.appendString(jsonToStr);
    }

    @Override
    public EventBusWrapper decodeFromWire(int position, Buffer buffer) {
        int _pos = position;
        int length = buffer.getInt(_pos);
        String jsonStr = buffer.getString(_pos += 4, _pos += length);
        JsonObject contentJson = new JsonObject(jsonStr);     
        EventBusWrapper wrapper = new EventBusWrapper();
        wrapper.initWithJson(contentJson);
        return wrapper;
    }

    @Override
    public EventBusWrapper transform(EventBusWrapper obj) {
        return obj;
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
