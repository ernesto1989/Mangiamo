package com.conciencia.vertx.verticles;

import com.conciencia.db.DatabaseUtilities;
import com.conciencia.db.impl.SqliteUtilities;
import com.conciencia.pojos.Cliente;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import java.util.List;


/**
 * Vérticle que contiene todas las operaciones permitidas con el repositorio 
 * de clientes.
 * 
 * @author Ernesto Cantu
 */
public class CustomersDatabaseVerticle extends AbstractVerticle{
    
    private DatabaseUtilities dbConn;
    private final String SEARCH_BY_PHONE = "Select id,nombre,telefono,calle,numero,colonia,ecalle1,ecalle2 from Clientes where telefono = ?";
    private final String SAVE_CUSTOMER = "Insert into Clientes (nombre,telefono,calle,numero,colonia,ecalle1,ecalle2) values(?,?,?,?,?,?,?)";
    
    /**
     * Método que busca a un cliente por su teléfono
     * @param phone telefono del cliente
     * @return cliente encontrado
     */
    private Cliente getCustomer(String phone){
        if(phone != null){
            List<Cliente> result = dbConn.
                executeQueryWithParams(SEARCH_BY_PHONE, Cliente.class, phone);
            if(!result.isEmpty())
                return result.get(0);
        }
        return null;
    }
    
    /**
     * Método eu permite insertar en la bd un nuevo cliente.
     * @param c cliente a insertar.
     * @return id si el cliente fue generado,-1 si el cliente existía, 0 si fue
     * error interno de insersión
     */
    private int insertCustomer(Cliente c){
        Cliente clienteExistente =  getCustomer(c.getTelefono());
        if(clienteExistente != null)
            return -1;
        if(c.hayDatosFaltantes())
            return -2;
        Integer id = (dbConn.executeInsert(SAVE_CUSTOMER, c.getNombre(),
                                c.getTelefono(),
                                    c.getCalle(),c.getNumero(),c.getColonia(),c.geteCalle1(),c.geteCalle2())).intValue();
        if(id == null)
            return 0;
        c.setId(id);
        return id;
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
        dbConn = new SqliteUtilities("db/MangiamoDB.db");
        vertx.eventBus().consumer("get_customer", msg->{
            // <editor-fold defaultstate="colapsed" desc="handler">
            String phone = (String) msg.body();
            Cliente c = getCustomer(phone);
            if(c != null){
                msg.reply(c);
            }else{
                msg.fail(0, "No se encontró el cliente solicitado");
            }   
            //</editor-fold>
        });
        
        vertx.eventBus().consumer("save_customer",msg->{
            // <editor-fold defaultstate="colapsed" desc="handler">
            Cliente customer = (Cliente)msg.body();
            int result = insertCustomer(customer);
            if(customer.getId() != null && customer.getId() == result){
                msg.reply(result);
            }else{
                if(result == -1){
                    msg.fail(0, "Teléfono existente");
                }if(result == -2){
                    msg.fail(0, "Hay datos faltantes del cliente");
                }else{
                    msg.fail(0, "Error en registro del cliente. Contacte al administrador");
                }
            }
            //</editor-fold>
        });
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Customers Verticle undeploy");
    }
    
    
}
