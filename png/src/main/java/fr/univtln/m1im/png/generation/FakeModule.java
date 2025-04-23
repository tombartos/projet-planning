package fr.univtln.m1im.png.generation;

record FakeModule(String code, String nom, String description) {
    // Modules génériques (communs ou multidisciplinaires)
    static final FakeModule MODULE_GENOMIQUE = new FakeModule("BIO101", "Génomique", "Introduction à la génomique");
    static final FakeModule MODULE_BIOINFORMATIQUE = new FakeModule("BIO102", "Bioinformatique",
            "Introduction à la bioinformatique");
    static final FakeModule MODULE_BIOCELLULAIRE = new FakeModule("BIO103", "Biologie cellulaire",
            "Structure et fonction des cellules");
    static final FakeModule MODULE_GENETIQUE = new FakeModule("BIO104", "Génétique",
            "Transmission de l'information génétique");

    static final FakeModule MODULE_ANALYSE_REEL_1 = new FakeModule("MAT101", "Analyse Réel 1",
            "Introduction à l'analyse réelle");
    static final FakeModule MODULE_ARITHMETIQUE_1 = new FakeModule("MAT102", "Arithmétique 1",
            "Arithmétique et théorie des groupes");
    static final FakeModule MODULE_PROBABILITEES_1 = new FakeModule("MAT103", "Probabilités 1",
            "Introduction à la théorie des probabilités");
    static final FakeModule MODULE_INTEGRATION = new FakeModule("MAT104", "Intégration",
            "Théorie de la mesure et intégration");
    static final FakeModule MODULE_ALGEBRE_LINEAIRE = new FakeModule("MAT105", "Algèbre linéaire",
            "Espaces vectoriels et matrices");

    static final FakeModule MODULE_PROGRAMMATION = new FakeModule("INF101", "Programmation",
            "Introduction à la programmation (Java/Python)");
    static final FakeModule MODULE_ALGORITHMIQUE = new FakeModule("INF102", "Algorithmique", "Bases des algorithmes");
    static final FakeModule MODULE_BASES_DE_DONNEES = new FakeModule("INF201", "Bases de données",
            "Modélisation et SQL");
    static final FakeModule MODULE_RESEAUX = new FakeModule("INF202", "Réseaux informatiques",
            "Modèles OSI, TCP/IP, routage");

    static final FakeModule MODULE_CHIMIE_ORGANIQUE = new FakeModule("CHM101", "Chimie organique",
            "Fondements de la chimie organique");
    static final FakeModule MODULE_PHYSIQUE_QUANTIQUE = new FakeModule("PHY201", "Physique quantique",
            "Concepts fondamentaux de la mécanique quantique");

    static final FakeModule MODULE_ECONOMIE_GENERALE = new FakeModule("ECO101", "Économie générale",
            "Introduction à l'économie");
    static final FakeModule MODULE_GESTION_ENTREPRISE = new FakeModule("ECO102", "Gestion d'entreprise",
            "Principes de gestion");
    static final FakeModule MODULE_COMPTABILITE = new FakeModule("ECO103", "Comptabilité",
            "Initiation à la comptabilité générale");

    static final FakeModule MODULE_DROIT_CONSTITUTIONNEL = new FakeModule("DRT101", "Droit constitutionnel",
            "Fondements du droit public");
    static final FakeModule MODULE_DROIT_CIVIL = new FakeModule("DRT102", "Droit civil", "Obligations et contrats");

    static final FakeModule MODULE_PSYCHOLOGIE_COGNITIVE = new FakeModule("PSY101", "Psychologie cognitive",
            "Étude des processus mentaux");
    static final FakeModule MODULE_PSYCHOLOGIE_DEVELOPPEMENT = new FakeModule("PSY102", "Psychologie du développement",
            "Développement de l'enfant à l'adulte");
    static final FakeModule MODULE_NEUROSCIENCES = new FakeModule("PSY103", "Neurosciences",
            "Bases biologiques du comportement humain");

    static final FakeModule MODULE_HISTOIRE_ANCIENNE = new FakeModule("HIS101", "Histoire ancienne",
            "Civilisations antiques");
    static final FakeModule MODULE_HISTOIRE_CONTEMPORAINE = new FakeModule("HIS102", "Histoire contemporaine",
            "Du XIXe siècle à nos jours");
    static final FakeModule MODULE_HISTOIRE_MEDIEVALE = new FakeModule("HIS103", "Histoire médiévale",
            "Europe médiévale et sociétés féodales");

    static final FakeModule MODULE_LITTERATURE_FRANCAISE = new FakeModule("LET101", "Littérature française",
            "Du Moyen Âge au XXIe siècle");
    static final FakeModule MODULE_LINGUISTIQUE = new FakeModule("LET102", "Linguistique",
            "Fonctionnement du langage humain");
    static final FakeModule MODULE_ECRITURE_CREATIVE = new FakeModule("LET103", "Écriture créative",
            "Techniques de narration et rédaction");

    static final FakeModule MODULE_GEO_URBAINE = new FakeModule("GEO101", "Géographie urbaine",
            "Dynamiques des villes");
    static final FakeModule MODULE_AMENAGEMENT_TERRITOIRE = new FakeModule("GEO102", "Aménagement du territoire",
            "Politiques d'aménagement");

    static final FakeModule MODULE_SCIENCE_POLITIQUE = new FakeModule("POL101", "Science politique",
            "Systèmes politiques et institutions");
    static final FakeModule MODULE_RELATIONS_INTERNATIONALES = new FakeModule("POL102", "Relations internationales",
            "Géopolitique contemporaine");

    static final FakeModule MODULE_MARKETING = new FakeModule("MKT101", "Marketing", "Fondements du marketing");
    static final FakeModule MODULE_COMMUNICATION_DIGITALE = new FakeModule("MKT102", "Communication digitale",
            "Stratégies numériques");

    static final FakeModule MODULE_IA_FONDAMENTAUX = new FakeModule("IA101", "Intelligence Artificielle",
            "Principes de l'IA");
    static final FakeModule MODULE_MACHINE_LEARNING = new FakeModule("IA102", "Machine Learning",
            "Apprentissage automatique");

    static final FakeModule[] MODULES_LICENCE_SCIENCE_DE_LA_VIE = {
            MODULE_GENOMIQUE,
            MODULE_BIOINFORMATIQUE,
            MODULE_BIOCELLULAIRE,
            MODULE_GENETIQUE,
            MODULE_CHIMIE_ORGANIQUE,
            MODULE_PHYSIQUE_QUANTIQUE,
            MODULE_ANALYSE_REEL_1,
            MODULE_PROBABILITEES_1,
            MODULE_INTEGRATION
    };

    static final FakeModule[] MODULES_LICENCE_MATHEMATIQUES = {
            MODULE_ANALYSE_REEL_1,
            MODULE_ARITHMETIQUE_1,
            MODULE_PROBABILITEES_1,
            MODULE_INTEGRATION,
            MODULE_ALGEBRE_LINEAIRE,
            MODULE_PROGRAMMATION,
            MODULE_ALGORITHMIQUE
    };

    static final FakeModule[] MODULES_MASTER_PHYSIQUE_CHIMIE = {
            MODULE_ANALYSE_REEL_1,
            MODULE_CHIMIE_ORGANIQUE,
            MODULE_PHYSIQUE_QUANTIQUE,
            MODULE_ALGEBRE_LINEAIRE
    };

    static final FakeModule[] MODULES_LICENCE_INFORMATIQUE = {
            MODULE_PROGRAMMATION,
            MODULE_ALGORITHMIQUE,
            MODULE_BASES_DE_DONNEES,
            MODULE_ANALYSE_REEL_1,
            MODULE_PROBABILITEES_1,
            MODULE_MACHINE_LEARNING,
            MODULE_RESEAUX
    };

    static final FakeModule[] MODULES_LICENCE_HISTOIRE = {
            MODULE_HISTOIRE_ANCIENNE,
            MODULE_HISTOIRE_CONTEMPORAINE,
            MODULE_HISTOIRE_MEDIEVALE,
            MODULE_SCIENCE_POLITIQUE
    };

    static final FakeModule[] MODULES_LICENCE_LETTRES_MODERNES = {
            MODULE_LITTERATURE_FRANCAISE,
            MODULE_LINGUISTIQUE,
            MODULE_ECRITURE_CREATIVE
    };

    static final FakeModule[] MODULES_LICENCE_ECONOMIE_ET_GESTION = {
            MODULE_ECONOMIE_GENERALE,
            MODULE_GESTION_ENTREPRISE,
            MODULE_MARKETING,
            MODULE_COMPTABILITE
    };

    static final FakeModule[] MODULES_MASTER_INTELLIGENCE_ARTIFICIELLE = {
            MODULE_IA_FONDAMENTAUX,
            MODULE_MACHINE_LEARNING,
            MODULE_BASES_DE_DONNEES,
            MODULE_PROGRAMMATION
    };

    static final FakeModule[] MODULES_LICENCE_PSYCHOLOGIE = {
            MODULE_PSYCHOLOGIE_COGNITIVE,
            MODULE_PSYCHOLOGIE_DEVELOPPEMENT,
            MODULE_NEUROSCIENCES,
            MODULE_PROBABILITEES_1
    };

    static final FakeModule[] MODULES_LICENCE_DROIT = {
            MODULE_DROIT_CONSTITUTIONNEL,
            MODULE_DROIT_CIVIL
    };

    static final FakeModule[] MODULES_LICENCE_GEOGRAPHIE_ET_AMENAGEMENT = {
            MODULE_GEO_URBAINE,
            MODULE_AMENAGEMENT_TERRITOIRE
    };

    static final FakeModule[] MODULES_MASTER_MARKETING_DIGITAL = {
            MODULE_MARKETING,
            MODULE_COMMUNICATION_DIGITALE,
            MODULE_GESTION_ENTREPRISE
    };

    static final FakeModule[] MODULES_LICENCE_SCIENCES_POLITIQUES = {
            MODULE_SCIENCE_POLITIQUE,
            MODULE_RELATIONS_INTERNATIONALES,
            MODULE_DROIT_CONSTITUTIONNEL
    };

    static final FakeModule[] MODULES_MASTER_FINANCE_ET_COMPTABILITE = {
            MODULE_ECONOMIE_GENERALE,
            MODULE_GESTION_ENTREPRISE,
            MODULE_COMPTABILITE
    };

    static final FakeModule[] MODULES_LICENCE_ARTS_PLASTIQUES = {
            new FakeModule("ART101", "Histoire de l'art", "Évolution des styles artistiques"),
            new FakeModule("ART102", "Pratique artistique", "Techniques et ateliers de création")
    };

    static final FakeModule[] MODULES_MASTER_SYSTEMES_DINFORMATION = {
            MODULE_BASES_DE_DONNEES,
            MODULE_PROGRAMMATION,
            MODULE_ALGORITHMIQUE,
            MODULE_RESEAUX
    };

    static final FakeModule[] MODULES_LICENCE_SOCIOLOGIE = {
            new FakeModule("SOC101", "Introduction à la sociologie", "Concepts fondamentaux"),
            new FakeModule("SOC102", "Méthodes en sciences sociales", "Outils méthodologiques")
    };

    static final FakeModule[] MODULES_MASTER_INGENIERIE_MECANIQUE = {
            new FakeModule("MEC101", "Mécanique des fluides", "Étude des fluides incompressibles"),
            new FakeModule("MEC102", "Résistance des matériaux", "Comportement des structures")
    };

    static final FakeModule[] MODULES_MASTER_BIOTECHNOLOGIES = {
            MODULE_GENOMIQUE,
            MODULE_BIOINFORMATIQUE,
            MODULE_CHIMIE_ORGANIQUE
    };

    static final FakeModule[] MODULES_MASTER_GENIE_CIVIL = {
            new FakeModule("CIV101", "Structures en béton armé", "Conception et calculs"),
            new FakeModule("CIV102", "Géotechnique", "Mécanique des sols et fondations")
    };
}
