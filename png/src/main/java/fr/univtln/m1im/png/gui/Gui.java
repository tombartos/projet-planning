package fr.univtln.m1im.png.gui;

import java.util.ArrayList;
import java.util.List;

import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Etudiant;
import fr.univtln.m1im.png.model.Salle;
import fr.univtln.m1im.png.repositories.EtudiantRepository;
import jakarta.persistence.EntityManager;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    private List<Rectangle> cCreneaux;
    
    private Group gpGrille;
    private Group gpCreneaux;
    private Group gpBarreFiltres;

    private HBox barreFiltres; // une barre de boutons pour filtrer les crene
    private ComboBox<String> salleDropdown; 

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
        this.cCreneaux = new ArrayList<>();
        this.gpGrille = new Group();
        this.gpCreneaux = new Group();

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
        this.gdSemainesGrille.add(this.gpGrille,1,1);
        this.gpGrille.getChildren().add(this.grille);
        this.gpGrille.getChildren().add(this.gpCreneaux);
        this.gdSemainesGrille.add(this.gdSemaines,1,1);
        this.gcGrille = this.grille.getGraphicsContext2D();

        // this.gdSemainesGrille.add(this.heures,0,1);
        this.gdHeuresEdt.add(this.heures,0,0);
        this.gdHeuresEdt.add(this.gdSemainesGrille,1,0);

        //Ajout des semaines
        for(int i = 0; i < this.nbSemaines; i++){
            Button semaine = new Button(""+(i+1));
            semaine.setPrefSize(this.wGrille/this.nbSemaines, 20);
            final int index = i+1;
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

        //Ajout de la barre de filtres
        this.gpBarreFiltres = new Group();
        this.barreFiltres = new HBox();
        this.barreFiltres.setSpacing(10); // Espacement entre les boutons
        this.salleDropdown = new ComboBox<>();
        this.salleDropdown.setPromptText("Salles");
        Button btnFormation = new Button("Formation");
        Button btnGroupe = new Button("Groupe");
        
        

        // Charger les salles depuis la base de données
        chargerSalles();


        // Gérer la sélection d'une salle
        this.salleDropdown.setOnAction(event -> {
            String salleChoisie = this.salleDropdown.getValue();
            if (salleChoisie != null) {
                afficherCoursSalle(salleChoisie);
                this.salleDropdown.setVisible(false); // Masquer après sélection
            }
        });

        // Ajouter les boutons dans la barre horizontale
        this.barreFiltres.getChildren().addAll(this.salleDropdown, btnFormation, btnGroupe);
        // Ajouter la barre de boutons au groupe
        this.gpBarreFiltres.getChildren().add(barreFiltres);
        // Ajouter ce groupe à l'interface
        this.gdSemainesGrille.add(gpBarreFiltres, 1, 0);

        
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


    public void majCreneaux(int numSemaine){
        this.gpCreneaux.getChildren().clear();
        EtudiantRepository etudiantRepository = new EtudiantRepository(entityManager);
        this.creneaux = etudiantRepository.getCreneaux(etudiant.getId(), 0, 100);
        for(Creneau creneau : this.creneaux){
            System.out.println((int)(creneau.getHeureDebut().getDayOfYear()/7 ));
            if((int)(creneau.getHeureDebut().getDayOfYear()/7 )== numSemaine)
            {
                GuiCreneau guiCreneau = new GuiCreneau(this.gpCreneaux, creneau, this.wGrille, this.hGrille, this.nbHeure, this.nbJour);
                guiCreneau.afficherCreneau();
            }
            
        }
    }

    private void chargerSalles() {
        List<Salle> salles;
        try (EntityManager em = entityManager) {
            salles = em.createQuery("SELECT s FROM Salle s", Salle.class).getResultList();
        }

        for (Salle salle : salles) {
            this.salleDropdown.getItems().add(salle.getCode());
        }
    }

    private void afficherCoursSalle(String salleCode) {
        this.gpCreneaux.getChildren().clear(); // Effacer les créneaux actuels
    
        List<Creneau> creneauxSalle;
        try (EntityManager em = entityManager) {
            creneauxSalle = em.createQuery("SELECT c FROM Creneau c WHERE c.salle.code = :salleCode", Creneau.class)
                              .setParameter("salleCode", salleCode)
                              .getResultList();
        }
    
        for (Creneau creneau : creneauxSalle) {
            GuiCreneau guiCreneau = new GuiCreneau(this.gpCreneaux, creneau, this.wGrille, this.hGrille, this.nbHeure, this.nbJour);
            guiCreneau.afficherCreneau();
        }
    }
    

        
        
    

}
