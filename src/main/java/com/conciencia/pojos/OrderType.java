package com.conciencia.pojos;

/**
 * Define los tipos de ordenes que existen
 * @author usuario
 */
public enum OrderType {
    
    MESA,LLEVAR,DOMICILIO;
    
    public static OrderType getType(String type){
        if(type.equals("MESA"))
            return MESA;
        if(type.equals("LLEVAR"))
            return LLEVAR;
        if(type.equals("DOMICILIO"))
            return DOMICILIO;
        return null;
    }
}
