package com.example.sudoku;

import java.util.*;

public class Sudoku {
    //Sudoku Array
    private Kachel[][] sudoku = new Kachel[9][9];

    //counts the number of returns in the solveSudoku Method
    private int returns = 0;

    //Konstruktor
    public Sudoku() {
        //Insert Kacheln
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudoku[i][j] = new Kachel(i,j);
            }
        }
    }

    //Solve the sudoku
    public Integer[][] sudokusolver(Integer[][] pinput) throws Exception{
        //Get Sudoku
        sudokuanlegen(pinput);
        //check if given sudoku is valid
        if(isillegal(sudoku)){
            throw new Exception("not possible");
        }
        //solve sudoku
        if (solveSudoku(sudoku)) {
            return sudokuausgeben(sudoku);
        } else {
            throw new Exception("not possible");
        }
    }

    private boolean isillegal(Kachel[][] psudoku){
        //check the rows for double values
        for (int i = 0; i < 9; i++) {
            ArrayList<Integer> plist = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                //contains a number?
                if(psudoku[i][j].getnum()!=0){
                    //if already in the list return false
                    if(plist.contains(psudoku[i][j].getnum())){
                        return true;
                    }
                    plist.add(psudoku[i][j].getnum());
                }
            }
        }
        //checks the columns
        for (int i = 0; i < 9; i++) {
            ArrayList<Integer> plist = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                //contains a number?
                if(psudoku[j][i].getnum()!=0){
                    //if already in the list return false
                    if(plist.contains(psudoku[j][i].getnum())){
                        return true;
                    }
                    plist.add(psudoku[j][i].getnum());
                }
            }
        }
        //check the quadrants
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //from here the parts of a single quadrant
                ArrayList<Integer> plist = new ArrayList<>();
                for (int k = i*3; k < i*3+3; k++) {
                    for (int l = j*3; l < j*3+3; l++) {
                        if(psudoku[k][l].getnum()!=0){
                            //if already in the list return false
                            if(plist.contains(psudoku[k][l].getnum())){
                                return true;
                            }
                            plist.add(psudoku[k][l].getnum());
                        }
                    }
                }
            }
        }
        return false;
    }

    //Possibilities
    public Integer[][][] possibilities(Integer[][] pint){
        //3d array to store the possible numbers in the array (playing 3d chess move)
        Integer[][][] pintnumbers = new Integer[9][9][9];
        //Instances the field
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    pintnumbers[i][j][k] = 0;
                }
            }
        }
        //Kacheln erstellen
        sudokuanlegen(pint);
        deletenums();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                //Adding numbers
                ArrayList<Integer> pnumbers = sudoku[i][j].getnumbers();
                for (int k = 0; k < pnumbers.size(); k++) {
                    pintnumbers[i][j][k] = pnumbers.get(k);
                }
            }
        }
        return pintnumbers;
    }

    public Integer getreturns(){
        return returns;
    }

    // Integer Array --> Kachel Array
    private void sudokuanlegen(Integer[][] intsudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                //Checks if the numbers are 1-9
                if (intsudoku[i][j] != null) {
                    if (intsudoku[i][j] > 0 && intsudoku[i][j] < 10) {
                        sudoku[i][j].setnum(intsudoku[i][j]);
                    } else { //somewhat redundant but avoids null pointers
                        sudoku[i][j].setnum(0);
                    }
                }
                else{
                    sudoku[i][j].setnum(0);
                }
            }
        }
    }

    //Kachel Array --> Integer Array
    private Integer[][] sudokuausgeben(Kachel[][] psudoku) {
        Integer[][] pintsudoku = new Integer[9][9];
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                if(psudoku[i][j].getnum()!=0) {
                    pintsudoku[i][j] = psudoku[i][j].getnum();
                }
                else{
                    pintsudoku[i][j] = 0;
                }
            }
        }
        return pintsudoku;
    }

    /*
        Working Sudoku solver (:
     */

    //finds empty fields in the sudoku
    private coords findemptiness(Kachel[][] psudoku) {
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                if (psudoku[j][i].getnum() == 0) {
                    return (new coords(j, i));
                }
            }
        }
        return new coords();
    }

    private boolean solveSudoku(Kachel[][] psudoku) {
        //gimme gimme gimme an empty field
        coords c = findemptiness(psudoku);
        // No empty spaces left? If yes, the job is done
        if (c.getdone()) {
            returns ++;
            return true;
        }
        //now lets get the row and column from the empty field
        int row = c.getrow();
        int column = c.getcolumn();
        // backtracking
        for (int num = 1; num<=9;num++) {
            if (valid(psudoku, row, column,num)) {
                psudoku[row][column].setnum(num);
                //Rekursion
                if (solveSudoku(psudoku)) {
                    returns ++;
                    return true;
                }
                //Replaces the made mistakes with 0, so it can work on it again
                else {
                    psudoku[row][column].setnum(0);
                }
            }
        }
        returns ++;
        return false;
    }

    private static boolean valid(Kachel[][] psudoku, int row, int column, Integer num) {
        // Check row
        for (int d = 0; d < psudoku.length; d++) {
            if (psudoku[row][d].getnum() == num) {
                return false;
            }
        }

        // Check the Column
        for (Kachel[] kachels : psudoku) {
            if (kachels[column].getnum() == num) {
                return false;
            }
        }

        //Check quadrant
        int sqrt = (int) Math.sqrt(psudoku.length);
        int boxRowStart = row - row % sqrt;
        int boxColStart = column - column % sqrt;

        for (int r = boxRowStart; r < boxRowStart + sqrt; r++) {
            for (int d = boxColStart; d < boxColStart + sqrt; d++) {
                if (psudoku[r][d].getnum() == num) {
                    return false;
                }
            }
        }
        //Seems legit
        return true;
    }

    public Integer[][] generatesudoku(int difficulty){
        //Gets a random Quadrant
        int qrow = (int)(Math.random()*((3-1)+1))+1;
        int qcolumn = (int)(Math.random()*((3-1)+1))+1;
        qrow = qrow * 3 - 3;
        qcolumn = qcolumn * 3 - 3;

        //DEBUG
        System.out.println("Generating Sudoku");
        System.out.println("Selected Quadrant:" + qrow + " ; " + qcolumn);

        //List cointaining 1-9
        List<Integer> plist = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        //counts the runs
        int count = 0;
        for(int i = 0; i < 3 ; i++){
            for (int j = 0; j < 3; j++) {
                int num = plist.get((int)(Math.random()*((plist.size()))));
                plist.remove(Integer.valueOf(num));
                System.out.print(num + "    ");
                sudoku[qrow+i][qcolumn+j].setnum(num);
            }
            System.out.println("");
        }


        //Difficulty comes in place
        int min = 20 + 11*difficulty;
        int max = 30 + 15*difficulty;
        int amount = (int)(Math.random()*((max-min)+1))+min;
        System.out.println("min:" + min + " max:" + max + " amount:" + amount);
        //solves it
        solveSudoku(sudoku);
        //Deletes Numbers from it
        ArrayList<String> pdeleted = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            int prandomrow = (int)(Math.random()*((9)));
            int prandomcolumn = (int)(Math.random()*((9)));
            String pcombined = prandomrow + String.valueOf(prandomcolumn);
            if(!pdeleted.contains(pcombined)){
                sudoku[prandomrow][prandomcolumn].setnum(0);
                pdeleted.add(0, prandomrow + String.valueOf(prandomcolumn));
            }
            else{
                i--;
            }
        }


        /*
        boolean difficultyright = true;

        while (difficultyright){
            solveSudoku();
            int min = 20;
            int max = 70;
            int amount = (int)(Math.random()*((max-min)+1))+min;
            //Deletes Numbers from it
            ArrayList<String> pdeleted = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                int prandomrow = (int)(Math.random()*((9)));
                int prandomcolumn = (int)(Math.random()*((9)));
                String pcombined = prandomrow + String.valueOf(prandomcolumn);
                if(!pdeleted.contains(pcombined)){
                    sudoku[prandomrow][prandomcolumn].setnum(0);
                    pdeleted.add(0, prandomrow + String.valueOf(prandomcolumn));
                }
                else{
                    i--;
                }
            }

            Sudoku psudoku = new Sudoku();
            psudoku.kachelsudokusolver(sudoku);
            Integer integer = psudoku.getreturns();
            if(difficulty == 0 && integer < 100){
                difficultyright = false;
            }
            else if(difficulty == 1 && integer < 100){
                difficultyright = false;
            }
            else if(difficulty == 2 && integer < 300){
                difficultyright = false;
            }
            else if(difficulty == 3 && integer < 500){
                difficultyright = false;
            }
        }
        */



        //Turns it into integer Array
        Integer[][] intsudoku = new Integer[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                intsudoku[i][j] = sudoku[i][j].getnum();
            }
        }
        return intsudoku;
    }

    /*
            Gets the possible numbers for every field, may be used later on
     */
    private void deletenums() {
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                Integer num = sudoku[j][i].getnum();
                if (num != 0) {
                    //removes possible numbers from the lists in the quadrant
                    removeinQ(j, i);
                    //remove possible numbers from the rows and column
                    removeZS(j, i, num);
                }
            }
        }
    }

    /*
        Row and column
     */
    private void removeZS(int row, int column, Integer num) {
        //Remove in Row
        for (int i = 0; i < 9; i++) {
            sudoku[i][column].removenumbers(num);
        }
        //Remove in Column
        for (int i = 0; i < 9; i++) {
            sudoku[row][i].removenumbers(num);
        }
    }

    /*
        quadrant
     */
    private void removeinQ(int row, int column) {
        int num = sudoku[row][column].getnum();
        switch (row) {
            case 0, 1, 2:
                switch (column) {
                    case 0, 1, 2:
                        for (int j = 0; j < 3; j++) {
                            for (int i = 0; i < 3; i++) {
                                sudoku[j][i].removenumbers(num);
                            }
                        }
                        break;
                    case 3, 4, 5:
                        for (int j = 0; j < 3; j++) {
                            for (int i = 3; i < 6; i++) {
                                sudoku[j][i].removenumbers(num);
                            }
                        }
                        break;
                    case 6, 7, 8:
                        for (int j = 0; j < 3; j++) {
                            for (int i = 6; i < 9; i++) {
                                sudoku[j][i].removenumbers(num);
                            }
                        }
                        break;
                }
                break;
            case 3, 4, 5:
                switch (column) {
                    case 0, 1, 2:
                        for (int j = 3; j < 6; j++) {
                            for (int i = 0; i < 3; i++) {
                                sudoku[j][i].removenumbers(num);
                            }
                        }
                        break;
                    case 3, 4, 5:
                        for (int j = 3; j < 6; j++) {
                            for (int i = 3; i < 6; i++) {
                                sudoku[j][i].removenumbers(num);
                            }
                        }
                        break;
                    case 6, 7, 8:
                        for (int j = 3; j < 6; j++) {
                            for (int i = 6; i < 9; i++) {
                                sudoku[j][i].removenumbers(num);
                            }
                        }
                        break;
                }
                break;
            case 6, 7, 8:
                switch (column) {
                    case 0, 1, 2:
                        for (int j = 6; j < 9; j++) {
                            for (int i = 0; i < 3; i++) {
                                sudoku[j][i].removenumbers(num);
                            }
                        }
                        break;
                    case 3, 4, 5:
                        for (int j = 6; j < 9; j++) {
                            for (int i = 3; i < 6; i++) {
                                sudoku[j][i].removenumbers(num);
                            }
                        }
                        break;
                    case 6, 7, 8:
                        for (int j = 6; j < 9; j++) {
                            for (int i = 6; i < 9; i++) {
                                sudoku[j][i].removenumbers(num);
                            }
                        }
                        break;
                }
                break;
        }
    }
}
