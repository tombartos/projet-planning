package fr.univtln.m1im.png.generation;

import fr.univtln.m1im.png.model.Salle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Utility class for generating fake room (salle) data.
 */
class SalleFaker implements Iterable<Salle> {
    private Random rand;
    private char lettre;
    private int numero;

    private SalleFaker(Random rand) {
        this.rand = rand;
        this.lettre = 'A';
        this.numero = 1;
    }

    static SalleFaker with(Random rand) {
        return new SalleFaker(rand);
    }

    public List<Salle> asList() {
        var result = new ArrayList<Salle>();
        this.forEach(result::add);
        return result;
    }

    @Override
    public Iterator<Salle> iterator() {
        return new Iterator<Salle>() {
            int etages = rand.nextInt(1, 4);
            int sallesParEtage = rand.nextInt(5, 20);

            @Override
            public boolean hasNext() {
                return lettre <= 'Z';
            }

            @Override
            public Salle next() {
                if (lettre > 'Z')
                    throw new NoSuchElementException();
                final var isAmphi = rand.nextFloat() < 0.2;
                final var salle = Salle.builder()
                        .code(String.format("%s.%03d", lettre, numero))
                        .description(isAmphi ? "Amphithéâtre" : "Salle de TD")
                        .capacite(isAmphi ? rand.nextInt(100, 400) : rand.nextInt(20, 60))
                        .build();
                numero += 1;
                if (numero % 100 > sallesParEtage)
                    numero = numero - numero % 100 + 101;
                if (numero > 100 * etages || rand.nextFloat() < 0.01) {
                    etages = rand.nextInt(1, 4);
                    sallesParEtage = rand.nextInt(5, 20);
                    lettre += 1;
                    numero = 1;
                }
                return salle;
            }
        };
    }
}
