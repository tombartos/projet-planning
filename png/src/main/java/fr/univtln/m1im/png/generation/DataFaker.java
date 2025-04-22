package fr.univtln.m1im.png.generation;

import java.util.List;

import fr.univtln.m1im.png.Utils;
import fr.univtln.m1im.png.model.Etudiant;
import fr.univtln.m1im.png.model.Professeur;

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
            fakeData(emf);
        }
    }

    public static void fakeData(RepositoryFactory erf) {
        final var rand = new java.util.Random(123);
        final var faker = new com.github.javafaker.Faker(java.util.Locale.FRANCE, rand);

        var salles = SalleFaker.with(rand).asList();
        var groupFaker = GroupFaker.with(rand).createGroups();
        var creneauFaker = CreneauFaker
            .with(rand, faker, groupFaker.getAllGroupe().iterator().next(), salles);
        erf.transaction(em -> {

            salles.forEach(em::persist);

            for (var module : groupFaker.getModules()) {
                var prof = FakeUser.with(faker, rand)
                    .withProfEmail()
                    .configure(Professeur.builder()).build();
                module.setProfesseurs(List.of(prof));
                em.persist(prof);
                em.persist(module);
            }

            for (var group : groupFaker.getAllGroupe()) {
                em.persist(group);

                if (group.getSousGroupes().isEmpty()) {
                    for (int i = 0; i < 50; ++i) {
                        var etudiant = FakeUser.with(faker, rand)
                            .withStudentEmail()
                            .configure(Etudiant.builder()).build();
                        em.persist(etudiant);
                    }
                }
            }

            for (var creneau : creneauFaker) {
                em.persist(creneau);
            }

        });
    }
}
