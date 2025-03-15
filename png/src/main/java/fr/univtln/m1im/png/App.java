package fr.univtln.m1im.png;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import fr.univtln.m1im.png.model.*;
import fr.univtln.m1im.png.model.Module;

/**
 * Hello world!
 */
@Slf4j
public final class App {
    private static final EntityManagerFactory emf;

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
        //TODO: fix failed to persist professor

        // Create entities
        Groupe groupe = Groupe.builder().code("G1").nom("Groupe1").formation("Formation1").build();
        Etudiant etudiant = Etudiant.builder().id(0L).nom("Nom1").prenom("Prenom1").login("et1").email("et1@email.com")
                .password("password").dateNaissance(new Date()).build();
        Professeur professeur = Professeur.builder().id(1L).nom("Nom2").prenom("Prenom2").login("pr1").email("pr1@email.com")
                .password("password").dateNaissance(new Date()).build();
        Module module = Module.builder().code("M1").nom("Module1").description("Description1").nbHeuresCM(10).nbHeuresTD(20).nbHeuresTP(30).build();
        Date heureDebut = new Date();
        Date heureFin = new Date(heureDebut.getTime() + 2 * 60 * 60 * 1000); // Add 2 hours
        Creneau creneau = Creneau.builder().id(2L).heureDebut(heureDebut).heureFin(heureFin).build();

        // groupe.getEtudiants().add(etudiant);
        // etudiant.getGroupes().add(groupe);
        // module.getProfesseurs().add(professeur);
        // professeur.getModules().add(module);
        // module.getGroupes().add(groupe);
        // groupe.getModules().add(module);
        // creneau.getModules().add(module);
        // module.getCreneaux().add(creneau);
        // creneau.getGroupes().add(groupe);
        // groupe.getCreneaux().add(creneau);
        // creneau.getProfesseurs().add(professeur);
        // professeur.getCreneaux().add(creneau);

        // Persist entities
        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            log.info("Persisting groupe");
            entityManager.getTransaction().begin();
            entityManager.persist(groupe);
            entityManager.getTransaction().commit();
            log.info("Groupe persisted successfully");
        } catch (Exception e) {
            log.error("Failed to persist groupe", e);
        }

        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            log.info("Persisting etudiant");
            entityManager.getTransaction().begin();
            entityManager.persist(etudiant);
            entityManager.getTransaction().commit();
            log.info("Etudiant persisted successfully");
        } catch (Exception e) {
            log.error("Failed to persist etudiant", e);
        }

        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            log.info("Persisting professeur");
            entityManager.getTransaction().begin();
            entityManager.persist(professeur);
            entityManager.getTransaction().commit();
            log.info("Professeur persisted successfully");
        } catch (Exception e) {
            log.error("Failed to persist professeur", e);
        }

        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            log.info("Persisting module");
            entityManager.getTransaction().begin();
            entityManager.persist(module);
            entityManager.getTransaction().commit();
            log.info("Module persisted successfully");
        } catch (Exception e) {
            log.error("Failed to persist module", e);
        }

        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            log.info("Persisting creneau");
            entityManager.getTransaction().begin();
            entityManager.persist(creneau);
            entityManager.getTransaction().commit();
            log.info("Creneau persisted successfully");
        } catch (Exception e) {
            log.error("Failed to persist creneau", e);
        }




    }

    private final class DatabaseConfig {
        private static final String PERSISTENCE_UNIT = "png";
    }
}
