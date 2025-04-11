package fr.univtln.m1im.png.generation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import com.github.javafaker.Faker;

import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Groupe;
import fr.univtln.m1im.png.model.Salle;

class CreneauFaker implements Iterable<Creneau> {
    private Random rand;
    private Groupe groupe;
    private Faker faker;
    private List<Salle> salles;

    static final ZoneId ZONE = ZoneId.of("Europe/Paris");
    static final LocalDate FIRST_DAY = LocalDate.parse("2024-09-03");
    static final ChronoLocalDate LAST_DAY = LocalDate.parse("2025-06-01");

    private static record TimeSlot(LocalTime start, LocalTime finish) {
        static TimeSlot fromString(String x) {
            var startFinish = Arrays.stream(x.split(" ")).map(LocalTime::parse)
                .toArray(LocalTime[]::new);
            return new TimeSlot(startFinish[0], startFinish[1]);
        }
    };

    private static TimeSlot[] timeSlots = Stream.of(
            "08:00:00 10:00:00",
            "08:30:00 10:30:00", "10:00:00 12:00:00", "10:30:00 12:30:00",
            "13:00:00 15:00:00",
            "13:30:00 15:30:00",
            "15:15:00 17:15:00",
            "15:30:00 17:30:00"
            ).map(TimeSlot::fromString).toArray(TimeSlot[]::new);

    private CreneauFaker(Random rand, Faker faker, Groupe groupe, List<Salle> salles) {
        this.rand = rand;
        this.faker = faker;
        this.groupe = groupe;
        this.salles = salles;
    }

    public static CreneauFaker with(Random rand, Faker faker, Groupe groupe, List<Salle> salles) {
        return new CreneauFaker(rand, faker, groupe, salles);
    }

    @Override
    public Iterator<Creneau> iterator() {
        return new Iterator<Creneau>() {
            private LocalDate date = FIRST_DAY;

            @Override
            public boolean hasNext() {
                return date.isBefore(LAST_DAY);
            }

            @Override
            public Creneau next() {
                date = date.plusWeeks(1);
                final var timeSlot = timeSlots[rand.nextInt(timeSlots.length)];

                final var modules = List.of(groupe.getModules().getFirst());
                final var groupes = List.of(groupe);
                final var profs = List.of(modules.getFirst().getProfesseurs().getFirst());

                var creneau = Creneau.builder()
                    //.modules(modules)
                    //.groupes(groupes)
                    //.professeurs(profs)
                    .salle(salles.getFirst())
                    .heureDebut(date.atTime(timeSlot.start()).atZone(ZONE).toOffsetDateTime())
                    .heureFin(date.atTime(timeSlot.finish()).atZone(ZONE).toOffsetDateTime())
                    .type("CM")
                    .status(0)
                    .build();

                // XXX the builder as is does not maintain coherence, though
                // that is where this belongs
                for (var module : modules) module.getCreneaux().add(creneau);
                for (var groupe : groupes) groupe.getCreneaux().add(creneau);
                for (var prof : profs) prof.getCreneaux().add(creneau);

                return creneau;
            }
        };
    }

}
