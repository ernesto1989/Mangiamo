package com.conciencia.vertx;

import com.conciencia.pojos.Customer;
import com.conciencia.pojos.Item;
import com.conciencia.pojos.Menu;
import com.conciencia.vertx.codecs.CustomerCodec;
import com.conciencia.vertx.codecs.MenuCodec;
import com.conciencia.vertx.codecs.MenuItemCodec;
import io.vertx.core.Vertx;

/**
 * Clase que crea la instancia de vertx y configura todo lo necesario.
 * 
 * 1.-Se crea la instancia de Vertx
 * 2.- Se registran los verticles de servicio de la aplicación
 * 3.- Se registran los codecs para transmisión de pojos a traves del event bus
 * 
 * @author Ernesto Cantu
 */
public class VertxConfig {
    
    /* Instancia de vertx que controla la aplicación */
    public static Vertx vertx;
    
    /**
     * Configuración de Vertx.
     * Se crea la instancia
     * Se registran los Verticles
     * Se registran los codecs
     */
    public static void config(){
        vertx = getVertxInstance();
        deployVerticles();
        registerCodecs();
    }
    
    /**
     * Método que genera la instancia de vertx por medio de la Api de Vertx
     * @return una instancia de vertx
     */
    public static Vertx getVertxInstance(){
        return Vertx.vertx();
    }
    
     /**
     * Método que despliega todos los verticles necesitados por la aplicación.
     * 
     * Verticles desplegados:
     * 
     * 1.- Repositorio de menú
     * 2.- Repositorio de clientes
     * 3.- Repositorio de órdenes?
     */
    private static void deployVerticles() {
        vertx.deployVerticle("com.conciencia.vertx.verticles.MenuDatabaseVerticle");
        vertx.deployVerticle("com.conciencia.vertx.verticles.CustomersDatabaseVerticle");
    }
    
    /**
     * Método que define los codecs para enviar objetos por eventbus.
     * 
     * Codecs registrados:
     * 
     * 1.- Items de menu.
     * 2.- Objeto Menu (Lista)
     * 3.- Objeto cliente
     */
    public static void registerCodecs(){
        vertx.eventBus().registerDefaultCodec(Item.class, new MenuItemCodec());
        vertx.eventBus().registerDefaultCodec(Menu.class, new MenuCodec());
        vertx.eventBus().registerDefaultCodec(Customer.class, new CustomerCodec());
    }
}
