package com.example.sudoku;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Objects;


public class SudokuApp extends Application {

    //Size
    public static final int TILE_SIZE = 100;
    //Tiles
    public static final int WIDTH = 9;
    public static final int HEIGHT = 9;
    //show or hide tooltips
    private boolean showtooltips = false;

    //Tile Groups
    private ArrayList<Tile> tileGroup = new ArrayList<>();
    //Sudoku "backup" saves the state before the user edited it
    private Integer[][] previousSudoku = new Integer[9][9];

    //Mistakes the user made
    private int mistakes = 0;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        //Buttons
        Button b1 = new Button("Solve");
        b1.setStyle("-fx-background-color: transparent;");

        Button b3 = new Button("Clear");
        b3.setStyle("-fx-background-color: transparent;");

        Button b4 = new Button("Generate");
        b4.setStyle("-fx-background-color: transparent;");

        //Dropdown menu for difficulty
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Easy",
                        "Medium",
                        "Hard",
                        "Very Hard"
                );
        ComboBox comboBox = new ComboBox(options);
        comboBox.getSelectionModel().select(1);

        comboBox.setStyle("-fx-background-color: transparent;");

        Button b5 = new Button("Possible Numbers");
        b5.setStyle("-fx-background-color: transparent;");

        //inserts tiles in group
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile(x, y);
                tileGroup.add(tile);
            }
        }

        //Hbox for the buttons
        HBox hbox = new HBox();
        hbox.getChildren().addAll(b1, b3, b4, comboBox, b5);
        //Vbox for the MenuBar and the Tiles
        VBox vbox = new VBox();
        vbox.getChildren().addAll(hbox, root);

        /*
            Listeners
         */

        //Adding tiles to the detector (detects if a key got pressed)
        for (Tile tile : tileGroup) {
            root.getChildren().add(tile.getStackPane());
            tile.getTextField().setOnKeyTyped(e -> updatepossibilities());
        }

        //b1
        b1.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                b1.setStyle("-fx-background.color: White;");
            } else {
                b1.setStyle("-fx-background-color: transparent;");
            }
        });

        b1.setOnAction(actionEvent -> solve());

        //b3
        b3.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                b3.setStyle("-fx-background.color: White;");
            } else {
                b3.setStyle("-fx-background-color: transparent;");
            }
        });

        b3.setOnAction(actionEvent -> clear());

        //b4
        b4.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                b4.setStyle("-fx-background.color: White;");
            } else {
                b4.setStyle("-fx-background-color: transparent;");
            }
        });

        b4.setOnAction(actionEvent -> generate(comboBox.getSelectionModel().getSelectedIndex()));

        //Combo box

        comboBox.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                comboBox.setStyle("-fx-background.color: White;");
            } else {
                comboBox.setStyle("-fx-background-color: transparent;");
            }
        });

        //b5
        b5.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                b5.setStyle("-fx-background.color: White;");
            } else {
                b5.setStyle("-fx-background-color: transparent;");
            }
        });

        b5.setOnAction(actionEvent -> possibilities());

        return vbox;
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Sudoku");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /*
        Methods executed after action
     */
    private void solve() {
        //-->int sudoku
        Integer[][] intsudoku = intconvert();

        Sudoku psudoku = new Sudoku();
        try {
            intsudoku = psudoku.sudokusolver(intsudoku);
            //<--intsudoku
            tileconvert(intsudoku);

            Integer integer = psudoku.getreturns();

            if(integer > 1) {
                //Alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Solved!");
                alert.setHeaderText("Backtracks " + integer);
                if (integer < 100) {
                    alert.setContentText("Difficulty rating: easy");
                } else if (integer < 200) {
                    alert.setContentText("Difficulty rating: medium");
                } else if (integer < 500) {
                    alert.setContentText("Difficulty rating: hard");
                } else {
                    alert.setContentText("Difficulty rating: very hard");
                }
                alert.show();
            }
            showtooltips = false;
            for (Tile tile : tileGroup) {
                tile.hidenumbers();
            }
        } catch (Exception e) {
            //Alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Sudoku not possible");
            alert.show();
        }
    }

    private void clear() {
        for (int i = 0; i < 81; i++) {
            //Number --> 0
            tileGroup.get(i).setNum(null);
            //Edit it!
            tileGroup.get(i).setEditable(true);
        }
        showtooltips = false;
        for (Tile tile : tileGroup) {
            tile.hidenumbers();
        }
        updateprevious();
    }

    private void generate(int index) {
        //clears the tiles
        clear();
        //new instance of Sudoku
        Sudoku psudoku = new Sudoku();
        //calls generate sudoku
        Integer[][] pint = psudoku.generatesudoku(index);
        //sets it
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                //Sets Number
                tileGroup.get(counter).setNum(pint[i][j]);
                //Makes it uneditable
                if(pint[i][j] != 0) {
                    tileGroup.get(counter).setEditable(false);
                }
                counter++;
            }
        }
        updateprevious();
    }

    private void possibilities() {
        if (showtooltips) {
            showtooltips = false;
            for (Tile tile : tileGroup) {
                tile.hidenumbers();
            }
        } else {
            showtooltips = true;
            tileGroup.get(0).setupdated(true);
            for (Tile tile : tileGroup) {
                tile.shownumbers();
            }
            updatepossibilities();
        }
    }

    //Updates the possibilites
    private void updatepossibilities(){
        //Event detector (nice)
        boolean updated = false;

        for (Tile tile : tileGroup) {
            if (tile.getupdated()) {
                System.out.println("Detected Update");
                updated = true;
                break;
            }
        }

        /*
            Generates the possibilities
         */
        if (updated) {
            //removes the updated batch
            for (Tile tile : tileGroup) {
                tile.setupdated(false);
            }

            /*
                tooltip updater
             */
            if(showtooltips) {
                //new Instance of Sudoku
                Sudoku psudoku = new Sudoku();
                Integer[][] intarr = intconvert();
                //Gets Possibilities
                Integer[][][] pintarr = psudoku.possibilities(intarr);
                //Hides all
                for (Tile tile : tileGroup) {
                    tile.hidenumbers();
                }
                //Shows them
                int counter = 0;
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        for (int k = 0; k < pintarr[i][j].length; k++) {
                            tileGroup.get(counter).shownumber(pintarr[i][j][k]);
                        }
                        counter++;
                    }
                }
            }

            /*
                handles the previous sudoku
             */

            //what tile changed?
            for (Tile tile : tileGroup) {
                if(!Objects.equals(tile.getNum(), previousSudoku[tile.y][tile.x])){
                    updateprevious();
                    if(tile.getNum() != null) {
                        //determind if the change works
                        Sudoku subSubdoku = new Sudoku();
                        //solves it looks and difficulty and then decides if its good or not
                        try {
                            //solves it
                            subSubdoku.sudokusolver(intconvert());
                            //if it gets till here it seems to work, so we paint it 2
                            tile.changeTileStyle(2);
                        } catch (Exception e) {
                            //doesnt seem to work? well then we color it red
                            tile.changeTileStyle(3);
                        }
                    }
                    else {
                        tile.changeTileStyle(0);
                    }
                }
            }
        }
    }

    //updates the saved state of the sudoku to the given state atm
    private void updateprevious(){
        for (Tile tile: tileGroup) {
            previousSudoku[tile.y][tile.x] = tile.getNum();
        }
    }

    /*
        Converters
     */
    //Tile --> Integer
    private Integer[][] intconvert() {
        Integer[][] intsudoku = new Integer[9][9];
        int counter = 0;
        //-->intsudoku
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                intsudoku[i][j] = tileGroup.get(counter).getNum();
                counter++;
            }
        }
        return intsudoku;
    }

    //Integer --> Tile
    private void tileconvert(Integer[][] intsudoku) {
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tileGroup.get(counter).setNum(intsudoku[i][j]);
                counter++;
            }
        }
    }
}
