package fr.univtln.m1im.png.gui;

import javafx.scene.paint.Color;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Professeur;
import fr.univtln.m1im.png.model.Module;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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

    private int collision;
    private int posCollision;

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

        this.collision = 1;
        this.posCollision = 0;
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
        System.out.println("TEST");
        System.out.println(convHeure(creneau));
        System.out.println(creneau.getHeureDebut().getHour());
        //System.out.println(creneau.getHeureDebut().getDayOfYear());
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
        rectangle = new Rectangle(jourDeLaSemaine*width/nbJour + (width/nbJour * posCollision/collision),
        convHeure(creneau)*height/nbHeure, 
        width/nbJour /collision,
        convDuree(creneau)*height/nbHeure);
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
        label.setPrefSize(width/nbJour /collision, height/nbHeure*convDuree(creneau));
        label.setLayoutX(jourDeLaSemaine*width/nbJour+ (width/nbJour * posCollision/collision));
        label.setLayoutY(convHeure(creneau)*height/nbHeure);
        label.setOnMouseClicked(e -> {
            
            rectangle.setStroke(Color.BLUE);
            rectangle.setStrokeWidth(4);
            System.out.println("Rectangle clicked: " + creneau.toString());
            afficherInformation();

        });
        // label.setStyle("-fx-font-size: "+10+"px");
        label.setStyle("-fx-font-size: " + 10 + "px; -fx-alignment: center; -fx-text-alignment: center;");
        String listModule = new String();
        for(Module module : creneau.getModules()){
            listModule += module.getNom()+"\n";
        }
        String listProf = new String();
        for(Professeur prof : creneau.getProfesseurs()){
            listProf += prof.getPrenom()+" "+prof.getNom()+"\n";
        }
        label.setText(creneau.getSalle().getCode()+"\n"+creneau.getGroupes().getFirst().getCode()+"\n"+listModule+creneau.getType()+"\n"+listProf);

        group.getChildren().add(rectangle);
        group.getChildren().add(label);
    }

    public void majAffichage()
    {
        rectangle.setX(jourDeLaSemaine*width/nbJour+ (width/nbJour * posCollision/collision));
        //rectangle.setLayoutY(convHeure(creneau)*height/nbHeure);
        rectangle.setWidth(width/nbJour /collision);
        rectangle.setHeight(height/nbHeure*convDuree(creneau));
        label.setPrefSize(width/nbJour /collision, height/nbHeure*convDuree(creneau));
        label.setLayoutX(jourDeLaSemaine*width/nbJour+ (width/nbJour * posCollision/collision));
        label.setLayoutY(convHeure(creneau)*height/nbHeure);
    }

    public void afficherInformation()
    {
        Stage popup = new Stage();
        //Désélectionner le rectangle lors de la fermeture de la fenêtre
        popup.onCloseRequestProperty().set(e -> {
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
        Label noteProfLabel = new Label("Aucune note");
        noteProfLabel.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
        List<Label> infoModules = new ArrayList<>();
        int nbAffichage = 3;
        //Label infoLabel2 = new Label();
        List<Creneau> listCreneaux = creneau.getModules().getFirst().getCreneaux().stream()
            .sorted((c1, c2) -> c1.getHeureDebut().compareTo(c2.getHeureDebut()))
            // .filter()
            .toList();
        String info = new String();
            for(int i = 0; i < nbAffichage; i++){
                info = listCreneaux.get(i).getHeureDebut().getDayOfWeek() + "\t";
                info += listCreneaux.get(i).getHeureDebut().toLocalDate() + "\t";
                info += listCreneaux.get(i).getHeureDebut().getHour() + ":"+listCreneaux.get(i).getHeureDebut().getMinute()+ "\t";
                info += listCreneaux.get(i).getHeureFin().getHour() + ":"+listCreneaux.get(i).getHeureFin().getMinute()+ "\t";
                info += listCreneaux.get(i).getType() + "\n";
                infoModules.add(new Label(info));
                if(this.creneau.getHeureDebut().equals(listCreneaux.get(i).getHeureDebut())){
                    infoModules.get(i).setTextFill(Color.RED);
                    infoModules.get(i).setStyle("-fx-background-color: lightgray;");
                }
                else{
                    infoModules.get(i).setTextFill(Color.BLACK);
                    infoModules.get(i).setStyle("-fx-background-color: white;");
                }
                
                if(listCreneaux.get(i).getHeureDebut().isBefore(this.creneau.getHeureDebut())){
                    infoModules.get(i).setStyle("-fx-background-color: lightgray;");
                }
                gridModules.add(infoModules.get(i), 0, 1+i);

            }
            //infoLabel2.setText(info);

        infoGroup.getChildren().add(grid);
        //Mise en place de la scrollbar
        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setOrientation(javafx.geometry.Orientation.VERTICAL);
        // scrollBar.setLayoutX(200);
        
        scrollBar.setMin(0);
        scrollBar.setMax(listCreneaux.size()-nbAffichage);
        scrollBar.setValue(0);
        scrollBar.setBlockIncrement(1);
        scrollBar.setUnitIncrement(1);
        scrollBar.valueProperty().addListener((obs, oldValue, newValue) -> {
            String infoSc = new String();
            int j = 0;
            for(int i = (int) scrollBar.getValue(); i < (int) scrollBar.getValue() + nbAffichage; i++){
                infoSc = listCreneaux.get(i).getHeureDebut().getDayOfWeek() + "\t";
                infoSc += listCreneaux.get(i).getHeureDebut().toLocalDate() + "\t";
                infoSc += listCreneaux.get(i).getHeureDebut().getHour() + ":" + listCreneaux.get(i).getHeureDebut().getMinute()+ "\t";
                infoSc += listCreneaux.get(i).getHeureFin().getHour() + ":" + listCreneaux.get(i).getHeureFin().getMinute()+ "\t";
                infoSc += listCreneaux.get(i).getType() + "\n";
                infoModules.get(j).setText(infoSc);

                if(this.creneau.getHeureDebut().equals(listCreneaux.get(i).getHeureDebut())){
                    infoModules.get(j).setTextFill(Color.RED);
                    infoModules.get(j).setStyle("-fx-background-color: lightgray;");
                }
                else{
                    infoModules.get(j).setTextFill(Color.BLACK);
                    infoModules.get(j).setStyle("-fx-background-color: white;");
                }
                
                if(listCreneaux.get(j).getHeureDebut().isBefore(this.creneau.getHeureDebut())){
                    infoModules.get(j).setStyle("-fx-background-color: lightgray;");
                }

                j++;
            }
            // infoLabel2.setText(infoSc);
        });
        

        // infoGroup.getChildren().add(scrollBar);
        infoLabel.setText(this.label.getText());

        //infoGroup.getChildren().add(infoLabel);
        grid.add(infoLabel, 0, 0);
        grid.add(noteProfLabel, 0, 1);
        grid.add(gridModules, 0, 2);
        grid.add(scrollBar, 1, 2);

        popup.setTitle("Information du créneau");
        popup.setWidth(400);
        popup.setHeight(300);
        popup.setScene(infoScene);
        popup.initStyle(StageStyle.UTILITY);
        popup.show();
    }

}
