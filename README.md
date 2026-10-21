# EduConnect — Client desktop JavaFX

Application cliente de la plateforme éducative EduConnect, développée dans le cadre du module PIDEV à Esprit School of Engineering (2024–2025).

## Objectif

Proposer une interface desktop permettant de travailler avec les contenus pédagogiques, les stages, les événements et les quiz. Le fonctionnement graphique et la connexion MySQL restent à vérifier sur un poste configuré.

## Technologies

- Java 17, JavaFX 17, FXML / CSS
- Maven Wrapper (`mvnw`, `mvnw.cmd`) et `javafx-maven-plugin`
- JDBC et MySQL, bibliothèques JavaFX additionnelles répertoriées dans `pom.xml`

## Installation et lancement

Sous Windows, avec le JDK 17 :

```powershell
git clone https://github.com/baaahaaaa/ProjectJava.git
cd ProjectJava
.\mvnw.cmd clean verify
.\mvnw.cmd javafx:run
```

Le lancement JavaFX exige un environnement desktop avec affichage graphique. Le `pom.xml` référence `ranim.projetpidev.HelloApplication` comme classe principale.

## Configurer la base MySQL

Consulter [DATABASE_CONFIGURATION.md](DATABASE_CONFIGURATION.md). Exemple de variables d'environnement locales :

```powershell
$env:EDUCONNECT_DB_URL = 'jdbc:mysql://localhost:3307/testpidev'
$env:EDUCONNECT_DB_USER = 'educonnect_user'
$env:EDUCONNECT_DB_PASSWORD = 'REMPLACER_PAR_UN_SECRET_LOCAL'
```

Utiliser un compte de base de développement, pas un secret réel dans le dépôt.

## Statut et limites

- Le code JavaFX, la configuration Maven et le guide JDBC sont présents.
- Le guide de configuration historique mentionne une compilation Maven validée sous Java 17.
- Aucun test automatisé n'a été observé ; démarrage UI, connexion MySQL, contrôle d'accès et parcours complets restent à valider.
- Le backend web Symfony associé se trouve dans [YRTBJ](https://github.com/baaahaaaa/YRTBJ).

**Projet académique — ne pas le présenter comme testé end-to-end avant vérification locale.**
