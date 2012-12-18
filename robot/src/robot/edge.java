/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;
import java.util.Comparator;
import java.util.ArrayList;

/**
 *
 * @author fix
 */
public class edge {
    vertex start;
    vertex end;
    double pheromon;
    int lenght;
    double chance;
    ArrayList<Point> path = new ArrayList<>();
    edge(vertex s, vertex e) {
        start=s;
        end=e;
        lenght=0;
        pheromon = 0.001;
        chance=0;
    }
    void updatePheromon(double p) {
        pheromon = p;
    }
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof edge))
            return false;
         edge tmp = (edge)obj;
         if (tmp.start.equals(start) && tmp.end.equals(end)) {
             return true; 
         }
         if (tmp.start.equals(end) && tmp.end.equals(start) ) {
             return true;
         }
         else return false;
     }
    
    
}

