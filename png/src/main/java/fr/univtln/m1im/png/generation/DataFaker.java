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

        var groups = GroupFaker.with(rand).createGroups();
        try (var em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();

                for (var group : groups) {
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

        //var module = Module.builder().code("M1").nom("Module1").description("Description1").nbHeuresCM(10).nbHeuresTD(20).nbHeuresTP(30).build();
    }
}
