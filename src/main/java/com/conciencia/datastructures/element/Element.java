package com.conciencia.datastructures.element;

import java.util.Objects;

/**
 * Clase de utilería que envuelve a cualquier objeto.
 * 
 * @author Ernesto Cantu
 * 27/07/2018
 * ernesto.cantu1989@live.com
 * @param <E> Clase de objeto que envuelve
 */
public class Element <E extends Object> {
    /**
     * Objeto a ser encapsulado
     */
    private E entity;

    /**
     * constructor genérico
     */
    public Element() {
    }

    /**
     * Constructor con el elemento a envolver.
     * @param entity elemento a envolver
     */
    public Element(E entity) {
        this.entity = entity;
    }    
    
    /* Métodos de acceso */
    
    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    /**
     * toString de la entidad
     * @return 
     */    
    @Override
    public String toString() {
        return entity.toString();
    }

    /**
     * Recibe un objeto y lo compara con el método equals del ambos objetos
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Element<?> other = (Element<?>) obj;
        if (!Objects.equals(this.entity, other.entity)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.entity);
        return hash;
    }
}
