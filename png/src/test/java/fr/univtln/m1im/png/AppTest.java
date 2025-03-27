package fr.univtln.m1im.png;

import org.junit.jupiter.api.Test;

import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Etudiant;
import fr.univtln.m1im.png.model.Groupe;
import fr.univtln.m1im.png.model.Professeur;
import fr.univtln.m1im.png.model.Salle;
import jakarta.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.univtln.m1im.png.model.Module;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Unit test for simple App.
 */
class AppTest {
    private static final Logger log = LoggerFactory.getLogger(AppTest.class);

    void createEtudiantUser(String username, String password){

        //I don't know why it doesnt work, there is just no error but the user is not created

        try (EntityManager entityManager = Utils.getEntityManagerFactory().createEntityManager()){
            entityManager.createNativeQuery("CREATE USER :username WITH PASSWORD ':password'")
            .setParameter("username", username)
            .setParameter("password", password)
            .executeUpdate();

            entityManager.createNativeQuery("GRANT CONNECT ON DATABASE postgres TO :username")
            .setParameter("username", username)
            .executeUpdate();

            entityManager.createNativeQuery("GRANT USAGE ON SCHEMA public TO :username")
            .setParameter("username", username)
            .executeUpdate();

            entityManager.createNativeQuery("GRANT SELECT ON ALL TABLES IN SCHEMA public TO :username")
            .setParameter("username", username)
            .executeUpdate();
            System.out.println("User created successfully");
        }
        catch (Exception e) {
            log.error("Failed to create user", e);
        }
    }
    /**
     * Rigorous Test.
     */
    @Test
    void testApp() {
        //!!!!All this is to initialize the database with a default admin account, this will be removed, this is only until we have the data generator  !!!!
        System.err.println("Hello World!");
        // Initialize EntityManagerFactory
        Utils.initconnection("postgres", "mysecretpassword");

        // Create entities
        // ids are generated automatically, by default when creating the object it's null, after persisting it's set
        Groupe groupe = Groupe.builder().code("G1").nom("Groupe1").formation("Formation1").build();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        Etudiant etudiant = Etudiant.builder().nom("Nom1").prenom("Prenom1").login("et1").email("et1@email.com")
            .password("password").dateNaissance(now).build();
        Professeur professeur = Professeur.builder().nom("Nom2").prenom("Prenom2").login("pr1").email("pr1@email.com")
                .password("password").dateNaissance(now).build();
        Module module = Module.builder().code("M1").nom("Module1").description("Description1").nbHeuresCM(10).nbHeuresTD(20).nbHeuresTP(30).build();
        OffsetDateTime heureDebut = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime heureFin = heureDebut.plusHours(2); // Add 2 hours
        Salle salle = Salle.builder().code("T001").description("Videoproj + PC").capacite(30).build();
        Creneau creneau = Creneau.builder().type("CM").heureDebut(heureDebut).heureFin(heureFin).salle(salle).build();
        Creneau creneau2 = Creneau.builder().type("TP").heureDebut(heureDebut.minusDays(14)).heureFin(heureFin.minusDays(14).plusHours(1)).salle(salle).build();
        Creneau creneau3 = Creneau.builder().type("TD").heureDebut(heureDebut.plusDays(7)).heureFin(heureFin.plusDays(7)).salle(salle).build();
        Creneau creneau4 = Creneau.builder().type("EXAM").heureDebut(heureDebut.minusHours(3)).heureFin(heureDebut.minusHours(2)).salle(salle).build();

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

        creneau4.getModules().add(module);
        module.getCreneaux().add(creneau4);
        creneau4.getGroupes().add(groupe);
        groupe.getCreneaux().add(creneau4);
        creneau4.getProfesseurs().add(professeur);
        professeur.getCreneaux().add(creneau4);



        // Persist entities, don't use save method, it's not recommended for the first time
        try (EntityManager entityManager = Utils.getEntityManagerFactory().createEntityManager()) {
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
            entityManager.persist(creneau4);
            entityManager.getTransaction().commit();
            log.info("Entities persisted successfully");
        } catch (Exception e) {
            log.error("Failed to persist entities", e);
        }

        createEtudiantUser(etudiant.getLogin(), etudiant.getPassword());
        //Doenst work, no error but the user is not created
        // CREATE USER et1 WITH PASSWORD 'password'; 
        // GRANT CONNECT ON DATABASE postgres TO et1;
        // GRANT USAGE ON SCHEMA public TO et1;
        // GRANT SELECT ON ALL TABLES IN SCHEMA public TO et1;
        Utils.getEntityManagerFactory().close();

        //Tests
        // try (EntityManager entityManager = Utils.getEntityManagerFactory().createEntityManager()){
        //     EtudiantRepository etudiantRepository = new EtudiantRepository(entityManager);

        //     //log.info(etudiantRepository.getAllCreneaux(etudiant.getId(), 0, 100).toString());
        //     log.info(etudiantRepository.getWeekCreneaux(etudiant.getId(), 1, 2024, 0, 100).toString());
        //     //creneau = week 12 2025
        //     //creneau2 = week 10 2025
        //     log.info("TEST1");
        //     log.info(etudiantRepository.getWeekCreneaux(etudiant.getId(), 12, 2025, 0, 100).toString());
        //     log.info("TEST2");
        //     log.info(etudiantRepository.getWeekCreneaux(etudiant.getId(), 10, 2025, 0, 100).toString());
        //     log.info("TEST3");
        //     log.info(etudiantRepository.getWeekCreneaux(etudiant.getId(), 10, 2024, 0, 100).toString());
        // }
    }
}
