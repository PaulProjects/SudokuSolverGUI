package com.example.sudoku;
import java.util.ArrayList;

public class Kachel {
    //List which contains the possible numbers (not used atm)
    private ArrayList<Integer> numbers = new ArrayList<>();
    //Display number
    public Integer num = 0;

    private int x;
    private int y;

    public Kachel(int x, int y){
        this.x =x;
        this.y =y;
        fillnumbers();
    }
    //falls es noch keine gibt
    public void fillnumbers(){
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(6);
        numbers.add(7);
        numbers.add(8);
        numbers.add(9);
    }

    //gibt Nummer aus
    public Integer getnum(){
        return num;
    }

    //setzt Nummer
    public void setnum(Integer pnum){
        num = pnum;
        if(pnum != 0){
            numbers.clear();
        }
    }

    public void removenumbers(Integer pnumbers){
        if(numbers.contains(pnumbers)) {
            numbers.remove(Integer.valueOf(pnumbers));
        }
    }

    public ArrayList<Integer> getnumbers(){
        return numbers;
    }

}
