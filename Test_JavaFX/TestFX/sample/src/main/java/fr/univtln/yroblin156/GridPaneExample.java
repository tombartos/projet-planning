package fr.univtln.yroblin156;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GridPaneExample extends Application {
    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);
        root.setStyle("-fx-background-color: red; -fx-stroke: #000000; -fx-stroke-width: 1;");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.setMinSize(200, 200);
        grid.setStyle("-fx-background-color: lightgray;");
        root.add(grid, 1, 2);

        // Ajouter des "Frames" à la grille
        Label frame1 = new Label("Frame (0,0)");
        frame1.setStyle("-fx-background-color: lightblue; -fx-padding: 20px; -fx-border-color: black;");

        Label frame2 = new Label("Frame (0,1)");
        frame2.setStyle("-fx-background-color: lightgreen; -fx-padding: 20px; -fx-border-color: black;");

        Label frame3 = new Label("Frame (1,0)");
        frame3.setStyle("-fx-background-color: lightcoral; -fx-padding: 20px; -fx-border-color: black;");

        root.add(frame1, 0, 0);
        root.add(frame2, 1, 0);
        root.add(frame3, 0, 1);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("GridPane (Frames en Grille)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}