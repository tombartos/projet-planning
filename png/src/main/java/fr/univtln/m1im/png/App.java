package fr.univtln.m1im.png;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import fr.univtln.m1im.png.gui.Gui;
import fr.univtln.m1im.png.model.*;
import fr.univtln.m1im.png.model.Module;
import fr.univtln.m1im.png.repositories.*;

/**
 * Hello world!
 */
@Slf4j
public final class App extends Application{
    private static final EntityManagerFactory emf;
    private static Etudiant etudiant;
    
        static {
            log.info("Starting EntityManagerFactory initialization");
            EntityManagerFactory tryEmf = null;
    
            try {
                log.info("Initializing EntityManagerFactory");
                tryEmf = Persistence.createEntityManagerFactory(DatabaseConfig.PERSISTENCE_UNIT);
                log.info("EntityManagerFactory initialized successfully");
            } catch (Exception e) {
                log.error("Failed to create EntityManagerFactory", e);
                System.exit(0);
            }
            emf = tryEmf;
        }
    
        public static EntityManagerFactory getEntityManagerFactory() {
            return emf;
        }
    
        public static void main(String[] args) {
            log.info("Entering main method");
    
            // Create entities
            // ids are generated automatically, by default when creating the object it's null, after persisting it's set
            Groupe groupe = Groupe.builder().code("G1").nom("Groupe1").formation("Formation1").build();
            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
            etudiant = Etudiant.builder().nom("Nom1").prenom("Prenom1").login("et1").email("et1@email.com")
                .password("password").dateNaissance(now).build();
        Professeur professeur = Professeur.builder().nom("Nom2").prenom("Prenom2").login("pr1").email("pr1@email.com")
                .password("password").dateNaissance(now).build();
        Module module = Module.builder().code("M1").nom("Module1").description("Description1").nbHeuresCM(10).nbHeuresTD(20).nbHeuresTP(30).build();
        OffsetDateTime heureDebut = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime heureFin = heureDebut.plusHours(2); // Add 2 hours
        Salle salle = Salle.builder().code("T001").description("Videoproj + PC").capacite(30).build();
        Creneau creneau = Creneau.builder().type("CM").heureDebut(heureDebut).heureFin(heureFin).salle(salle).build();
        Creneau creneau3 = Creneau.builder().type("TD").heureDebut(heureDebut.minusDays(3)).heureFin(heureFin.minusDays(3)).salle(salle).build();
        Creneau creneau2 = Creneau.builder().type("TP").heureDebut(heureDebut.minusDays(23)).heureFin(heureFin.minusDays(23).plusHours(1)).salle(salle).build();

        groupe.getEtudiants().add(etudiant);
        etudiant.getGroupes().add(groupe);
        module.getProfesseurs().add(professeur);
        professeur.getModules().add(module);
        module.getGroupes().add(groupe);
        groupe.getModules().add(module);
        creneau.getModules().add(module);
        module.getCreneaux().add(creneau);
        creneau.getGroupes().add(groupe);
        groupe.getCreneaux().add(creneau);
        creneau.getProfesseurs().add(professeur);
        professeur.getCreneaux().add(creneau);

        creneau2.getModules().add(module);
        module.getCreneaux().add(creneau2);
        creneau2.getGroupes().add(groupe);
        groupe.getCreneaux().add(creneau2);
        creneau2.getProfesseurs().add(professeur);
        professeur.getCreneaux().add(creneau2);

        creneau3.getModules().add(module);
        module.getCreneaux().add(creneau3);
        creneau3.getGroupes().add(groupe);
        groupe.getCreneaux().add(creneau3);
        creneau3.getProfesseurs().add(professeur);
        professeur.getCreneaux().add(creneau3);


        // Persist entities, don't use save method, it's not recommended for the first time
        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            log.info("Persisting entities");
            entityManager.getTransaction().begin();
            entityManager.persist(salle);
            entityManager.persist(professeur);
            entityManager.persist(groupe);
            entityManager.persist(etudiant);
            entityManager.persist(module);
            entityManager.persist(creneau);
            entityManager.persist(creneau2);
            entityManager.persist(creneau3);
            entityManager.getTransaction().commit();
            log.info("Entities persisted successfully");
        } catch (Exception e) {
            log.error("Failed to persist entities", e);
        }

        //Tests
        // try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()){
        //     EtudiantRepository etudiantRepository = new EtudiantRepository(entityManager);

        //     log.info(etudiantRepository.getCreneaux(etudiant.getId(), 0, 100).toString());
        // }
        launch(args);
       
    }

    private static final class DatabaseConfig {
        private static final String PERSISTENCE_UNIT = "png";
    }

    @Override
    public void start(Stage stage)  {
        Group root = new Group();
        int width = 1200;
        int height = 800;
        Scene scene = new Scene(root, width, height);
        new Gui(etudiant,root, width, height, getEntityManagerFactory().createEntityManager());
        stage.setScene(scene);
        stage.setTitle("Hyperplanning");
        stage.show();
    }
}
//LINUX :
// mvn exec:java -Dexec.mainClass="fr.univtln.m1im.png.App"

//WINDOWS:
//mvn exec:java "-Dexec.mainClass=fr.univtln.m1im.png.App"