package com.conciencia.vertx.verticles;

import com.conciencia.controllers.AdminController;
import com.conciencia.db.DatabaseUtilities;
import com.conciencia.db.impl.SqliteUtilities;
import com.conciencia.pojos.Usuario;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import java.util.List;


/**
 * Vérticle que contiene todas las operaciones permitidas con el repositorio 
 * de clientes.
 * 
 * @author Ernesto Cantu
 */
public class UsersDatabaseVerticle extends AbstractVerticle{
    
    private DatabaseUtilities dbConn;
    private final String SEARCH_BY_USERNAME = "Select id,nombre,user,password from usuarios where user = ? and password = ?";
    
    /**
     * Método que busca a un usuario por su usuario y password
     *
     * @return cliente encontrado
     */
    private Usuario getUser(String userName, String password){
        if(userName != null && password != null){
            List<Usuario> result = 
            dbConn.executeQueryWithParams(SEARCH_BY_USERNAME, Usuario.class, userName,password);
            if(!result.isEmpty())
                return result.get(0);
        }
        return null;
    }

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
        vertx.eventBus().consumer("get_user", msg->{
            // <editor-fold defaultstate="colapsed" desc="handler">
            Usuario user = (Usuario) msg.body();
            Usuario u = getUser(user.getUser(),user.getPassword());
            if(u != null){
                msg.reply(u);
            }else{
                msg.fail(0, "No se encontró el usuario solicitado");
            }   
            //</editor-fold>
        });
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Users Verticle undeploy");
    }
    
    
}
