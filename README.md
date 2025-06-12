# Travel Agency API

Une API REST moderne construite avec Spring Boot 3.5.0 et Java 21 pour la gestion d'une plateforme e-commerce d'agence de voyage.

## 🚀 Fonctionnalités

- **Authentification JWT** : Système d'authentification sécurisé avec tokens JWT
- **Gestion des utilisateurs** : Inscription, connexion, réinitialisation de mot de passe
- **Catalogue de voyages** : Gestion des destinations, séjours et packages touristiques
- **Sécurité** : Protection des endpoints avec Spring Security
- **Base de données** : Intégration MySQL avec JPA/Hibernate
- **Email** : Service d'envoi d'emails pour confirmations et notifications
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
- **Docker**
- **AWS (EC2, S3)**

## 📋 Prérequis

- Java 21 ou supérieur
- Maven 3.6+
- MySQL 8.0+

## ⚙️ Configuration


### 1. Variables d'environnement

Configurez les variables d'environnement suivantes :

```bash
# Base de données
export DB_URL=jdbc:mysql://localhost:3306/travel_agency_dev?useSSL=false&serverTimezone=UTC
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
## 🚀 Installation et démarrage

### 1. Cloner le projet

```bash
git clone https://github.com/LionelBatt/BootRest.git
cd BootRest
```

### 2. Compiler le projet

```bash
mvn clean compile
```

### 3. Lancer l'application

#### lancement de l'application:
```bash
mvn spring-boot:run
```

### 4. Créer un JAR exécutable

```bash
mvn clean package
java -jar target/travel-agency-1.0.1-SNAPSHOT.jar
```

## 📚 Documentation API

Une fois l'application démarrée, accédez à la documentation Swagger à :

- **Interface Swagger UI** : http://localhost:8080/travel/swagger-ui/index.html  
- **Spécification OpenAPI** : http://localhost:8080/travel/v3/api-docs

### Tests
- `GET /travel/api/test/all` - Endpoint de test (accessible sans authentification)

## 🧪 Tests

### Lancer tous les tests
```bash
mvn test
```

## 📦 Déploiement

### 🐳 Docker Local

Un Dockerfile est fourni pour containeriser l'application :

```bash
# Construire l'image
docker build -t travel-agency-api .

# Lancer le conteneur
docker run -p 8080:8080 \
  -e DB_URL="jdbc:mysql://host.docker.internal:3306/travel_agency" \
  -e DB_USER=root \
  -e DB_PASSWORD=password \
  -e JWT_SECRET=your_jwt_secret \
  travel-agency-api
```

### ☁️ Déploiement Automatique sur AWS EC2

L'application est automatiquement déployée sur AWS EC2 via GitHub Actions :

#### 🔧 Configuration requise

1. **Secrets GitHub à configurer** :
   ```
   AWS_ACCESS_KEY_ID          # Clé d'accès AWS
   AWS_SECRET_ACCESS_KEY      # Clé secrète AWS
   EC2_INSTANCE_ID           # ID de l'instance EC2
   EC2_SSH_PRIVATE_KEY       # Clé SSH privée pour EC2
   ```

2. **Infrastructure AWS** :
   - Instance EC2 avec Docker installé
   - Bucket S3 `prod-travel-agency` 
   - Région AWS : `eu-west-3`
   - Base de données RDS MySQL (optionnel)

#### 🚀 Workflow de déploiement

1. **Push sur main** → Déclenchement automatique
2. **Tests** → Exécution des tests unitaires
3. **Build** → Compilation et packaging
4. **Upload S3** → JAR + Dockerfile vers S3
5. **Déploiement EC2** → Container Docker sur EC2

#### 🌐 Accès à l'application

Une fois déployée, l'application est accessible sur :
- **URL** : `http://[EC2_IP]:8080/travel`
- **Swagger** : `http://[EC2_IP]:8080/travel/swagger-ui/index.html`
- **Health Check** : `http://[EC2_IP]:8080/travel/api/test/all`

### 🛠️ Variables d'environnement pour la production

```env
# Base de données
DB_URL=jdbc:mysql://localhost:3306/travel_agency_prod
DB_USER=travel_user
DB_PASSWORD=secure_password

# JWT
JWT_SECRET=your_very_long_and_secure_jwt_secret_key_at_least_256_bits

# Email
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USER=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

## 🏗️ Architecture CI/CD

### 📋 Pipeline GitHub Actions

```yaml
Tests → Build → Deploy EC2
  ↓       ↓         ↓
JUnit   JAR     Docker Container
        S3      AWS EC2
```

### 🐳 Infrastructure Docker

- **Image de base** : `openjdk:21-jdk-slim`
- **Port exposé** : `8080`
- **Utilisateur** : `appuser` (non-root)
- **Restart policy** : `unless-stopped`

## 🔧 Développement

### Structure du projet

```
src/
├── main/
│   ├── java/com/app/travel/
│   │   ├── config/          # Configurations
│   │   ├── controller/      # Contrôleurs REST
│   │   ├── exception/       # Gestion des exceptions
│   │   ├── model/          # Entités JPA (User, Destination, Booking, Package...)
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

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.
