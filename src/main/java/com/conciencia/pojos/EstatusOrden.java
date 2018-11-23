package com.conciencia.pojos;

/**
 * ENUM que define los estados de una orden.
 * 
 * ESPERA: La orden fue tomada, pero aun no es atendida por cocina.
 * COCINA: La orden se está preparando.
 * ENTREGADA: La orden ya fue entregada en mesa o al repartidor para su entrega.
 *            Las órdenes para llevar no son entregadas.
 * CERRADA: La orden ya fue cobrada por el cajero. Ya no se puede editar
 * 
 * @author Ernesto Cantu
 */
public enum EstatusOrden {
    ESPERA,COCINA,ENTREGADA,CERRADA;
    
    public static EstatusOrden getStatus(String type){
        if(type.equals("ESPERA"))
            return ESPERA;
        if(type.equals("COCINA"))
            return COCINA;
        if(type.equals("ENTREGADA"))
            return ENTREGADA;
        if(type.equals("CERRADA"))
            return CERRADA;
        return null;
    }
}   
