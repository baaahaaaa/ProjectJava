# EduConnect JavaFX - Configuration MySQL

## Prerequis

- Java JDK 17
- MySQL
- Maven Wrapper fourni avec le projet

## Variables d'environnement

La connexion JDBC utilise les variables suivantes :

| Variable | Description |
|---|---|
| EDUCONNECT_DB_URL | URL JDBC MySQL |
| EDUCONNECT_DB_USER | Utilisateur MySQL |
| EDUCONNECT_DB_PASSWORD | Mot de passe MySQL |

## Exemple PowerShell

```powershell
$env:EDUCONNECT_DB_URL = "jdbc:mysql://localhost:3307/testpidev"
$env:EDUCONNECT_DB_USER = "educonnect_user"
$env:EDUCONNECT_DB_PASSWORD = "REMPLACER_PAR_MOT_DE_PASSE_LOCAL"

.\mvnw.cmd clean verify
```

La base MySQL et son schema doivent etre configures avant d'utiliser les fonctionnalites qui accedent aux donnees.

## Configuration historique

Pour maintenir la compatibilite avec le developpement local, le code conserve les anciennes valeurs par defaut : port 3307, base testpidev, utilisateur root et mot de passe vide.

Ces valeurs sont destinees uniquement a un environnement de developpement local.

## Limites actuelles

- Compilation et packaging Maven valides avec Java 17.
- Aucun test automatise present.
- Interface JavaFX et integration MySQL non encore validees de bout en bout.
- La gestion des mots de passe utilisateurs necessite un audit de securite.

Ne jamais publier de vrais mots de passe dans Git.