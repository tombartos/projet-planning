package fr.univtln.m1im.png;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    private VBox contentArea = new VBox(); // zone où s'affiche la liste

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Barre de boutons
        HBox topBar = new HBox(10);
        Button salleBtn = new Button("Salle");
        Button groupeBtn = new Button("Groupe");
        Button formationBtn = new Button("Formation");

        topBar.getChildren().addAll(salleBtn, groupeBtn, formationBtn);

        // Zone de contenu sous la barre
        contentArea.setSpacing(10);
        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200); // hauteur de la zone de scroll

        root.getChildren().addAll(topBar, scrollPane);

        // Action bouton "Salle"
        salleBtn.setOnAction(e -> afficherListeSalles());

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Boutons + Liste Salle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void afficherListeSalles() {
        contentArea.getChildren().clear();

        // Exemple de liste de salles
        for (int i = 1; i <= 20; i++) {
            Label salleLabel = new Label("Salle " + i);
            salleLabel.setStyle("-fx-padding: 10px; -fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7;");
            contentArea.getChildren().add(salleLabel);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
