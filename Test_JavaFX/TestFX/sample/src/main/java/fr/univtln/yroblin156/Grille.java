package fr.univtln.yroblin156;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Grille {
    // Taille de la grille
    private int width;
    private int height;
    private Group root;
    private Pane rootBis;
    private Canvas grille;
    private GraphicsContext gc;
    
    // Taille du calendrier
    private int calendarx;
    private int calendary;
    private int calendarwidth;
    private int calendarheight;
    private int jours;
    private int heures;
    private List<Button> semaines;
    private int nbsemaines;

    private List<Creneau> creneaux;
    private List<Rectangle> cvCreneaux;

    private Group group;

    
    


    public Grille(Group root, int width, int height) {
        this.width = width;
        this.height = height;
        this.root = root;
        this.group = new Group();
        this.grille = new Canvas(this.width , this.height);
        this.gc = grille.getGraphicsContext2D();
        this.root.getChildren().add(this.grille);
        // Fond gris
        this.gc.setFill(Color.LIGHTGRAY);
        this.calendarx = this.width * 1/10;
        this.calendary = this.height * 2/10;
        this.calendarwidth = this.width * 8/10;
        this.calendarheight = this.height * 6/10;

        this.creneaux = new ArrayList<Creneau>();
        this.cvCreneaux = new ArrayList<Rectangle>();

        this.creneaux.add( new Creneau("", 0, "8:00", "2:00", "Maths", 0, "A1", "Prof", 1));
        this.creneaux.add( new Creneau("", 2, "9:00", "3:00", "Maths", 1, "A1", "Prof", 2));
        this.creneaux.add( new Creneau("", 2, "13:15", "3:00", "Maths", 1, "A1", "Prof", 1));

        this.gc.fillRect(this.calendarx, this.calendary, this.calendarwidth, this.calendarheight);
        // Calendrier
        jours = 6;
        heures = 12;
        this.gc.setStroke(Color.BLACK);
        for (int i = 0; i < this.heures; i ++) {
            gc.strokeText((i+8)+":00", this.calendarx/2 , this.calendary + i * this.calendarheight / this.heures);
        }
        for (int i = 0; i < this.jours; i ++) {
            for (int j = 0; j < this.heures; j ++) {
                this.gc.strokeRect(this.calendarx + i *  this.calendarwidth / this.jours, this.calendary + j  * this.calendarheight / this.heures, this.calendarwidth / this.jours, this.calendarheight / this.heures);
            }
        }
        this.nbsemaines = 30;
        this.semaines = new ArrayList<Button>();
        for(int i = 0; i < this.nbsemaines; i ++) {
            //gc.strokeRect(this.calendarx + i * this.calendarwidth / this.nbsemaines, this.calendary-(this.calendarwidth/this.nbsemaines), this.calendarwidth / this.nbsemaines,  this.calendarwidth / this.nbsemaines);
            semaines.add(new Button((""+(i+1))));
            this.semaines.get(i).setLayoutX(this.calendarx + i * this.calendarwidth / this.nbsemaines);
            this.semaines.get(i).setLayoutY(this.calendary-(this.calendarwidth/this.nbsemaines));
            this.semaines.get(i).setPrefWidth(this.calendarwidth / this.nbsemaines);
            this.semaines.get(i).setPrefHeight(this.calendarwidth / this.nbsemaines);
            // this.semaines.get(i).setStroke(Color.BLACK);
            final int index = i+1;
            this.semaines.get(i).setOnMouseClicked(e -> afficherSemaine(index));
             gc.strokeText(" "+(i+1), this.calendarx + i * this.calendarwidth / this.nbsemaines, this.calendary);
             this.root.getChildren().add(semaines.get(i));
        }

        
        this.root.getChildren().add(this.group);
    }


    public void afficherSemaine(int semaine) {
        System.out.println("Semaine : "+semaine);
        this.group.getChildren().clear();
        for(Creneau creneau: creneaux) {
            if(creneau.getSemaine() == semaine) {
                creneau.afficherCreneau(this.group,this.calendarx, this.calendary, this.calendarwidth / this.jours, this.calendarheight / this.heures);
            }
        }
    }

    

    public Group getRoot() {
        return root;
    }

    public Pane getRootBis() {
        return rootBis;
    }

    public Canvas getGrille() {
        return grille;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setRoot(Group root) {
        this.root = root;
    }

    public void setRoot(Pane rootBis) {
        this.rootBis = rootBis;
    }

    public void setGrille(Canvas grille) {
        this.grille = grille;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }



    
}
