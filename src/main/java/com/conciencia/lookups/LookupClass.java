package com.conciencia.lookups;

import com.conciencia.pojos.Orden;

/**
 * Clase que funge como LookUp API para compartir objetos que no pueden ser
 * asíncronos en la carga de pantallas.
 * 
 * @author Ernesto Cantu
 */
public class LookupClass {
    
    public static Orden current;
    public static String telefono;
}