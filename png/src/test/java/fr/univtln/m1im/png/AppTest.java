package fr.univtln.m1im.png;

import org.junit.jupiter.api.Test;


import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Etudiant;
import fr.univtln.m1im.png.model.Groupe;
import fr.univtln.m1im.png.model.Professeur;
import fr.univtln.m1im.png.model.Responsable;
import fr.univtln.m1im.png.model.Salle;
import fr.univtln.m1im.png.model.Utilisateur;
// import fr.univtln.m1im.png.repositories.ProfesseurRepository;
// import fr.univtln.m1im.png.repositories.SalleRepository;
import jakarta.persistence.EntityManager;
import fr.univtln.m1im.png.repositories.CreneauRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.univtln.m1im.png.model.Module;
import fr.univtln.m1im.png.model.NotePersonnelle;

import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.time.ZoneOffset;


/**
 * Unit test for simple App.
 */
class AppTest {
    private static final Logger log = LoggerFactory.getLogger(AppTest.class);

    void createUser(String username, String password) {
        try (EntityManager entityManager = Utils.getEntityManagerFactory().createEntityManager()) {
            entityManager.getTransaction().begin();

            // We can't use the classic JPA way to create a user because it's not supported by the JPA standard
            String createUserQuery = String.format("CREATE USER %s WITH PASSWORD '%s';", username, password);
            entityManager.createNativeQuery(createUserQuery).executeUpdate();
            entityManager.getTransaction().commit();

            // Grant privileges
            entityManager.getTransaction().begin();
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

    void tryCreateUser(Utilisateur user, String password){
        try (EntityManager entityManager = Utils.getEntityManagerFactory().createEntityManager()) {
            String checkUserQuery = String.format("SELECT COUNT(*) FROM pg_roles WHERE rolname='%s';", user.getLogin());
            //String checkUserQuery = "SELECT COUNT(*) FROM pg_roles";
            //log.info(checkUserQuery);
            int userExists = ((Number) entityManager.createNativeQuery(checkUserQuery).getSingleResult()).intValue();
            if (userExists == 0) {
                createUser(user.getLogin(), password);
            } 
            else {
                log.info("User already exists: {}", user.getLogin());
            }
        } catch (Exception e) {
            log.error("Failed to check or create user", e);
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
        Groupe groupe = Groupe.builder().code("TD1").nom("Groupe TD1").formation("Formation1").build();
        Groupe groupe2 = Groupe.builder().code("TD1_TP1").nom("Groupe TD1_TP1").formation("Formation1").parent(groupe).build();
        Groupe groupe3 = Groupe.builder().code("TD1_TP2").nom("Groupe TD1_TP2").formation("Formation1").parent(groupe).build();
        LocalDate now = LocalDate.now();
        Etudiant etudiant = Etudiant.builder().nom("Nom1").prenom("Prenom1").login("et1").email("et1@email.com").dateNaissance(now).build();
        Professeur professeur = Professeur.builder().nom("Nom2").prenom("Prenom2").login("pr1").email("pr1@email.com").dateNaissance(now).build();
        Module module = Module.builder().code("M1").nom("Module1").description("Description1").nbHeuresCM(10).nbHeuresTD(20).nbHeuresTP(30).build();
        OffsetDateTime heureDebut = OffsetDateTime.of(2025, 3, 27, 9, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime heureFin = heureDebut.plusHours(2); // Add 2 hours
        Salle salle = Salle.builder().code("T001").description("Videoproj + PC").capacite(30).build();
        Salle salle2 = Salle.builder().code("T002").description("Videoproj").capacite(20).build();
        Creneau creneau = Creneau.builder().type("CM").heureDebut(heureDebut).heureFin(heureFin).salle(salle).build();
        Creneau creneau2 = Creneau.builder().type("TP").heureDebut(heureDebut.minusDays(14)).heureFin(heureFin.minusDays(14).plusHours(1)).salle(salle).build();
        Creneau creneau3 = Creneau.builder().type("TD").heureDebut(heureDebut.plusDays(6)).heureFin(heureFin.plusDays(6)).salle(salle).build();
        Creneau creneau4 = Creneau.builder().type("EXAM").heureDebut(heureDebut.plusHours(3)).heureFin(heureFin.plusHours(3)).salle(salle2).build();
        Creneau creneau5 = Creneau.builder().type("TP").heureDebut(heureDebut.plusDays(7)).heureFin(heureFin.plusDays(7).minusHours(1)).salle(salle2).build();
        Creneau creneau6 = Creneau.builder().type("TP").heureDebut(heureDebut.plusDays(7)).heureFin(heureFin.plusDays(7)).salle(salle).build();

        Responsable responsable = Responsable.builder().nom("Nom3").prenom("Prenom3").login("resp1").email("resp1@email.com").dateNaissance(now).build();
        
        groupe.getSousGroupes().add(groupe2); // Add groupe2 and groupe3 to groupe
        groupe.getSousGroupes().add(groupe3); // Add groupe2 and groupe3 to groupe
        groupe2.addEtudiant(etudiant);  // Add etudiant to groupe2, add groupe2 to etudiant, and same for groupe

        module.getProfesseurs().add(professeur);
        professeur.getModules().add(module);
        module.getGroupes().add(groupe);
        module.getGroupes().add(groupe2);
        groupe.getModules().add(module);
        
        creneau.getModules().add(module);
        module.getCreneaux().add(creneau);
        creneau.getGroupes().add(groupe);
        groupe.getCreneaux().add(creneau);
        creneau.getProfesseurs().add(professeur);
        professeur.getCreneaux().add(creneau);

        creneau2.getModules().add(module);
        module.getCreneaux().add(creneau2);
        creneau2.getGroupes().add(groupe2);
        groupe2.getCreneaux().add(creneau2);
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

        creneau5.getModules().add(module);
        module.getCreneaux().add(creneau5);
        creneau5.getGroupes().add(groupe2);
        groupe2.getCreneaux().add(creneau5);
        creneau5.getProfesseurs().add(professeur);
        professeur.getCreneaux().add(creneau5);

        creneau6.getModules().add(module);
        module.getCreneaux().add(creneau6);
        creneau6.getGroupes().add(groupe3);
        groupe3.getCreneaux().add(creneau6);
        creneau6.getProfesseurs().add(professeur);
        professeur.getCreneaux().add(creneau6);

        NotePersonnelle notePersonnelle = NotePersonnelle.builder().notePerso("Note1").utilisateur(etudiant).creneau(creneau).build();
        creneau.getNotesPerso().add(notePersonnelle);
        etudiant.getNotesPerso().add(notePersonnelle);



        // Persist entities, don't use save method, it's not recommended for the first time
        try (EntityManager entityManager = Utils.getEntityManagerFactory().createEntityManager()) {
            log.info("Persisting entities");
            entityManager.getTransaction().begin();
            entityManager.persist(salle);
            entityManager.persist(salle2);
            entityManager.persist(professeur);
            entityManager.persist(groupe);
            entityManager.persist(groupe2);
            entityManager.persist(groupe3);
            entityManager.persist(etudiant);
            entityManager.persist(module);
            entityManager.persist(creneau);
            entityManager.persist(creneau2);
            entityManager.persist(creneau3);
            entityManager.persist(creneau4);
            entityManager.persist(creneau5);
            entityManager.persist(creneau6);
            entityManager.persist(responsable);
            entityManager.persist(notePersonnelle);
            entityManager.getTransaction().commit();
            log.info("Entities persisted successfully");
        } catch (Exception e) {
            log.error("Failed to persist entities", e);
        }

        //TESTS
        try (EntityManager entityManager = Utils.getEntityManagerFactory().createEntityManager()){
            // ProfesseurRepository professeurRepository = new ProfesseurRepository(entityManager);
            // log.info("TEST1");
            // log.info(professeurRepository.getWeekCrenaux(professeur.getId(), 9, 2025, 0, 100).toString());
            // log.info(professeurRepository.getWeekCrenaux(professeur.getId(), 10, 2025, 0, 100).toString());
            // log.info(professeurRepository.getWeekCrenaux(professeur.getId(), 11, 2025, 0, 100).toString());
            // log.info(professeurRepository.getWeekCrenaux(professeur.getId(), 12, 2025, 0, 100).toString());
            // //creneau = week 12 2025
            // //creneau2 = week 10 2025
            // log.info("TEST2");
            // SalleRepository salleRepository = new SalleRepository(entityManager);
            // log.info(salleRepository.getWeekCrenaux(salle.getCode(), 10, 2025, 0, 100).toString());
            // log.info(salleRepository.getWeekCrenaux(salle.getCode(), 11, 2025, 0, 100).toString());
            // log.info(salleRepository.getWeekCrenaux(salle.getCode(), 12, 2025, 0, 100).toString());
            log.info("TEST1");
            // ModuleRepository moduleRepository = new ModuleRepository(entityManager);
            // log.info(moduleRepository.getAllCreneaux(module.getCode(), 0, 100).toString());
            CreneauRepository creneauRepository = new CreneauRepository(entityManager);
            log.info(creneauRepository.addCreneau(creneau, null)); //Excepted : error
            Creneau creneau7 = Creneau.builder().type("CM").heureDebut(OffsetDateTime.of(2025, 3, 3, 8, 0, 0, 0, ZoneOffset.UTC)).heureFin(OffsetDateTime.of(2025, 3, 3, 11, 0, 0, 0, ZoneOffset.UTC)).salle(salle).build();
            creneau7.getModules().add(module);
            creneau7.getGroupes().add(groupe);
            creneau7.getProfesseurs().add(professeur);
            log.info(creneauRepository.addCreneau(creneau7, null)); //Excepted : success

            Creneau creneau8 = Creneau.builder().type("CM").heureDebut(OffsetDateTime.of(2025, 3, 3, 9, 0, 0, 0, ZoneOffset.UTC)).heureFin(OffsetDateTime.of(2025, 3, 3, 10, 0, 0, 0, ZoneOffset.UTC)).salle(salle).build();
            creneau8.getModules().add(module);
            creneau8.getGroupes().add(groupe);
            creneau8.getProfesseurs().add(professeur);
            log.info(creneauRepository.addCreneau(creneau8, null)); //Excepted : error

        }

        // //Create postgres users
        // tryCreateUser(etudiant, "password");
        // tryCreateUser(professeur, "password");
        // tryCreateUser(responsable, "password");

        // Voir script create-users pour crée utilisateurs
        Utils.getEntityManagerFactory().close();


    }
}
