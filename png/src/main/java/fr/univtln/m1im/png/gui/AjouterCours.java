package fr.univtln.m1im.png.gui;


import java.util.ArrayList;
import java.util.List;

import fr.univtln.m1im.png.dto.GroupeDTO;
import fr.univtln.m1im.png.dto.ProfesseurDTO;
import fr.univtln.m1im.png.model.Professeur;
import fr.univtln.m1im.png.model.Salle;
import fr.univtln.m1im.png.repositories.GroupeRepository;
import fr.univtln.m1im.png.repositories.ModuleRepository;
import fr.univtln.m1im.png.repositories.ProfesseurRepository;
import fr.univtln.m1im.png.repositories.SalleRepository;
import jakarta.persistence.EntityManager;
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

import java.util.logging.Logger;

@Getter @Setter
public class AjouterCours {
    private static final Logger log = Logger.getLogger(AjouterCours.class.getName());
    
    private int width;
    private int height;
    private EntityManager entityManager;
    private int anneeDebut;
    private String role;

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
        ComboBox<String> moduleField = new ComboBox<>();
        moduleField.setPromptText("Sélectionner un module");
        ModuleRepository moduleRepository  = new ModuleRepository(entityManager);
        moduleField.getItems().addAll(moduleRepository.getAllModulesCodes(0, 100));
        
        ComboBox<String> profField = new ComboBox<>();
        profField.setPromptText("Sélectionner un professeur");
        ProfesseurRepository professeurRepository = new ProfesseurRepository(entityManager);
        List<ProfesseurDTO> proflist = professeurRepository.getAllDTO(0, 100);
        for (ProfesseurDTO p : proflist){
            profField.getItems().add(p.getNom() + " " + p.getPrenom());
        }
        
        ComboBox<String> groupeField = new ComboBox<>();
        groupeField.setPromptText("Sélectionner un groupe");
        GroupeRepository groupeRepository = new GroupeRepository(entityManager);
        List<GroupeDTO> grouplist = groupeRepository.getAllDTO(0, 100);
        for (GroupeDTO g : grouplist){
            groupeField.getItems().add(g.getCode());
        }

        ComboBox<String> typeField = new ComboBox<>();
        typeField.setPromptText("Sélectionner un type de cours");
        typeField.getItems().addAll("CM", "TD", "TP", "Exam");
        
        ComboBox<String> salleField = new ComboBox<>();
        salleField.setPromptText("Sélectionner une salle");
        SalleRepository salleRepository = new SalleRepository(entityManager);
        List<Salle> sallelist = salleRepository.getAll(0, 100);
        for (Salle s : sallelist){
            salleField.getItems().add(s.getCode());
        }


        // Sélection de date et heure
        ComboBox<String> anneeField =new ComboBox<>();
        anneeField.setPromptText("Sélectionner une année");
        anneeField.setPrefWidth(200);
        anneeField.getItems().addAll(String.valueOf(anneeDebut), String.valueOf(anneeDebut + 1));
        
        ComboBox<String> moisField = new ComboBox<>();
        moisField.setPromptText("Sélectionner un mois");
        TextField jourField = new TextField();
        ComboBox<String> heureField = new ComboBox<>();
        heureField.setPromptText("Sélectionner une heure");
        heureField.setPrefWidth(200);
        ArrayList<String> heures = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            heures.add(String.format("%02d", i)); 
        }
        heureField.getItems().addAll(heures);

        ComboBox<String> minuteField = new ComboBox<>();
        minuteField.setPromptText("Sélectionner une minute");
        ArrayList<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minutes.add(String.format("%02d", i));
        }
        minuteField.getItems().addAll(minutes);
        
        anneeField.setOnAction(event -> {
            log.info("Année sélectionnée : " + anneeField.getValue());
            String selectedYear = anneeField.getValue();
            if (selectedYear != null) {
                int year = Integer.parseInt(selectedYear);
                // Remplir le mois en fonction de l'année sélectionnée
                if (year == anneeDebut) {
                    moisField.getItems().clear();
                    moisField.getItems().addAll("Septembre", "Octobre", "Novembre", "Décembre");
                } else {
                    moisField.getItems().clear();
                    moisField.getItems().addAll("Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                        "Juillet");
                }
            }
        });

        moisField.setOnAction(event -> {
            String selectedMonth = moisField.getValue();
            if (selectedMonth != null) {
                int monthIndex = moisField.getItems().indexOf(selectedMonth) + 1; // +1 pour correspondre à l'index du mois
                jourField.getItems().clear(); // Réinitialiser les jours
                int daysInMonth = java.time.Month.of(monthIndex).length(false); // Nombre de jours dans le mois
                for (int i = 1; i <= daysInMonth; i++) {
                    jourField.getItems().add(String.valueOf(i));
                }
            }
        });

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
        grid.add(new Label("Étendre de semaine à semaine"), 0, row++, 2, 1);
        grid.add(semaineDebutField, 0, row);
        grid.add(semaineFinField, 1, row++);

        grid.add(buttonBox, 0, row++, 6, 1);
        grid.add(errorLabel, 1, row++, 6, 1);

        // Actions des boutons
        annulerButton.setOnAction(e -> stage.close());
        validerButton.setOnAction(e -> {
            // Validation des champs
            if (moduleField.getValue() == null || profField.getValue() == null || groupeField.getValue() == null ||
                typeField.getValue() == null || salleField.getValue() == null || anneeField.getValue() == null ||
                moisField.getValue() == null || jourField.getValue() == null || heureField.getValue() == null ||
                minuteField.getValue() == null) {
                errorLabel.setText("Veuillez remplir tous les champs !");
                errorLabel.setVisible(true);
                return;
            } else {
                errorLabel.setVisible(false);
            }
            System.out.println("Cours ajouté !");
            stage.close();
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
        Scene scene = new Scene(grid, this.width/1.2, this.height/1.45);
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