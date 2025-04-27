package fr.univtln.m1im.png.gui;

import static fr.univtln.m1im.png.model.Creneau.Status.*;

import javafx.scene.paint.Color;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.DemandeCreneau;
import fr.univtln.m1im.png.model.Etudiant;
import fr.univtln.m1im.png.model.Groupe;
import fr.univtln.m1im.png.model.Professeur;
import fr.univtln.m1im.png.model.Responsable;
import fr.univtln.m1im.png.model.Utilisateur;
import fr.univtln.m1im.png.repositories.CreneauRepository;
import fr.univtln.m1im.png.repositories.NotePersonnelleRepository;
import jakarta.persistence.EntityManager;
import fr.univtln.m1im.png.model.Module;
import fr.univtln.m1im.png.model.NotePersonnelle;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuiCreneau {
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(GuiCreneau.class.getName());
    private Utilisateur utilisateur;
    private Group group;
    private Rectangle rectangle;
    private Label label;

    private int width;
    private int height;
    private int nbHeure;
    private int nbJour;
    private int jourDeLaSemaine;
    private EntityManager entityManager;

    private int collision;
    private int posCollision;

    private Creneau creneau;

    private Gui gui;

    private OffsetDateTime premierJour; // Premier jour de l'année
    private OffsetDateTime dernierJour; // Dernier jour de l'année

    private Stage[] popup;

    public GuiCreneau(Stage[] popup, Utilisateur utilisateur, Group group, Creneau creneau, int width, int height,
            int nbHeure, int nbJour, EntityManager entityManager, Gui gui) {
        this.popup = popup;
        this.utilisateur = utilisateur;
        this.group = group;
        this.creneau = creneau;
        this.width = width;
        this.height = height;
        this.nbHeure = nbHeure;
        this.nbJour = nbJour;
        this.entityManager = entityManager;

        this.collision = 1;
        this.posCollision = 0;
        this.gui = gui;
    }

    public String dateFr(OffsetDateTime jour) {
        List<String> jours = List.of("Lundi\t", "Mardi\t", "Mercredi\t", "Jeudi \t", "Vendredi\t", "Samedi\t",
                "Dimanche\t");
        String strJourSemaineFr = jours.get(jour.getDayOfWeek().getValue() - 1);
        String strMoisFr = "" + jour.getMonthValue();
        if (jour.getMonthValue() < 10) {
            strMoisFr = "0" + strMoisFr;
        }
        String strJourFr = "" + jour.getDayOfMonth();
        if (jour.getDayOfMonth() < 10) {
            strJourFr = "0" + strJourFr;
        }
        String strAnneeFr = "" + jour.getYear();
        return strJourSemaineFr + "\t" + strJourFr + "/" + strMoisFr + "/" + strAnneeFr + "\t  ";

    }

    public float convHeure(Creneau c) {
        float r;
        r = c.getHeureDebut().getHour() - 8;
        r += c.getHeureDebut().getMinute() / 60f;
        return r;

    }

    public float convDuree(Creneau c) {
        float r;
        r = c.getHeureFin().getHour() - c.getHeureDebut().getHour();
        r += c.getHeureFin().getMinute() / 60f - c.getHeureDebut().getMinute() / 60f;
        return r;
    }

    public void afficherCreneau() {
        switch (creneau.getHeureDebut().getDayOfWeek()) {
            case MONDAY:
                jourDeLaSemaine = 0;
                break;
            case TUESDAY:
                jourDeLaSemaine = 1;
                break;
            case WEDNESDAY:
                jourDeLaSemaine = 2;
                break;
            case THURSDAY:
                jourDeLaSemaine = 3;
                break;
            case FRIDAY:
                jourDeLaSemaine = 4;
                break;
            case SATURDAY:
            case SUNDAY:
                jourDeLaSemaine = 5;
                break;
        }

        rectangle = new Rectangle(jourDeLaSemaine * width / nbJour + (width / nbJour * posCollision / collision),
                convHeure(creneau) * height / nbHeure,
                width / nbJour / collision,
                convDuree(creneau) * height / nbHeure);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(1);

        switch (creneau.getType()) {
            case "CM":
                rectangle.setFill(Color.YELLOW);

                break;

            case "TD":
                rectangle.setFill(Color.LIGHTGREEN);

                break;

            case "TP":
                rectangle.setFill(Color.RED);

                break;

            case "EXAM":
                rectangle.setFill(Color.MAGENTA);

                break;

            default:
                rectangle.setFill(Color.WHITE);
                break;
        }

        label = new Label();
        label.setPrefSize(width / nbJour / collision, height / nbHeure * convDuree(creneau));
        label.setLayoutX(jourDeLaSemaine * width / nbJour + (width / nbJour * posCollision / collision));
        label.setLayoutY(convHeure(creneau) * height / nbHeure);
        label.setOnMouseClicked(e -> {
            if (this.creneau.getStatus() != EPHEMERE) {
                rectangle.setStroke(Color.BLUE);
                rectangle.setStrokeWidth(4);
                afficherInformation();
            }
        });
        label.setStyle("-fx-font-size: " + 10 + "px; -fx-alignment: center; -fx-text-alignment: center;");
        String listGroupe = new String();
        for (Groupe groupe : creneau.getGroupes()) {
            listGroupe += groupe.getCode() + " ";
        }
        String listModule = new String();
        for (Module module : creneau.getModules()) {
            listModule += module.getCode() + " " + module.getNom() + "\n";
        }
        String listProf = new String();
        for (Professeur prof : creneau.getProfesseurs()) {
            listProf += prof.getPrenom() + " " + prof.getNom() + "\n";
        }

        // FIXME we either need a not empty constraint on groupes or this needs to
        // handle the empty case
        label.setText(creneau.getSalle().getCode() + "\n" + listGroupe + "\n" + listModule + creneau.getType() + "\n"
                + listProf);

        group.getChildren().add(rectangle);
        if (this.creneau.getStatus() == ANNULE) {
            Label labelAnnule = new Label("Annulé");
            labelAnnule.setPrefSize(width / nbJour / collision, height / nbHeure * convDuree(creneau));
            labelAnnule.setStyle(
                    "-fx-text-fill: violet; -fx-font-size: 20px; -fx-alignment: center; -fx-text-alignment: center;");
            labelAnnule.setLayoutX(jourDeLaSemaine * width / nbJour + (width / nbJour * posCollision / collision));
            labelAnnule.setLayoutY(convHeure(creneau) * height / nbHeure);
            group.getChildren().add(labelAnnule);
        }
        group.getChildren().add(label);
    }

    public void majAffichage() {
        rectangle.setX(jourDeLaSemaine * width / nbJour + (width / nbJour * posCollision / collision));
        rectangle.setWidth(width / nbJour / collision);
        rectangle.setHeight(height / nbHeure * convDuree(creneau));
        label.setPrefSize(width / nbJour / collision, height / nbHeure * convDuree(creneau));
        label.setLayoutX(jourDeLaSemaine * width / nbJour + (width / nbJour * posCollision / collision));
        label.setLayoutY(convHeure(creneau) * height / nbHeure);
    }

    public void afficherInformation() {
        popup[0].close();
        popup[0] = new Stage();
        // Désélectionner le rectangle lors de la fermeture de la fenêtre
        popup[0].onCloseRequestProperty().set(e -> {
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(1);
        });
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        GridPane gridModules = new GridPane();
        gridModules.setHgap(10);
        gridModules.setVgap(10);

        Group infoGroup = new Group();
        Scene infoScene = new Scene(infoGroup);
        Label infoLabel = new Label();
        infoLabel.setStyle("-fx-text-alignment: center;");

        // Note personnelle
        NotePersonnelleRepository notePersoRepo = new NotePersonnelleRepository(entityManager);
        NotePersonnelle notePerso = notePersoRepo.getByCreneauUtilisateur(this.utilisateur.getId(),
                this.creneau.getId());
        TextField notePersoField = new TextField();
        Button notePersoButton = new Button("Modifier");
        if (notePerso != null) {
            notePersoField.setText(notePerso.getNotePerso());
        } else {
            notePersoField.setPromptText("Aucune note personnelle");
        }
        notePersoField.setOnKeyReleased(e -> {
            notePersoButton.setStyle("-fx-text-fill: red;");
        });
        notePersoButton.setOnAction(e -> {
            notePersoButton.setStyle("-fx-text-fill: black;");
            notePersoRepo.modify(notePerso, notePersoField.getText(), creneau, utilisateur);
        });
        grid.add(notePersoField, 0, 1);
        grid.add(notePersoButton, 1, 1);

        // Note professeur
        if (this.utilisateur instanceof Professeur || this.utilisateur instanceof Responsable) {
            TextField noteProfField = new TextField();
            Button noteProfButton = new Button("Modifier");
            noteProfField.setPromptText("Aucune note de cours");
            if (creneau.getNoteProf() != "") {
                noteProfField.setText(creneau.getNoteProf());
            } else {
                noteProfField.setText("Aucune note de cours");
            }
            noteProfField.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
            noteProfField.setOnKeyReleased(e -> {
                noteProfButton.setStyle("-fx-text-fill: red;");
            });

            noteProfButton.setOnAction(e -> {
                noteProfButton.setStyle("-fx-text-fill: black;");

                entityManager.getTransaction().begin();
                Creneau managedCreneau = entityManager.merge(creneau);
                managedCreneau.setNoteProf(noteProfField.getText());
                entityManager.getTransaction().commit();
            });
            grid.add(noteProfField, 0, 2);
            grid.add(noteProfButton, 1, 2);
        } else {
            Label noteProfLabel = new Label("Aucune note de cours");
            if (creneau.getNoteProf() != "") {
                noteProfLabel.setText(creneau.getNoteProf());
            } else {
                noteProfLabel.setText("Aucune note de cours");
            }
            noteProfLabel.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
            grid.add(noteProfLabel, 0, 2);
        }
        List<Label> infoModules = new ArrayList<>();
        int nbAffichage = 3;

        List<Creneau> listCreneaux;
        if (this.utilisateur instanceof Etudiant) {

            Boolean trouve = false;
            List<Creneau> tmpListCreneaux = new ArrayList<>();
            for (Module module : creneau.getModules()) {
                for (Creneau c : module.getCreneaux()) {
                    for (Groupe g : c.getGroupes()) {
                        for (Etudiant e : g.getEtudiants()) {
                            if (e.getId().equals(this.utilisateur.getId())) {
                                tmpListCreneaux.add(c);
                                trouve = true;
                                break;
                            }
                        }
                        if (trouve) {
                            trouve = false;
                            break;
                        }
                    }
                }
            }
            listCreneaux = tmpListCreneaux.stream()
                    .sorted((c1, c2) -> c1.getHeureDebut().compareTo(c2.getHeureDebut()))
                    .toList();
        } else {
            listCreneaux = creneau.getModules().getFirst().getCreneaux().stream()
                    .sorted((c1, c2) -> c1.getHeureDebut().compareTo(c2.getHeureDebut()))
                    .toList();
        }

        int position = 0;
        for (Creneau c : listCreneaux) {
            if (c.equals(creneau)) {
                break;
            }
            position++;
        }
        if (position > listCreneaux.size() - nbAffichage) {
            position = listCreneaux.size() - nbAffichage;
        }
        String info = new String();
        for (int i = 0; i < nbAffichage; i++) {
            info = dateFr(listCreneaux.get(position + i).getHeureDebut());
            if (listCreneaux.get(position + i).getHeureDebut().getHour() < 10) {
                info += "0";
            }
            info += listCreneaux.get(position + i).getHeureDebut().getHour() + ":";
            if (listCreneaux.get(position + i).getHeureDebut().getMinute() < 10) {
                info += "0";
            }
            info += listCreneaux.get(position + i).getHeureDebut().getMinute() + "\t";
            if (listCreneaux.get(position + i).getHeureFin().getHour() < 10) {
                info += "0";
            }
            info += listCreneaux.get(position + i).getHeureFin().getHour() + ":";
            if (listCreneaux.get(position + i).getHeureFin().getMinute() < 10) {
                info += "0";
            }
            info += listCreneaux.get(position + i).getHeureFin().getMinute() + "\t";
            info += listCreneaux.get(position + i).getType() + "\n";
            infoModules.add(new Label(info));
            infoModules.get(i).setPrefSize(350, 10);
            if (this.creneau.getHeureDebut().equals(listCreneaux.get(position + i).getHeureDebut())) {
                infoModules.get(i).setTextFill(Color.RED);
                infoModules.get(i).setStyle("-fx-background-color: lightgray;");
            } else {
                if (listCreneaux.get(position + i).getHeureDebut().isBefore(this.creneau.getHeureDebut())) {
                    infoModules.get(i).setTextFill(Color.BLACK);
                    infoModules.get(i).setStyle("-fx-background-color: lightgray;");
                } else {
                    infoModules.get(i).setTextFill(Color.BLACK);
                    infoModules.get(i).setStyle("-fx-background-color: white;");
                }

            }
            gridModules.add(infoModules.get(i), 0, 1 + i);

        }

        infoGroup.getChildren().add(grid);
        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setOrientation(javafx.geometry.Orientation.VERTICAL);

        scrollBar.setMin(0);
        scrollBar.setMax(listCreneaux.size() - nbAffichage);
        scrollBar.setValue(position);
        scrollBar.setBlockIncrement(1);
        scrollBar.setUnitIncrement(1);
        scrollBar.valueProperty().addListener((obs, oldValue, newValue) -> {
            String infoSc = new String();
            int j = 0;
            for (int i = (int) scrollBar.getValue(); i < (int) scrollBar.getValue() + nbAffichage; i++) {
                // infoSc = listCreneaux.get(i).getHeureDebut().getDayOfWeek() + "\t";
                // infoSc += listCreneaux.get(i).getHeureDebut().toLocalDate() + "\t";
                infoSc = dateFr(listCreneaux.get(i).getHeureDebut());
                if (listCreneaux.get(i).getHeureDebut().getHour() < 10) {
                    infoSc += "0";
                }
                infoSc += listCreneaux.get(i).getHeureDebut().getHour() + ":";
                if (listCreneaux.get(i).getHeureDebut().getMinute() < 10) {
                    infoSc += "0";
                }
                infoSc += listCreneaux.get(i).getHeureDebut().getMinute() + "\t";
                if (listCreneaux.get(i).getHeureFin().getHour() < 10) {
                    infoSc += "0";
                }
                infoSc += listCreneaux.get(i).getHeureFin().getHour() + ":";
                if (listCreneaux.get(i).getHeureFin().getMinute() < 10) {
                    infoSc += "0";
                }
                infoSc += listCreneaux.get(i).getHeureFin().getMinute() + "\t";
                infoSc += listCreneaux.get(i).getType() + "\n";
                infoModules.get(j).setText(infoSc);

                if (this.creneau.getHeureDebut().equals(listCreneaux.get(i).getHeureDebut())) {
                    infoModules.get(j).setTextFill(Color.RED);
                    infoModules.get(j).setStyle("-fx-background-color: lightgray;");
                } else {
                    if (listCreneaux.get(i).getHeureDebut().isBefore(this.creneau.getHeureDebut())) {
                        infoModules.get(j).setTextFill(Color.BLACK);
                        infoModules.get(j).setStyle("-fx-background-color: lightgray;");
                    } else {
                        infoModules.get(j).setTextFill(Color.BLACK);
                        infoModules.get(j).setStyle("-fx-background-color: white;");
                    }
                }

                j++;
            }
        });

        infoLabel.setText(this.label.getText());

        grid.add(infoLabel, 0, 0);
        grid.add(gridModules, 0, 3);
        grid.add(scrollBar, 1, 3);
        // Ensure no TextField is selected by default
        popup[0].setOnShown(e -> {
            infoScene.getRoot().requestFocus();
        });

        if (this.utilisateur instanceof Responsable) {
            Button modifierCoursButton = new Button("Modifier le cours");
            modifierCoursButton.setOnAction(e -> {
                ModifierCreneau modifierCreneau = new ModifierCreneau(creneau, entityManager, gui);
                modifierCreneau.afficherModifierCreneau();
                popup[0].close();
            });
            Button annulerCoursButton = new Button("Annuler le cours");
            if (creneau.getStatus() == ANNULE) {
                annulerCoursButton.setText("Restaurer le cours");
            }
            annulerCoursButton.setOnAction(e -> {
                entityManager.getTransaction().begin();
                Creneau managedCreneau = entityManager.merge(creneau);
                if (creneau.getStatus() == ANNULE) {
                    managedCreneau.setStatus(ACTIF);
                    annulerCoursButton.setText("Annuler le cours");
                } else {
                    managedCreneau.setStatus(ANNULE);
                    annulerCoursButton.setText("Restaurer le cours");
                }
                entityManager.getTransaction().commit();
                gui.genererCreneaux();
                popup[0].close();
            });

            Button supprimerCoursButton = new Button("Supprimer le cours");
            supprimerCoursButton.setOnAction(e -> {
                CreneauRepository creneauRepository = new CreneauRepository(entityManager);
                creneauRepository.deleteCreneau(creneau);
                gui.genererCreneaux();

            });
            grid.add(modifierCoursButton, 0, 4);
            grid.add(annulerCoursButton, 0, 5);
            grid.add(supprimerCoursButton, 0, 6);
        }

        else if (this.utilisateur instanceof Professeur) {
            Button modifierCoursButton = new Button("Demande de modifier le cours");
            modifierCoursButton.setOnAction(e -> {
                ModifierCreneau modifierCreneau = new ModifierCreneau(creneau, entityManager, gui);
                modifierCreneau.afficherModifierCreneau();
                popup[0].close();
            });
            Button annulerCoursButton = new Button("Demande d'annuler le cours");
            if (creneau.getStatus() == ANNULE) {
                annulerCoursButton.setText("Demande de restaurer le cours");
            }
            annulerCoursButton.setOnAction(e -> {
                // L'objet demande est identique pour les deux cas, on va juste inverser le
                // status du cours a l'acceptation de la demande
                entityManager.getTransaction().begin();
                DemandeCreneau demandeCreneau = DemandeCreneau.makeFromCreneau(creneau);
                demandeCreneau.setTypeDemande(2);
                demandeCreneau.setCreneauToModify(creneau);
                for (Professeur prof : demandeCreneau.getProfesseurs()) {
                    Professeur managedProf = entityManager.merge(prof);
                    managedProf.getDemandes_creneaux().add(demandeCreneau);
                }
                entityManager.persist(demandeCreneau);
                entityManager.getTransaction().commit();
                log.info("DEBUG :" + String.valueOf(demandeCreneau.getProfesseurs().size()));
                for (Professeur prof : demandeCreneau.getProfesseurs()) {
                    log.info(prof.getNom() + " " + prof.getPrenom());
                }

                if (creneau.getStatus() == ANNULE) {
                    annulerCoursButton.setText("Demande d'annuler le cours");
                } else {
                    // TODO: YANN :Label pour afficher "Demande envoyée"
                    annulerCoursButton.setText("Demande de restaurer le cours");
                }
                gui.genererCreneaux();
                popup[0].close();
            });

            Button supprimerCoursButton = new Button("Demande de supprimer le cours");
            supprimerCoursButton.setOnAction(e -> {
                // TODO: Demande suppimer cours
                // CreneauRepository creneauRepository = new CreneauRepository(entityManager);
                // creneauRepository.deleteCreneau(creneau);
                // gui.genererCreneaux();

            });
            grid.add(modifierCoursButton, 0, 4);
            grid.add(annulerCoursButton, 0, 5);
            grid.add(supprimerCoursButton, 0, 6);
        }

        popup[0].setTitle("Information du créneau");
        popup[0].setMinWidth(450);
        popup[0].setMinHeight(400);
        popup[0].setScene(infoScene);
        popup[0].initStyle(StageStyle.UTILITY);
        popup[0].show();

    }

}
