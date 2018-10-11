package com.conciencia.vertx.verticles;

import com.conciencia.db.DatabaseUtilities;
import com.conciencia.db.impl.SqliteUtilities;
import com.conciencia.pojos.Customer;
import com.conciencia.pojos.Item;
import com.conciencia.pojos.Menu;
import com.conciencia.pojos.Section;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ernesto Cantu
 */
public class CustomersDatabaseVerticle extends AbstractVerticle{
    
    private DatabaseUtilities dbConn;
    private final String SEARCH_BY_PHONE = "Select id,nombre,telefono,direccion from customers where telefono = ?";
    
    
    private Customer getCustomer(String phone){
        Customer c = (Customer)(dbConn.executeQueryWithParams(SEARCH_BY_PHONE, Customer.class, phone)).get(0);
        return c;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        dbConn = new SqliteUtilities("db/MangiamoDB.db");
        vertx.eventBus().consumer("get_customer", msg->{
            // <editor-fold defaultstate="colapsed" desc="handler">
            String phone = (String) msg.body();
            vertx.executeBlocking(execute->{
                //el executeBlocking funciona via 2 handlers. El primero se encarga de ejecutar el codigo e indicar el resultado. 
                execute.complete(getCustomer(phone));
            }, executionResult->{
                //este segundo handler recibe el resultado de la ejecución
                if(executionResult.succeeded())
                    msg.reply(executionResult.result());
                else
                    msg.fail(0, "Could not get list of books");
            });   
            //</editor-fold>
        });
    }
    
    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        //persist database....
        System.out.println("bye");
    }
}
