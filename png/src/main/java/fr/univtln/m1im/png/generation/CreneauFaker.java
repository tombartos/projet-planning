package fr.univtln.m1im.png.generation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import fr.univtln.m1im.png.model.Creneau;
import fr.univtln.m1im.png.model.Groupe;
import fr.univtln.m1im.png.model.Salle;

class CreneauFaker implements Iterable<Creneau> {
    private Random rand;
    private Groupe rootGroupe;
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

    private static final TimeSlot[] TIME_SLOTS = Stream.of(
            "08:00:00 10:00:00",
            "08:30:00 10:30:00",
            "10:00:00 12:00:00",
            "10:30:00 12:30:00",
            "13:00:00 15:00:00",
            "13:30:00 15:30:00",
            "15:15:00 17:15:00",
            "15:30:00 17:30:00"
            ).map(TimeSlot::fromString).toArray(TimeSlot[]::new);

    private CreneauFaker(Random rand, Groupe groupe, List<Salle> salles) {
        this.rand = rand;
        this.rootGroupe = groupe;
        this.salles = salles;
    }

    public static CreneauFaker with(Random rand, Groupe groupe, List<Salle> salles) {
        return new CreneauFaker(rand, groupe, salles);
    }

    @Override
    public Iterator<Creneau> iterator() {
        final HashMap<Groupe, Position> positions = new HashMap<>();
        rootGroupe.forEachDescendant(g -> positions.put(g, new Position()));
        return new Iterator<Creneau>() {
            private Position pos = new Position();

            @Override
            public boolean hasNext() {
                // We stop whenever a group reaches the last day. i.e., there is one unlucky
                // group who is last to start summer break :,(
                return pos.date.isBefore(LAST_DAY);
            }

            @Override
            public Creneau next() {
                final var groupe = randomSousGroupe(rootGroupe);
                this.pos = positions.get(groupe);
                groupe.forEachAncetre(anc -> pos.advanceUpTo(positions.get(anc)));
                pos.advance(rand);
                groupe.forEachAncetre(anc -> positions.get(anc).advanceUpTo(pos));
                return randomCreneau(groupe, pos);
            }

            private Groupe randomSousGroupe(Groupe groupe) {
                if (!groupe.isLeaf() && rand.nextFloat() < 0.5) {
                    return randomSousGroupe(pickRandom(groupe.getSousGroupes()));
                } else {
                    return groupe;
                }
            }

        };
    }

    private <T> T pickRandom(List<T> list) {
        return list.get(rand.nextInt(list.size()));
    }

    private Creneau randomCreneau(Groupe groupe, Position pos) {
        final var timeSlot = TIME_SLOTS[pos.slot];

        final var modules = List.of(pickRandom(groupe.getModules()));
        final var groupes = List.of(groupe);
        final var profs = List.of(modules.getFirst().getProfesseurs().getFirst());

        var creneau = Creneau.builder()
            .modules(modules)
            .groupes(groupes)
            .professeurs(profs)
            .salle(pickRandom(salles))
            .heureDebut(pos.date.atTime(timeSlot.start()).atZone(ZONE).toOffsetDateTime())
            .heureFin(pos.date.atTime(timeSlot.finish()).atZone(ZONE).toOffsetDateTime())
            .type("CM")
            .status(0)
            .build();

        // XXX the builder as is does not maintain coherence, though
        // that is where this belongs. either that, or we should change
        // the direction of ownership.
        for (var module : modules) module.getCreneaux().add(creneau);
        for (var group : groupes) group.getCreneaux().add(creneau);
        for (var prof : profs) prof.getCreneaux().add(creneau);

        return creneau;
    }

    private static class Position {
        public LocalDate date = FIRST_DAY;
        public int slot = 0;

        public void advanceUpTo(Position other) {
            if (other.date.isAfter(this.date)) {
                this.date = other.date;
                if (other.slot > this.slot) {
                    this.slot = other.slot;
                }
            }
        }

        public void advance(Random rand) {
            var firstSlot = nextAvailSlotToday();
            if (firstSlot == TIME_SLOTS.length) {
                final var tomorrow = date.plusDays(1);
                firstSlot = 0;
                switch (tomorrow.getDayOfWeek()) {
                    case MONDAY:
                    case TUESDAY:
                    case WEDNESDAY:
                    case THURSDAY:
                    case FRIDAY:
                        this.date = tomorrow;
                        break;
                    case SATURDAY:
                    case SUNDAY:
                        this.date = tomorrow.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                        break;
                }
            }
            this.slot = rand.nextInt(firstSlot, TIME_SLOTS.length);
        }

        private int nextAvailSlotToday() {
            int n = this.slot;
            final var slot = TIME_SLOTS[n];
            while (n < TIME_SLOTS.length && TIME_SLOTS[n].start.isBefore(slot.finish))
                ++n;
            return n;
        }

    }
}
