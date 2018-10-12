package com.conciencia.vertx.verticles;

import com.conciencia.db.DatabaseUtilities;
import com.conciencia.db.impl.SqliteUtilities;
import com.conciencia.pojos.Customer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;


/**
 *
 * @author Ernesto Cantu
 */
public class CustomersDatabaseVerticle extends AbstractVerticle{
    
    private DatabaseUtilities dbConn;
    private final String SEARCH_BY_PHONE = "Select id,nombre,telefono,direccion from customers where telefono = ?";
    private final String INSERT = "Insert into Customers (nombre,telefono,direccion) values(?,?,?)";
    
    
    private Customer getCustomer(String phone){
        Customer c = (Customer)(dbConn.executeQueryWithParams(SEARCH_BY_PHONE, Customer.class, phone)).get(0);
        return c;
    }
    
    private Customer insertCustomer(Customer c){
        Integer id = (dbConn.executeInsert(INSERT, c.getNombre(),
                                c.getTelefono(),
                                    c.getDireccion())).intValue();
        
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
}
