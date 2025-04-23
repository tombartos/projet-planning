# PLANNING NOUVELLE GÉNÉRATION
Ce projet a été réalisé par Tom BARTIER, Yann ROBLIN, Emad BA GUBAIR et André Marçais de l'université de Toulon dans le cadre du module de Modélisation encadré par Madame Murisasco de l'université de Toulon, Jeremy Mallofre et Vincent Loiseau de Soprasteria.
Il a pour but d'introduire les notions de méthode agiles aux étudiants comme la méthode Scrum. Le sujet du projet est de faire un logiciel de gestion d'emploi du temps pour une université en Java.

# Compilation
Le projet utilise une base de données PostGRESQL installée dans un conteneur Docker. Il faut donc installer Docker et Docker Compose pour faire fonctionner le projet. Il faut aussi installer Maven pour compiler le projet et Java 17 ou supérieur pour faire fonctionner le projet.

Le code source du projet se trouve dans le dossier /projet-planning/png
```bash
cd projet-planning/png
```

Créer la base de données :
Linux MacOS et Windows:
```bash
docker-compose up -d
```

Pour compiler le projet et peupler la base de données, il faut utiliser la commande suivante : 
Linux, MacOS et Windows:
```bash
mvn clean install
```
Linux et MacOS:
```bash
mvn exec:java -Dexec.mainClass=fr.univtln.m1im.png.generation.DataFaker
```
Windows:
A venir

Une fois ceci fait il faut créer les utilisateurs PostGRES et leur donner les permissions nécessaires :

-Méthode 1 Linux et MacOS avec psql installé sur la machine hôte : 

    ./create_users

-Méthode 2 : Windows:
    A venir


# Execution
Pour exécuter le projet, il faut utiliser les commandes suivantes : 
```bash
cd projet-planning/png
mvn exec:java
```

Pour réinitialiser la base de données, il faut utiliser la commande suivante : 
Linux et MacOS :
```bash
mvn exec:java -Dexec.mainClass=fr.univtln.m1im.png.generation.DataFaker
```
Cette commande va supprimer la base de données et la recréer avec les données de test.
Il faut ensuite reutiliser le script SQL (méthode 1 ou 2) pour créer les utilisateurs et leur donner les permissions nécessaires.




