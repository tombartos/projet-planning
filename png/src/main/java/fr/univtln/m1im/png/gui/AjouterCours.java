package fr.univtln.m1im.png.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AjouterCours {
    
    private int width;
    private int height;

    public AjouterCours( int width, int height) {
        this.width = width;
        this.height = height;
    }   

    public void afficherFenetreAjoutCours() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Ajouter Cours");

        // GridPane pour organiser les champs
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        // Champs de saisie
        TextField moduleField = new TextField();
        TextField profField = new TextField();
        TextField groupeField = new TextField();
        TextField typeField = new TextField();
        TextField salleField = new TextField();

        // Sélection de date et heure
        TextField anneeField = new TextField();
        TextField moisField = new TextField();
        TextField jourField = new TextField();
        TextField heureField = new TextField();
        TextField minuteField = new TextField();

        // Étendre les semaines
        TextField semaineDebutField = new TextField();
        TextField semaineFinField = new TextField();

        // Boutons
        Button annulerButton = new Button("Annuler");
        Button validerButton = new Button("Valider");
        HBox buttonBox = new HBox(10, annulerButton, validerButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // Ajout au grid
        int row = 0;
        grid.add(new Label("Module"), 0, row++); 
        grid.add(moduleField, 0, row++);
        grid.add(new Label("Professeur"), 0, row++); 
        grid.add(profField, 0, row++);
        grid.add(new Label("Groupe"), 0, row++); 
        grid.add(groupeField, 0, row++);
        grid.add(new Label("Type"), 0, row++); 
        grid.add(typeField, 0, row++);
        grid.add(new Label("Salle"), 0, row++); 
        grid.add(salleField, 0, row++);

        int row_lable_anne = row++;
        int row_entry = row++;

        grid.add(new Label("Année"), 0, row_lable_anne); 
        grid.add(anneeField, 0, row_entry);
        grid.add(new Label("Mois"), 1, row_lable_anne); 
        grid.add(moisField, 1, row_entry);
        grid.add(new Label("Jour"), 2, row_lable_anne); 
        grid.add(jourField, 2, row_entry);
        grid.add(new Label("Heure"), 3, row_lable_anne); 
        grid.add(heureField, 3, row_entry);
        grid.add(new Label("Minute"), 4, row_lable_anne); 
        grid.add(minuteField, 4, row_entry);
        grid.add(new Label("Étendre de semaine à semaine"), 0, row++, 2, 1);
        grid.add(semaineDebutField, 0, row);
        grid.add(semaineFinField, 1, row++);

        grid.add(buttonBox, 0, row, 6, 1);

        // Actions des boutons
        annulerButton.setOnAction(e -> stage.close());
        validerButton.setOnAction(e -> {
            System.out.println("Cours ajouté !");
            stage.close();
        });

        // 🚀 Ajout de la scène et affichage de la fenêtre
        Scene scene = new Scene(grid, this.width/1.5, this.height/1.45);
        stage.setScene(scene);
        stage.show();

        
    }
}


