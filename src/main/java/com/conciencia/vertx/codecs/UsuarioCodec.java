package com.conciencia.vertx.codecs;

import com.conciencia.pojos.Usuario;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * Clase que permite transmitir un objeto Customer a traves del event bus
 * 
 * @author Ernesto Cantu
 */
public class UsuarioCodec implements MessageCodec<Usuario, Usuario> {

    @Override
    public void encodeToWire(Buffer buffer, Usuario usuario) {
        JsonObject jsonToEncode = usuario.toJson();
        String jsonToStr = jsonToEncode.encode();
        int length = jsonToStr.getBytes().length;
        buffer.appendInt(length);
        buffer.appendString(jsonToStr);
    }

    @Override
    public Usuario decodeFromWire(int position, Buffer buffer) {
        int _pos = position;
        int length = buffer.getInt(_pos);
        String jsonStr = buffer.getString(_pos += 4, _pos += length);
        JsonObject contentJson = new JsonObject(jsonStr);     
        
        return new Usuario(contentJson);
    }

    @Override
    public Usuario transform(Usuario usuario) {
        return usuario;
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
