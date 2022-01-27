package com.example.sudoku;

public class coords {
    //I hate java
    
    //coordinates of the field
    int row;
    int column;
    //tells if the search for emptiness is done
    boolean done;

    public coords(int prow, int pcolumn) {
        row = prow;
        column = pcolumn;
        done = false;
    }

    public coords() {
        done = true;
    }

    public int getrow() {
        return row;
    }

    public int getcolumn() {
        return column;
    }

    public boolean getdone() {
        return done;
    }

}
