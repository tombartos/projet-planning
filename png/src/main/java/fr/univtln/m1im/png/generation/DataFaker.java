package fr.univtln.m1im.png.generation;

import fr.univtln.m1im.png.Utils;
import fr.univtln.m1im.png.model.Etudiant;
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
        try (var em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();

                salles.forEach(em::persist);
                groupFaker.getModules().forEach(em::persist);

                for (var group : groupFaker.getAllGroupe()) {
                    em.persist(group);

                    if (group.getSousGroupes().isEmpty()) {
                        for (int i = 0; i < 1000; ++i) {
                            var etudiant = FakeUser.with(faker, rand).withStudentEmail()
                                .configure(Etudiant.builder()).build();
                            em.persist(etudiant);
                        }
                    }
                }

                em.getTransaction().commit();
            } catch (Throwable e) {
                em.getTransaction().rollback();
                throw e;
            }
        }
    }
}
