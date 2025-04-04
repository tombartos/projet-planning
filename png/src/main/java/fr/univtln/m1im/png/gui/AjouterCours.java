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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AjouterCours {
    
    private int width;
    private int height;
    private String role;

    public AjouterCours( int width, int height, String role) {
        this.width = width;
        this.height = height;
        this.role = role;
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
        ComboBox<String> moduleField = new ComboBox<>();
        moduleField.setPromptText("Sélectionner un module");
        ComboBox<String> profField = new ComboBox<>();
        profField.setPromptText("Sélectionner un professeur");
        ComboBox<String> groupeField = new ComboBox<>();
        groupeField.setPromptText("Sélectionner un groupe");
        ComboBox<String> typeField = new ComboBox<>();
        typeField.setPromptText("Sélectionner un type de cours");
        typeField.getItems().addAll("CM", "TD", "TP", "Examen");
        ComboBox<String> salleField = new ComboBox<>();
        salleField.setPromptText("Sélectionner une salle");

        // Sélection de date et heure
        ComboBox<String> anneeField =new ComboBox<>();
        anneeField.setPromptText("Sélectionner une année");
        anneeField.setPrefWidth(200);
        ComboBox<String> moisField = new ComboBox<>();
        moisField.setPromptText("Sélectionner un mois");
        moisField.setPrefWidth(250);
        ComboBox<String> jourField = new ComboBox<>();
        jourField.setPromptText("Sélectionner un jour");
        jourField.setPrefWidth(250);
        ComboBox<String> heureField = new ComboBox<>();
        heureField.setPromptText("Sélectionner une heure");
        heureField.setPrefWidth(200);
        ComboBox<String> minuteField = new ComboBox<>();
        minuteField.setPromptText("Sélectionner une minute");
        minuteField.setPrefWidth(200);
        ComboBox<String> heurefin = new ComboBox<>();
        heurefin.setPromptText("Sélectionner une heure fin");
        heurefin.setPrefWidth(200);
        ComboBox<String> minutefin = new ComboBox<>();
        minutefin.setPromptText("Sélectionner une minute");
        minutefin.setPrefWidth(200);

        // Étendre les semaines
        ComboBox<String> semaineDebutField = new ComboBox<>();
        semaineDebutField.setPromptText("Sélectionner la semaine de début");
        ComboBox<String> semaineFinField = new ComboBox<>();
        semaineFinField.setPromptText("Sélectionner la semaine de fin");

        // Label pour afficher les erreurs
        Label errorLabel = new Label("Exemple d'erreur affichée ici");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(new Font(13));
        errorLabel.setVisible(true); 
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

        int rowLabeleFin = row++;
        int rowEntryFin = row++;
        grid.add(new Label("Heure Fin"), 3, rowLabeleFin); 
        grid.add(heurefin ,3, rowEntryFin);
        grid.add(new Label("Minute"), 4, rowLabeleFin); 
        grid.add(minutefin, 4, rowEntryFin);

        grid.add(new Label("Étendre de semaine à semaine"), 0, row++, 2, 1);
        grid.add(semaineDebutField, 0, row);
        grid.add(semaineFinField, 1, row++);

        grid.add(buttonBox, 0, row++, 6, 1);
        grid.add(errorLabel, 1, row++, 6, 1);

        // Actions des boutons
        annulerButton.setOnAction(e -> stage.close());
        validerButton.setOnAction(e -> {
            if (this.role.equals("Ajouter")) {
                System.out.println("Cours ajouté !");
                stage.close();
            }
            else if (this.role.equals("Demander")) {
                System.out.println("Demande de cours envoyée !");
                stage.close();
            }
            
        });

        //  Ajout de la scène et affichage de la fenêtre
        Scene scene = new Scene(grid, this.width/1.2, this.height/1.35);
        stage.setScene(scene);
        stage.show();

        // les methodes pour remplir les ComboBox
        moduleField.setOnAction(event -> {

        });
        profField.setOnAction(event -> {

        });
        groupeField.setOnAction(event -> {

        });
        salleField.setOnAction(event -> {

        });
        anneeField.setOnAction(event -> {

        });
        moisField.setOnAction(event -> {

        });
        heureField.setOnAction(event -> {

        });
        minuteField.setOnAction(event -> {

        });
        semaineDebutField.setOnAction(event -> {

        });
        semaineFinField.setOnAction(event -> {

        });


        
    }
}