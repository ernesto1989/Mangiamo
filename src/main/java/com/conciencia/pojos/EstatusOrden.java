package com.conciencia.pojos;

/**
 * ENUM que define los estados de una orden.
 * 
 * COCINA: La orden se encuentra en cocina.
 * SERVIDA: La orden ya fue entregada en mesa o al repartidor para su entrega.
 *          Las órdenes para llevar no son entregadas.
 * ENVIADA : La orden salió del restaurante al domicilio
 * CERRADA: La orden ya fue cobrada por el cajero. Ya no se puede editar
 * 
 * @author Ernesto Cantu
 */
public enum EstatusOrden {
    COCINA,SERVIDA,ENVIADA,CERRADA;
    
    public static EstatusOrden getStatus(String type){
        if(type.equals("COCINA"))
            return COCINA;
        if(type.equals("SERVIDA"))
            return SERVIDA;
        if(type.equals("ENVIADA"))
            return ENVIADA;
        if(type.equals("CERRADA"))
            return CERRADA;
        return null;
    }
}   
