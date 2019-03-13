package com.conciencia.repositories;

import com.conciencia.controllers.AdminController;
import com.conciencia.db.DatabaseUtilities;
import com.conciencia.db.impl.SqliteUtilities;
import static com.conciencia.utilities.LookupClass.menu;
import com.conciencia.pojos.Item;
import com.conciencia.pojos.Seccion;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de apoyo que crea el menu al arranque de Mangiamo.
 *
 * @author Ernesto Cantu
 * 12 marzo 2019
 */
public class MenuRepository {

    private static DatabaseUtilities dbConn = new SqliteUtilities(AdminController.DB_URL);
    private static final String SEARCH_ALL = "Select id,seccion,nombre,precio_unitario,es_orden,cantidad_orden from menu order by seccion asc";
    private static final String GET_SECTIONS = "Select distinct seccion from menu order by seccion asc";
    private static final String GET_RELATED_ITEMS = "Select id,seccion,nombre,0.0 as precio_unitario,es_orden,cantidad_orden from menu "
            + "where id in(Select item_relacionado from items_relacionados where item_ordenado = ?)";

    /**
     * Método que inicializa el Menu desde la base de datos. 
     * 1.- Lee las distintas secciones de la base de datos
     * 2.- Lee los distintos items de la base de datos
     * 3.- Asigna los elementos de menu a cada sección y lee sus elementos 
     *     relacionados.
     * 
     */
    public static void initMenu() {
        menu = new ArrayList<>();
        List<Seccion> sections = dbConn.executeQueryNoParams(GET_SECTIONS, Seccion.class);
        List<Item> items = dbConn.executeQueryNoParams(SEARCH_ALL, Item.class);
        List<Item> sectionItems;
        List<Item> relacionados;
        int i = 0;
        for (Seccion s : sections) {
            sectionItems = new ArrayList<>();
            s.setItems(sectionItems);
            menu.add(s);
            for (int j = i; j < items.size(); j++) {
                if (items.get(j).getSeccion().equals(s.getNombre())) {
                    relacionados = dbConn.executeQueryWithParams(GET_RELATED_ITEMS, 
                            Item.class, items.get(j).getId());
                    items.get(j).setRelacionados(relacionados);
                    s.getItems().add(items.get(j));
                } else {
                    i = j;
                    break;
                }
            }
        }
    }
}
