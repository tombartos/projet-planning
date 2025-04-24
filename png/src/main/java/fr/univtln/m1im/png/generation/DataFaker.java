package fr.univtln.m1im.png.generation;

import java.time.LocalDate;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import fr.univtln.m1im.png.Utils;
import fr.univtln.m1im.png.model.Etudiant;
import fr.univtln.m1im.png.model.Professeur;
import fr.univtln.m1im.png.model.Responsable;

public class DataFaker {
    public static void main(String[] args) {
        RepositoryFactory emf;
        if (args.length > 1) {
            System.err.println("Expected at most one argument");
            System.exit(1);
        }
        if (args.length == 0 || args[0].equals("database")) {
            Utils.initconnection("postgres", "mysecretpassword");
            emf = new RepositoryFactory.Database();
        } else if (args[0].equals("stdout")) {
            emf = new RepositoryFactory.Stdout();
        } else {
            emf = null;
            System.err.println("Expected \"database\" or \"stdout\" as an argument.");
            System.exit(1);
        }
        try (emf) {
            final var rand = new java.util.Random(123);
            final var faker = new com.github.javafaker.Faker(java.util.Locale.FRANCE, rand);
            final var dataFaker = new DataFaker(rand, faker, emf);
            dataFaker.fakeData();
            dataFaker.initResp();
        }
    }

    private java.util.Random rand;
    private com.github.javafaker.Faker faker;
    private RepositoryFactory emf;

    public DataFaker(java.util.Random rand, com.github.javafaker.Faker faker, RepositoryFactory emf) {
        this.rand = rand;
        this.faker = faker;
        this.emf = emf;
    }

    public void fakeData() {
        final var salles = SalleFaker.with(rand).asList();
        final var groupFaker = GroupFaker.with(rand).createGroups();

        emf.transaction(em -> {

            salles.forEach(em::persist);

            for (var module : groupFaker.getModules()) {
                final var prof = FakeUser.with(faker, rand)
                    .withProfEmail()
                    .configure(Professeur.builder()).build();
                module.setProfesseurs(List.of(prof));
                em.persist(prof);
                em.persist(module);
            }

            for (var group : groupFaker.getAllGroupe()) {
                em.persist(group);

                if (group.getSousGroupes().isEmpty()) {
                    var groupeAncetres = new java.util.ArrayList<>(group.getAncetres());

                    for (int i = 0; i < 50; ++i) {
                        final var etudiant = FakeUser.with(faker, rand)
                            .withStudentEmail()
                            .configure(Etudiant.builder())
                            .groupes(groupeAncetres)
                            .build();
                        for (var g : groupeAncetres) {
                            g.getEtudiants().add(etudiant);
                        }
                        em.persist(etudiant);
                    }
                }
            }

            for (var groupe : groupFaker.getRacines()) {
                final var creneauFaker = CreneauFaker.with(rand, groupe, salles);

                for (var creneau : creneauFaker) {
                    em.persist(creneau);
                }
            }

        });
    }

    public void initResp(){
        emf.transaction(em -> {
            LocalDate birth = LocalDate.parse("1985-01-01");
            Responsable responsable = Responsable.builder()
                .nom("Dupont")
                .prenom("Jean")
                .login("dupont888")
                .email("dupont888@email.com")
                .dateNaissance(birth)
                .UFR("Sciences")
                .build();
            em.persist(responsable);
        });
    }
}
