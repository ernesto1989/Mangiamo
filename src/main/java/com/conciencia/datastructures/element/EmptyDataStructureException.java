package com.conciencia.datastructures.element;

/**
 * Excepción que permite identificar que un stack está vacio.
 * 
 * @author Ernesto Cantu
 * 03/08/2018
 * ernesto.cantu1989@live.com
 */
public class EmptyDataStructureException extends RuntimeException{

    public EmptyDataStructureException() {
        super("Stack is empty");
    }
    
}
