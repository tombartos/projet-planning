package fr.univtln.m1im.png.generation;

import static java.text.Normalizer.normalize;

import java.text.Normalizer.Form;
import java.util.stream.Collectors;

import fr.univtln.m1im.png.model.Groupe;
import static fr.univtln.m1im.png.generation.FakeModule.*;

public class GroupFaker {
    public static record FakeFormation(String nom, FakeModule[] modules) { }

    final static FakeFormation[] FORMATIONS = new FakeFormation[] {
        new FakeFormation( "Licence Science de la Vie",          MODULES_LICENCE_SCIENCE_DE_LA_VIE         ),
        new FakeFormation( "Licence Mathématiques",              MODULES_LICENCE_MATHEMATIQUES             ),
        new FakeFormation( "Master Physique Chimie",             MODULES_MASTER_PHYSIQUE_CHIMIE            ),
        new FakeFormation( "Licence Informatique",               MODULES_LICENCE_INFORMATIQUE              ),
        new FakeFormation( "Licence Histoire",                   MODULES_LICENCE_HISTOIRE                  ),
        new FakeFormation( "Master Génie Civil",                 MODULES_MASTER_GENIE_CIVIL                ),
        new FakeFormation( "Licence Lettres Modernes",           MODULES_LICENCE_LETTRES_MODERNES          ),
        new FakeFormation( "Licence Économie et Gestion",        MODULES_LICENCE_ECONOMIE_ET_GESTION       ),
        new FakeFormation( "Master Intelligence Artificielle",   MODULES_MASTER_INTELLIGENCE_ARTIFICIELLE  ),
        new FakeFormation( "Licence Psychologie",                MODULES_LICENCE_PSYCHOLOGIE               ),
        new FakeFormation( "Licence Droit",                      MODULES_LICENCE_DROIT                     ),
        new FakeFormation( "Master Biotechnologies",             MODULES_MASTER_BIOTECHNOLOGIES            ),
        new FakeFormation( "Licence Géographie et Aménagement",  MODULES_LICENCE_GEOGRAPHIE_ET_AMENAGEMENT ),
        new FakeFormation( "Master Marketing Digital",           MODULES_MASTER_MARKETING_DIGITAL          ),
        new FakeFormation( "Licence Sciences Politiques",        MODULES_LICENCE_SCIENCES_POLITIQUES       ),
        new FakeFormation( "Master Finance et Comptabilité",     MODULES_MASTER_FINANCE_ET_COMPTABILITE    ),
        new FakeFormation( "Licence Arts Plastiques",            MODULES_LICENCE_ARTS_PLASTIQUES           ),
        new FakeFormation( "Master Systèmes d'Information",      MODULES_MASTER_SYSTEMES_DINFORMATION      ),
        new FakeFormation( "Licence Sociologie",                 MODULES_LICENCE_SOCIOLOGIE                ),
        new FakeFormation( "Master Ingénierie Mécanique",        MODULES_MASTER_INGENIERIE_MECANIQUE       )
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

        for (var fakeFormation : FORMATIONS) {
            final var nomFormation = fakeFormation.nom();
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
