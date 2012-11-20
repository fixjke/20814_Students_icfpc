/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.ListIterator;

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
    //ArrayList<Point> path;
    ArrayList<Point> lmb = new ArrayList<Point>();
    Robot(char[][] map, int w, int h){
        height=h;
        width=w;
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
    boolean W() {
        simulate();
        return true;
    }
    boolean L() {
        if (field[robotX-1][robotY]==cell.OEXIT) {
            field[robotX][robotY]=cell.EMPTY;
            robotX--;
            field[robotX][robotY]=cell.ROBOT;   
            gameOver(2);
            return true;
        }
        else if (field[robotX-1][robotY]==cell.EMPTY || field[robotX-1][robotY]==cell.GROUND) {
            field[robotX][robotY]=cell.EMPTY;
            robotX--;
            field[robotX][robotY]=cell.ROBOT;   
            score--;
            simulate();
            return true;
        }
        else if (field[robotX-1][robotY]==cell.LAMBDA) {
            field[robotX][robotY]=cell.EMPTY;
            robotX--;
            field[robotX][robotY]=cell.ROBOT;
            score+=25;
            getLambds++;
            lambds--;
            simulate();
            return true;
        }
        else {
            System.out.println("turn L failed");
            return false;
        }
    }
    boolean R() {
        if (field[robotX+1][robotY]==cell.OEXIT) {
            field[robotX][robotY]=cell.EMPTY;
            robotX++;
            field[robotX][robotY]=cell.ROBOT;
            gameOver(2);
            return true;
        }
        else if (field[robotX+1][robotY]==cell.EMPTY || field[robotX+1][robotY]==cell.GROUND ) {
            field[robotX][robotY]=cell.EMPTY;
            robotX++;
            score--;
            field[robotX][robotY]=cell.ROBOT;
            simulate();
            return true;
        }
       else if (field[robotX+1][robotY]==cell.LAMBDA) {
            field[robotX][robotY]=cell.EMPTY;
            robotX++;
            field[robotX][robotY]=cell.ROBOT;
            score+=25;
            getLambds++;
            lambds--;
            simulate();
            return true;
        }
        else {
            System.out.println("turn R failed");
            return false;
        }
    }
    boolean U() {
        if (field[robotX][robotY-1]==cell.OEXIT) {
            field[robotX][robotY]=cell.EMPTY;
            robotY--;
            field[robotX][robotY]=cell.ROBOT;
            gameOver(2);
            return true;
        }
        else if (field[robotX][robotY-1]==cell.EMPTY || field[robotX][robotY-1]==cell.GROUND) {
            field[robotX][robotY]=cell.EMPTY;
            robotY--;
            score--;
            field[robotX][robotY]=cell.ROBOT;
            simulate();
            return true;
        }
        else if (field[robotX][robotY-1]==cell.LAMBDA) {
            field[robotX][robotY]=cell.EMPTY;
            robotY--;
            field[robotX][robotY]=cell.ROBOT;
            score+=25;
            lambds--;
            getLambds++;
            simulate();
            return true;
        }
         else {
            System.out.println("turn U failed");
            return false;
        }
    }
    boolean D() {
        if (field[robotX][robotY+1]==cell.OEXIT) {
            field[robotX][robotY]=cell.EMPTY;
            robotY++;
            field[robotX][robotY]=cell.ROBOT;  
            gameOver(2);
            return true;
        }
        else if (field[robotX][robotY+1]==cell.EMPTY || field[robotX][robotY+1]==cell.GROUND) {
            field[robotX][robotY]=cell.EMPTY;
            robotY++;
            score--;
            field[robotX][robotY]=cell.ROBOT;  
            simulate();
            return true;
        }
        else if  (field[robotX][robotY+1]==cell.LAMBDA) {
            field[robotX][robotY]=cell.EMPTY;
            robotY++;
            field[robotX][robotY]=cell.ROBOT;   
            score+=25;
            getLambds++;
            lambds--;
            simulate();
            return true;
        }
        else {
            System.out.println("turn D failed");
            return false;
        }
    }
    void print(){
        StringBuilder sb = new StringBuilder();
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
    boolean simulate() {
        for (int i=height-1; i>=0; i--) {
            for (int j=0; j<width; j++) {
                if (field[j][i]==cell.STONE) {
                    if (field[j][i+1]==cell.EMPTY || field[j][i+1]==cell.ROBOT ) {
                        field[j][i+1]=cell.STONE;
                        field[j][i]=cell.EMPTY;
                    }
                    else if (field[j][i+1]==cell.STONE && field[j+1][i]==cell.EMPTY && (field[j+1][i+1]==cell.EMPTY || field[j+1][i+1]==cell.ROBOT) ) {
                        field[j+1][i+1]=cell.STONE;
                        field[j][i]=cell.EMPTY;
                    }
                    else if (field[j][i+1]==cell.STONE && field[j-1][i]==cell.EMPTY && (field[j-1][i+1]==cell.EMPTY || field[j-1][i+1]==cell.ROBOT)) {
                        field[j-1][i+1]=cell.STONE;
                        field[j][i]=cell.EMPTY;
                    }
                    else if (field[j][i+1]==cell.LAMBDA && field[j+1][i]==cell.EMPTY && (field[j+1][i+1]==cell.EMPTY || field[j+1][i+1]==cell.ROBOT)) {
                        field[j+1][i+1]=cell.STONE;
                        field[j][i]=cell.EMPTY;
                    }
                    else if (field[j][i+1]==cell.LAMBDA && field[j-1][i]==cell.EMPTY && (field[j-1][i+1]==cell.EMPTY || field[j-1][i+1]==cell.ROBOT) ) {
                        field[j-1][i+1]=cell.STONE;
                        field[j][i]=cell.EMPTY;
                    }
                }
            }
        }
        //if (!isAlive()) return false;
        if (lambds == 0) { //открытие выхода
            for (int i=0; i<height; i++) {
                for (int j=0; j<width; j++) {
                    if (field[j][i]==cell.EXIT) field[j][i]=cell.OEXIT;
                }
            }          
        }
        return true;
    }
    boolean simulate(int i) {
        while (i>0) {
            if (!simulate()) return false;
            i--;
        }
        return true;
    }
    void move(ArrayList<Point> path_map) {
        while(path_map.size()!=1) {
            Point last = path_map.get(path_map.size()-1);
            Point prev = path_map.get(path_map.size()-2);
            if (prev.x-last.x == 0 && prev.y-last.y==1) {
                D();
                print();
                System.out.println("D");
                path_map.remove(last);
            }
            else if (prev.x-last.x==0 && prev.y-last.y==-1) {
                U();
                print();
                System.out.println("U");
                path_map.remove(last);
            }
            else if (prev.y-last.y==0 && prev.x-last.x==1){
                R();
                print();
                System.out.println("R");
                path_map.remove(last);
            }
            else if (prev.y-last.y==0 && prev.x-last.x==-1){
                L();
                print();
                System.out.println("L");
                path_map.remove(last);
            }
        }
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
            if (best.f==0) System.out.println("saSa");
        }
        //TBD
        return p;
    }
    ArrayList<Point> reconstruct_path(Point start, Point goal, ArrayList<Point> closedset) {
        //TBD
        
        ArrayList<Point> path_map = new ArrayList<Point>();
        if (closedset.isEmpty()) return path_map;
        Point p = closedset.get(closedset.size()-1);
        int i=0;
        while (p!=null) {
            path_map.add(p);
            p=p.came_from;
            i++;
        }
        return path_map;
    }
    ArrayList<Point> get_neighbor_nodes(Point p,Point start,Point goal) {
        //копия поля
        cell[][] tmp = new cell[width][height];
        for (int i=0; i<height;i++){
            for (int j=0; j<width; j++) {
                tmp[j][i]=field[j][i];
            }
        }
        simulate(p.g+1);
        ArrayList<Point> neighbors = new ArrayList<Point>();
        if ((field[p.x][p.y]!=cell.STONE || p==start) && (field[p.x][p.y-1]==cell.EMPTY || field[p.x][p.y-1]==cell.LAMBDA || field[p.x][p.y-1]==cell.OEXIT)) {
            Point n = new Point (p.x,p.y-1);
            n.g = p.g+1;
            n.h = heuristic_cost_estimate(p,goal);
            n.f = n.g+n.h;
            n.came_from = p;
            neighbors.add(n);
        }
        if ((field[p.x][p.y]!=cell.STONE || p==start) && (field[p.x+1][p.y]==cell.EMPTY || field[p.x+1][p.y]==cell.LAMBDA || field[p.x+1][p.y]==cell.OEXIT )) {
            Point n = new Point (p.x+1,p.y);
            n.g = p.g+1;
            n.h = heuristic_cost_estimate(p,goal);
            n.f = n.g+n.h;
             n.came_from = p;
            neighbors.add(n);
        }
        if ((field[p.x][p.y]!=cell.STONE || p==start) && (field[p.x][p.y+1]==cell.EMPTY || field[p.x][p.y+1]==cell.LAMBDA || field[p.x][p.y+1]==cell.OEXIT )) {
            Point n = new Point (p.x,p.y+1);
            n.g = p.g+1;
            n.h = heuristic_cost_estimate(p,goal);
            n.f = n.g+n.h;
             n.came_from = p;
            neighbors.add(n);
        }
        if ((field[p.x][p.y]!=cell.STONE || p==start) && (field[p.x-1][p.y]==cell.EMPTY || field[p.x-1][p.y]==cell.LAMBDA || field[p.x-1][p.y]==cell.OEXIT)) {
            Point n = new Point (p.x-1,p.y);
            n.g = p.g+1;
            n.h = heuristic_cost_estimate(p,goal);
            n.f = n.g+n.h;
            n.came_from = p;
            neighbors.add(n);
        }
        //восстановление копии
        for (int i=0; i<height;i++){
            for (int j=0; j<width; j++) {
                field[j][i]=tmp[j][i];
            }
        }
        return neighbors;
    }
    ArrayList<Point> getPathAStar(Point start, Point goal) {
        ArrayList<Point> closedset = new ArrayList<Point>();
        ArrayList<Point> openset = new ArrayList<Point>();
        start.g=0; // g(x). Стоимость пути от начальной вершины. У start g(x) = 0
        start.h=heuristic_cost_estimate(start, goal); // Эвристическая оценка расстояние до цели. h(x)
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
            //создание копии поля
            ArrayList<Point> neighbor_nodes = get_neighbor_nodes(x,start,goal);
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
        //ArrayList<Point> path = new ArrayList<Point>();
        while(robotX!=exitX && robotY!=exitY) {
            move(getPathAStar(new Point(robotX,robotY),getBest()));
        }
        
    }
    public static void main(String[] args) {
         //TODO code application logic here
        try {
            FileReader file = new FileReader("in.txt");
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
            R.passLabyrinth();
//            ArrayList<Point> path = R.getPathAStar(new Point(4,4), new Point(1,1));
//            StringBuilder sb = new StringBuilder();
//            for (Point p:path) {
//                sb.append(p.x);
//                sb.append("-");
//                sb.append(p.y);
//                sb.append('\n');
//            }
//            R.move(path);
//            System.out.println(sb); 
//            char c;
//            do {
//                c = (char)System.in.read();
//                if (c=='U') {
//                     if (R.U()) R.print();
//                }
//                else if (c=='D') {
//                    if (R.D()) R.print();
//                }
//                else if (c=='R') {
//                     if (R.R()) R.print();
//                }
//                else if (c=='L') {
//                     if (R.L()) R.print();
//                }
//                else if (c=='A'){
//                    R.A();
//                    R.print();
//                }
//                else if (c=='W') {
//                    R.W();
//                    R.print();
//                }
//            }
//            while (true);
        }
                                     
         
        catch (IOException e)  {
            System.out.println("IOException");
        }
    }
}

