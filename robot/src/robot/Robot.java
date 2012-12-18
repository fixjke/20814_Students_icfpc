/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.ListIterator;
import java.util.Comparator;

/**
 *
 * @author fix
 */
//!!!!добавить проверки на выход за  границу поля при ходах робота
//!!!!поправить тот факт, что U ходит вправо
public class Robot {

    /**
     * @param args the command line arguments
     */
    cell[][] field;
    int robotX;
    int robotY;
    int height;
    int width;
    int score;
    int exitX;
    int exitY;
    int lambds; //лямбд на карте
    int getLambds; //собрано лямбд
    int currentCost;
    //ArrayList<Point> path;
    ArrayList<Point> lmb = new ArrayList<Point>();
    Robot(char[][] map, int w, int h){
        height=h;
        width=w;
        currentCost=0;
        score=0;
        field = new cell[width][height];
        for (int i=0; i<height;i++){
            for (int j=0; j<width; j++) {
                if (map[j][i]=='*')  field[j][i]=cell.STONE; 
                else if (map[j][i]=='#') field[j][i]=cell.WALL;
                else if (map[j][i]=='R') {
                    field[j][i]=cell.ROBOT;
                    robotX=j;
                    robotY=i;
                }
                else if (map[j][i]=='.') field[j][i]=cell.GROUND;
                else if (map[j][i]=='\\') {
                    field[j][i]=cell.LAMBDA;
                    lambds++;
                    
                }
                else if (map[j][i]=='L') {
                    field[j][i]=cell.EXIT;
                    exitX=j;
                    exitY=i;
                }
                else if (map[j][i]=='O') field[j][i]=cell.OEXIT;
                else if (map[j][i]==' ') field[j][i]=cell.EMPTY;
            }
        }
    }
    boolean A() {
        gameOver(1);
        return true;
    }
    boolean W( boolean shouldCount) {
        simulateField();
        if (shouldCount) score--;
        return true;
    }
    boolean L(boolean shouldCount) {
        if (field[robotX-1][robotY]==cell.OEXIT) {
            field[robotX][robotY]=cell.EMPTY;
            robotX--;
            field[robotX][robotY]=cell.ROBOT;
            if (shouldCount) score--;
            gameOver(2);
            return true;
        }
        else if (field[robotX-1][robotY]==cell.STONE && field[robotX-2][robotY]==cell.EMPTY ) {
            field[robotX][robotY]=cell.EMPTY;
            robotX--;
            field[robotX][robotY]=cell.ROBOT;
            field[robotX-1][robotY]=cell.STONE;
            if (shouldCount) score--;
            simulateField();
            return true;
        }
        else if (field[robotX-1][robotY]==cell.EMPTY || field[robotX-1][robotY]==cell.GROUND) {
            field[robotX][robotY]=cell.EMPTY;
            robotX--;
            field[robotX][robotY]=cell.ROBOT;   
            if (shouldCount) score--;
            simulateField();
            return true;
        }
        else if (field[robotX-1][robotY]==cell.LAMBDA) {
            field[robotX][robotY]=cell.EMPTY;
            robotX--;
            field[robotX][robotY]=cell.ROBOT;
            if (shouldCount) {
                score+=25;
                getLambds++;
                lambds--;
            }
            simulateField();
            return true;
        }
        //else {
         //   System.out.println("turn L failed");
            return false;
        //}
    }
    boolean R(boolean shouldCount) {
        if (field[robotX+1][robotY]==cell.OEXIT) {
            field[robotX][robotY]=cell.EMPTY;
            robotX++;
            field[robotX][robotY]=cell.ROBOT;
            if (shouldCount) score--;
            gameOver(2);
            return true;
        }
        else if (field[robotX+1][robotY]==cell.STONE && (field[robotX+2][robotY]==cell.EMPTY )) {
            field[robotX][robotY]=cell.EMPTY;
            robotX++;
            field[robotX][robotY]=cell.ROBOT;
            field[robotX+1][robotY]=cell.STONE;
            if (shouldCount) score--;
            simulateField();
            return true;
        }
        else if (field[robotX+1][robotY]==cell.EMPTY || field[robotX+1][robotY]==cell.GROUND ) {
            field[robotX][robotY]=cell.EMPTY;
            robotX++;
            if (shouldCount) score--;
            field[robotX][robotY]=cell.ROBOT;
            simulateField();
            return true;
        }
       else if (field[robotX+1][robotY]==cell.LAMBDA) {
            field[robotX][robotY]=cell.EMPTY;
            robotX++;
            field[robotX][robotY]=cell.ROBOT;
            if (shouldCount){
                score+=25;
                getLambds++;
                lambds--;
            }
            simulateField();
            return true;
        }
//        else {
//            System.out.println("turn R failed");
            return false;
        
    }
    boolean U(boolean shouldCount) {
        if (field[robotX][robotY-1]==cell.OEXIT) {
            field[robotX][robotY]=cell.EMPTY;
            robotY--;
            if (shouldCount) score--;
            field[robotX][robotY]=cell.ROBOT;
            gameOver(2);
            return true;
        }
        else if (field[robotX][robotY-1]==cell.EMPTY || field[robotX][robotY-1]==cell.GROUND) {
            field[robotX][robotY]=cell.EMPTY;
            robotY--;
            if (shouldCount) score--;
            field[robotX][robotY]=cell.ROBOT;
            simulateField();
            return true;
        }
        else if (field[robotX][robotY-1]==cell.LAMBDA) {
            field[robotX][robotY]=cell.EMPTY;
            robotY--;
            field[robotX][robotY]=cell.ROBOT;
            if (shouldCount) {
                
            
                score+=25;
                lambds--;
                getLambds++;
            }
            simulateField();
            return true;
        }
         //else {
         //   System.out.println("turn U failed");
            return false;
        //}
    }
    boolean D(boolean shouldCount) {
        if (field[robotX][robotY+1]==cell.OEXIT) {
            field[robotX][robotY]=cell.EMPTY;
            robotY++;
            if (shouldCount) score--;
            field[robotX][robotY]=cell.ROBOT;  
            gameOver(2);
            return true;
        }
        else if (field[robotX][robotY+1]==cell.EMPTY || field[robotX][robotY+1]==cell.GROUND) {
            field[robotX][robotY]=cell.EMPTY;
            robotY++;
            if (shouldCount) score--;
            field[robotX][robotY]=cell.ROBOT;  
            simulateField();
            return true;
        }
        else if  (field[robotX][robotY+1]==cell.LAMBDA) {
            field[robotX][robotY]=cell.EMPTY;
            robotY++;
            field[robotX][robotY]=cell.ROBOT;   
            if (shouldCount) {
                score+=25;
                getLambds++;
                lambds--;
            }
            simulateField();
            return true;
        }
        //else {
        //    System.out.println("turn D failed");
            return false;
        //}
    }
    void print(){
        StringBuilder sb = new StringBuilder();
        System.out.println(" 01234567");
        for (int i=0; i<height;i++){
            //sb.append(i);
            for (int j=0; j<width; j++) {
                if (field[j][i]==cell.STONE)  sb.append('*');
                else if (field[j][i]==cell.WALL) sb.append('#');
                else if (field[j][i]==cell.ROBOT) sb.append('R');         
                else if (field[j][i]==cell.LAMBDA) sb.append('\\');
                else if (field[j][i]==cell.EXIT) sb.append('L');
                else if (field[j][i]==cell.OEXIT) sb.append('O');
                else if (field[j][i]==cell.EMPTY) sb.append(' ');
                else if (field[j][i]==cell.GROUND) sb.append('.');
            }
            sb.append('\n');
        }
        System.out.println(sb);
    }
    //функция конца игры, вызывается при смерти робота, либо при достижении выхода
    //result - тип конца игры
    //0 - смерть робота
    //1 - выполнение команды А
    //2 - вход в открытый выход
    void gameOver(int result) {
        if (result==0) System.out.println("LOSE");
        else if (result == 1) {
            score+=getLambds*25;
            System.out.println("WIN, POINTS:" + score);
        }
        else if (result == 2) {
            score+=getLambds*50;
            System.out.println("WIN, POINTS:" + score);
        }
                
    }
    //проверка на смерть робота
    boolean isAlive() {
        for (int i=0; i<height; i++) {
            for (int j=0; j<width; j++) {
                if (field[j][i]==cell.ROBOT) {
                    return true;
                }
            }
        }
        return false;
    }
    //безопасно ли идти в клетку
    boolean isSafeCell(int x, int y) {
        if (x<=0 || x>width-1 || y<=0 || y>height-1) {
            return false;
        }
        if (field[x][y]==cell.WALL) return false;
        cell[][] tmp = new cell[width][height];
        for (int i=0; i<height;i++){
            for (int j=0; j<width; j++) {
                tmp[j][i]=field[j][i];
            }
        }
        field[robotX][robotY]=cell.EMPTY;
        field[x][y]=cell.ROBOT;
        simulateField();
        if (field[x][y]!=cell.ROBOT) {
             for (int i=0; i<height;i++){
                for (int j=0; j<width; j++) {
                    field[j][i]=tmp[j][i];
                }
            }
            return false;
        }
        else  {
            for (int i=0; i<height;i++){
                for (int j=0; j<width; j++) {
                    field[j][i]=tmp[j][i];
                }
            }
            return true;
        }
        
        
        
    }
    boolean isSafePath(ArrayList<Point> path) {
        //COPY
        cell[][] tmp = new cell[width][height];
        for (int i=0; i<height;i++){
            for (int j=0; j<width; j++) {
                tmp[j][i]=field[j][i];
            }
        }
        
        move(path,false);
        if (robotX==exitX && robotY==exitY){
            //восстановление копии поля
            for (int i=0; i<height;i++) {
                for (int j=0; j<width; j++) {
                    field[j][i]=tmp[j][i];
                    if (field[j][i]==cell.ROBOT) {
                        robotX=j;
                        robotY=i;
                    }
                }
            }
            return true;
        }
        //проверяем, можем ли мы выйти из данной точки
        //boolean isSafe=false;
        ArrayList<Point> pth = new ArrayList<Point>();
        for (int i=0;i<height;i++) {
            for (int j=0; j<width;j++) {
                if ((field[j][i]==cell.LAMBDA || field[j][i]==cell.OEXIT)) {
                        //&& 
                        //!getPathAStar(new Point(robotX,robotY),new Point(j,i)).isEmpty()) {
                    //восстановление копии поля
                    pth = getPathAStar(new Point(robotX,robotY),new Point(j,i));
                    if (pth.size()>0) {
                        for (int g=0; g<height;g++) {
                            for (int h=0; h<width; h++) {
                                field[h][g]=tmp[h][g];
                                if (field[h][g]==cell.ROBOT) {
                                    robotX=h;
                                    robotY=g;
                                }
                             }
                        }
                        return true;
                    }
                    //return true;
                }
            }
        }
        for (int i=0; i<height;i++) {
            for (int j=0; j<width; j++) {
                field[j][i]=tmp[j][i];
                if (field[j][i]==cell.ROBOT) {
                    robotX=j;
                    robotY=i;
                }
            }
        }
        return false;
    }
    
    boolean simulateField() {
        cell[][] tmp_field = new cell[width][height];
        //cell[][] copy_field = field.clone();
        for (int i=height-1; i>=0; i--) {
            for (int j=0;j<width;j++) {
                simulateCell(j,i,tmp_field);
                //System.out.println(tmp_field[j][i]);
            }
        }
        for (int i=0; i<height;i++){
            for (int j=0; j<width; j++) {
                field[j][i]=tmp_field[j][i];
                
            }
        }
        return true;
        
    }
    void simulateCell(int j,int i,cell[][] temp) {
        if (field[j][i]==cell.STONE) {
                if (field[j][i+1]==cell.EMPTY) {
                    temp[j][i+1]=cell.STONE;
                    temp[j][i]=cell.EMPTY;  
                    if (field[j][i+2]==cell.ROBOT) {
                        temp[j][i+2]=cell.STONE;
                        temp[j][i+1]=cell.EMPTY;
                       
                    }
                   
                }
                else if (field[j][i+1]==cell.STONE && field[j+1][i]==cell.EMPTY && field[j+1][i+1]==cell.EMPTY ) {
                    temp[j+1][i+1]=cell.STONE;
                    temp[j][i]=cell.EMPTY;
                    if (field[j+1][i+2]==cell.ROBOT) {
                        temp[j+1][i+2]=cell.STONE;
                        temp[j+1][i+1]=cell.EMPTY;
                       
                    }
                    
                }
                else if (field[j][i+1]==cell.STONE && field[j-1][i]==cell.EMPTY && field[j-1][i+1]==cell.EMPTY ) {
                    temp[j-1][i+1]=cell.STONE;
                    temp[j][i]=cell.EMPTY;
                    if (field[j-1][i+2]==cell.ROBOT) {
                        temp[j-1][i+2]=cell.STONE;
                        temp[j-1][i+1]=cell.EMPTY;
                        
                    }
                  
                }
                else if (field[j][i+1]==cell.LAMBDA && field[j+1][i]==cell.EMPTY && field[j+1][i+1]==cell.EMPTY ) {
                    temp[j+1][i+1]=cell.STONE;
                    temp[j][i]=cell.EMPTY;
                    if (field[j][i+2]==cell.ROBOT) {
                        temp[j+1][i+2]=cell.STONE;
                        temp[j+1][i+1]=cell.EMPTY;
                       
                    }
            
                }
                else temp[j][i]=field[j][i];
        }
        else if (field[j][i]==cell.EXIT && lambds==0) {
            temp[j][i]=cell.OEXIT;
           
        }
        else temp[j][i]=field[j][i];
        //return temp;
     
    }
    boolean simulate(int i) {
        while (i>0) {
            if (!simulateField()) return false;
            i--;
        }
        return true;
    }
    void move(ArrayList<Point> path_map, boolean shouldCount) {
        //System.out.println(path_map.size());
        if (!shouldCount && path_map.isEmpty()) return;
        if (path_map.isEmpty() || path_map==null) {
            //System.out.println("FAIL");
            A();
            //gameOver(1);
            return;
        }
        while(path_map.size()!=1) {
            Point last = path_map.get(path_map.size()-1);
            Point prev = path_map.get(path_map.size()-2);
            if (prev.x-last.x == 0 && prev.y-last.y==1) {
                D(shouldCount);
                if (shouldCount) {
                    print();
                    //D(shouldCount);
                    System.out.println("D");
                }
                path_map.remove(last);
            }
            else if (prev.x-last.x==0 && prev.y-last.y==-1) {
                U(shouldCount);
                if (shouldCount) {
                    print();
                    System.out.println("U");
                }
                path_map.remove(last);
            }
            else if (prev.y-last.y==0 && prev.x-last.x==1){
                R(shouldCount);
                if (shouldCount) {
                    print();
                    System.out.println("R");
                }
                path_map.remove(last);
            }
            else if (prev.y-last.y==0 && prev.x-last.x==-1){
                L(shouldCount);
                if (shouldCount) {
                    print();
                    System.out.println("L");
                }
                path_map.remove(last);
            }
        }
        //System.out.println(path_map.size());
    }
    int heuristic_cost_estimate(Point start, Point goal) {
        return Math.abs(goal.x-start.x) + Math.abs(goal.y-start.y); //TBD
    }
    Point getBest(ArrayList<Point> list) { //выбрать вершину с лучшми f(x)
        Point p = new Point(0,0);
        p.f=1000; // REWRITE
        for (Point best:list) {
            if (best.f<p.f) {
                p=best;
            }
            //if (best.f==0) System.out.println("saSa");
        }
        //TBD
        return p;
    }
    ArrayList<Point> reconstruct_path(Point start, Point goal, ArrayList<Point> closedset) {
        //TBD
        
        ArrayList<Point> path_map = new ArrayList<Point>();
        if (closedset.isEmpty()) {
            //System.out.println("PATH BETWEET" + start.x + "-" +start.y +"AND" +goal.x + "-" +goal.y+ "NOT FOUND");
            return path_map;
        }
        Point p = closedset.get(closedset.size()-1);
        //Point p = closedset.
        //int i=0;
        while (p!=null) {
            path_map.add(p);
            p=p.came_from;
            //i++;
        } 
        return path_map;
    }
//    ArrayList<Point> get_neighbor_nodes(Point p,Point start,Point goal) {
//      
//        cell[][] tmp = new cell[width][height];
//        for (int i=0; i<height;i++){
//            for (int j=0; j<width; j++) {
//                tmp[j][i]=field[j][i];
//            }
//        }
//   
//        for (Point k:p.moved_from) {
//            field[k.x][k.y]=cell.EMPTY;
//        }
//        for (Point k:p.moved_to) {
//            field[k.x][k.y]=cell.STONE;
//        }
//        simulate(p.g);
//        ArrayList<Point> neighbors = new ArrayList<Point>();
//        if (((field[p.x][p.y]!=cell.STONE && field[p.x][p.y]!=cell.WALL) || p==start) && isSafeCell(p.x,p.y-1) &&
//                (field[p.x][p.y-1]==cell.EMPTY || field[p.x][p.y-1]==cell.LAMBDA 
//                || field[p.x][p.y-1]==cell.OEXIT || field[p.x][p.y-1]==cell.GROUND || field[p.x][p.y-1]==cell.EXIT    )) {
//            Point n = new Point (p.x,p.y-1);
//            n.g = p.g+1;
//            n.h = heuristic_cost_estimate(p,goal);
//            n.f = n.g+n.h;
//            n.came_from = p;
////            for (Point k:p.moved_from) {
////                n.moved_from.add(k);
////            }
////            for (Point k:p.moved_to) {
////                n.moved_to.add(k);
////            }
//            n.moved_from=p.moved_from;
//            n.moved_to=p.moved_to;
//            neighbors.add(n);
//        }
//        if (((field[p.x][p.y]!=cell.STONE && field[p.x][p.y]!=cell.WALL)|| p==start) && isSafeCell(p.x+1,p.y) &&
//                (field[p.x+1][p.y]==cell.EMPTY || field[p.x+1][p.y]==cell.LAMBDA ||
//                field[p.x+1][p.y]==cell.OEXIT || field[p.x+1][p.y]==cell.GROUND || field[p.x+1][p.y]==cell.EXIT   )) {
//            Point n = new Point (p.x+1,p.y);
//            n.g = p.g+1;
//            n.h = heuristic_cost_estimate(p,goal);
//            n.f = n.g+n.h;
//            n.came_from = p;
////            for (Point k:p.moved_from) {
////                n.moved_from.add(k);
////            }
////            for (Point k:p.moved_to) {
////                n.moved_to.add(k);
////            }
//            n.moved_from=p.moved_from;
//            n.moved_to=p.moved_to;
//            neighbors.add(n);
//        }
//        else if ( ((field[p.x][p.y]!=cell.STONE && field[p.x][p.y]!=cell.WALL)|| p==start) && isSafeCell(p.x+1,p.y) &&
//                field[p.x+1][p.y]==cell.STONE && field[p.x+2][p.y]==cell.EMPTY ) {
//            Point n = new Point(p.x+1,p.y);
//            n.g=p.g+1;
//            n.h = heuristic_cost_estimate(p,goal);
//            n.f = n.g+n.h;
//            n.came_from = p;
////            for (Point k:p.moved_from) {
////                n.moved_from.add(k);
////            }
////            for (Point k:p.moved_to) {
////                n.moved_to.add(k);
////            }
//            
//            n.moved_from.add(new Point(p.x+1,p.y));
//            n.moved_to.add(new Point(p.x+2,p.y));
//            neighbors.add(n);
//            
//        }
//        if (((field[p.x][p.y]!=cell.STONE && field[p.x][p.y]!=cell.WALL) || p==start) && isSafeCell(p.x,p.y+1)
//                && (field[p.x][p.y+1]==cell.EMPTY || field[p.x][p.y+1]==cell.LAMBDA || 
//                field[p.x][p.y+1]==cell.OEXIT || field[p.x][p.y+1]==cell.GROUND || field[p.x][p.y+1]==cell.EXIT )) {
//            Point n = new Point (p.x,p.y+1);
//            n.g = p.g+1;
//            n.h = heuristic_cost_estimate(p,goal);
//            n.f = n.g+n.h;
//             n.came_from = p;
////             for (Point k:p.moved_from) {
////                n.moved_from.add(k);
////            }
////            for (Point k:p.moved_to) {
////                n.moved_to.add(k);
////            }
//            n.moved_from=p.moved_from;
//            n.moved_to=p.moved_to;
//            neighbors.add(n);
//        }
//        if (((field[p.x][p.y]!=cell.STONE && field[p.x][p.y]!=cell.WALL) || p==start) && isSafeCell(p.x-1,p.y) &&
//                (field[p.x-1][p.y]==cell.EMPTY || field[p.x-1][p.y]==cell.LAMBDA || 
//                field[p.x-1][p.y]==cell.OEXIT || field[p.x-1][p.y]==cell.GROUND || field[p.x-1][p.y]==cell.EXIT  )) {
//            Point n = new Point (p.x-1,p.y);
//            n.g = p.g+1;
//            n.h = heuristic_cost_estimate(p,goal);
//            n.f = n.g+n.h;
//            n.came_from = p;
////            for (Point k:p.moved_from) {
////                n.moved_from.add(k);
////            }
////            for (Point k:p.moved_to) {
////                n.moved_to.add(k);
////            }
//            n.moved_from=p.moved_from;
//            n.moved_to=p.moved_to;
//            neighbors.add(n);
//        }
//        else if ( ((field[p.x][p.y]!=cell.STONE && field[p.x][p.y]!=cell.WALL)|| p==start) && isSafeCell(p.x-1,p.y) &&
//                field[p.x-1][p.y]==cell.STONE && field[p.x-2][p.y]==cell.EMPTY ) {
//            Point n = new Point(p.x-1,p.y);
//            n.g=p.g+1;
//            n.h = heuristic_cost_estimate(p,goal);
//            n.f = n.g+n.h;
//            n.came_from = p;
////            for (Point k:p.moved_from) {
////                n.moved_from.add(k);
////            }
////            for (Point k:p.moved_to) {
////                n.moved_to.add(k);
////            }
//            
//            n.moved_from.add(new Point(p.x-1,p.y));
//            n.moved_to.add(new Point(p.x-2,p.y));
//            neighbors.add(n);
//        }
//        //восстановление копии
//        for (int i=0; i<height;i++){
//            for (int j=0; j<width; j++) {
//                field[j][i]=tmp[j][i];
//            }
//        }
//        return neighbors;
//    }
    
    
    
    ArrayList<Point> get_neighboor_nodes(Point p, Point start, Point goal, ArrayList<Point> closedset) {
        cell[][] tmp = new cell[width][height];
        for (int i=0; i<height;i++){
            for (int j=0; j<width; j++) {
                tmp[j][i]=field[j][i];
            }
        }
        //System.out.println("p: " + robotX + "-" + robotY);
        ArrayList<Point> neighbors = new ArrayList<Point>();
        //передвигаем робота в проверяемую клетку:
//        ArrayList<Point> pathToPoint = new ArrayList<Point>();
//        Point k = p;
//        while (k!=null) {
//            pathToPoint.add(k);
//            k=k.came_from;
//            //i++;
//        } 
        //System.out.println(height);
        //System.out.println(width);
        //StringBuilder sb1 = new StringBuilder();
        if (!p.equals(start)) {
            ArrayList<Point> pathToPoint = reconstruct_path(start,p,closedset);

//            sb1.append("___");
//            sb1.append('\n');
//            sb1.append("P: ");
//            sb1.append(p.x);
//            sb1.append("-");
//            sb1.append(p.y);
//            sb1.append('\n');
//            sb1.append("Path: ");
//            for (Point das:pathToPoint) {
//
//                sb1.append(das.x);
//                sb1.append("-");
//                sb1.append(das.y);
//                sb1.append('\n');
//            }
//            sb1.append("___");
            //System.out.println(pathToPoint.size());
        
            move(pathToPoint,false);
        }
        //if (p.x!=robotX || p.y!=robotY) return neighbors;
        //System.out.println("R: " + robotX + "-" + robotY);
        //System.out.println("p: " + p.x + "-" + p.y);
        //StringBuilder sb = new StringBuilder();
        //sb.append("Current point: " + p.x + "-" + p.y);
        //sb.append('\n');
        //System.out.println(isSafeCell(-1,2));
        if (checkU()) {
            Point n = new Point(p.x,p.y-1);
            n.g = p.g+1;
            n.h = heuristic_cost_estimate(p,goal);
            n.f = n.g+n.h;
            n.came_from = p;
            neighbors.add(n);
        }
        if (checkR()) {
            Point n = new Point(p.x+1,p.y);
            n.g = p.g+1;
            n.h = heuristic_cost_estimate(p,goal);
            n.f = n.g+n.h;
            n.came_from = p;
            neighbors.add(n);
        }
        if (checkD()) {
            //System.out.println("D: " + checkD());
            //System.out.println(p.x + "-" +p.y);
            //System.out.println(checkD());
            Point n = new Point (p.x,p.y+1);
            n.g = p.g+1;
            n.h = heuristic_cost_estimate(p,goal);
            n.f = n.g+n.h;
            n.came_from = p;
            neighbors.add(n);
        }
        if (checkL()) {
            Point n = new Point (p.x-1,p.y);
            n.g = p.g+1;
            n.h = heuristic_cost_estimate(p,goal);
            n.f = n.g+n.h;
            n.came_from = p;
            neighbors.add(n);
        }
        for (int i=0; i<height;i++){
            for (int j=0; j<width; j++) {
                field[j][i]=tmp[j][i];
                if (tmp[j][i]==cell.ROBOT) {
                    robotX=j;
                    robotY=i;
                }
            }
        }
        //for (Point n:neighbors) {
        //    sb.append("neighbors: ");
        //    sb.append(n.x + "-" + n.y);
        //    sb.append('\n');
        //}
        //sb.append("______");
        //sb.append('\n');
        //System.out.println(sb);
        return neighbors;
    }
    
    ArrayList<Point> getPathAStar(Point start, Point goal) {
        ArrayList<Point> closedset = new ArrayList<Point>();
        ArrayList<Point> openset = new ArrayList<Point>();
        start.g=0; // g(x). Стоимость пути от начальной вершины. У start g(x) = 0
        start.h=heuristic_cost_estimate(start, goal); // Эвристическая оценка расстояния до цели. h(x)
        start.f = start.g + start.h;      // f(x) = g(x) + h(x)
        openset.add(start);
        while (!openset.isEmpty()) {
            Point x=getBest(openset); //вершина с лучшим f(x)
            //System.out.println(sb);
            if (x.x==goal.x && x.y==goal.y) {
                //return reconstruct_path(start,goal,closedset);
                openset.remove(x);
                closedset.add(x);
                return reconstruct_path(start,goal,closedset);
            }
            openset.remove(x);
            if (!closedset.contains(x)) {
                closedset.add(x);
            }
            
            ArrayList<Point> neighbor_nodes = get_neighboor_nodes(x,start,goal,closedset);
            if (neighbor_nodes.isEmpty()) continue;
            for (Point y:neighbor_nodes) {
                if (closedset.contains(y)) {
                    continue;
                }
                if (!openset.contains(y)) {// || (tentative_g_score < y.g)) {
                    openset.add(y);
                }
                else  {
                    for (ListIterator<Point> i = openset.listIterator();i.hasNext();) {
                        Point p = i.next();
                        if (p.x==y.x && p.y == y.y && p.g>y.g) {
                            i.set(y);
                        }
                    }
        
                }  
            }
        }
        return new ArrayList<Point>();
    }
    //поиск ближайшей лямды(heuristic_cost_estimate())
    Point getBest() {
        if (lambds==0) return new Point(exitX,exitY);
        Point best = new Point(0,0);
        best.h=1000;
        for (int i=0; i<height;i++){
            for (int j=0; j<width; j++) {
                if (field[j][i]==cell.LAMBDA) {
                    Point l = new Point(j,i);
                    l.h=heuristic_cost_estimate(new Point(robotX,robotY), l);
                    if (l.h<best.h) {
                        best = l;
                    }
                }
            }
        }
        return best;
       
    }
    void passLabyrinth() {
 
            ArrayList<edge> route = getRoute(new Point(robotX,robotY),new Point(exitX,exitY));
            //System.out.println(route.size());
            if (route.isEmpty()) {
                A();
                return;
            }
            for (edge e:route) {
                if (e.start.x==robotX && e.start.y==robotY) {
                    ArrayList<Point> path = getPathAStar(new Point(e.start.x,e.start.y), new Point(e.end.x,e.end.y));
                    
                    move(path,true);
                    
                }
                else if (e.end.x==robotX && e.end.y==robotY) {
                     ArrayList<Point> path = getPathAStar(new Point(e.end.x,e.end.y), new Point(e.start.x,e.start.y));
                     move(path,true);
                    
                }
                //move(e.path,true);
            }
            if (robotX!=exitX && robotY!=exitY) A();
    }
   
    //GLOBAL SEARCH, ANT COLONY
    
    ArrayList<edge> getRoute(Point start, Point goal) {
        cell[][] tmp = new cell[width][height];
        for (int i=0; i<height;i++){
            for (int j=0; j<width; j++) {
                tmp[j][i]=field[j][i];
            }
        }
        
        ArrayList<edge> edges = new ArrayList<edge>(); //список ребер
        ArrayList<vertex> vertices = new ArrayList<vertex>(); //список всех вершин, возможно не нужен
        ArrayList<edge> route = new ArrayList<edge>(); // путь
        ArrayList<vertex> path = new ArrayList<vertex>();
        vertex first = new vertex(start.x,start.y);
        vertices.add(first);
        //return new ArrayList<edge>();
        for (int i=0; i<height;i++){
            for (int j=0; j<width; j++) {
                if (field[j][i]==cell.LAMBDA || field[j][i]==cell.EXIT || field[j][i]==cell.OEXIT) {
                    vertices.add(new vertex(j,i));
                }
            }
        }
        //ArrayList<path> p= findPathsFromPoint(new Point(robotX,robotY), vertices);
        //System.out.print("LAMBDS: " +lambds + "PATHS: " + p.size());
//        for (vertex v:vertices) {
//            for (vertex t:vertices) {
//               if (!v.equals(t) && t.checked==false) {
//                    ArrayList<Point> pth = getPathAStar(new Point(v.x,v.y),new Point(t.x,t.y));
//                    int len = pth.size();
//                    //if (len!=0 && v.x==start.x && v.y==start.y && isSafePath(pth)) {
//                    //   edge e = new edge(v,t);
//                    //    e.lenght=len;
//                    //    edges.add(e);
//                    //}
//                    if (len!=0) { //&& v.x!=start.x && v.y!=start.y)) {
//                        edge e = new edge(v,t);
//                        e.lenght=len;
//                        edges.add(e);
//                    }
////                    edge e = new edge(v,t);
////                    e.lenght=heuristic_cost_estimate(new Point(v.x,v.y),new Point(t.x,t.y));
////                    edges.add(e);
//                }
//            }
//            v.checked=true;
//       }
        
        
            //for (vertex t:vertices) {
              //      ArrayList<Point> pth = getPathAStar(new Point(robotX,robotY),new Point(t.x,t.y)); }
       
        int tmax = 1; //время жизни колонии
        
        int t=0;
        int cost=10000; //лучшая стоимость пути
        
        while (t<tmax) {
            
            for (int i=0; i<height;i++){
                for (int j=0; j<width; j++) {
                    field[j][i]=tmp[j][i];
                    if (tmp[j][i]==cell.ROBOT) {
                        robotX=j;
                        robotY=i;
                    }
                }
            }
            //System.out.println("OK");
            ant a0 = new ant(new vertex(robotX,robotY)); 
            //System.out.println("OK");
            ArrayList<edge> passedEdges = new ArrayList<edge>();
            
            while ((a0.passed.size()!= vertices.size()) ) {
                a0.passed.add(a0.current);
                //System.out.println("OK");
                getEdges(vertices,edges,a0);
                //System.out.println("OK!!");
                edge psd = a0.move(edges, new vertex(exitX,exitY));
                
                //System.out.println(a0.current.x);
                //System.out.println(a0.current.y);
                //System.out.print("VISIT");
                if (psd!=null) {
                     passedEdges.add(psd);
                     move(psd.path,false);
                }
                
                else break;
            }
            int currentCost=0;//текущая стоимость пути
            for (edge e:passedEdges){
                currentCost+=e.lenght;
            }
            
            if (currentCost<cost) {
                cost=currentCost;
                route = passedEdges;
            }
            updatePheromon(a0,edges,passedEdges);
            t++;
            
        }
//        int c=0;
//        for (edge e:route) {
//            c+=e.lenght;
//        }
//        System.out.println(c);
        //System.out.println(route.size());
        for (int i=0; i<height;i++){
                for (int j=0; j<width; j++) {
                    field[j][i]=tmp[j][i];
                    if (tmp[j][i]==cell.ROBOT) {
                        robotX=j;
                        robotY=i;
                    }
                }
            }
        return route;

    }
    
    void getEdges(ArrayList<vertex> vertices, ArrayList<edge> edges, ant a0) {
        
        cell[][] tmp = new cell[width][height];
        for (int i=0; i<height;i++){
            for (int j=0; j<width; j++) {
                tmp[j][i]=field[j][i];
            }
        }
        //System.out.println("OK");
        for (vertex v: vertices) {
                edge e = new edge(a0.current,v);
                if (!edges.contains(e)   && !a0.passed.contains(v)) {
                    //field[a0.current.x][a0.current.y]=cell.ROBOT;
                    //System.out.println("OK");
                    //System.out.print(a0.current.x + "-" + a0.current.y + "to" + v.x + "-" +v.y);
                    ArrayList<Point> pth = getPathAStar(new Point(a0.current.x,a0.current.y),new Point(v.x,v.y));
                    //System.out.println("!OK");
                    boolean t = false;
                    for (Point pi:pth) {
                        if (!pi.equals(new Point(a0.current.x,a0.current.y)) && !pi.equals(new Point(v.x,v.y)) && vertices.contains(new vertex(pi.x,pi.y))) {
                            t=true;
  
                        }
                    }
                    //System.out.println("OK!");
                    //System.out.println(edges.size());
                    int len = pth.size();
                    e.lenght = len;
                    
                    if (len!=0 && !t) {
                        e.path=pth;
                        edges.add(e);
                        
                    }
                    //System.out.println(edges.size());
                    
                }
            }
        //System.out.println("OK!");
        
        //System.out.println(edges.size());
    
    }
    
    void updatePheromon(ant a, ArrayList<edge> edges, ArrayList<edge> passedEdges) {
        double phi = 0.2; //скорость испарения феромонов
        for (edge passed:passedEdges) {
            for (edge e:edges) {
                if (passed==e) {
                    e.pheromon = (1.0-phi) * e.pheromon + 1.0/e.lenght;
                }
            }
        }
    }
    boolean checkU() {
        if (field[robotX][robotY] == cell.ROBOT && isSafeCell(robotX,robotY-1) && field[robotX][robotY-1]!=cell.WALL && field[robotX][robotY-1]!=cell.STONE) {
                //|| field[robotX][robotY-1]==cell.GROUND || field[robotX][robotY-1]==cell.LAMBDA)) {
            return true;
        }
        return false;
    }
    boolean checkD() {
        if (field[robotX][robotY] == cell.ROBOT && isSafeCell(robotX,robotY+1) && field[robotX][robotY+1]!=cell.WALL && field[robotX][robotY+1]!=cell.STONE) { 
                //|| field[robotX][robotY+1]==cell.GROUND || field[robotX][robotY+1]==cell.LAMBDA)) {
            //System.out.println(field[robotX][robotY+1]!=cell.STONE);
            //System.out.println(robotX);
            //System.out.println(robotY);
            return true;
        }
        return false;
    }
    boolean checkR() {
        if (field[robotX][robotY] == cell.ROBOT && isSafeCell(robotX+1,robotY) && field[robotX+1][robotY]!=cell.WALL && field[robotX+1][robotY]!=cell.STONE) {
                //|| field[robotX+1][robotY]==cell.GROUND || field[robotX+1][robotY]==cell.LAMBDA)) {
            return true;
        }
        else if (field[robotX][robotY] == cell.ROBOT && isSafeCell(robotX+1,robotY) && field[robotX+1][robotY]==cell.STONE && (field[robotX+2][robotY]==cell.EMPTY )) {
            return true;
        }
       return false;
    }
    boolean checkL() {
        //System.out.println(field[robotX][robotY] == cell.ROBOT && isSafeCell(robotX-1,robotY) && (field[robotX-1][robotY]!=cell.WALL && field[robotX-1][robotY]==cell.STONE));
        //System.out.println(robotY);
        if (field[robotX][robotY] == cell.ROBOT && isSafeCell(robotX-1,robotY) && (field[robotX-1][robotY]!=cell.WALL && field[robotX-1][robotY]!=cell.STONE)) { 
                //|| field[robotX-1][robotY]==cell.GROUND || field[robotX-1][robotY]==cell.LAMBDA)) {
            
            return true;
        }
        else if (field[robotX][robotY] == cell.ROBOT && isSafeCell(robotX-1,robotY) && field[robotX-1][robotY]==cell.STONE && (field[robotX-2][robotY]==cell.EMPTY )) {
            return true;
        }
       return false;
    }
    

    
    public static void main(String[] args) {
         //TODO code application logic here
        try {
            FileReader file = new FileReader("map5.txt");
            BufferedReader reader = new BufferedReader(file);
            int height=0;
            int width = 0;
            ArrayList<String> list = new ArrayList<String>();
            String currentString;
            while ((null != (currentString = reader.readLine()))) { 
               list.add(currentString);
               height++;
            }
            reader.close();
            file.close();
            width = list.get(0).length();
            char[][] map = new char[width][height];
            for (int i=0; i<height; i++) {
                for (int j=0; j<width; j++) {
                    map[j][i] = list.get(i).charAt(j);
                }
            }
            Robot R = new Robot(map,width,height);
            R.print();
            

            if (args[0].equals(new String("-manual"))) {
               
                char c;
                do {
                    c = (char)System.in.read();
                    if (c=='U') {
                         if (R.U(true)) R.print();
                    }
                    else if (c=='D') {
                        if (R.D(true)) R.print();
                    }
                    else if (c=='R') {
                         if (R.R(true)) R.print();
                    }
                    else if (c=='L') {
                         if (R.L(true)) R.print();
                    }
                    else if (c=='A'){
                        R.A();
                        R.print();
                    }
                    else if (c=='W') {
                        R.W(true);
                        R.print();
                    }
                }
                while (true);
            }
            else if (args[0].equals(new String("-debug"))) {
                R.passLabyrinth();
//                for (int i=0; i<height; i++) {
//                 /  for (int j=0; j<width; j++) {
//                        if (R.field[j][i]==cell.LAMBDA) {
//                            System.out.println(j + "-" + i);
//                            R.getPathAStar(new Point(R.robotX,R.robotY),new Point(j,i));
//                            if (j==6 && i == 10) R.print();
//                            System.out.println(j + "-" + i);
//                        }
//                    }
//                }
                //System.out.println(R.field[27][23]);
                //ArrayList<Point> p = R.getPathAStar(new Point(R.robotX,R.robotY),new Point(8,10));
                
                //System.out.println(p.size());
                //R.move(p,true);
                //System.out.print(R.isSafePath(R.getPathAStar(new Point(1,5),new Point(5,4))));
                //R.move(R.getPathAStar(new Point(R.robotX,R.robotY), new Point (5,4)), true);
                //while (i< 
                //System.out.println(p.size());
                //R.move(R.getPathAStar(new Point(2,4), new Point(4,4)));
                //System.out.println(R.isSafeCell(3,4));
                
                
            }
        }
                                     
         
        catch (IOException e)  {
            System.out.println("IOException");
        }
    }
}
