package fr.univtln.m1im.png.gui;


import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univtln.m1im.png.dto.GroupeDTO;
import fr.univtln.m1im.png.dto.ProfesseurDTO;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.DemandeCreneau;
import fr.univtln.m1im.png.model.Groupe;
import fr.univtln.m1im.png.model.Professeur;
import fr.univtln.m1im.png.model.Salle;
import fr.univtln.m1im.png.repositories.CreneauRepository;
import fr.univtln.m1im.png.repositories.DemandeCreneauRepository;
import fr.univtln.m1im.png.repositories.GroupeRepository;
import fr.univtln.m1im.png.repositories.ModuleRepository;
import fr.univtln.m1im.png.repositories.ProfesseurRepository;
import fr.univtln.m1im.png.repositories.SalleRepository;
import jakarta.persistence.EntityManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import fr.univtln.m1im.png.model.Module;

import java.util.logging.Logger;

@Getter @Setter
public class AjouterCours {
    private static final Logger log = Logger.getLogger(AjouterCours.class.getName());
    
    private int width;
    private int height;
    private EntityManager entityManager;
    private int anneeDebut;
    private String role;
    private Gui gui;

    public AjouterCours( int width, int height, EntityManager entityManager, int anneeDebut, String role, Gui gui) {
        this.entityManager = entityManager;
        this.width = width;
        this.height = height;
        this.anneeDebut = anneeDebut;
        this.role = role;
        this.gui = gui;
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
        ComboBox<String> moduleField1 = new ComboBox<>();
        moduleField1.setPromptText("Sélectionner un module 1");
        ComboBox<String> moduleField2 = new ComboBox<>();
        moduleField2.setPromptText("Sélectionner un module 2");
        ModuleRepository moduleRepository  = new ModuleRepository(entityManager);
        moduleField1.getItems().addAll(moduleRepository.getAllModulesCodes(0, 100));
        moduleField2.getItems().addAll(moduleRepository.getAllModulesCodes(0, 100));
        
        ComboBox<String> profField1 = new ComboBox<>();
        profField1.setPromptText("Sélectionner un professeur 1");
        ComboBox<String> profField2 = new ComboBox<>();
        profField2.setPromptText("Sélectionner un professeur 2");
        ProfesseurRepository professeurRepository = new ProfesseurRepository(entityManager);
        List<ProfesseurDTO> proflist = professeurRepository.getAllDTO(0, 100);
        for (ProfesseurDTO p : proflist){
            profField1.getItems().add(p.getNom() + " " + p.getPrenom());
            profField2.getItems().add(p.getNom() + " " + p.getPrenom());
        }
        
        ComboBox<String> groupeField1 = new ComboBox<>();
        groupeField1.setPromptText("Sélectionner un groupe 1");
        ComboBox<String> groupeField2 = new ComboBox<>();
        groupeField2.setPromptText("Sélectionner un groupe 2");
        GroupeRepository groupeRepository = new GroupeRepository(entityManager);
        List<GroupeDTO> grouplist = groupeRepository.getAllDTO(0, 100);
        for (GroupeDTO g : grouplist){
            groupeField1.getItems().add(g.getCode());
            groupeField2.getItems().add(g.getCode());
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
        ComboBox<String> jourField = new ComboBox<>();
        jourField.setPromptText("Sélectionner un jour");
        ComboBox<String> heureField = new ComboBox<>();
        heureField.setPromptText("Sélectionner une heure");
        heureField.setPrefWidth(200);
        ArrayList<String> heures = new ArrayList<>();
        for (int i = 8; i < 20; i++) {
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
        minuteField.setPrefWidth(200);
        
        ComboBox<String> heurefin = new ComboBox<>();
        heurefin.setPromptText("Sélectionner une heure fin");
        heurefin.setPrefWidth(200);
        heurefin.getItems().addAll(heures);
        
        ComboBox<String> minutefin = new ComboBox<>();
        minutefin.setPromptText("Sélectionner une minute");
        minutefin.setPrefWidth(200);
        minutefin.getItems().addAll(minutes);

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
        grid.add(new Label("Module 1"), 0, row);
        grid.add(new Label("Module 2"), 1, row++); 
        grid.add(moduleField1, 0, row);
        grid.add(moduleField2, 1, row++);
        grid.add(new Label("Professeur 1"), 0, row);
        grid.add(new Label("Professeur 2"), 1, row++);
        grid.add(profField1, 0, row);
        grid.add(profField2, 1, row++);
        grid.add(new Label("Groupe 1"), 0, row);
        grid.add(new Label("Groupe 2"), 1, row++);
        grid.add(groupeField1, 0, row);
        grid.add(groupeField2, 1, row++);
        grid.add(new Label("Type"), 0, row++); 
        grid.add(typeField, 0, row++);
        grid.add(new Label("Salle"), 0, row++); 
        grid.add(salleField, 0, row++);

        grid.add(new Label("Année"), 0, row);
        grid.add(new Label("Mois"), 1, row);
        grid.add(new Label("Jour"), 2, row);
        grid.add(new Label("Heure"), 3, row); 
        grid.add(new Label("Minute"), 4, row++);  
        grid.add(anneeField, 0, row);
        grid.add(moisField, 1, row);
        grid.add(jourField, 2, row);
        grid.add(heureField, 3, row);
        grid.add(minuteField, 4, row++);
        
        grid.add(new Label("Heure Fin"), 3, row); 
        grid.add(new Label("Minute"), 4, row++); 
        grid.add(heurefin ,3, row);
        grid.add(minutefin, 4, row++);

        grid.add(new Label("Étendre de semaine à semaine"), 0, row++, 2, 1);
        grid.add(semaineDebutField, 0, row);
        grid.add(semaineFinField, 1, row++);

        grid.add(buttonBox, 0, row++, 6, 1);
        grid.add(errorLabel, 1, row++, 6, 1);

        // Map des mois avec leur index
        Map<String, Integer> moisMap = new HashMap<>();
        moisMap.put("Janvier", 1);
        moisMap.put("Février", 2);
        moisMap.put("Mars", 3);
        moisMap.put("Avril", 4);
        moisMap.put("Mai", 5);
        moisMap.put("Juin", 6);
        moisMap.put("Juillet", 7);
        moisMap.put("Août", 8);
        moisMap.put("Septembre", 9);
        moisMap.put("Octobre", 10);
        moisMap.put("Novembre", 11);
        moisMap.put("Décembre", 12);

        // Actions des boutons
        annulerButton.setOnAction(e -> stage.close());
        validerButton.setOnAction(e -> {
            // Validation des champs
            if ((moduleField1.getValue() == null && moduleField2.getValue() == null) || 
                (profField1.getValue() == null && profField2.getValue() == null) || 
                (groupeField1.getValue() == null && groupeField2.getValue() == null) ||
                typeField.getValue() == null || salleField.getValue() == null || anneeField.getValue() == null ||
                moisField.getValue() == null || jourField.getValue() == null || heureField.getValue() == null ||
                minuteField.getValue() == null || heurefin.getValue() == null || minutefin.getValue() == null) {
                errorLabel.setText("Veuillez remplir tous les champs !");
                errorLabel.setVisible(true);
                return;
            } else {
                OffsetDateTime heureDebut = OffsetDateTime.of(
                    Integer.parseInt(anneeField.getValue()),
                    moisMap.get(moisField.getValue()),
                    Integer.parseInt(jourField.getValue()),
                    Integer.parseInt(heureField.getValue()),  
                    Integer.parseInt(minuteField.getValue()),
                    0, 0,
                    ZoneOffset.UTC
                );
                OffsetDateTime heureFin = OffsetDateTime.of(
                    Integer.parseInt(anneeField.getValue()),
                    moisMap.get(moisField.getValue()),
                    Integer.parseInt(jourField.getValue()),
                    Integer.parseInt(heurefin.getValue()),
                    Integer.parseInt(minutefin.getValue()),
                    0, 0,
                    ZoneOffset.UTC
                );

                if (!heureDebut.isBefore(heureFin)) {
                    errorLabel.setText("L'heure de début doit être avant l'heure de fin !");
                    errorLabel.setVisible(true);
                    return;
                }
                Module module = moduleRepository.getModuleByCode(moduleField1.getValue());
                List<Professeur> professeurlist = professeurRepository.getAll(0, 100);
                Professeur professeur = professeurlist.get(profField1.getSelectionModel().getSelectedIndex());
                Groupe groupe = groupeRepository.getByCode(groupeField1.getValue());
                Salle salle = salleRepository.getByCode(salleField.getValue());
                Creneau creneau = Creneau.builder()
                                    .type(typeField.getValue())
                                    .heureDebut(heureDebut)
                                    .heureFin(heureFin)
                                    .salle(salle)
                                    .build();
                creneau.getGroupes().add(groupe);
                creneau.getProfesseurs().add(professeur);
                creneau.getModules().add(module);
                if (groupeField2.getValue() != null) {
                    Groupe groupe2 = groupeRepository.getByCode(groupeField2.getValue());
                    creneau.getGroupes().add(groupe2);
                }
                if (profField2.getValue() != null) {
                    Professeur professeur2 = professeurlist.get(profField2.getSelectionModel().getSelectedIndex());
                    creneau.getProfesseurs().add(professeur2);
                }
                if (moduleField2.getValue() != null) {
                    Module module2 = moduleRepository.getModuleByCode(moduleField2.getValue());
                    creneau.getModules().add(module2);
                }
                if (this.role.equals("Ajouter")) {
                    CreneauRepository creneauRepository = new CreneauRepository(entityManager);
                    String res = creneauRepository.addCreneau(creneau, null);
                    if (res == "Le créneau a été inséré") {
                        errorLabel.setText("Créneau ajouté !");
                        gui.genererCreneaux();

                    } else {
                        errorLabel.setText(res);
                    }
                    errorLabel.setText(res);
                
                }
                else if (this.role.equals("Demander")) {
                    DemandeCreneau demandeCreneau = DemandeCreneau.makeFromCreneau(creneau);
                    DemandeCreneauRepository demandeCreneauRepository = new DemandeCreneauRepository(entityManager);
                    String res = demandeCreneauRepository.addDemandeCreneau(demandeCreneau);
                    errorLabel.setText(res);
                    log.info(res);
                }
            }
        });

        //  Ajout de la scène et affichage de la fenêtre
        Scene scene = new Scene(grid, this.width/1.2, this.height/1.35);
        stage.setScene(scene);
        stage.show();
        

        
    }
}