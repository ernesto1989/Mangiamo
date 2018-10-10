package com.conciencia.pojos;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

/**
 *
 * @author usuario
 */
public class Ticket implements Printable {
    
    private String ticket;

    public Ticket(String ticket) {
        this.ticket = ticket;
    }
    
    
    
    public int print (Graphics g, PageFormat f, int pageIndex) {
        if (pageIndex == 0) 
        {
            // Imprime "Hola mundo" en la primera pagina, en la posicion 100,100
            g.drawString(ticket, 100,100);
            return PAGE_EXISTS;
        }
        else
            return NO_SUCH_PAGE;
   }
}
