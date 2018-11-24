package com.conciencia.datastructures.element;

import com.conciencia.datastructures.element.Element;

/**
 * Clase que representa a un elemento genérico que pertenece una estructura de datos
 * con referencia al siguiente elemento de la estructura.Extiende a la clase Element, 
 * envolviendo a un elemento de la clase object
 * 
 * @author Ernesto Cantu
 * 03/08/2018
 * ernesto.cantu1989@live.com
 * @param <E> Clase de objeto que envuelve
 */
public class Link <E extends Object> extends Element<E>{

    private Link next;
    
    /**
     * Constructor por default
     */
    public Link() {
    }

    /**
     * Método que permite obtener el siguiente elemento del stack.
     * @return Regresa el siguiente elemento del stack
     */
    public Link getNext() {
        return next;
    }

    /**
     * Método que permite definir el siguiente elemento a este elemento del stack
     * @param next El siguiente elemento a este elemento del stack
     */
    public void setNext(Link next) {
        this.next = next;
    }
}
