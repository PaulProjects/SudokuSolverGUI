package com.example.sudoku;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.Objects;

public class Tile{
    /*
            Instances Visual Objects (?)
     */
    //coordinates
    int x;
    int y;

    //Stackpane with Background and the Text
    private StackPane stackPane = new StackPane();
    //Background
    private Region rectangle = new Region();
    //Text
    private TextField textField = new TextField("");
    //got updated
    private boolean gotupdated = true;

    //Magic pane
    private GridPane gridPane = new GridPane();

    //List containing the Numbers
    ArrayList<TextField> list = new ArrayList<>();

    /*
            Getter / Setter Methods
     */

    public StackPane getStackPane() {
        return stackPane;
    }

    public TextField getTextField(){return textField;}

    public boolean getupdated(){
            return gotupdated;
    }

    public void setupdated(boolean b){
        gotupdated = b;
    }

    //changes the user permission to edit and the style (j = true --> let user edit)
    public void setEditable(boolean j){
        textField.setEditable(j);
        if(j){
            changeTileStyle(0);
        }
        else{
            changeTileStyle(1);
        }
    }

    public void setNum(Integer i){
        if(i != null) {
            textField.setText(String.valueOf(i));
        }
        else{
            textField.setText("");
        }
    }

    public Integer getNum(){
        if(!Objects.equals(textField.getText(), "")) {
            return Integer.valueOf(textField.getText());
        }
        else{
            return null;
        }
    }

    public void hidenumbers(){
        for (TextField textField : list) {
            textField.setVisible(false);
        }
    }

    public void shownumbers(){
        for (TextField textField : list) {
            textField.setVisible(true);
        }
    }

    public void shownumber(int num){
        for (TextField textField : list) {
            if(textField.getText().equals(String.valueOf(num))){
                textField.setVisible(true);
            }
        }
    }

    /*
        Main Method for configuring stuff
     */
    public Tile(int px, int py){
        //sets coords
        x = px;
        y = py;

        //UI Elements from here
        /*
            Rectangle
         */
        //Size
        rectangle.setPrefWidth(SudokuApp.TILE_SIZE);
        rectangle.setPrefHeight(SudokuApp.TILE_SIZE);

        //Changes style
        changeTileStyle(0);

         /*
                Possibilities
         */
        gridPane.setVgap(10);
        gridPane.setHgap(5);
        //creates the textFields in the List
        for (int i = 1; i < 10; i++) {
            list.add(new TextField(String.valueOf(i)));
        }

        //creates the style
        for (TextField tf :list) {
            tf.setStyle("-fx-background-color: transparent; ");
            //hides them (till the user decides otherwise)
            tf.setVisible(false);
        }
        //adds them to the grid pane
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gridPane.add(list.get(counter),j,i);
                counter++;
            }
        }


        /*
            TextField
         */
        textField.setAlignment(Pos.CENTER);
        textField.setPrefWidth(SudokuApp.TILE_SIZE);
        textField.setPrefHeight(SudokuApp.TILE_SIZE);
        textField.setEditable(true);
        textField.setStyle("-fx-background-color: transparent; ");
        textField.setFont(Font.font("Verdana", 20));

        /*
            StackPane
         */
        stackPane.getChildren().addAll(rectangle,gridPane, textField);
        stackPane.setPrefWidth(SudokuApp.TILE_SIZE);
        stackPane.setPrefHeight(SudokuApp.TILE_SIZE);
        stackPane.relocate(x * SudokuApp.TILE_SIZE, y * SudokuApp.TILE_SIZE);

        /*
            Listeners
         */
        //only numbers in the field
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            gotupdated = true;
            //one digit or nothing
            if (!newValue.matches("[1-9]|$")) {
                textField.setText(oldValue);
            }
        });

    }

    //Changes Style of the Tiles
    //Style 0: White Background
    //Style 1: Grey Background
    //Style 2: #D0FDFB Background
    //Style 3: Red Background
    public void changeTileStyle(int s){
        switch (s) {
            case 0 -> {
                rectangle.setStyle("-fx-fill: white; -fx-border-width: 2 2 2 2; -fx-border-color: black;");
                //Right Border
                if (x == 2 || x == 5) {
                    rectangle.setStyle("-fx-fill: white; -fx-border-width: 2 5 2 2; -fx-border-color: black;");
                }
                //Bottom Border
                if (y == 2 || y == 5) {
                    rectangle.setStyle("-fx-fill: white; -fx-border-width: 2 2 5 2; -fx-border-color: black;");
                }
                if (x == 2 && y == 2 || x == 5 && y == 5 || x == 2 && y == 5 || x == 5 && y == 2) {
                    rectangle.setStyle("-fx-fill: white; -fx-border-width: 2 5 5 2; -fx-border-color: black;");
                }
            }
            case 1 -> {
                rectangle.setStyle("-fx-fill: green; -fx-border-width: 2 2 2 2; -fx-border-color: black; -fx-background-color: #C0C0C0;");
                //Right Border
                if (x == 2 || x == 5) {
                    rectangle.setStyle("-fx-fill: white; -fx-border-width: 2 5 2 2; -fx-border-color: black; -fx-background-color: #C0C0C0;");
                }
                //Bottom Border
                if (y == 2 || y == 5) {
                    rectangle.setStyle("-fx-fill: white; -fx-border-width: 2 2 5 2; -fx-border-color: black; -fx-background-color: #C0C0C0;");
                }
                if (x == 2 && y == 2 || x == 5 && y == 5 || x == 2 && y == 5 || x == 5 && y == 2) {
                    rectangle.setStyle("-fx-fill: white; -fx-border-width: 2 5 5 2; -fx-border-color: black; -fx-background-color: #C0C0C0;");
                }
            }
            case 2 -> {
                rectangle.setStyle("-fx-fill: green; -fx-border-width: 2 2 2 2; -fx-border-color: black; -fx-background-color: #D0FDFB;");
                //Right Border
                if (x == 2 || x == 5) {
                    rectangle.setStyle("-fx-fill: white; -fx-border-width: 2 5 2 2; -fx-border-color: black; -fx-background-color: #D0FDFB;");
                }
                //Bottom Border
                if (y == 2 || y == 5) {
                    rectangle.setStyle("-fx-fill: white; -fx-border-width: 2 2 5 2; -fx-border-color: black; -fx-background-color: #D0FDFB;");
                }
                if (x == 2 && y == 2 || x == 5 && y == 5 || x == 2 && y == 5 || x == 5 && y == 2) {
                    rectangle.setStyle("-fx-fill: white; -fx-border-width: 2 5 5 2; -fx-border-color: black; -fx-background-color: #D0FDFB;");
                }
            }
            case 3 -> {
                rectangle.setStyle("-fx-fill: green; -fx-border-width: 2 2 2 2; -fx-border-color: black; -fx-background-color: #ff0000;");
                //Right Border
                if (x == 2 || x == 5) {
                    rectangle.setStyle("-fx-fill: white; -fx-border-width: 2 5 2 2; -fx-border-color: black; -fx-background-color: #ff0000;");
                }
                //Bottom Border
                if (y == 2 || y == 5) {
                    rectangle.setStyle("-fx-fill: white; -fx-border-width: 2 2 5 2; -fx-border-color: black; -fx-background-color: #ff0000;");
                }
                if (x == 2 && y == 2 || x == 5 && y == 5 || x == 2 && y == 5 || x == 5 && y == 2) {
                    rectangle.setStyle("-fx-fill: white; -fx-border-width: 2 5 5 2; -fx-border-color: black; -fx-background-color: #ff0000;");
                }
            }
        }
    }
}
