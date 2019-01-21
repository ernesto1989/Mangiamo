package com.conciencia.utilities;

import com.conciencia.pojos.ItemOrdenado;
import com.conciencia.pojos.Orden;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ernesto Cantu
 */
public class PrintableClass implements Printable {
    
    int x = 10, y = 10;
    public List<String> printingString = new ArrayList<>();

    public PrintableClass(Orden o) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        printingString.add(sdf.format(new Date()));
        printingString.add("Orden para: " + o.toString());
        printingString.add("Descripcion*********Cantidad***********Total");
        BigDecimal total = BigDecimal.ZERO;
        for(ItemOrdenado item: o.getOrderedItems()){
            if(item.getServido()){
                printingString.add(item.print());
            }
            total = total.add(item.getTotal());
        }
        //printingString.add("Total:" + total.toString());
    }    
    
    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        // We have only one page, and 'page'
        // is zero-based
        if (page > 0) {
            return NO_SUCH_PAGE;
        }

        // User (0,0) is typically outside the
        // imageable area, so we must translate
        // by the X and Y values in the PageFormat
        // to avoid clipping.
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        // Now we perform our rendering
        for(String s:printingString){
            g.drawString(s, x, y);
            y = y+15;
        }   
        

        // tell the caller that this page is part
        // of the printed document
        return PAGE_EXISTS;
    }

}
