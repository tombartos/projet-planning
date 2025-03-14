package fr.univtln.yroblin156;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        Group group = new Group();
        Scene scene = new Scene(group, 1920, 1080);
        Grille grille = new Grille(group, 1920, 1080);
        // Canvas canvas = new Canvas(640, 480);
        // GraphicsContext gc = canvas.getGraphicsContext2D();
        // gc.setFill(Color.GRAY);
        // gc.fillRect(0, 0, 640, 480);
        // gc.setStroke(Color.BLACK);
        // gc.strokeRect(200, 200, 400, 400);
        // gc.setFill(Color.ROYALBLUE);

        // gc.fillRect(75, 75, 100, 100);
        // gc.strokeText("Hello World", 100, 100);
        // group.getChildren().add(canvas);

        stage.setScene(scene);
        stage.setTitle("Hello World!");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}