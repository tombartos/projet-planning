package fr.univtln.m1im.png.gui;

import fr.univtln.m1im.png.dto.GroupeDTO;
import fr.univtln.m1im.png.dto.ProfesseurDTO;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Salle;
import fr.univtln.m1im.png.repositories.GroupeRepository;
import fr.univtln.m1im.png.repositories.ModuleRepository;
import fr.univtln.m1im.png.repositories.ProfesseurRepository;
import fr.univtln.m1im.png.repositories.SalleRepository;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import jakarta.persistence.EntityManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ModifierCreneau {

    private final EntityManager entityManager;
    private final int anneeDebut;
    private final Creneau creneau;

    public ModifierCreneau(Creneau creneau, EntityManager entityManager, int anneeDebut) {
        this.creneau = creneau;
        this.entityManager = entityManager;
        this.anneeDebut = anneeDebut;
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

        ComboBox<String> moisField = new ComboBox<>();
        moisField.getItems().addAll("Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre");
        moisField.setValue(moisField.getItems().get(debut.getMonthValue() - 1));

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

        HBox buttonBox = new HBox(10, annulerButton, validerButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 0, row++, 5, 1);

        // === Actions boutons ===
        annulerButton.setOnAction(e -> stage.close());

        validerButton.setOnAction(e -> {
            // Tu pourras ici récupérer les valeurs modifiées et mettre à jour le Creneau dans la base
            // Exemple :
            // creneau.setHeureDebut(LocalDateTime.of(...));
            // creneau.setType(typeField.getValue());
            // entityManager.getTransaction().begin();
            // entityManager.merge(creneau);
            // entityManager.getTransaction().commit();
            stage.close();
        });

        Scene scene = new Scene(grid, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}