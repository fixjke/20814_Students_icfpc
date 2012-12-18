/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;
import java.util.ArrayList;

/**
 *
 * @author fix
 */
public class vertex {
    int x;
    int y;
    boolean checked;
    boolean passed;
    vertex(int xx, int yy ) {
        x=xx;
        y=yy;
        checked = false;
    }
    vertex(int xx, int yy, ArrayList<edge> e) {
        x=xx;
        y=yy;
        edges=e;
        checked=false;
    }
    
    ArrayList<edge> edges = new ArrayList<edge>(); //список прилегающих граней
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        //if (!(obj instanceof vertex))
        //    return false;
         vertex tmp = (vertex)obj;
         if (tmp.x==x && tmp.y==y) {
             return true; 
         }
         else return false;
     }
    
    
}
