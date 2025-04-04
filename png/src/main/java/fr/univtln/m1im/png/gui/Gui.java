package fr.univtln.m1im.png.gui;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import fr.univtln.m1im.png.dto.GroupeDTO;
import fr.univtln.m1im.png.dto.ProfesseurDTO;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Etudiant;
import fr.univtln.m1im.png.model.Salle;
import fr.univtln.m1im.png.model.Utilisateur;
import fr.univtln.m1im.png.repositories.EtudiantRepository;
import fr.univtln.m1im.png.repositories.GroupeRepository;
import fr.univtln.m1im.png.repositories.ProfesseurRepository;
import fr.univtln.m1im.png.repositories.SalleRepository;
import jakarta.persistence.EntityManager;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.control.ComboBox;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Gui {
    private static final Logger log = LoggerFactory.getLogger(Gui.class);
    //La fenêtre de l'application
    private Group group;
    private GridPane gui;
    private int width;
    private int height;
    //L'emploi du temps
    private GridPane gdHeuresEdt; // (0,0) : Les heures; (1,0) L'edt
    private Canvas heures;
    private GraphicsContext gcHeures;
    private GridPane gdSemainesGrille; // (0,0) : Les semaines; (0,1) La grille
    private int nbSemaines; // Nombre de semaines
    private GridPane gdSemaines; // Les semaines
    private List<Button> semaines;
    private int numSemaine;
    private int premierSemaine;
    private int derniereSemaine;
    private Canvas grille;
    private int wGrille;
    private int hGrille;
    private GraphicsContext gcGrille;
    private int nbHeure;
    private int nbJour;

    private EntityManager entityManager;
    private Etudiant etudiant;
    private Utilisateur utilisateur;
    private List<Creneau> creneaux;
    private List<Rectangle> cCreneaux;
    private List<GuiCreneau> guiCreneaux;
    
    private Group gpGrille;
    private Group gpCreneaux;
    private Group gpJour;
    private Canvas cJours;
    private GraphicsContext gcJours;
    
    private int etatCourant = 0; //0: edt perso,1: edt prof, 2: edt salle, 3: edt groupe
    private String salleChoisie;

    private int anneeDebut;

    //Barre de filtres
    private Group gpBarreFiltres;
    private HBox barreFiltres;
    private ComboBox<String> filtreDropdown;
    private ComboBox<String> salleDropdown;
    private ComboBox<String> groupesDropdown;
    private ComboBox<String> profDropdown;

    private List<ProfesseurDTO> professeurs;
    private long idProfChoisi;

    private String codeGroupeChoisi;
    private Button btnEdt;
    private Button ajoutCours;
    private Button demandeCours;

    private Stage[] popups = {new Stage()};


    public Gui(Utilisateur utilisateur, Group group, int width, int height, EntityManager entityManager, Stage stage, Scene scene) {
        this.utilisateur = utilisateur;
        //this.etudiant = etudiant;
        this.group = group;
        this.width = width;
        this.height = height;
        this.entityManager = entityManager;


        //variables
        this.nbHeure = 12;
        this.nbJour = 6;
        this.nbSemaines = 48;
        this.etatCourant = 0;
        this.cCreneaux = new ArrayList<>();
        this.gpGrille = new Group();
        this.gpCreneaux = new Group();
        this.anneeDebut = 2024;

        this.gui = new GridPane();
        this.group.getChildren().add(this.gui);

        this.gdHeuresEdt = new GridPane();
        this.gdSemainesGrille = new GridPane();
        this.gdSemainesGrille.setHgap(20);
        this.gdSemainesGrille.setVgap(20);
        this.gdSemaines = new GridPane();

        this.gui.add(this.gdHeuresEdt, 0, 2);
        this.gui.add(this.gdSemainesGrille, 1, 0);
        this.semaines = new ArrayList<>();

        this.guiCreneaux = new ArrayList<>();

        this.wGrille = width * 9/10;
        this.hGrille = height * 7/10;

        this.grille = new Canvas(this.wGrille,this.hGrille);
        this.grille.setOnMouseClicked(e->{
            for(GuiCreneau gc : guiCreneaux)
            {
                gc.getRectangle().setStrokeWidth(1);
                gc.getRectangle().setStroke(Color.BLACK);
                this.popups[0].close();
            }});
        this.heures = new Canvas(width * 1/20, height*8/10);
        this.gcHeures = this.heures.getGraphicsContext2D();

        this.gpJour = new Group();
        this.cJours = new Canvas(this.width, 10);
        this.gpJour.getChildren().add(this.cJours);
        this.gcJours = this.cJours.getGraphicsContext2D();
        this.gcJours.setFill(Color.YELLOW);
        this.gcJours.fillRect(50, 50, 200, 50);

        this.gdSemainesGrille.add(this.gpJour,1,2);
        //this.gdSemainesGrille.add(this.gpJour,1,1);
        this.gdSemainesGrille.add(this.gpGrille,1,3);
        this.gpGrille.getChildren().add(this.grille);
        this.gpGrille.getChildren().add(this.gpCreneaux);
        this.gdSemainesGrille.add(this.gdSemaines,1,1);
        this.gcGrille = this.grille.getGraphicsContext2D();

        // this.gdSemainesGrille.add(this.heures,0,1);
        this.gdHeuresEdt.add(this.heures,0,0);
        this.gdHeuresEdt.add(this.gdSemainesGrille,1,0);

        //Ajout des semaines
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        OffsetDateTime premierJourAnnee = OffsetDateTime.now().withYear(this.anneeDebut).withMonth(9).withDayOfMonth(1);
        OffsetDateTime tmpJour = premierJourAnnee;
        this.premierSemaine = tmpJour.get(weekFields.weekOfWeekBasedYear());
        this.derniereSemaine = OffsetDateTime.now().withYear(this.anneeDebut).withMonth(7).withDayOfMonth(31).get(weekFields.weekOfWeekBasedYear());
        int tmpSemaine = tmpJour.get(weekFields.weekOfWeekBasedYear());
        int indexSemaine = 0;
        while(tmpSemaine != derniereSemaine)
        {
            Button semaine = new Button(""+(tmpSemaine));
            semaine.setStyle("-fx-font-size: 8px;");
            semaine.setPrefSize(this.wGrille/this.nbSemaines, 30);
            final int index = tmpSemaine;
            semaine.setOnMouseClicked(event -> {this.numSemaine = index; genererCreneaux();});
            this.semaines.add(semaine);
            this.gdSemaines.add(semaine, indexSemaine, 0);

            tmpJour = tmpJour.plusDays(7);
            tmpSemaine = tmpJour.get(weekFields.weekOfWeekBasedYear());
            indexSemaine++;
        }

        //Initialisation de la grille
        //Ajout des heures
        for (int i = 0; i < this.nbHeure; i++) {
            this.gcHeures.strokeText((i+8)+":00", this.heures.getWidth() / 2 - this.gcHeures.getFont().getSize(), i * this.hGrille / this.nbHeure + this.hGrille / this.nbHeure * 2.5);
        }
        //fond de la grille
        this.gcGrille.setFill(Color.LIGHTGRAY);
        this.gcGrille.fillRect(0, 0, this.wGrille, this.hGrille);
        //les cases
        this.gcGrille.setStroke(Color.BLACK);
        for (int i = 0; i < this.nbJour; i++) {
            for (int j = 0; j < this.nbHeure; j++) {
                this.gcGrille.strokeRect(i*this.wGrille/this.nbJour, j*this.hGrille/this.nbHeure, this.wGrille/this.nbJour, this.hGrille/this.nbHeure);
            }
        }
        

        //Ajout de la barre de filtres
        this.gpBarreFiltres = new Group();
        this.barreFiltres = new HBox();
        this.barreFiltres.setSpacing(10); // Espacement entre les boutons
        this.btnEdt = new Button("Mon EDT");
        this.ajoutCours = new Button("Ajouter cours");
        this.demandeCours = new Button("Demander cours");
        this.salleDropdown = new ComboBox<>();
        this.salleDropdown.setPromptText("Salles");
        this.salleDropdown.setVisible(false); 
        this.groupesDropdown = new ComboBox<>();
        this.groupesDropdown.setPromptText("Groupes");
        this.groupesDropdown.setVisible(false);
        this.profDropdown = new ComboBox<>();
        this.profDropdown.setPromptText("Professeurs");
        this.profDropdown.setVisible(false);

        this.filtreDropdown = new ComboBox<>();
        this.filtreDropdown.getItems().addAll("Salles", "Groupes", "Professeurs");
        this.filtreDropdown.setPromptText("Filtrer par");
        
        this.filtreDropdown.setOnAction(event -> {
            String choix = filtreDropdown.getValue();
            switch (choix) {
                case "Salles":
                    //this.filtreDropdown.setVisible(false);
                    this.salleDropdown.setVisible(true); 
                    this.groupesDropdown.setVisible(false);
                    this.profDropdown.setVisible(false);
                    chargerSalles();
                    break;
                case "Groupes":
                    //this.filtreDropdown.setVisible(false);
                    this.groupesDropdown.setVisible(true);
                    this.salleDropdown.setVisible(false);
                    this.profDropdown.setVisible(false);
                    chargerGroupes();
                    break;
                case "Professeurs":
                    //this.filtreDropdown.setVisible(false);
                    this.profDropdown.setVisible(true);
                    this.salleDropdown.setVisible(false);
                    this.groupesDropdown.setVisible(false);
                    chargerProfesseurs();
                    break;
            }
        });

        // Gérer la sélection d'un groupe
        this.btnEdt.setOnAction(event -> {
            this.etatCourant = 0;
            this.salleDropdown.setVisible(false);
            this.groupesDropdown.setVisible(false); 
            this.profDropdown.setVisible(false);
            genererCreneaux();
        });
        this.ajoutCours.setOnAction(event -> {
            AjouterCours cours = new AjouterCours( this.width, this.height, entityManager, anneeDebut, "Ajouter", this);
            cours.afficherFenetreAjoutCours();
        });
        this.demandeCours.setOnAction(event -> {
            AjouterCours cours = new AjouterCours(this.width, this.height, entityManager, anneeDebut, "Demander", this);
            cours.afficherFenetreAjoutCours();
        });
        // Gérer la sélection d'un professeur
        this.profDropdown.setOnAction(event -> {
            this.etatCourant = 1;
            idProfChoisi = professeurs.get(profDropdown.getSelectionModel().getSelectedIndex()).getId();
            genererCreneaux();
        });
        // Gérer la sélection d'une salle
        this.salleDropdown.setOnAction(event -> {
            this.etatCourant = 2;
            this.salleChoisie = salleDropdown.getValue();
            genererCreneaux();
        });
        // Gérer la sélection d'un groupe
        this.groupesDropdown.setOnAction(event -> {
            this.etatCourant = 3;
            codeGroupeChoisi = groupesDropdown.getValue();
            genererCreneaux();

        });
        

        // Ajouter les boutons dans la barre horizontale
        this.barreFiltres.getChildren().addAll(btnEdt, this.salleDropdown, this.groupesDropdown, this.profDropdown, this.filtreDropdown, this.ajoutCours, this.demandeCours);
        // Ajouter la barre de boutons au groupe
        this.gpBarreFiltres.getChildren().add(barreFiltres);
        // Ajouter ce groupe à l'interface
        this.gdSemainesGrille.add(this.gpBarreFiltres, 1, 0);



        stage.setScene(scene);
        stage.setTitle("Planning Nouvelle Génération");
        stage.show();
        
    }

    public float convHeure(Creneau c)
    {
        float r;
        r = c.getHeureDebut().getHour()-8;
        r += c.getHeureDebut().getMinute()/60f;
        return r;

    }

    public float convDuree(Creneau c)
    {
        float r;
        r = c.getHeureFin().getHour()-c.getHeureDebut().getHour();
        r += c.getHeureFin().getMinute()/60f-c.getHeureDebut().getMinute()/60f;
        return r;
    }

    public void genererCreneaux()
    {   
        int annee;
        if (numSemaine >= this.premierSemaine){
            annee = this.anneeDebut;
        }
        else{
            annee = this.anneeDebut + 1;
        }
        if(this.etatCourant == 0)
        {
            switch (this.utilisateur.getClass().getSimpleName()) {
                case "Etudiant":
                    EtudiantRepository etudiantRepository = new EtudiantRepository(entityManager);
                    creneaux = etudiantRepository.getWeekCreneaux(utilisateur.getId(), this.numSemaine, annee, 0, 100);
                    break;
                case "Professeur":
                    ProfesseurRepository professeurRepository = new ProfesseurRepository(entityManager);
                    creneaux = professeurRepository.getWeekCrenaux(utilisateur.getId(), this.numSemaine, annee, 0, 100);
                    break;
                case "Responsable":
                    SalleRepository salleRepository = new SalleRepository(entityManager);
                    salleChoisie = salleRepository.getAll(0, 1).getFirst().getCode();
                    creneaux = salleRepository.getWeekCrenaux(salleChoisie, this.numSemaine, annee, 0, 100);
                    this.btnEdt.setVisible(false);
                    break;
                default:
                    log.error("Erreur : utilisateur non reconnu");
                    break;
            }
        }
        else if(this.etatCourant == 1)
        {
            ProfesseurRepository professeurRepository = new ProfesseurRepository(entityManager);
            creneaux = professeurRepository.getWeekCrenaux(idProfChoisi, this.numSemaine, annee, 0, 100);

        }
        else if(this.etatCourant == 2)
        {
            SalleRepository salleRepository = new SalleRepository(entityManager);
            creneaux = salleRepository.getWeekCrenaux(salleChoisie, this.numSemaine, annee, 0, 100); 
        }
        else if(this.etatCourant == 3)
        {
            GroupeRepository groupeRepository = new GroupeRepository(entityManager);
            creneaux = groupeRepository.getWeekCreneaux(codeGroupeChoisi, this.numSemaine, annee, 0, 100);

        }

        majCreneaux(this.numSemaine);
    }

    public void majCreneaux(int numSemaine){
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        //this.gpJour.getChildren().clear();
        this.gcJours.clearRect(0, 0, this.cJours.getWidth(), this.cJours.getHeight());
        int annee = 0;
        if (numSemaine >= this.premierSemaine){
            annee = this.anneeDebut;
        }
        else{
            annee = this.anneeDebut + 1;
        }
        OffsetDateTime permierJourSemaine = OffsetDateTime.now()
        .with(weekFields.weekOfWeekBasedYear(),numSemaine).withYear(annee)
        .with(TemporalAdjusters.previousOrSame(weekFields.getFirstDayOfWeek()));
        int anneeTest = permierJourSemaine.getYear();

        OffsetDateTime permierJourAnnee = OffsetDateTime.now().withYear(anneeTest).withMonth(9).withDayOfMonth(1);
        for(int i = 0; i < this.nbJour; i++){
            this.gcJours.strokeText(permierJourSemaine.plusDays(i).getDayOfWeek().toString() + " " + permierJourSemaine.plusDays(i).toLocalDate(), i * this.wGrille / this.nbJour, 10);
        }


        
        this.gpCreneaux.getChildren().clear();
        // Les 2 prochaines lignes sont à supprimer à long terme
        //if (utilisateur instanceof Etudiant) {
        
            // EtudiantRepository etudiantRepository = new EtudiantRepository(entityManager);
            // this.creneaux = etudiantRepository.getWeekCreneaux(utilisateur.getId(), numSemaine, anneeTest, 0, 100);
            // genererCreneaux();
        guiCreneaux = new ArrayList<>();
        for(Creneau creneau : this.creneaux){
                GuiCreneau guiCreneau = new GuiCreneau(this.popups ,this.utilisateur, this.gpCreneaux, creneau, this.wGrille, this.hGrille, this.nbHeure, this.nbJour, entityManager);
                gestionCollision(guiCreneau);
                guiCreneaux.add(guiCreneau);
                guiCreneau.afficherCreneau();
            
            
        }
        // }
    }

    public void gestionCollision(GuiCreneau guiCreneau){
        for(GuiCreneau gc : guiCreneaux){
            if(((guiCreneau.getCreneau().getHeureDebut().isAfter(gc.getCreneau().getHeureDebut()) || guiCreneau.getCreneau().getHeureDebut().isEqual(gc.getCreneau().getHeureDebut()))
             && (guiCreneau.getCreneau().getHeureDebut().isBefore(gc.getCreneau().getHeureFin()) || guiCreneau.getCreneau().getHeureDebut().isEqual(gc.getCreneau().getHeureFin())))
             || 
             ((guiCreneau.getCreneau().getHeureFin().isAfter(gc.getCreneau().getHeureDebut()) || guiCreneau.getCreneau().getHeureFin().isEqual(gc.getCreneau().getHeureDebut()))
             && (guiCreneau.getCreneau().getHeureFin().isBefore(gc.getCreneau().getHeureFin()) || guiCreneau.getCreneau().getHeureFin().isEqual(gc.getCreneau().getHeureFin())))){
                
                gc.setCollision(gc.getCollision() + 1);
                guiCreneau.setCollision(guiCreneau.getCollision() + 1);
                guiCreneau.setPosCollision(gc.getPosCollision() + 1);
                //gc.afficherCreneau();
                gc.majAffichage();
            }
        }
    }

    private void chargerSalles() {
        List<Salle> salles;
        SalleRepository salleRepository = new SalleRepository(entityManager);
        salles = salleRepository.getAll(0, 100);
        this.salleDropdown.getItems().clear();
        for (Salle salle : salles) {
            this.salleDropdown.getItems().add(salle.getCode());
        }
    }

    private void chargerGroupes(){
        List<GroupeDTO> groupes;
        GroupeRepository groupeRepository = new GroupeRepository(entityManager);
        groupes = groupeRepository.getAllDTO(0, 100);
        this.groupesDropdown.getItems().clear();
        for (GroupeDTO groupe : groupes) {
            this.groupesDropdown.getItems().add(groupe.getCode());
        }
    }

    private void chargerProfesseurs(){
        ProfesseurRepository professeurRepository = new ProfesseurRepository(entityManager);
        professeurs = professeurRepository.getAllDTO(0, 100);
        this.profDropdown.getItems().clear();
        for (ProfesseurDTO professeur : professeurs) {
            this.profDropdown.getItems().add(professeur.getNom() +" "+ professeur.getPrenom());
        }
    }    

}

