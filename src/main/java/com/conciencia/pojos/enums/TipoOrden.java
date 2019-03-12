package com.conciencia.pojos.enums;

/**
 * Define los tipos de ordenes que existen
 * @author usuario
 */
public enum TipoOrden {
    
    MESA,LLEVAR,DOMICILIO;
    
    public static TipoOrden getTipo(String type){
        if(type.equals("MESA"))
            return MESA;
        if(type.equals("LLEVAR"))
            return LLEVAR;
        if(type.equals("DOMICILIO"))
            return DOMICILIO;
        return null;
    }
}
