package fr.univtln.m1im.png.generation;

import static java.text.Normalizer.normalize;

import java.text.Normalizer.Form;
import java.time.LocalDate;
import java.util.stream.Collectors;

import fr.univtln.m1im.png.Utils;
import fr.univtln.m1im.png.model.Etudiant;
import fr.univtln.m1im.png.model.Groupe;
import fr.univtln.m1im.png.model.Module;
import fr.univtln.m1im.png.model.Utilisateur;
import jakarta.persistence.Persistence;

public class DataFaker {
    public static void main(String[] args) {
        final var emf = Utils.getEntityManagerFactory();
        final var rand = new java.util.Random(123);
        final var faker = new com.github.javafaker.Faker(java.util.Locale.FRANCE, rand);

        //Etudiant etudiant = FakeUserBuilder.of(Etudiant.builder(), faker, rand).build();
        var etudiant = FakeUser.with(faker, rand).withStudentEmail().configure(Etudiant.builder()).build();

        var module = Module.builder().code("M1").nom("Module1").description("Description1").nbHeuresCM(10).nbHeuresTD(20).nbHeuresTP(30).build();
    }
}
