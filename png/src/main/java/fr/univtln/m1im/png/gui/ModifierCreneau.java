package fr.univtln.m1im.png.gui;

//Cette classe est principalement copiee collee de la classe AjouterCreneau, nous avons conscience qu'il y a de la redondance
//TODO: Refactoriser le code dans AjouterCreneau pour eviter la redondance

import fr.univtln.m1im.png.dto.GroupeDTO;
import fr.univtln.m1im.png.dto.ProfesseurDTO;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.DemandeCreneau;
import fr.univtln.m1im.png.model.Groupe;
import fr.univtln.m1im.png.model.Professeur;
import fr.univtln.m1im.png.model.Responsable;
import fr.univtln.m1im.png.model.Utilisateur;
import fr.univtln.m1im.png.model.Salle;
import fr.univtln.m1im.png.repositories.CreneauRepository;
import fr.univtln.m1im.png.repositories.DemandeCreneauRepository;
import fr.univtln.m1im.png.repositories.GroupeRepository;
import fr.univtln.m1im.png.repositories.ModuleRepository;
import fr.univtln.m1im.png.repositories.ProfesseurRepository;
import fr.univtln.m1im.png.repositories.SalleRepository;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fr.univtln.m1im.png.model.Module;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import jakarta.persistence.EntityManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class ModifierCreneau {

    private final EntityManager entityManager;
    private final Gui gui;
    private final int anneeDebut;
    private final Creneau creneau;
    private String selectedYear = null;
    private Utilisateur utilisateur;
    private static final Logger log = Logger.getLogger(AjouterCours.class.getName());

    public ModifierCreneau(Creneau creneau, EntityManager entityManager, Gui gui) {
        this.creneau = creneau;
        this.entityManager = entityManager;
        this.gui = gui;
        this.anneeDebut = gui.getAnneeDebut();
        this.utilisateur = gui.getUtilisateur();
    }

    public void afficherModifierCreneau() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modifier Cours");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        // === Remplissage des champs comme dans ta méthode précédente ===
        ModuleRepository moduleRepository = new ModuleRepository(entityManager);
        ProfesseurRepository professeurRepository = new ProfesseurRepository(entityManager);
        GroupeRepository groupeRepository = new GroupeRepository(entityManager);
        SalleRepository salleRepository = new SalleRepository(entityManager);

        ComboBox<String> moduleField1 = new ComboBox<>();
        ComboBox<String> moduleField2 = new ComboBox<>();
        moduleField1.getItems().addAll(moduleRepository.getAllModulesCodes(0, 100));
        moduleField2.getItems().addAll(moduleRepository.getAllModulesCodes(0, 100));

        if (!creneau.getModules().isEmpty()) {
            moduleField1.setValue(creneau.getModules().get(0).getCode());
            if (creneau.getModules().size() > 1)
                moduleField2.setValue(creneau.getModules().get(1).getCode());
        }

        ComboBox<String> profField1 = new ComboBox<>();
        ComboBox<String> profField2 = new ComboBox<>();
        List<ProfesseurDTO> profs = professeurRepository.getAllDTO(0, 100);
        for (ProfesseurDTO p : profs) {
            String fullName = p.getNom() + " " + p.getPrenom();
            profField1.getItems().add(fullName);
            profField2.getItems().add(fullName);
        }
        if (!creneau.getProfesseurs().isEmpty()) {
            profField1.setValue(creneau.getProfesseurs().get(0).getNom() + " " + creneau.getProfesseurs().get(0).getPrenom());
            if (creneau.getProfesseurs().size() > 1)
                profField2.setValue(creneau.getProfesseurs().get(1).getNom() + " " + creneau.getProfesseurs().get(1).getPrenom());
        }

        ComboBox<String> groupeField1 = new ComboBox<>();
        ComboBox<String> groupeField2 = new ComboBox<>();
        List<GroupeDTO> groupes = groupeRepository.getAllDTO(0, 100);
        for (GroupeDTO g : groupes) {
            groupeField1.getItems().add(g.getCode());
            groupeField2.getItems().add(g.getCode());
        }
        if (!creneau.getGroupes().isEmpty()) {
            groupeField1.setValue(creneau.getGroupes().get(0).getCode());
            if (creneau.getGroupes().size() > 1)
                groupeField2.setValue(creneau.getGroupes().get(1).getCode());
        }

        ComboBox<String> typeField = new ComboBox<>();
        typeField.getItems().addAll("CM", "TD", "TP", "Exam");
        typeField.setValue(creneau.getType());

        ComboBox<String> salleField = new ComboBox<>();
        List<Salle> salles = salleRepository.getAll(0, 100);
        for (Salle s : salles) salleField.getItems().add(s.getCode());
        if (creneau.getSalle() != null) salleField.setValue(creneau.getSalle().getCode());

        OffsetDateTime debut = creneau.getHeureDebut();
        OffsetDateTime fin = creneau.getHeureFin();

        ComboBox<String> anneeField = new ComboBox<>();
        anneeField.getItems().addAll(String.valueOf(anneeDebut), String.valueOf(anneeDebut + 1));
        anneeField.setValue(String.valueOf(debut.getYear()));

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

        Map<Integer, String> reverseMoisMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : moisMap.entrySet()) {
            reverseMoisMap.put(entry.getValue(), entry.getKey());
        }

        ComboBox<String> moisField = new ComboBox<>();
        moisField.setValue(reverseMoisMap.get(debut.getMonthValue()));

        ComboBox<String> jourField = new ComboBox<>();
        for (int i = 1; i <= 31; i++) jourField.getItems().add(String.valueOf(i));
        jourField.setValue(String.valueOf(debut.getDayOfMonth()));

        ArrayList<String> heures = new ArrayList<>();
        for (int i = 8; i < 20; i++) heures.add(String.format("%02d", i));
        ComboBox<String> heureField = new ComboBox<>();
        heureField.getItems().addAll(heures);
        heureField.setValue(String.format("%02d", debut.getHour()));

        ArrayList<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) minutes.add(String.format("%02d", i));
        ComboBox<String> minuteField = new ComboBox<>();
        minuteField.getItems().addAll(minutes);
        minuteField.setValue(String.format("%02d", debut.getMinute()));

        ComboBox<String> heureFinField = new ComboBox<>();
        heureFinField.getItems().addAll(heures);
        heureFinField.setValue(String.format("%02d", fin.getHour()));

        ComboBox<String> minuteFinField = new ComboBox<>();
        minuteFinField.getItems().addAll(minutes);
        minuteFinField.setValue(String.format("%02d", fin.getMinute()));

        selectedYear = anneeField.getValue();
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

        anneeField.setOnAction(event -> {
            selectedYear = anneeField.getValue();
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

        // Label pour afficher les erreurs
        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(new Font(13));
        errorLabel.setVisible(true);

        // === Boutons ===
        Button annulerButton = new Button("Annuler");
        Button validerButton = new Button("Valider");

        // === Layout (comme dans ton code) ===
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
        grid.add(heureFinField, 3, row);
        grid.add(minuteFinField, 4, row++);
        grid.add(errorLabel, 1, row++, 6, 1);

        HBox buttonBox = new HBox(10, annulerButton, validerButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 0, row++, 5, 1);

        // === Actions boutons ===
        annulerButton.setOnAction(e -> stage.close());

        validerButton.setOnAction(e -> {
            // Validation des champs
            if ((moduleField1.getValue() == null && moduleField2.getValue() == null) || 
                (profField1.getValue() == null && profField2.getValue() == null) || 
                (groupeField1.getValue() == null && groupeField2.getValue() == null) ||
                typeField.getValue() == null || salleField.getValue() == null || anneeField.getValue() == null ||
                moisField.getValue() == null || jourField.getValue() == null || heureField.getValue() == null ||
                minuteField.getValue() == null || heureFinField.getValue() == null || minuteFinField.getValue() == null) {
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
                    Integer.parseInt(heureFinField.getValue()),
                    Integer.parseInt(minuteFinField.getValue()),
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
                Creneau newCreneau = Creneau.builder()
                                    .type(typeField.getValue())
                                    .heureDebut(heureDebut)
                                    .heureFin(heureFin)
                                    .salle(salle)
                                    .build();
                newCreneau.getGroupes().add(groupe);
                newCreneau.getProfesseurs().add(professeur);
                newCreneau.getModules().add(module);
                if (groupeField2.getValue() != null) {
                    Groupe groupe2 = groupeRepository.getByCode(groupeField2.getValue());
                    newCreneau.getGroupes().add(groupe2);
                }
                if (profField2.getValue() != null) {
                    Professeur professeur2 = professeurlist.get(profField2.getSelectionModel().getSelectedIndex());
                    newCreneau.getProfesseurs().add(professeur2);
                }
                if (moduleField2.getValue() != null) {
                    Module module2 = moduleRepository.getModuleByCode(moduleField2.getValue());
                    newCreneau.getModules().add(module2);
                }
                if ( this.utilisateur instanceof Responsable){
                    CreneauRepository creneauRepository = new CreneauRepository(entityManager);
                    String res = creneauRepository.addCreneau(newCreneau, creneau);
                    if (res == "Le créneau a été inséré") {
                        gui.genererCreneaux();
                        stage.close();
                    } else {
                        errorLabel.setText(res);
                    }
                    errorLabel.setText(res);
                }
                else if (this.utilisateur instanceof Professeur){
                    DemandeCreneau demandeCreneau = DemandeCreneau.makeFromCreneau(newCreneau);
                    demandeCreneau.setTypeDemande(1);
                    demandeCreneau.setCreneauToModify(creneau);
                    DemandeCreneauRepository demandeCreneauRepository = new DemandeCreneauRepository(entityManager);
                    String res = demandeCreneauRepository.addDemandeCreneau(demandeCreneau);
                    errorLabel.setText(res);
                    log.info(res);
                }
         
            }
        });

        Scene scene = new Scene(grid, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}

//TODO : gérer demandes annulation et suppression