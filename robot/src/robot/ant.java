/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 *
 * @author fix
 */
public class ant {
    vertex current;
    ArrayList<vertex> passed = new ArrayList<vertex>();
    ant(vertex v) {
        current = v;
    }
    ant(vertex v, double a, double b) {
        current = v;
    }
    edge chooseEdge(ArrayList<edge> edges,vertex exit) {
        
        double alph=0.5;
        double bet=0.5;
        double P=0.0; // вероятность
        double Pnumer=0.0;
        double Pdenom=0.0;
        ArrayList<Double> prob = new ArrayList<Double>();
        ArrayList<edge> possibleEdges = new ArrayList<edge>();
        
        for (edge e:edges) {
             if ((e.start.equals(current) && !passed.contains(e.end)) || (e.end.equals(current)
                    && !passed.contains(e.start))) {
                 possibleEdges.add(e);
             }
        }
        if (possibleEdges.size()==1 && (possibleEdges.get(0).start.equals(exit) || possibleEdges.get(0).end.equals(exit)))
            return possibleEdges.get(0);
        for (edge e:possibleEdges) {
             if ((!e.end.equals(exit)) && (!e.start.equals(exit))) {
                 Pdenom += Math.pow(1.0/e.lenght, alph) * Math.pow(e.pheromon,bet);
                 
             }              
        }
        for (edge e:possibleEdges) {
             if ((!e.end.equals(exit)) && (!e.start.equals(exit))) {
                 Pnumer = Math.pow(1.0/e.lenght, alph) * Math.pow(e.pheromon,bet);
                e.chance=Pnumer/Pdenom;
                prob.add(Pnumer/Pdenom);
                 
             }              
        }
        //        for (edge e:edges) {
//            if ((e.start.x==current.x && e.start.y==current.y && !passed.contains(e.end)) || (e.end.x==current.x && e.end.y==current.y
//                    && !passed.contains(e.start))) {
//                Pdenom += Math.pow(1.0/e.lenght, alph) * Math.pow(e.pheromon,bet);
//                //System.out.println(e.start.x+ "-" +e.start.y + " to " +e.end.x+ "-" +e.end.y);
//            }  
//        }
//        
//        for (edge e:edges) {
//            if ((e.start.x==current.x && e.start.y==current.y && !passed.contains(e.end)) || (e.end.x==current.x && e.end.y==current.y
//                    && !passed.contains(e.start))) {
//                Pnumer = Math.pow(1.0/e.lenght, alph) * Math.pow(e.pheromon,bet);
//                e.chance=Pnumer/Pdenom;
//                prob.add(Pnumer/Pdenom);
//                
//            }
//        }
        Collections.sort(prob);
        Double r = Math.random();
        Double t = 0.0; //верхняя граница
        double s = 0.0; //нижняя граница
        double chan = 0.0;
        for (Double d:prob) {
            //t=d;
            //System.out.println(d);
            t+=d;
            if (r<=t && r>=s) {
                //System.out.println(s + "-" + t);
                chan = d;
            }
            s=t;
        }
        for (edge e:possibleEdges) {
            if (e.chance == chan) {
               //System.out.println(e.start.x+ "-" +e.start.y + " to " +e.end.x+ "-" +e.end.y);
                
                return e;
            }
        }
        return null;
    }
    edge move(ArrayList<edge> edges, vertex end) {
        //passed.add(current);
        edge movedOn = chooseEdge(edges,end);
        if (movedOn==null)
             return null;
        if (current.equals(movedOn.start)) {
            current = movedOn.end;
            //passed.add(movedOn.start);
            //System.out.println(movedOn.end.x + "-" + movedOn.end.y);
        }
        else {
            current = movedOn.start;
            //passed.add(movedOn.end);
            //System.out.println(movedOn.start.x + "-" + movedOn.start.y);
        }
        return movedOn;
        //passed.add(movedOn);
        //System.out.println("MOVED ON: " +movedOn.start.x+ "-" +movedOn.start.y + " to " +movedOn.end.x+ "-" +movedOn.end.y);
        
    }
    
}
