package fr.univtln.m1im.png;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginPage extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Création du GridPane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); // Centrer le contenu
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Titre
        Label title = new Label("Connexion");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        grid.add(title, 0, 0, 2, 1); // colonne 0, ligne 0, colspan 2

        // Label + champ utilisateur
        Label userLabel = new Label("Nom d'utilisateur :");
        grid.add(userLabel, 0, 1);

        TextField userField = new TextField();
        grid.add(userField, 1, 1);

        // Label + champ mot de passe
        Label passLabel = new Label("Mot de passe :");
        grid.add(passLabel, 0, 2);

        PasswordField passField = new PasswordField();
        grid.add(passField, 1, 2);

        // Bouton de connexion
        Button loginBtn = new Button("Se connecter");
        grid.add(loginBtn, 1, 3);

        // Message d'erreur / succès
        Label message = new Label();
        grid.add(message, 0, 4, 2, 1); // Colspan 2

        // Action sur bouton
        loginBtn.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            if ("admin".equals(username) && "1234".equals(password)) {
                message.setText("✅ Connexion réussie !");
                message.setStyle("-fx-text-fill: green;");
            } else {
                message.setText("❌ Identifiants incorrects !");
                message.setStyle("-fx-text-fill: red;");
            }
        });

        // Création et affichage de la scène
        Scene scene = new Scene(grid, 400, 250);
        primaryStage.setTitle("Page de Connexion");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

