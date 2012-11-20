package robot;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fix
 */
public class Point {
    int x;
    int y;
    int g;
    int f;
    int h;
    Point came_from;
    Point(int xx, int yy) {
        x=xx;
        y=yy;
        g=0;
        f=0;
        h=0;
    }
    Point (int xx,int yy, int gg,int hh, int ff, Point parent) {
        x=xx;
        y=yy;
        g=gg;
        f=ff;
        h=hh;
        came_from=parent;
    }
     @Override
    public boolean equals(Object obj) {
         Point tmp = (Point)obj;
         if (tmp.x==x && tmp.y==y) {
             return true; 
         }
         else return false;
     }
    
}
