package com.conciencia.utilities;

import com.conciencia.pojos.Orden;
import com.conciencia.pojos.Seccion;
import java.util.List;

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
    public static List<Seccion> menu;
}
