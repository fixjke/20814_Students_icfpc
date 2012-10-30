/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

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
    int lambds; //лямбд на карте
    int getLambds; //собрано лямбд
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
                else if (map[j][i]=='L') field[j][i]=cell.EXIT;
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
            System.out.println("turn failed");
            return false;
        }
    }
    boolean R() {
        if (field[robotX+1][robotY]==cell.OEXIT) {
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
            System.out.println("turn failed");
            return false;
        }
    }
    boolean U() {
        if (field[robotX][robotY-1]==cell.OEXIT) {
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
            System.out.println("turn failed");
            return false;
        }
    }
    boolean D() {
        if (field[robotX][robotY+1]==cell.OEXIT) {
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
            System.out.println("turn failed");
            return false;
        }
    }
    void print(){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<height;i++){
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
    void simulate() {
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
        if (!isAlive()) gameOver(0);
        if (lambds == 0) { //открытие выхода
            for (int i=0; i<height; i++) {
                for (int j=0; j<width; j++) {
                    if (field[j][i]==cell.EXIT) field[j][i]=cell.OEXIT;
                }
            }          
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
            char c;
            do {
                c = (char)System.in.read();
                if (c=='U') {
                     if (R.U()) R.print();
                }
                else if (c=='D') {
                    if (R.D()) R.print();
                }
                else if (c=='R') {
                     if (R.R()) R.print();
                }
                else if (c=='L') {
                     if (R.L()) R.print();
                }
                else if (c=='A'){
                    R.A();
                    R.print();
                }
                else if (c=='W') {
                    R.W();
                    R.print();
                }
            }
            while (true);
        }
                                     
         
        catch (IOException e)  {
            System.out.println("IOException");
        }
    }
}

