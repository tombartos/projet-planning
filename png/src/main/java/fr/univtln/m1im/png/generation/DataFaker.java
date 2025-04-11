package fr.univtln.m1im.png.generation;

import java.util.List;

import fr.univtln.m1im.png.Utils;
import fr.univtln.m1im.png.model.Etudiant;
import fr.univtln.m1im.png.model.Professeur;
import jakarta.persistence.EntityManagerFactory;

public class DataFaker {
    public static void main(String[] args) {
        Utils.initconnection("postgres", "mysecretpassword");
        try (final var emf = Utils.getEntityManagerFactory()) {
            fakeData(emf);
        }
    }

    public static void fakeData(EntityManagerFactory emf) {
        final var rand = new java.util.Random(123);
        final var faker = new com.github.javafaker.Faker(java.util.Locale.FRANCE, rand);

        var salles = SalleFaker.with(rand).asList();
        var groupFaker = GroupFaker.with(rand).createGroups();
        var creneauFaker = CreneauFaker
            .with(rand, faker, groupFaker.getAllGroupe().iterator().next(), salles);
        try (var em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();

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

                em.getTransaction().commit();

            } catch (Throwable e) {
                em.getTransaction().rollback();
                throw e;
            }
        }
    }
}
