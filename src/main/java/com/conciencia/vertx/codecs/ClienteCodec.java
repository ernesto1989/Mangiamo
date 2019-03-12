package com.conciencia.vertx.codecs;

import com.conciencia.pojos.Cliente;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * Clase que permite transmitir un objeto Cliente a traves del event bus
 * 
 * @author Ernesto Cantu
 */
public class ClienteCodec implements MessageCodec<Cliente, Cliente> {

    
    @Override
    public void encodeToWire(Buffer buffer, Cliente cliente) {
        JsonObject jsonToEncode = cliente.toJson();
        String jsonToStr = jsonToEncode.encode();
        int length = jsonToStr.getBytes().length;
        buffer.appendInt(length);
        buffer.appendString(jsonToStr);
    }

    @Override
    public Cliente decodeFromWire(int position, Buffer buffer) {
        int _pos = position;
        int length = buffer.getInt(_pos);
        String jsonStr = buffer.getString(_pos += 4, _pos += length);
        JsonObject contentJson = new JsonObject(jsonStr);     
        return new Cliente(contentJson);
    }

    @Override
    public Cliente transform(Cliente cliente) {
        return cliente;
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
