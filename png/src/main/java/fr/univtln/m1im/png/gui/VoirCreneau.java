package fr.univtln.m1im.png.gui;

import fr.univtln.m1im.png.model.Creneau;
import jakarta.persistence.EntityManager;

import java.time.OffsetDateTime;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * GUI class for viewing details of a time slot (creneau).
 */
public class VoirCreneau {
    private final Creneau creneau;
    private final EntityManager entityManager;
    private final Gui gui;

    public VoirCreneau(Creneau creneau, EntityManager entityManager, Gui gui) {
        this.creneau = creneau;
        this.entityManager = entityManager;
        this.gui = gui;
    }

    public void afficherCreneau() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Voir Cours");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        Label moduleField1 = new Label();
        moduleField1.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");
        Label moduleField2 = new Label();
        moduleField2.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");
        if (!creneau.getModules().isEmpty()) {
            moduleField1.setText(creneau.getModules().get(0).getCode());
            if (creneau.getModules().size() > 1)
                moduleField2.setText(creneau.getModules().get(1).getCode());
        }

        Label profField1 = new Label();
        profField1.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");
        Label profField2 = new Label();
        profField2.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");
        if (!creneau.getProfesseurs().isEmpty()) {
            profField1.setText(
                    creneau.getProfesseurs().get(0).getNom() + " " + creneau.getProfesseurs().get(0).getPrenom());
            if (creneau.getProfesseurs().size() > 1)
                profField2.setText(
                        creneau.getProfesseurs().get(1).getNom() + " " + creneau.getProfesseurs().get(1).getPrenom());
        }

        Label groupeField1 = new Label();
        groupeField1.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");
        Label groupeField2 = new Label();
        groupeField2.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");
        if (!creneau.getGroupes().isEmpty()) {
            groupeField1.setText(creneau.getGroupes().get(0).getCode());
            if (creneau.getGroupes().size() > 1)
                groupeField2.setText(creneau.getGroupes().get(1).getCode());
        }

        Label typeField = new Label(creneau.getType());
        typeField.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");

        Label salleField = new Label();
        salleField.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");
        if (creneau.getSalle() != null)
            salleField.setText(creneau.getSalle().getCode());

        OffsetDateTime debut = creneau.getHeureDebut();
        OffsetDateTime fin = creneau.getHeureFin();

        Label anneeField = new Label(String.valueOf(debut.getYear()));
        anneeField.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");

        String[] mois = { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre" };
        Label moisField = new Label(mois[debut.getMonthValue() - 1]);
        moisField.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");

        Label jourField = new Label(String.valueOf(debut.getDayOfMonth()));
        jourField.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");

        Label heureField = new Label(String.format("%02d", debut.getHour()));
        heureField.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");

        Label minuteField = new Label(String.format("%02d", debut.getMinute()));
        minuteField.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");

        Label heureFinField = new Label(String.format("%02d", fin.getHour()));
        heureFinField.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");

        Label minuteFinField = new Label(String.format("%02d", fin.getMinute()));
        minuteFinField.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");

        // === Bouton ===
        Button okButton = new Button("OK");
        Button modifButton = new Button("Modifier");

        // === Layout (comme dans ton code) ===
        int row = 0;
        grid.add(new Label("Module 1"), 0, row);
        Label labelModul2 = new Label("Module 2");
        grid.add(labelModul2, 1, row++);
        grid.add(moduleField1, 0, row);
        grid.add(moduleField2, 1, row++);
        if (creneau.getModules().size() < 2) {
            labelModul2.setDisable(true);
            labelModul2.setOpacity(0.01); // pour bien etre clair
            moduleField2.setDisable(true);
            moduleField2.setOpacity(0.01);
        }

        grid.add(new Label("Professeur 1"), 0, row);
        Label labelProf2 = new Label("Professeur 2");
        grid.add(labelProf2, 1, row++);
        grid.add(profField1, 0, row);
        grid.add(profField2, 1, row++);
        if (creneau.getProfesseurs().size() < 2) {
            labelProf2.setDisable(true);
            labelProf2.setOpacity(0.01);
            profField2.setDisable(true);
            profField2.setOpacity(0.01);
        }

        grid.add(new Label("Groupe 1"), 0, row);
        Label labelGroupe2 = new Label("Groupe 2");
        grid.add(labelGroupe2, 1, row++);
        grid.add(groupeField1, 0, row);
        grid.add(groupeField2, 1, row++);
        if (creneau.getGroupes().size() < 2) {
            labelGroupe2.setDisable(true);
            labelGroupe2.setOpacity(0.01);
            groupeField2.setDisable(true);
            groupeField2.setOpacity(0.01);
        }

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

        HBox buttonBox = new HBox(10, okButton, modifButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 0, row++, 5, 1);

        // === Actions boutons ===
        okButton.setOnAction(e -> stage.close());

        modifButton.setOnAction(e -> {
            stage.close();
            ModifierCreneau modifierCreneau = new ModifierCreneau(creneau, entityManager, gui);
            modifierCreneau.afficherModifierCreneau();
        });

        Scene scene = new Scene(grid, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}