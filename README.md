# # Resto API

Une API REST moderne construite avec Spring Boot 3.5.0 et Java 21 pour la gestion d'un système de restaurant.

## 🚀 Fonctionnalités

- **Authentification JWT** : Système d'authentification sécurisé avec tokens JWT
- **Gestion des utilisateurs** : Inscription, connexion, réinitialisation de mot de passe
- **Sécurité** : Protection des endpoints avec Spring Security
- **Base de données** : Intégration MySQL avec JPA/Hibernate
- **Email** : Service d'envoi d'emails pour la réinitialisation de mots de passe
- **Documentation API** : Interface Swagger/OpenAPI intégrée
- **Validation** : Validation des données d'entrée
- **Gestion d'erreurs** : Gestion centralisée des exceptions

## 🛠️ Technologies utilisées

- **Java 21**
- **Spring Boot 3.5.0**
- **Spring Security**
- **Spring Data JPA**
- **MySQL**
- **JWT (JSON Web Tokens)**
- **Maven**
- **Swagger/OpenAPI 3**

## 📋 Prérequis

- Java 21 ou supérieur
- Maven 3.6+
- MySQL 8.0+

## ⚙️ Configuration

### 1. Base de données

Créez une base de données MySQL :

```sql
CREATE DATABASE resto_dev;
```

### 2. Variables d'environnement

Configurez les variables d'environnement suivantes :

```bash
# Base de données
export DB_URL=jdbc:mysql://localhost:3306/resto_dev?useSSL=false&serverTimezone=UTC
export DB_USER=root
export DB_PASSWORD=votre_mot_de_passe

# JWT
export JWT_SECRET=votre_secret_jwt_très_long_et_sécurisé_au_moins_256_bits

# Email
export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
export MAIL_USER=votre_email@gmail.com
export MAIL_PASSWORD=votre_mot_de_passe_application
```

### 3. Profils d'environnement

L'application supporte plusieurs profils :

- **dev** : Développement (base de données locale, logs détaillés)
- **prod** : Production (configuration optimisée)

## 🚀 Installation et démarrage

### 1. Cloner le projet

```bash
git clone <votre-repo-url>
cd BootRest
```

### 2. Compiler le projet

```bash
mvn clean compile
```

### 3. Lancer l'application

#### Mode développement :
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Mode production :
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 4. Créer un JAR exécutable

```bash
mvn clean package
java -jar target/resto-1.0.0-SNAPSHOT.jar
```

## 📚 Documentation API

Une fois l'application démarrée, accédez à la documentation Swagger à :

- **Interface Swagger UI** : http://localhost:8080/app/swagger-ui.html  
- **Spécification OpenAPI** : http://localhost:8080/app/api-docs

## 🔗 Endpoints principaux

### Authentification
- `POST /app/auth/register` - Inscription d'un nouvel utilisateur
- `POST /app/auth/login` - Connexion utilisateur
- `POST /app/auth/refresh` - Rafraîchissement du token

### Gestion des mots de passe
- `POST /app/password/forgot` - Demande de réinitialisation
- `POST /app/password/reset` - Réinitialisation du mot de passe

### Utilisateurs (authentification requise)
- `GET /app/users/profile` - Profil utilisateur
- `PUT /app/users/profile` - Mise à jour du profil

## 🧪 Tests

### Lancer tous les tests
```bash
mvn test
```

### Lancer les tests avec couverture
```bash
mvn test jacoco:report
```

## 📦 Déploiement

### Docker

Un Dockerfile est fourni pour containeriser l'application :

```bash
# Construire l'image
docker build -t resto-api .

# Lancer le conteneur
docker run -p 8080:8080 --env-file .env resto-api
```

### Fichier .env exemple

```env
DB_URL=jdbc:mysql://db:3306/resto
DB_USER=resto_user
DB_PASSWORD=secure_password
JWT_SECRET=your_very_long_and_secure_jwt_secret_key_at_least_256_bits
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USER=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

## 🔧 Développement

### Structure du projet

```
src/
├── main/
│   ├── java/com/app/resto/
│   │   ├── config/          # Configurations
│   │   ├── controller/      # Contrôleurs REST
│   │   ├── exception/       # Gestion des exceptions
│   │   ├── model/          # Entités JPA
│   │   ├── repos/          # Repositories
│   │   ├── security/       # Configuration sécurité
│   │   ├── service/        # Services métier
│   │   └── utils/          # Utilitaires
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       └── application-prod.properties
└── test/                   # Tests unitaires et d'intégration
```

### Bonnes pratiques

- Utilisez les DTOs pour les échanges API
- Validez toujours les données d'entrée
- Loggez les actions importantes
- Écrivez des tests pour vos endpoints
- Respectez les conventions REST

## 🤝 Contribution

1. Fork le projet
2. Créez une branche feature (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.
