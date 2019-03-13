package com.conciencia.pojos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.conciencia.db.ExpectedResult;

/**
 * Seccion de menu con sus items.
 * @author usuario
 */
public class Seccion implements TreeContainer,ExpectedResult {
    
    private String nombre;
    private List<Item> items;

    public Seccion() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String section) {
        this.nombre = section;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
    
    @Override
    public void mapResult(ResultSet rs) {
        try{
            setNombre(rs.getString("seccion"));
        }catch(SQLException ex){
            //handle exceptions...
        }
    }
}
