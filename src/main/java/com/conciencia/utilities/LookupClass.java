package com.conciencia.utilities;

import com.conciencia.pojos.Menu;
import com.conciencia.pojos.Orden;

/**
 * Clase que funge como LookUp API para compartir objetos que no pueden ser
 * asíncronos en la carga de pantallas.
 * 
 * @author Ernesto Cantu
 */
public class LookupClass {
    
    public static Orden current;
    public static Orden toBill;
    public static String telefono;
    public static Menu menu;
}
