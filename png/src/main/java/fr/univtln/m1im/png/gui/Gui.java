package fr.univtln.m1im.png.gui;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.qos.logback.classic.pattern.Util;
import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Etudiant;
import fr.univtln.m1im.png.model.Utilisateur;
import fr.univtln.m1im.png.repositories.EtudiantRepository;
import jakarta.persistence.EntityManager;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import net.sf.jsqlparser.statement.select.Offset;

@Getter @Setter
public class Gui {
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
    
    private Group gpGrille;
    private Group gpCreneaux;
    private Group gpJour;
    private Canvas cJours;
    private GraphicsContext gcJours;

    private int etatCourant; //0: edt perso,1: edt prof, 2: edt salle, 3: edt groupe

    public Gui(Etudiant etudiant, Group group, int width, int height, EntityManager entityManager, Stage stage, Scene scene) {
        this.etudiant = etudiant;
        this.group = group;
        this.width = width;
        this.height = height;
        this.entityManager = entityManager;

        //variables
        this.nbHeure = 12;
        this.nbJour = 6;
        this.nbSemaines = 30;
        this.etatCourant = 0;
        this.cCreneaux = new ArrayList<>();
        this.gpGrille = new Group();
        this.gpCreneaux = new Group();

        this.gui = new GridPane();
        this.group.getChildren().add(this.gui);

        this.gdHeuresEdt = new GridPane();
        this.gdSemainesGrille = new GridPane();
        this.gdSemainesGrille.setHgap(20);
        this.gdSemainesGrille.setVgap(20);
        this.gdSemaines = new GridPane();

        this.gui.add(this.gdHeuresEdt, 0, 0);
        this.gui.add(this.gdSemainesGrille, 1, 0);
        this.semaines = new ArrayList<>();

        this.wGrille = width * 9/10;
        this.hGrille = height * 8/10;

        this.grille = new Canvas(this.wGrille,this.hGrille);
        this.heures = new Canvas(width * 1/20, hGrille);
        this.gcHeures = this.heures.getGraphicsContext2D();

        this.gpJour = new Group();
        this.cJours = new Canvas(this.width, 10);
        this.gpJour.getChildren().add(this.cJours);
        this.gcJours = this.cJours.getGraphicsContext2D();
        this.gcJours.setFill(Color.YELLOW);
        this.gcJours.fillRect(50, 50, 200, 50);

        this.gdSemainesGrille.add(this.gpJour,1,1);
        //this.gdSemainesGrille.add(this.gpJour,1,1);
        this.gdSemainesGrille.add(this.gpGrille,1,2);
        this.gpGrille.getChildren().add(this.grille);
        this.gpGrille.getChildren().add(this.gpCreneaux);
        this.gdSemainesGrille.add(this.gdSemaines,1,0);
        this.gcGrille = this.grille.getGraphicsContext2D();

        // this.gdSemainesGrille.add(this.heures,0,1);
        this.gdHeuresEdt.add(this.heures,0,0);
        this.gdHeuresEdt.add(this.gdSemainesGrille,1,0);

        //Ajout des semaines
        for(int i = 0; i < this.nbSemaines; i++){
            Button semaine = new Button(""+(i+1));
            semaine.setPrefSize(this.wGrille/this.nbSemaines, 20);
            final int index = i+1;
            // semaine.setOnMouseClicked(event -> majCreneaux(index));
            semaine.setOnMouseClicked(event -> {this.numSemaine = index; genererCreneaux();});
            this.semaines.add(semaine);
            this.gdSemaines.add(semaine, i, 0);
        }

        //Initialisation de la grille
        //Ajout des heures
        for (int i = 0; i < this.nbHeure; i++) {
            this.gcHeures.strokeText((i+8)+":00", 0, i * this.hGrille/this.nbHeure+20);
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
        if(this.etatCourant == 0)
        {
            //TODO : récupérer les créneaux du groupe
        }
        else if(this.etatCourant == 1)
        {
            //TODO : récupérer les créneaux du professeur
        }
        else if(this.etatCourant == 2)
        {
            //TODO : récupérer les créneaux de la salle
        }

        majCreneaux(this.numSemaine);
    }

    public void majCreneaux(int numSemaine){
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        //this.gpJour.getChildren().clear();
        this.gcJours.clearRect(0, 0, this.cJours.getWidth(), this.cJours.getHeight());
        OffsetDateTime permierJourSemaine = OffsetDateTime.now()
        .with(weekFields.weekOfWeekBasedYear(),numSemaine)
        .with(TemporalAdjusters.previousOrSame(weekFields.getFirstDayOfWeek()));
        System.out.println("test : "+permierJourSemaine.getDayOfYear());
        int annee = permierJourSemaine.getYear();
;

        for(int i = 0; i < this.nbJour; i++){
            this.gcJours.strokeText(permierJourSemaine.plusDays(i).getDayOfWeek().toString() + " " + permierJourSemaine.plusDays(i).toLocalDate(), i * this.wGrille / this.nbJour, 10);
        }


        
        this.gpCreneaux.getChildren().clear();
        // Les 2 prochaines lignes sont à supprimer à long terme
        EtudiantRepository etudiantRepository = new EtudiantRepository(entityManager);
        this.creneaux = etudiantRepository.getWeekCreneaux(etudiant.getId(), numSemaine, annee, 0, 100);
        // genererCreneaux();
        for(Creneau creneau : this.creneaux){
                GuiCreneau guiCreneau = new GuiCreneau(this.gpCreneaux, creneau, this.wGrille, this.hGrille, this.nbHeure, this.nbJour);
                guiCreneau.afficherCreneau();
            
            
        }
    }
        
        
    

}
