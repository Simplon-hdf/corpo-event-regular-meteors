# CorpoEvent

Application de gestion d'événements d'entreprise.

## Stack Technique

- Java 11
- Spring Boot 2.7.x
- Spring Data JPA
- Spring MVC
- Thymeleaf
- Bootstrap 5
- H2 Database
- Maven

## Prérequis

- Java 11 ou supérieur
- Maven 3.6 ou supérieur

## Installation et Lancement

1. Cloner le projet :
```bash
git clone [url-du-repo]
cd corpo-event
```

2. Compiler le projet :
```bash
mvn clean install
```

3. Lancer l'application :
```bash
mvn spring-boot:run
```

4. Accéder à l'application :
- URL : http://localhost:8080
- Utilisateurs par défaut :
  - Admin : admin@corpoevent.fr / password
  - Collaborateur : collab1@corpoevent.fr / password

## Base de données H2

H2 est une base de données en mémoire utilisée pour le développement.

- Console H2 : http://localhost:8080/h2-console
- JDBC URL : jdbc:h2:mem:corpoevent
- Username : sa
- Password : password

La base de données est réinitialisée à chaque redémarrage de l'application avec les données par défaut :
- Un administrateur
- Deux collaborateurs
- Aucun événement initial

## ⏳ Brief Corpo-Event

### Contexte :

Une entreprise souhaite faire partager des évènements qu'elle organise à l'ensemble de ses collaborateurs.  
Le problème c'est que ses collaborateurs sont situés sur énormément de sites différents.  
Et ceux-ci ne lisent pas forcément tous les mails qui leur sont adressés.  
C'est pourquoi elle vous demande de **réfléchir à la conception et de démarrer la réalisation d'une plate-forme permettant de partager sur des évènements futurs et aussi d'en proposer des nouveaux**.  
​
*Optionnel : il sera également possible pour les collaborateurs d'ajouter des commentaires sur les évènements.*  

### Enjeux : 
<hr>

### Modalités pédagogiques :

Activité en binôme
​
Vous devrez réaliser les tâches suivantes :  
En analysant le besoin, vous devrez construire le diagramme de cas d'utilisation.  
Vous devrez élaborer le diagramme de classes afin d'avoir une vision d'ensemble des classes à utiliser.  
Vous devrez ensuite concevoir les intérations entre les différents classes en réfléchissant aux différents scénarios possibles via le diagramme de classes
Vous devrez réaliser les développements en respectant les principes de la POO (SOLID, ...).  

### Livrables :

Repo Git avec :
- dossier de conception contenant :
    - diagramme de cas d'utilisation
    - diagramme de classes
    - diagramme de séquences
- les sources

### Critères de performance :

- Exhaustivité des fonctionnalités dans les diagrammes
- Quelques fonctionnalités développées dans le langage de votre choix
- Respect du principe SOLID
- Notions POO correctement appréhendées