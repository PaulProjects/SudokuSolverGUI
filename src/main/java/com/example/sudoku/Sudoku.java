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
                sudoku[i][j] = new Kachel(i, j);
            }
        }
    }

    //Solve the sudoku
    public Integer[][] sudokusolver(Integer[][] pinput) throws Exception {
        //Get Sudoku
        sudokuanlegen(pinput);
        //check if given sudoku is valid
        if (isillegal(sudoku)) {
            throw new Exception("not possible");
        }
        //solve sudoku
        if (solveSudoku(sudoku)) {
            return sudokuausgeben(sudoku);
        } else {
            throw new Exception("not possible");
        }
    }

    //may be used later on
    public boolean kachelsolver(Kachel[][] pinput) {
        sudoku = pinput;
        if (isillegal(sudoku)) {
            return false;
        }
        //solve sudoku
        return solveSudoku(sudoku);
    }

    private boolean isillegal(Kachel[][] psudoku) {
        //check the rows for double values
        for (int i = 0; i < 9; i++) {
            ArrayList<Integer> plist = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                //contains a number?
                if (psudoku[i][j].getnum() != 0) {
                    //if already in the list return false
                    if (plist.contains(psudoku[i][j].getnum())) {
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
                if (psudoku[j][i].getnum() != 0) {
                    //if already in the list return false
                    if (plist.contains(psudoku[j][i].getnum())) {
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
                for (int k = i * 3; k < i * 3 + 3; k++) {
                    for (int l = j * 3; l < j * 3 + 3; l++) {
                        if (psudoku[k][l].getnum() != 0) {
                            //if already in the list return false
                            if (plist.contains(psudoku[k][l].getnum())) {
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
    public Integer[][][] possibilities(Integer[][] pint) {
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

    public Integer getreturns() {
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
                } else {
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
                if (psudoku[i][j].getnum() != 0) {
                    pintsudoku[i][j] = psudoku[i][j].getnum();
                } else {
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
            returns++;
            return true;
        }
        //now lets get the row and column from the empty field
        int row = c.getrow();
        int column = c.getcolumn();
        // backtracking
        for (int num = 1; num <= 9; num++) {
            if (valid(psudoku, row, column, num)) {
                psudoku[row][column].setnum(num);
                //Rekursion
                if (solveSudoku(psudoku)) {
                    returns++;
                    return true;
                }
                //Replaces the made mistakes with 0, so it can work on it again
                else {
                    psudoku[row][column].setnum(0);
                }
            }
        }
        returns++;
        return false;
    }

    private static boolean valid(Kachel[][] psudoku, int row, int column, Integer num) {
        // Check row
        for (int d = 0; d < psudoku.length; d++) {
            if (Objects.equals(psudoku[row][d].getnum(), num)) {
                return false;
            }
        }

        // Check the Column
        for (Kachel[] kachels : psudoku) {
            if (Objects.equals(kachels[column].getnum(), num)) {
                return false;
            }
        }

        //Check quadrant
        int sqrt = (int) Math.sqrt(psudoku.length);
        int boxRowStart = row - row % sqrt;
        int boxColStart = column - column % sqrt;

        for (int r = boxRowStart; r < boxRowStart + sqrt; r++) {
            for (int d = boxColStart; d < boxColStart + sqrt; d++) {
                if (Objects.equals(psudoku[r][d].getnum(), num)) {
                    return false;
                }
            }
        }
        //Seems legit
        return true;
    }

    /*
            Generates sudokus
     */

    //nice Information to watch
    private StringBuilder generationinfos = new StringBuilder();

    public Integer[][] generatesudoku(int difficulty) {
        int tries = 0;
        while (true) {
            tries++;
            Integer[][] psudoku;
            //instances new Sudoku (it self)
            Sudoku subSudoku2 = new Sudoku();
            psudoku = subSudoku2.generatesudokuhelper(difficulty);
            generationinfos = subSudoku2.generationinfos;
            //2nd Instance of it (idk works better)
            Sudoku subSubdoku = new Sudoku();
            //solves it looks and difficulty and then decides if its good or not
            try {
                subSubdoku.sudokusolver(psudoku);
            } catch (Exception ignored) {
            }
            Integer preturns = subSubdoku.getreturns();
            generationinfos.append(" returns:").append(preturns).append("\n");
            if(preturns > 0) {
                //not the shortest, but its readable
                if (difficulty == 0 && preturns < 100) {
                    generationtextprinter(tries, difficulty);
                    return psudoku;
                } else if (difficulty == 1 && preturns > 100 && preturns < 200) {
                    generationtextprinter(tries, difficulty);
                    return psudoku;
                } else if (difficulty == 2 && preturns > 200 && preturns < 500) {
                    generationtextprinter(tries, difficulty);
                    return psudoku;
                } else if (difficulty == 3 && preturns > 500 && preturns < 1000) {
                    generationtextprinter(tries, difficulty);
                    return psudoku;
                }
            }
        }
    }

    private Integer[][] generatesudokuhelper(int difficulty) {

        //Gets a random Quadrant
        int qrow = (int) (Math.random() * ((3 - 1) + 1)) + 1;
        int qcolumn = (int) (Math.random() * ((3 - 1) + 1)) + 1;
        qrow = qrow * 3 - 3;
        qcolumn = qcolumn * 3 - 3;

        //clears quadrantinfos from the last run
        generationinfos.setLength(0);
        generationinfos.append("\nSelected Quadrant:").append(qrow).append(" ; ").append(qcolumn).append("\n");

        //List cointaining 1-9
        List<Integer> plist = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num = plist.get((int) (Math.random() * ((plist.size()))));
                plist.remove(Integer.valueOf(num));
                generationinfos.append(num).append("    ");
                sudoku[qrow + i][qcolumn + j].setnum(num);
            }
            generationinfos.append("\n");
        }

        //Difficulty comes in place
        int min = 20 + 11 * difficulty;
        int max = 30 + 15 * difficulty;
        int amount = (int) (Math.random() * ((max - min) + 1)) + min;
        generationinfos.append("min:").append(min).append(" max:").append(max).append(" amount:").append(amount);
        //solves it
        solveSudoku(sudoku);
        //Deletes Numbers from it
        ArrayList<String> pdeleted = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            int prandomrow = (int) (Math.random() * ((9)));
            int prandomcolumn = (int) (Math.random() * ((9)));
            String pcombined = prandomrow + String.valueOf(prandomcolumn);
            if (!pdeleted.contains(pcombined)) {
                sudoku[prandomrow][prandomcolumn].setnum(0);
                pdeleted.add(0, prandomrow + String.valueOf(prandomcolumn));
            } else {
                i--;
            }
        }

        //Turns it into integer Array
        Integer[][] intsudoku = new Integer[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                intsudoku[i][j] = sudoku[i][j].getnum();
            }
        }
        return intsudoku;
    }

    //I love sub methods
    private void generationtextprinter(int tries, int difficulty){
        //1 trie, 2 tries
        String str;
        if(tries == 1) {
            str = "\nTook " + tries + " trie for a lvl " + difficulty + " sudoku";
        }
        else{
            str = "\nTook " + tries + " tries for a lvl " + difficulty + " sudoku";
        }
        generationinfos.insert(0, str);
        System.out.println(generationinfos);
    }

    /*
            Gets the possible numbers for every field
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
