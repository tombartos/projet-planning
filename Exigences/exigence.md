---
abstract: |
  Voici les exigences du projet planning réalisé par les M1IM de
  l'université de Toulon dans le cadre du cours de modélisation et
  gestion de projet.
author:
- BARTIER Tom
- ROBLIN Yann
- BA GUBAIR Emad
- MARCAIS André
date: 2025-03-01
title: Exigences Planning
---

# Exigences Planning

## Vocabulaire Métier

-   **Utilisateur** : Personne utilisant l'application
-   **Étudiant** : Personne suivant des cours
-   **Professeur** aussi équivalent à "enseignant": Personne enseignant des cours
-   **Administrateur** : Personne gérant les comptes
-   **Visiteur** : Personne non connectée
-   **Secrétaire** : Personne gérant les absences
-   **Responsable EDT** : Personne gérant l'emploi du temps
-   **Module** : Ensemble de cours sur un même thème
-   **Groupe** : Ensemble d'étudiants suivant les mêmes cours
-   **Salle** : Lieu où se déroulent les cours
-   **Cours** aussi équivalent à "créneau" ou "séance" : un cours est un moment réservé dans l'emploi du temps pour un module donné
-   **EDT** : Emploi du temps
-   **iCal** : Format de fichier pour les calendriers
-   **PDF** : Format de fichier pour les documents
-   **Note** : Informations supplémentaires sur un cours
-   **Note personnelle** : Note ajoutée par un utilisateur sur un cours visible uniquement par lui-même
-   **Note générale** : Note ajoutée par un professeur sur un cours visible par tous

## Login

-   EX_LOGIN_001

    -   Créer un compte

    -   L'administrateur doit pouvoir créer des comptes élève,
        professeur, et responsable d'emploi du temps

-   EX_LOGIN_002

    -   Se connecter

    -   L'utilisateur doit pouvoir se connecter qu'il soit élève,
        professeur ou responsable d'emploi du temps sur une unique
        interface.

-   EX_LOGIN_003

    -   Consulter anonymement

    -   Les utilisateurs doivent pouvoir accéder anonymement à
        l'application avec des restrictions.

## Fonctionnalités générales

-   EX_BASE_FEATURES_001

    -   Consulter emploi du temps personnel

    -   Les utilisateurs connectés doivent pouvoir consulter leur emploi
        temps respectif

-   EX_BASE_FEATURES_002

    -   Consulter emploi du temps salles

    -   Les utilisateurs connectés ainsi que les anonymes doivent
        pouvoir consulter l'emploi du temps des salles

-   EX_BASE_FEATURES_003

    -   Consulter emploi du temps d'un groupe

    -   Les utilisateurs connectés doivent pouvoir consulter l'emploi du
        temps d'un groupe

-   EX_BASE_FEATURES_004

    -   Consulter emploi du temps d'un professeur

    -   Les utilisateurs connectés doivent pouvoir consulter l'emploi du
        temps d'un professeur

-   EX_BASE_FEATURES_005

    -   Consulter emploi du temps d'un module

    -   Les utilisateurs connectés doivent pouvoir consulter l'emploi du
        temps d'un module

-   EX_BASE_FEATURES_006

    -   Export iCal

    -   Les utilisateurs connectés doivent pouvoir exporter leur emploi
        du temps au format iCal pour le sy

-   EX_BASE_FEATURES_007

    -   Consulter nombre d'heures par module

    -   Les utilisateur euvent consulter le nombre d'heures d'un module

-   EX_BASE_FEATURES_008

    -   Consulter le nombre d'heures total

    -   Les utilisateurs peuvent consulter le nombre d'heures total d'un
        groupe ou d'un étudiant

-   EX_BASE_FEATURES_009

    -   Télécharger PDF

    -   Les utilisateurs peuvent télécharger le planning au format PDF

-   EX_BASE_FEATURES_010

    -   Rajouter une note sur un cours
    -   Les professeurs peuvent rajouter une petite note sur un cours

-   EX_BASE_FEATURES_011
    -   Consulter les notes de cours
    -   Les étudiants et les professeurs peuvent consulter les petites notes de cours

-   EX_BASE_FEATURES_012
    -   Création de notes personnelles
    -   Visible que par l'auteur
    -   e.g., un étudiant peut noter ce qu'on fais ce jour la si le prof
        la pas noté

## Report/annulation de cours

-   EX_REPORT_001

    -   Reporter un cours

    -   Les professeurs doivent pouvoir demander au responsable de l'emploi du temps de reporter un cours

-   EX_REPORT_002

    -   Annuler un cours

    -   Les professeurs doivent pouvoir demander au responsable de l'emploi du temps d'annuler un cours

-   EX_REPORT_003

    -   Contacter responsable

    -   Un professeur peut contacter automatiquement le responsable de
        l'emploi du temps correspondant pour réserver une salle à une
        heure pour un cours

-   EX_REPORT_004

    -   Consulter cours reporté/annulé

    -   Les utilisateurs connectés peuvent voir la liste des cours
        reportés ou annulés

## Absences

-   EX_ABSENCES_001

    -   Faire l'appel
    -   Les professeurs peuvent faire l'appel pendant un cours

-   EX_ABSENCES_002

    -   Consulter absences sur un cours
    -   Les professeurs peuvent consulter les élèves qu'ils ont noté comme absents à un cours

-   EX_ABSENCES_003

    -   Etudiant consulter absences
    -   Les étudiants peuvent consulter leurs absences passées

-   EX_ABSENCES_004

    -   Prévenir d'un retard/absence
    -   Les étudiants peuvent prévenir les professeurs si ils ont un retard / absence de prévu

## Fonctionalités Responsable EDT

-   EX_RESP_001

    -   Ajouter un cours
    -   Le responsable EDT doit pouvoir insérer un nouveau cours avec les informations qui le concerne dans la base de données de l'application (voir notes interview), il peut en définir les droits de modification, il peut aussi copier coller un cours existant

-   EX_RESP_002

    -   Modifier un cours
    -   Le responsable EDT doit pouvoir modifier un cours (heure, prof, module, salle) si il a les droits de modification sur ce cours
    
-   EX_RESP_003

    -   Supprimer un cours
    -   Le responsable EDT doit pouvoir supprimer un cours si il a les droits de modification sur ce dernier

-   EX_RESP_004

    -   Accepter demande de modification de cours
    -   Le responsable EDT doit pouvoir accepter une demande de report de cours effectuée par un professeur

-   EX_RESP_005

    -   Reporter un cours

    -   Le responsable EDT doit pouvoir reporter un cours

-   EX_RESP_006

    -   Annuler un cours

    -    Le responsable EDT doit pouvoir annuler un cours



<!-- vim: set ts=4 sw=4 tw=72 et spell spelllang=fr :-->
