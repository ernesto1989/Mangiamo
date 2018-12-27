package com.conciencia.vertx.verticles;

import com.conciencia.db.DatabaseUtilities;
import com.conciencia.db.impl.SqliteUtilities;
import com.conciencia.pojos.Item;
import com.conciencia.pojos.Menu;
import com.conciencia.pojos.Seccion;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import java.util.ArrayList;
import java.util.List;

/**
 * Verticle que contiene el repositorio de menu.
 * 
 * 1.- Lee el menu de la base de datos.
 * 2.- Carga los elementos y las secciones de menu en el objeto Menu.
 * 3.- La deja en memoria durante el uso de la aplicación.
 * 
 * @author Ernesto Cantu
 */
public class MenuDatabaseVerticle extends AbstractVerticle{
    
    private DatabaseUtilities dbConn;
    private final String SEARCH_ALL = "Select id,seccion,nombre,precio_unitario,es_orden,cantidad_orden,imprimir from menu order by seccion asc";
    private final String GET_SECTIONS = "Select distinct seccion from menu order by seccion asc";
    private final String GET_RELATED_ITEMS = "Select id,seccion,nombre,0.0 as precio_unitario,es_orden,cantidad_orden,imprimir from menu "
                                           + "where id in(Select item_relacionado from items_relacionados where item_ordenado = ?)";
    private Menu menu;
    
   /**
    * Método que inicializa el Menu desde la base de datos.
    * 1.- Lee el menu de la base de datos
    * 2.- Lee las secciones definidas en la base de datos
    * 3.- Arma el objeto menu por secciones e items.
    */
    private void initMenu(){
        menu = new Menu();
        List<Seccion> sections = dbConn.executeQueryNoParams(GET_SECTIONS, Seccion.class);
        List<Item> items = dbConn.executeQueryNoParams(SEARCH_ALL, Item.class);
        List<Item> sectionItems;
        List <Item> relacionados;
        int i = 0;
        for(Seccion s: sections){
            sectionItems = new ArrayList<>();
            s.setItems(sectionItems);
            menu.add(s);
            for(int j = i; j<items.size(); j++){
                if(items.get(j).getSeccion().equals(s.getNombre())){
                    if(s.getNombre() == "Tacos")
                        System.out.println("hola");
                    relacionados = dbConn.executeQueryWithParams(GET_RELATED_ITEMS, Item.class, items.get(j).getId());
                    items.get(j).setRelacionados(relacionados);
                    s.getItems().add(items.get(j));
                }else{
                    i = j;
                    break;
                }
            }
        } 
    }

    /**
     * Método ejecutado al arranque del verticle.
     * 1.- Inicializa el objeto de conección a la base de datos.
     * 2.- Ejecuta la carga inicial del menu
     * 3.- Define el handler del event bus para solicitud de menu.
     * @param startFuture
     * @throws Exception 
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        dbConn = new SqliteUtilities("db/MangiamoDB.db");
        vertx.executeBlocking(execution->{
            try{
                initMenu();
            }catch(Exception e){
                e.toString();
                execution.fail(e.toString());
            }
            execution.complete();
        }, executionResult->{
            if(executionResult.failed()){
                System.out.println("Error al cargar el menu");
                System.exit(1);
            }
        });
        vertx.eventBus().consumer("get_menu", msg-> msg.reply(menu));
    }
    
    @Override
    public void stop() throws Exception {
        System.out.println("Menu Database  Verticle undeploy");
    }
}
