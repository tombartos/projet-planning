package fr.univtln.m1im.png.generation;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Stream;

import com.github.javafaker.Faker;

import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Groupe;

class CreneauFaker implements Iterable<Creneau> {
    private Random rand;
	private Groupe groupe;
	private Faker faker;

    private static record TimeSlot(LocalTime start, LocalTime finish) {
        static TimeSlot fromString(String x) {
            var startFinish = Arrays.stream(x.split(" ")).map(LocalTime::parse)
                .toArray(LocalTime[]::new);
            return new TimeSlot(startFinish[0], startFinish[1]);
        }
    };

    private static TimeSlot[] timeSlots = Stream.of(
            "8:00 10:00",
            "8:30 10:30",
            "10:00 12:00",
            "10:30 12:30",
            "13:00 15:00",
            "13:30 15:30",
            "15:15 17:15",
            "15:30 17:30"
            ).map(TimeSlot::fromString).toArray(TimeSlot[]::new);

	private CreneauFaker(Random rand, Faker faker, Groupe groupe) {
        this.rand = rand;
        this.faker = faker;
        this.groupe = groupe;
	}

	public static CreneauFaker with(Random rand, Faker faker, Groupe groupe) {
        return new CreneauFaker(rand, faker, groupe);
    }

    @Override
    public Iterator<Creneau> iterator() {
        return new Iterator<Creneau>() {
            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'hasNext'");
            }

            @Override
            public Creneau next() {
                var creneau = Creneau.builder()
                    .modules(null)
                    .groupes(null)
                    .heureDebut(null)
                    .heureFin(null)
                    .build();
                return creneau;
            }
        };
    }

}
