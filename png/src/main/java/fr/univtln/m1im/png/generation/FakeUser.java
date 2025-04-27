package fr.univtln.m1im.png.generation;

import java.time.LocalDate;
import java.text.Normalizer.Form;
import static java.text.Normalizer.normalize;

import fr.univtln.m1im.png.model.Utilisateur;
import com.github.javafaker.Faker;
import java.util.Random;

/**
 * Utility class for generating fake user data.
 * This includes generating random names, emails, logins, and other user
 * attributes.
 * It supports creating both student and professor users.
 */
class FakeUser {
    Random rand;
    String firstName;
    String lastName;
    String asciiFirstName;
    String asciiLastName;
    String login;
    String email;
    String password;

    private FakeUser() {
    }

    public static FakeUser with(Faker faker, Random rand) {
        final var fu = new FakeUser();
        fu.rand = rand;
        fu.firstName = faker.name().firstName();
        fu.lastName = faker.name().lastName();
        fu.asciiFirstName = normalize(fu.firstName, Form.NFKD).toLowerCase().replaceAll("[^a-z]", "");
        fu.asciiLastName = normalize(fu.lastName, Form.NFKD).toLowerCase().replaceAll("[^a-z]", "");
        fu.password = faker.internet().password();
        return fu;
    }

    public FakeUser withStudentEmail() {
        email = String.format("%s.%s@etud.univ-tln.fr", asciiFirstName, asciiLastName);
        return this;
    }

    public FakeUser withProfEmail() {
        email = String.format("%s.%s@univ-tln.fr", asciiFirstName, asciiLastName);
        return this;
    }

    public <B extends Utilisateur.UtilisateurBuilder<?, ?>> B configure(B builder) {
        builder
                .nom(lastName)
                .prenom(firstName)
                .login(String.format("%s%s%03d", asciiFirstName.charAt(0), asciiLastName, rand.nextInt(1000)))
                .email(email)
                .dateNaissance(LocalDate.ofYearDay(1998 + rand.nextInt(8), 1 + rand.nextInt(364)));
        return builder;
    }
}
