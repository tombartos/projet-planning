package fr.univtln.m1im.png.gui;

import javafx.scene.paint.Color;

import java.time.OffsetDateTime;

import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Professeur;
import fr.univtln.m1im.png.model.Module;
import fr.univtln.m1im.png.model.Salle;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GuiCreneau {
    private Group group;
    private Rectangle rectangle;
    private Label label;

    //private int semaine;
    private int width;
    private int height;
    private int nbHeure;
    private int nbJour;
    private int jourDeLaSemaine;

    private Creneau creneau;

    private OffsetDateTime premierJour; // Premier jour de l'année
    private OffsetDateTime dernierJour; // Dernier jour de l'année

    public GuiCreneau(Group group, Creneau creneau, int width, int height, int nbHeure, int nbJour) {
        this.group = group;
        this.creneau = creneau;
        this.width = width;
        this.height = height;
        this.nbHeure = nbHeure;
        this.nbJour = nbJour;
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

    public void afficherCreneau()
    {
        System.out.println(creneau.getHeureDebut().getDayOfYear());
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
        rectangle = new Rectangle(jourDeLaSemaine*width/nbJour,convHeure(creneau)*height/nbHeure,width/nbJour,convDuree(creneau)*height/nbHeure);
        switch (creneau.getType()) {
            case "CM":
                rectangle.setFill(Color.YELLOW);

                break;

            case "TD":
            rectangle.setFill(Color.GREEN);
                
                break;

            case "TP":
                rectangle.setFill(Color.RED);
                
                break;
        
            default:
            rectangle.setFill(Color.WHITE);
                break;
        }
        
        label = new Label();
        String listModule = new String();
        for(Module module : creneau.getModules()){
            listModule += module.getNom()+"\n";
        }
        String listProf = new String();
        for(Professeur prof : creneau.getProfesseurs()){
            listProf += prof.getPrenom()+" "+prof.getNom()+"\n";
        }
        label.setText("Un cours "+creneau.getType()+"\n"+listModule+listProf+"\n"+creneau.getSalle().getCode());
        label.setLayoutX(jourDeLaSemaine*width/nbJour);
        label.setLayoutY(convHeure(creneau)*height/nbHeure);

        group.getChildren().add(rectangle);
        group.getChildren().add(label);
    }

}
