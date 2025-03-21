---
title: Scénarios des UC
---

# Package Login
## UC01 Se connecter
### Scénario Nominal
1. Le visiteur rentre son login et son mot de passe
2. Le visiteur clique sur le bouton "Connexion"
3. *[Identifiants corrects]
4. Le système affiche la page d'accueil de l'utilisateur

### Scénario d'alternatif
3. a *[Identifiants incorrects]
4. Le système affiche "Identifiants incorrect", l'UC reprend en 1.


## UC02 Consulter anonymement
### Scénario nominal
1. Le visiteur clique sur le bouton "Connexion anonyme"
2. Le système affiche l'emeploi du temps des salles, voir UC "Emploi du temps des salles"

# Package Fonctionalités générales
## UC03 Consulter emploi du temps des salles
### Scénario nominal
1. L'utilisateur clique sur le bouton "Salles"
2. Le système affiche la liste des salles
3. L'utilisateur clique sur le nom de la salle qui l'intéresse
4. Le système affiche l'emploi du temps de la salle sur une semaine

### Scénarios alternatifs
#### Changer salle affichée
3. a L'utilisateur clique sur le nom de la salle
4. a Le système affiche toutes les salles
5. a L'utilisateur clique sur le nom de la salle qui l'intéresse
6. a Le système affiche l'EDT de la salle sur la semaine

#### Choix semaine
3. b L'utilisateur clique sur le numéro de la semaine qui l'intéresse
4. b Le système affiche l'EDT de la salle sur la semaine choisie

## UC04 Consulter emploi du temps personnel
Prérequit : L'utilisateur est connecté

### Scénario nominal
1. L'utilisateur clique sur le bouton "Mon EDT"
2. Le système affiche l'emploi du temps de l'utilisateur sur une semaine

### Scénarios alternatifs
#### Choix semaine
3. a L'utilisateur clique sur le numéro de la semaine qui l'intéresse
4. a Le système affiche l'EDT de l'utilisateur sur la semaine choisie

## UC05 Consulter emploi du temps d'un groupe
Prérequis : L'utilisateur est connecté

### Scénario nominal
1. L'utilisateur clique sur le bouton "Groupes"
2. Le système affiche la liste des groupes
3. L'utilisateur clique sur le nom du groupe qui l'intéresse
4. Le système affiche l'emploi du temps du groupe sur une semaine

### Scénarios alternatifs
#### Changer groupe affiché
3. a L'utilisateur clique sur le nom du groupe
4. a Le système affiche tous les groupes
5. a L'utilisateur clique sur le nom du groupe qui l'intéresse
6. a Le système affiche l'EDT du groupe sur la semaine

#### Choix semaine
3. a L'utilisateur clique sur le numéro de la semaine qui l'intéresse
4. a Le système affiche l'EDT du groupe sur la semaine choisie

## UC06 Consulter emploi du temps d'un module
Prérequis : L'utilisateur est connecté 
TODO: Décider qui peut consulter les modules

### Scénario nominal
1. L'utilisateur clique sur le bouton "Modules"
2. Le système affiche la liste des modules
3. L'utilisateur clique sur le nom du module qui l'intéresse
4. Le système affiche la liste des cours du module

### Scénarios alternatifs
#### Changer module affiché
3. a L'utilisateur clique sur le nom du module
4. a Le système affiche tous les modules
5. a L'utilisateur clique sur le nom du module qui l'intéresse
6. a Le système affiche la liste des cours du module

## UC07 Ajouter une note personelle
Prérequis : L'utilisateur est connecté

### Scénario nominal
1. L'utilisateur clique sur le cours qui l'intéresse
2. Le système affiche une fenêtre pour ajouter une note personnelle
3. L'utilisateur rentre sa note
4. L'utilisateur clique sur le bouton "Valider"
5. Le système affiche la note sur le cours

### Scénario d'exception
4. a L'utilisateur clique hors de la fenêtre
5. a Le système ne prend pas en compte la note

## UC08 Modifier une note personelle
Prérequis : L'utilisateur est connecté et une note est déjà présente

### Scénario nominal
1. L'utilisateur clique sur le cours qui l'intéresse
2. Le système affiche la fenêtre de la note personnelle
3. L'utilisateur modifie sa note
4. L'utilisateur clique sur le bouton "Valider"
5. Le système affiche la note modifiée sur le cours

### Scénario d'exception
3. a L'utilisateur clique hors de la fenêtre
4. a Le système ne modifie pas la note

## UC09 Consulter notes
Prérequis : L'utilisateur est connecté

### Scénario nominal
1. L'utilisateur clique sur le cours qui l'intéresse
2. Le système affiche la note personnelle de l'utilisateur et la note générale

## UC10 Consulter nombre d'heures par groupe
Prérequis : L'utilisateur est connecté

TODO : décider comment on gère ça





# Package Modification de l'emploi du temps
## UC11 Ajouter un cours
Prérequis : Le responsable est connecté

### Scénario nominal
1. Le responsable clique sur le bouton "Ajouter un cours"
2. Le système affiche une fenêtre pour rentrer les informations du cours
3. Le responsablee clique sur le bouton "Module"
4. Le système affiche la liste des modules
5. Le responsable clique sur le module du cours
6. le responsable clique sur le bouton "Professeur"
7. Le système affiche la liste des professeurs
8. Le responsable clique sur le professeur du cours
9. Le responsable clique sur le bouton "Horaire"
10. Le système affiche la liste des jours et  heures
11. Le responsable clique sur le jour et l'heure de début et de fin du cours
12. Le responsable clique sur le bouton "Salle"
13. Le système affiche la liste des salles disponibles
14. Le responsable clique sur la salle du cours
15. Le responsable clique sur le bouton "Valider"
16. Le système affiche le cours ajouté

### Scénario alternatif
15. Le responsable clique sur le bouton "Propager"
16. Le système affiche deux menus déroulants de semaine
17. Le responsable sélectionne la semaine de début et la semaine de fin
18. Le responsable clique sur le bouton "Valider"
19. Le système affiches les cours ajoutés

### Scénario d'exception
15. Le responsable clique sur le bouton "Annuler"
16. Le système ferme la fenêtre

## UC12 Modification d'un créneau d'un cours
Prérequis : Le responsable est connecté et un cours est déjà présent

### Scénario nominal
1. Le responsable clique sur le cours qui l'intéresse
2. Le système affiche une fenêtre pour modifier les informations du cours
3. Le responsable modifie les informations du cours qu'il veut modifier, voir UC "Ajouter un cours"
4. Le responsable clique sur le bouton "Valider"
5. Le système affiche le cours modifié

## UC13 Annulation de cours
Prérequis : Le responsable est connecté et un cours est déjà présent

### Scénario nominal
1. Le responsable clique sur le cours qui l'intéresse
2. Le système affiche les informations du cours (module, professeur, salle, horaires, nom du responsable qui l'a ajouté, jour de dernière modification)
3. Le responsable clique sur le bouton "Supprimer"
4. Le système affiche "Voulez-vous vraiment supprimer ce cours ?"
5. Le responsable clique sur "Oui"
6. Le système affiche "Cours supprimé"

### Scénario d'exception
5. a Le responsable clique sur "Non"
6. a Le système ne supprime pas le cours

## UC14 Accepter demande de modification de cours
Prérequis : Le responsable est connecté et une demande de modification est présente

### Scénario nominal
1. Le responsable clique sur le bouton "Demandes de modification"
2. Le système affiche la liste des demandes de modification
3. Le responsable clique sur la demande qui l'intéresse
4. Le système affiche les informations du cours (module, professeur, salle, horaires)
5. Le responsable clique sur le bouton "Accepter"
6. Le système affiche "Demande acceptée"

### Scénario d'exception
5. a Le responsable clique sur le bouton "Refuser"
6. a Le système affiche "Demande refusée"

## UC15 Ajouter un module
Prérequis : Le responsable est connecté

### Scénario nominal
1. Le responsable clique sur le bouton "Ajouter un module"
2. Le système affiche une fenêtre pour rentrer les informations du module (id, nom, nombre d'heures)
3. Le responsable clique sur le bouton "Valider"
4. Le système affiche le module ajouté

### Scénario d'exception
3. a Le responsable clique sur le bouton "Annuler"
4. a Le système n'ajoute pas le module


## UC16 Faire une demande de Report de cours
Prérequis : Le professeur est connecté

### Scénario nominal
1. Le professeur clique sur le cours qui l'intéresse
2. Le système affiche les informations du cours (module, professeur, salle, horaires)
3. Le professeur clique sur le bouton "Reporter"
4. Le système affiche une fenêtre pour rentrer les informations du report (nouvelle date, nouvelle heure, nouvelle salle)
5. Le professeur clique sur le bouton "Valider"
6. Le système affiche "Demande de report envoyée"

### Scénario d'exception
5. a Le professeur clique sur le bouton "Annuler"
6. a Le système n'envoie pas la demande de report

### Scénario alternatif
6. b Les informations du nouveau créneau sont incorrectes (salle occupée ou groupe occupé), le système affiche "Créneau indisponible" avec la raison de l'indisponibilité, l'UC reprend en 4.


## UC17 Ajouter une note de cours générale
Prérequis : Le professeur est connecté

### Scénario nominal
1. Le professeur clique sur le cours qui l'intéresse
2. Le système affiche les informations du cours (module, professeur, salle, horaires)
3. Le professeur clique sur le bouton "Ajouter une note générale"
4. Le système affiche une fenêtre pour rentrer la note générale
5. Le professeur rentre la note
6. Le professeur clique sur le bouton "Valider"
7. Le système affiche la note générale sur le cours

### Scénario d'exception
6. a Le professeur clique sur le bouton "Annuler"
7. a Le système n'ajoute pas la note

## UC18 Modifier une note de cours générale
Prérequis : Le professeur est connecté et une note est déjà présente

### Scénario nominal
1. Le professeur clique sur le cours qui l'intéresse
2. Le système affiche les informations du cours (module, professeur, salle, horaires)
3. Le professeur clique sur le bouton "Modifier la note générale"
4. Le système affiche la note générale
5. Le professeur modifie la note
6. Le professeur clique sur le bouton "Valider"
7. Le système affiche la note modifiée

### Scénario d'exception
6. a Le professeur clique sur le bouton "Annuler"
7. a Le système ne modifie pas la note


