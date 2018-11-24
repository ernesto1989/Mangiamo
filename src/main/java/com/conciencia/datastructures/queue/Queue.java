package com.conciencia.datastructures.queue;

import com.conciencia.datastructures.element.EmptyDataStructureException;
import com.conciencia.datastructures.element.Link;

/**
 * Clase que representa una cola
 * 
 * @author Ernesto Cantu
 * 03/08/2018
 * ernesto.cantu1989@live.com
 * @param <E> Clase de objeto que se almacena en la cola
 */
public class Queue<E extends Object> {
    
    /* elemento que está al frente de la cola*/
    private Link<E> peek;
    
    /* elemento que está al final de la cola*/
    private Link<E> rear;
    
    /* número de elementos que tiene la cosa*/
    private Integer size;

    public Queue() {
        this.size = 0;
    }
    
    /**
     * Método que permite saber si una cola está vacía.
     * @return True si existe un elemento en la cima del stack. False si no.
     */
    public boolean isEmpty(){
        return (peek == null);
    }
    
    /**
     * Método que regresa el tamaño de la cola
     * @return el tamaño de la cola
     */
    public Integer size(){
        return this.size;
    }
    
    /**
     * Método que inseta un elemento al inicio de la cola
     * @param element 
     */
    public void insert(E element){
        Link<E> e = new Link<>();
        e.setEntity(element);
        if(!isEmpty()){
            this.rear.setNext(e);
            this.rear = e;
        }else{
            this.peek = e;
            this.rear = e;
            this.peek.setNext(this.rear);
        }   
        this.size++;
    }
    
    /**
     * Método que regresa al primer elemento de la cola
     * @return el primer elemento de la cola
     */
    public E getPeek(){
        if(isEmpty())
            throw new EmptyDataStructureException();
        return peek.getEntity();
    }
    
    /**
     * método que permite eliminar el primer elemento de la cola
     * @return el elemento eliminado
     */
    public E remove(){
        E removedElement = this.peek.getEntity();
        this.peek = this.peek.getNext();
        this.size--;
        return removedElement;
    }
}
