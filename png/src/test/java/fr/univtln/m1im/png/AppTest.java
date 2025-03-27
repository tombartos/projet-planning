package fr.univtln.m1im.png;

import org.junit.jupiter.api.Test;

import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Etudiant;
import fr.univtln.m1im.png.model.Groupe;
import fr.univtln.m1im.png.model.Professeur;
import fr.univtln.m1im.png.model.Salle;
import fr.univtln.m1im.png.repositories.ProfesseurRepository;
import fr.univtln.m1im.png.repositories.SalleRepository;
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

    void createEtudiantUser(String username, String password) {
        try (EntityManager entityManager = Utils.getEntityManagerFactory().createEntityManager()) {
            entityManager.getTransaction().begin();
    
            // We can't use the classic JPA way to create a user because it's not supported by the JPA standard
            String createUserQuery = String.format("CREATE USER %s WITH PASSWORD '%s';", username, password);
            entityManager.createNativeQuery(createUserQuery).executeUpdate();
    
            // Grant privileges
            String grantConnectQuery = String.format("GRANT CONNECT ON DATABASE postgres TO %s;", username);
            entityManager.createNativeQuery(grantConnectQuery).executeUpdate();
    
            String grantUsageQuery = String.format("GRANT USAGE ON SCHEMA public TO %s;", username);
            entityManager.createNativeQuery(grantUsageQuery).executeUpdate();
    
            String grantSelectQuery = String.format("GRANT SELECT ON ALL TABLES IN SCHEMA public TO %s;", username);
            entityManager.createNativeQuery(grantSelectQuery).executeUpdate();
    
            entityManager.getTransaction().commit();
            log.info("User created successfully: {}", username);
        } catch (Exception e) {
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

        //TESTS
        try (EntityManager entityManager = Utils.getEntityManagerFactory().createEntityManager()){
            ProfesseurRepository professeurRepository = new ProfesseurRepository(entityManager);
            log.info("TEST1");
            log.info(professeurRepository.getWeekCrenaux(professeur.getId(), 9, 2025, 0, 100).toString());
            log.info(professeurRepository.getWeekCrenaux(professeur.getId(), 10, 2025, 0, 100).toString());
            log.info(professeurRepository.getWeekCrenaux(professeur.getId(), 11, 2025, 0, 100).toString());
            log.info(professeurRepository.getWeekCrenaux(professeur.getId(), 12, 2025, 0, 100).toString());
            //creneau = week 12 2025
            //creneau2 = week 10 2025
            log.info("TEST2");
            SalleRepository salleRepository = new SalleRepository(entityManager);
            log.info(salleRepository.getWeekCrenaux(salle.getCode(), 10, 2025, 0, 100).toString());
            log.info(salleRepository.getWeekCrenaux(salle.getCode(), 11, 2025, 0, 100).toString());
            log.info(salleRepository.getWeekCrenaux(salle.getCode(), 12, 2025, 0, 100).toString());

        }

        createEtudiantUser(etudiant.getLogin(), etudiant.getPassword());
        
        // CREATE USER et1 WITH PASSWORD 'password'; 
        // GRANT CONNECT ON DATABASE postgres TO et1;
        // GRANT USAGE ON SCHEMA public TO et1;
        // GRANT SELECT ON ALL TABLES IN SCHEMA public TO et1;
        Utils.getEntityManagerFactory().close();

    }
}
