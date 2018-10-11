package com.conciencia.vertx;

import com.conciencia.main.MainApp;
import static com.conciencia.main.MainApp.vertx;
import com.conciencia.pojos.Customer;
import com.conciencia.pojos.Item;
import com.conciencia.pojos.Menu;
import com.conciencia.vertx.codecs.CustomerCodec;
import com.conciencia.vertx.codecs.MenuCodec;
import com.conciencia.vertx.codecs.MenuItemCodec;
import io.vertx.core.Vertx;

/**
 * Clase que crea la instancia de vertx y configura todo lo necesario
 * 
 * @author Ernesto Cantu
 */
public class VertxConfig {
    
    /**
     * Configuración de Vertx.
     * Se crea la instancia
     * Se registran los Verticles
     * Se registran los codecs
     */
    public static void config(){
        MainApp.vertx = getVertxInstance();
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
     * Método que despliega todos los verticles necesitados por la aplicación
     */
    private static void deployVerticles() {
        vertx.deployVerticle("com.conciencia.vertx.verticles.MenuDatabaseVerticle");
        vertx.deployVerticle("com.conciencia.vertx.verticles.CustomersDatabaseVerticle");
    }
    
    /**
     * Método que define los codecs para enviar objetos por eventbus.
     */
    public static void registerCodecs(){
        vertx.eventBus().registerDefaultCodec(Item.class, new MenuItemCodec());
        vertx.eventBus().registerDefaultCodec(Menu.class, new MenuCodec());
        vertx.eventBus().registerDefaultCodec(Customer.class, new CustomerCodec());
    }
}
