package fr.univtln.yroblin156;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

public class Creneau {
    private String date;
    private String heure;
    private String duree;
    private String matiere;
    private String salle;
    private String professeur;
    private int cour;

    private Rectangle cvCreneau;
    private Label labelCreneau;

    //Temporaire
    private int jour;
    private int semaine;

    public Creneau(String date, int jour, String heure, String duree, String matiere, int cour, String salle, String professeur, int semaine) {
        this.date = date;
        this.jour = jour;
        this.heure = heure;
        this.duree = duree;
        this.matiere = matiere;
        this.cour = cour;
        this.salle = salle;
        this.professeur = professeur;
        this.labelCreneau = new Label();
        
        this.semaine = semaine;
    }

    public float convHeure()
    {
        float r;
        List<Integer> h = new ArrayList<>();
        for (String part : this.heure.split(":")) {
            h.add(Integer.parseInt(part));
        }
        r = h.get(0)-8;
        r += h.get(1)/60f;
        //System.out.println(r);
        //int m = this.heure.charAt(2) - '0';
        return r;

    }

    public float convDuree()
    {
        float r;
        List<Integer> h = new ArrayList<>();
        for (String part : this.duree.split(":")) {
            h.add(Integer.parseInt(part));
        }
        r = h.get(0);
        r += h.get(1)/60f;
        //System.out.println(r);
        //int m = this.heure.charAt(2) - '0';
        return r;
    }


    public void afficherCreneau(Group group,int x, int y, int width, int height) 
    {
        this.cvCreneau = new Rectangle(x+width*this.jour, y+height*convHeure(), width, height*convDuree());
        this.cvCreneau.setOnMousePressed(event -> {this.labelCreneau.setText("Clicked");});
        this.cvCreneau.setOnMouseReleased(event -> {this.labelCreneau.setText(this.matiere + "\n" + this.heure + "\n" + this.duree + "\n" + this.salle + "\n" + this.professeur);});
        if(this.cour == 0)
            this.cvCreneau.setStyle("-fx-fill: #00ff00; -fx-stroke: #000000; -fx-stroke-width: 1;");
        else
        if (this.cour == 1)
            this.cvCreneau.setStyle("-fx-fill: #ffff00; -fx-stroke: #000000; -fx-stroke-width: 1;");
        else
        if (this.cour == 2)
            this.cvCreneau.setStyle("-fx-fill: #ff0000; -fx-stroke: #000000; -fx-stroke-width: 1;");
        else
        {
            this.cvCreneau.setStyle("-fx-fill: #ffffff; -fx-stroke: #000000; -fx-stroke-width: 1;");
            
        }
        
        this.labelCreneau.setText(this.matiere + "\n" + this.heure + "\n" + this.duree + "\n" + this.salle + "\n" + this.professeur);
        this.labelCreneau.setLayoutX(x+width*this.jour);
        this.labelCreneau.setLayoutY(y+height*convHeure());
        group.getChildren().add(this.cvCreneau);
        group.getChildren().add(this.labelCreneau);
 
    }

    public int getSemaine() {
        return semaine;
    }

}
