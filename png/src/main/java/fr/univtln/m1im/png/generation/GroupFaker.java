package fr.univtln.m1im.png.generation;

import static java.text.Normalizer.normalize;

import java.text.Normalizer.Form;
import java.util.stream.Collectors;

import fr.univtln.m1im.png.model.Groupe;

public class GroupFaker {
    final static String[] NOMS_FORMATIONS = new String[] {
        "Licence Science de la Vie",
        "Licence Mathématiques",
        "Master Physique Chimie",
        "Licence Informatique",
        "Licence Histoire",
        "Master Génie Civil",
        "Licence Lettres Modernes",
        "Licence Économie et Gestion",
        "Master Intelligence Artificielle",
        "Licence Psychologie",
        "Licence Droit",
        "Master Biotechnologies",
        "Licence Géographie et Aménagement",
        "Master Marketing Digital",
        "Licence Sciences Politiques",
        "Master Finance et Comptabilité",
        "Licence Arts Plastiques",
        "Master Systèmes d'Information",
        "Licence Sociologie",
        "Master Ingénierie Mécanique"
    };

    final java.util.Random rand;

    private GroupFaker(java.util.Random rand) {
        this.rand = rand;
    }

    public static GroupFaker with(java.util.Random rand) {
        return new GroupFaker(rand);
    }

    Groupe createFormation(String nomFormation) {
        return Groupe.builder()
            .code(normalize(nomFormation, Form.NFKD).chars()
                    .filter(Character::isUpperCase)
                    .mapToObj(Character::toString)
                    .collect(Collectors.joining("")))
            .nom(nomFormation)
            .formation(nomFormation)
            .build();
    }


    Groupe createGroupeTD(int i, Groupe formation, String nomFormation) {
        return Groupe.builder()
            .code(String.format("%s-TD%d", formation.getCode(), i))
            .nom(String.format("%s - Groupe TD %d", formation.getCode(), i))
            .formation(nomFormation)
            .parent(formation)
            .build();
    }

    Groupe createGroupeTP(int j, Groupe gtd, String nomFormation) {
        return Groupe.builder()
            .code(String.format("%s-TP%d", gtd.getCode(), j))
            .nom(String.format("%s TP %d", gtd.getCode(), j))
            .formation(nomFormation)
            .parent(gtd)
            .build();
    }

    /**
     * Créer un foret de groupes et renvoie la liste de toute les groupes.
     */
    public java.util.List<Groupe> createGroups() {
        var list = new java.util.ArrayList<Groupe>();

        for (var nomFormation : NOMS_FORMATIONS) {
            final var formation = createFormation(nomFormation);
            list.add(formation);

            if (rand.nextBoolean()) {
                for (var i = 0; i < 2; ++i) {
                    final var gtd = createGroupeTD(i, formation, nomFormation);
                    list.add(gtd);

                    if (rand.nextBoolean()) {
                        for (var j = 0; j < 2; ++j) {
                            final var gtp = createGroupeTP(j, gtd, nomFormation);
                            list.add(gtp);
                        }
                    }
                }
            }
        }

        return list;
    }
}
