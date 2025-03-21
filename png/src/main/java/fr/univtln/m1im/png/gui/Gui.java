package fr.univtln.m1im.png.gui;

import java.util.ArrayList;
import java.util.List;

import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Etudiant;
import fr.univtln.m1im.png.repositories.EtudiantRepository;
import jakarta.persistence.EntityManager;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

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
    private Canvas grille;
    private int wGrille;
    private int hGrille;
    private GraphicsContext gcGrille;
    private int nbHeure;
    private int nbJour;

    private EntityManager entityManager;
    private Etudiant etudiant;
    private List<Creneau> creneaux;
    private List<Creneau> cCreneaux;

    public Gui(Etudiant etudiant, Group group, int width, int height, EntityManager entityManager) {
        this.etudiant = etudiant;
        this.group = group;
        this.width = width;
        this.height = height;
        this.entityManager = entityManager;

        //variables
        this.nbHeure = 12;
        this.nbJour = 6;
        this.nbSemaines = 30;

        this.gui = new GridPane();
        this.group.getChildren().add(this.gui);

        this.gdHeuresEdt = new GridPane();
        this.gdSemainesGrille = new GridPane();
        this.gdSemaines = new GridPane();

        this.gui.add(this.gdHeuresEdt, 0, 0);
        this.gui.add(this.gdSemainesGrille, 1, 0);
        this.semaines = new ArrayList<>();
        this.wGrille = width * 9/10;
        this.hGrille = height * 8/10;
        this.grille = new Canvas(this.wGrille,this.hGrille);
        this.heures = new Canvas(width * 1/20, hGrille);
        this.gcHeures = this.heures.getGraphicsContext2D();
        this.gdSemainesGrille.add(this.grille,1,1);
        this.gdSemainesGrille.add(this.gdSemaines,1,0);
        this.gcGrille = this.grille.getGraphicsContext2D();

        // this.gdSemainesGrille.add(this.heures,0,1);
        this.gdHeuresEdt.add(this.heures,0,0);
        this.gdHeuresEdt.add(this.gdSemainesGrille,1,0);

        //Ajout des semaines
        for(int i = 0; i < this.nbSemaines; i++){
            Button semaine = new Button(""+(i+1));
            semaine.setPrefSize(this.wGrille/this.nbSemaines, 20);
            final int index = i;
            semaine.setOnMouseClicked(event -> majCreneaux(index));
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
        
    }

    public void majCreneaux(int numSemaine){
        EtudiantRepository etudiantRepository = new EtudiantRepository(entityManager);
        this.creneaux = etudiantRepository.getCreneaux(etudiant.getId(), 0, 100);
        System.out.println(this.creneaux.getLast().getHeureDebut().getHour());
        int jourDeLaSemaine = 0;
        for(Creneau creneau : this.creneaux){
            switch (creneau.getHeureDebut().getDayOfWeek().toString()) {
                case "MONDAY":
                    jourDeLaSemaine = 0;
                    break;
                case "TUESDAY":
                    jourDeLaSemaine = 1;
                    break;

                case "WEDNESDAY":
                    jourDeLaSemaine = 2;
                    
                    break;
                case "THURSDAY":
                    jourDeLaSemaine = 3;
                    
                    break;

                case "FRIDAY":
                    jourDeLaSemaine = 4;
                    
                    break;

            
                default:
                    jourDeLaSemaine = 5;
                    break;
            }

            
        }
    }
        
        
    

}
