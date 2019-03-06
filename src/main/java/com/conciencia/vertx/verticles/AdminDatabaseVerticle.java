package com.conciencia.vertx.verticles;

import com.conciencia.controllers.AdminController;
import static com.conciencia.controllers.AdminController.COSTO_ENVIO;
import static com.conciencia.controllers.AdminController.MESAS;
import static com.conciencia.controllers.AdminController.MINUTOS_ESPERA;
import static com.conciencia.controllers.AdminController.MINUTOS_ESPERA_MAX;
import static com.conciencia.controllers.AdminController.CONF;
import com.conciencia.db.DatabaseUtilities;
import com.conciencia.db.impl.SqliteUtilities;
import com.conciencia.pojos.Config;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import java.math.BigDecimal;


/**
 * Vérticle que inicializa y persiste los parámetros de configuración
 * 
 * @author Ernesto Cantu
 */
public class AdminDatabaseVerticle extends AbstractVerticle{
    
    private DatabaseUtilities dbConn;
    private final String QUERY = "SELECT id,parametro,valor FROM configuracion ORDER BY id";
    private final String SAVE_QUERY = "UPDATE configuracion set valor = ? where id = ?";
    
    /**
     * Método sobreescrito del verticle.
     * 
     * 1.- Se crea el objeto de conexión a bd
     * 2.- Sse crea el consumidor de mensajes para buscar clientes
     * 3.- Sse crea el consumidor de mensajes para crear clientes
     * 
     * @param startFuture
     * @throws Exception 
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        dbConn = new SqliteUtilities(AdminController.DB_URL);
        vertx.eventBus().consumer("init_params", msg->{
            CONF = dbConn.executeQueryWithParams(QUERY, Config.class);
            MESAS = Integer.parseInt(CONF.get(0).getValor());
            COSTO_ENVIO = new BigDecimal(CONF.get(1).getValor());
            MINUTOS_ESPERA = Integer.parseInt(CONF.get(2).getValor());
            MINUTOS_ESPERA_MAX  = Integer.parseInt(CONF.get(3).getValor());
        });
        
        vertx.eventBus().consumer("persist_params", msg->{
            for(Config c:CONF)
                dbConn.executeUpdate(SAVE_QUERY,c.getValor(),c.getId());
        });
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Customers Verticle undeploy");
    }
    
    
}
