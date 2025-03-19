package fr.univtln.yroblin156;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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
        int width = 1920;
        int height = 1000;
        Scene scene = new Scene(group, width, height);
        Pane pane = new Pane();
        // gridPane grid = new gridPane();
        // grid.setPrefSize(200, 200);
        // grid.setTranslateX(400);
        // grid.setTranslateY(200);
        // pane.setTranslateX(400);
        // // pane.setTranslateY(200);
        // pane.setMaxSize(200, 200);
        // group.getChildren().add(pane);
        // Grille grille = new Grille(pane, width, height);
        Grille grille = new Grille(group, width, height);
        Creneau cr1 = new Creneau("", 0, "8:00", "1:00", "Maths", 0, "A1", "Prof", 1);
        //grille.ajouterCreneau(cr1);

        // Canvas canvas = new Canvas(640, 480);
        // GraphicsContext gc = canvas.getGraphicsContext2D();
        // gc.setStroke(Color.BLACK);
        // gc.strokeRect(100, 100, 100, 5);

        // gc.setFill(Color.GRAY);
        // gc.fillRect(0, 0, 640, 480);
        // gc.setStroke(Color.BLACK);
        // gc.strokeRect(200, 200, 400, 400);
        // gc.setFill(Color.ROYALBLUE);

        // gc.fillRect(75, 75, 100, 100);
        // gc.strokeText("Hello World", 100, 100);
        // group.getChildren().add(canvas);

        stage.setScene(scene);
        stage.setTitle("Hyperplanning");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}