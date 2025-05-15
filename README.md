
---

### ✅ `README-JavaFX.md` (Application Client Desktop)

```markdown
# EduConnect - Application JavaFX Éducative

## Overview

Cette application JavaFX constitue la partie client desktop du projet **EduConnect**, développée dans le cadre du module PIDEV à **Esprit School of Engineering** (2024-2025).  
Elle permet aux étudiants d'interagir avec les contenus éducatifs (cours, stages, événements), et de passer des quiz évalués dynamiquement.

## Features ##

- 🧑‍🎓 Espace étudiant :
  - Achat de cours
  - Réservation de stages et d’événements
  - Quiz à choix multiples avec notation automatique

- 🎮 Interfaces dynamiques et stylisées (JavaFX)

- 🔗 Connexion à la base de données distante

Tech Stack ###

Frontend ####

- JavaFX 17
- FXML + CSS
- Scene Builder

Backend

- Java 17
- JDBC
- MySQL

### Other Tools

- Maven
- Git / GitHub
- JavaMail API (si envoi mail)
- Apache POI (PDF/Excel)

## Directory Structure ##

```arduino
📂 SkyLearn-Client/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/skylearn/client/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── service/
│   │   │   └── util/
│   │   └── resources/
│   │       ├── view/          # FXML files
│   │       └── config.properties
└── README-JavaFX.md
