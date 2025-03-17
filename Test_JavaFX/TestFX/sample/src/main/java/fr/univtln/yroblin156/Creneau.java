package fr.univtln.yroblin156;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;

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
        
        this.semaine = semaine;
    }

    public int convHeure()
    {
        int h = this.heure.charAt(0) - '0'-8;
        //int m = this.heure.charAt(2) - '0';
        return h;

    }



    public Rectangle afficherCreneau(int x, int y, int width, int height) 
    {
        this.cvCreneau = new Rectangle(x+width*this.jour, y+height*convHeure(), width, height);
        return this.cvCreneau;
    }

    public int getSemaine() {
        return semaine;
    }

}
