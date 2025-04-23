package fr.univtln.m1im.png.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

import fr.univtln.m1im.png.dto.GroupeDTO;
import fr.univtln.m1im.png.dto.ProfesseurDTO;
import fr.univtln.m1im.png.model.Groupe;
import fr.univtln.m1im.png.model.Professeur;
import fr.univtln.m1im.png.repositories.GroupeRepository;
import fr.univtln.m1im.png.repositories.ProfesseurRepository;
import jakarta.persistence.EntityManager;
import fr.univtln.m1im.png.model.Module;

public class AjouterModule {

    private EntityManager entityManager;

    public AjouterModule( EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void afficherAjoutModule() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Ajouter Module");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        int row = 0;

        TextField codeField = new TextField();
        TextField nomField = new TextField();
        TextField descriptionField = new TextField();
        TextField cmField = new TextField();
        TextField tdField = new TextField();
        TextField tpField = new TextField();

        ComboBox<String> profCombo = new ComboBox<>();
        profCombo.setPromptText("Choisir un professeur");
        ProfesseurRepository professeurRepository = new ProfesseurRepository(entityManager);
        List<ProfesseurDTO> proflist = professeurRepository.getAllDTO(0, 1000);
        for (ProfesseurDTO p : proflist){
            profCombo.getItems().add(p.getNom() + " " + p.getPrenom());
        }

        ComboBox<String> groupeCombo = new ComboBox<>();
        groupeCombo.setPromptText("Choisir un groupe");
        GroupeRepository groupeRepository = new GroupeRepository(entityManager);
        List<GroupeDTO> grouplist = groupeRepository.getAllDTO(0, 1000);
        for (GroupeDTO g : grouplist){
            groupeCombo.getItems().add(g.getCode());
        }

        grid.add(new Label("Code"), 0, row); grid.add(codeField, 1, row++);
        grid.add(new Label("Nom"), 0, row); grid.add(nomField, 1, row++);
        grid.add(new Label("Description"), 0, row); grid.add(descriptionField, 1, row++);
        grid.add(new Label("Professeur"), 0, row); grid.add(profCombo, 1, row++);
        grid.add(new Label("Groupe"), 0, row); grid.add(groupeCombo, 1, row++);
        grid.add(new Label("Heures CM"), 0, row); grid.add(cmField, 1, row++);
        grid.add(new Label("Heures TD"), 0, row); grid.add(tdField, 1, row++);
        grid.add(new Label("Heures TP"), 0, row); grid.add(tpField, 1, row++);

        
        Button annulerButton = new Button("Annuler");
        Button validerButton = new Button("Valider");
        HBox buttonBox = new HBox(10, annulerButton, validerButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 0, row++, 2, 1);

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        grid.add(messageLabel, 0, row, 2, 1);

        annulerButton.setOnAction(e -> stage.close());
        validerButton.setOnAction(e -> {
            if( (codeField.getText().trim().isEmpty()) || (nomField.getText().trim().isEmpty()) || (descriptionField.getText().trim().isEmpty()) ||
                (profCombo.getValue() == null) || ( groupeCombo.getValue() == null) || (cmField.getText().trim().isEmpty()) ||
                (tdField.getText().trim().isEmpty() ) || (tpField.getText().trim().isEmpty())) {

                    messageLabel.setText("Veuillez remplir tous les champs !");
                    return;
            } else {
                int cm, td, tp;
                try {
                    cm = Integer.parseInt(cmField.getText().trim());
                    td = Integer.parseInt(tdField.getText().trim());
                    tp = Integer.parseInt(tpField.getText().trim());
            
                } catch (NumberFormatException ex) {
                    messageLabel.setText("Veuillez entrer des valeurs numériques pour CM, TD et TP.");
                    return;
                }

                String codeM = codeField.getText();
                String nomM = nomField.getText();
                String desc = descriptionField.getText();
                List<Professeur> professeurlist = professeurRepository.getAll(0, 1000);
                Professeur professeur = professeurlist.get(profCombo.getSelectionModel().getSelectedIndex());
                Groupe groupe = groupeRepository.getByCode(groupeCombo.getValue());
                Module newModule = Module.builder() 
                                    .code(codeM)
                                    .nom(nomM)
                                    .description(desc)
                                    .nbHeuresCM(cm)
                                    .nbHeuresTD(td)
                                    .nbHeuresTP(tp)
                                    .build();
                                    
                newModule.getProfesseurs().add(professeur);
                newModule.getGroupes().add(groupe);
                try {
                    entityManager.getTransaction().begin();
                    entityManager.persist(newModule);
                    entityManager.getTransaction().commit();
                    messageLabel.setText("Module ajouté !");
                }
                catch (Exception ex) {
                    entityManager.getTransaction().rollback();
                    if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                        messageLabel.setText("Erreur : Un module avec ce code existe déjà !");
                    } else {
                        messageLabel.setText("Erreur lors de l'ajout du module : " + ex.getMessage());
                    }
                }
            }
        });
        stage.setScene(new Scene(grid, 450, 500));
        stage.showAndWait();
    }

}
