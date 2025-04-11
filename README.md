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
```bash
docker-compose up -d
```

Pour compiler le projet et peupler la base de données, il faut utiliser la commande suivante : 
```bash
mvn clean install
```

Une fois ceci fait il faut créer les utilisateurs PostGRES et leur donner les permissions nécessaires :

-Méthode 1 Linux et MacOS UNIQUEMENT avec psql installé sur la machine hôte : 

    cd ..
    ./create_user.sh

-Méthode 2 : Linux, MacOS et Windows: 
    Connectez vous à la base de données en utilisant le client de votre choix avec les identifiants suivants :

    Username : postgres
    Password : mysecretpassword
    Database : postgres
    Port : 8080

Lancez le script SQL suivant :
`projet-planning/init.sql `
 ou copiez collez le dans votre client SQL. 

# Execution
Pour exécuter le projet, il faut utiliser les commandes suivantes : 
```bash
cd projet-planning/png
mvn exec:java
```




