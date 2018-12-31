package com.conciencia.pojos;

/**
 * ENUM que define los estados de una orden.
 * 
 * CANCELADA: La orden fue cancelada por el cliente.
 * COCINA: La orden se encuentra en cocina.
 * ENTREGADA: La orden ya fue entregada en mesa o al repartidor para su entrega.
 *            Las órdenes para llevar no son entregadas.
 * CERRADA: La orden ya fue cobrada por el cajero. Ya no se puede editar
 * 
 * @author Ernesto Cantu
 */
public enum EstatusOrden {
    CANCELADA,COCINA,ENTREGADA,CERRADA;
    
    public static EstatusOrden getStatus(String type){
        if(type.equals("CANCELADA"))
            return CANCELADA;
        if(type.equals("COCINA"))
            return COCINA;
        if(type.equals("ENTREGADA"))
            return ENTREGADA;
        if(type.equals("CERRADA"))
            return CERRADA;
        return null;
    }
}   
