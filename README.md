# Travel Agency API

Une API REST construite avec Spring Boot 3.5.0 et Java 21 pour la gestion d'une plateforme e-commerce d'agence de voyage avec système de cache Redis optimisé.

## 🚀 Fonctionnalités

- **Authentification JWT** : Système d'authentification sécurisé avec tokens JWT
- **Gestion des utilisateurs** : Inscription, connexion, réinitialisation de mot de passe
- **Catalogue de voyages** : Gestion des destinations, séjours et packages touristiques
- **Cache Redis** : Cache haute performance pour optimiser les requêtes fréquentes
- **Sécurité** : Protection des endpoints avec Spring Security
- **Base de données** : Intégration MySQL avec JPA/Hibernate
- **Documentation API** : Interface Swagger/OpenAPI intégrée
- **Validation** : Validation des données d'entrée
- **Gestion d'erreurs** : Gestion centralisée des exceptions
- **Monitoring** : Endpoints de surveillance du cache et de l'application

## 🛠️ Technologies utilisées

- **Java 21**
- **Spring Boot 3.5.0**
- **Spring Security**
- **Spring Data JPA**
- **Spring Cache + Redis**
- **MySQL**
- **Redis 7.2** (conteneur local)
- **JWT (JSON Web Tokens)**
- **Maven**
- **Swagger/OpenAPI 3**
- **Docker**
- **AWS (EC2, RDS)**

## 📋 Prérequis

- Java 21 ou supérieur
- Maven 3.6+
- MySQL 8.0+
- Docker (pour Redis local)

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

# Redis Cache
export REDIS_HOST=localhost
export REDIS_PORT=6379
export REDIS_PASSWORD=votre_mot_de_passe_redis_sécurisé

```

### 🗄️ Cache Redis

L'application utilise Redis pour améliorer les performances :

- **Cache des voyages** : Liste complète et détails individuels (TTL: 3 minutes)
- **Cache des destinations** : Par continent, pays, ville (TTL: 5 minutes)
- **Cache des options** : Par catégorie et fourchette de prix (TTL: 3-5 minutes)
- **Cache des recherches** : Résultats de recherche par caractère (TTL: 1 minute)
- **Cache des statistiques** : Métriques globales (TTL: 1 minute)

#### 📊 Architecture Cache

```
Controller → Service (Cache) → Repository → Database
     ↓           ↓                ↓           ↓
ResponseEntity  List<Entity>   JPA Query   MySQL
(pas caché)     (caché Redis)   (si cache miss)
```

## 🚀 Installation et démarrage

### 1. Cloner le projet

```bash
git clone https://github.com/LionelBatt/BootRest.git
cd BootRest
```

### 2. Démarrer Redis (conteneur local)

```bash
# Démarrer Redis avec mot de passe
docker run -d --name travel-redis \
  -p 6379:6379 \
  redis:7.2-alpine \
  redis-server --requirepass votre_mot_de_passe_redis_sécurisé

# Vérifier que Redis fonctionne
docker exec -it travel-redis redis-cli -a votre_mot_de_passe_redis_sécurisé ping
```

### 3. Compiler le projet

```bash
mvn clean compile
```

### 4. Lancer l'application

```bash
mvn spring-boot:run
```

### 5. Créer un JAR exécutable

```bash
mvn clean package
java -jar target/travel-agency-1.0.1-SNAPSHOT.jar
```

## 📚 Documentation API

Une fois l'application démarrée, accédez à la documentation Swagger à :

- **Interface Swagger UI** : http://localhost:8080/travel/swagger-ui/index.html  
- **Spécification OpenAPI** : http://localhost:8080/travel/v3/api-docs

### 🔍 Endpoints de surveillance

- **Test général** : `GET /travel/api/test/all`
- **Santé Redis** : `GET /travel/cache/health`
- **Statistiques cache** : `GET /travel/cache/stats`
- **Vider cache** : `DELETE /travel/cache/clear-all`
- **Test performance** : `POST /travel/cache/performance-test`

## 🧪 Tests

### Lancer tous les tests
```bash
mvn test
```

### Tester le cache Redis
```bash
# Test de santé du cache
curl http://localhost:8080/travel/cache/health

# Statistiques du cache
curl http://localhost:8080/travel/cache/stats

# Test de performance (100 opérations)
curl -X POST http://localhost:8080/travel/cache/performance-test?operations=100
```

## 📦 Déploiement

### 🐳 Docker Local

#### Configuration complète avec Redis :

```bash
# 1. Réseau Docker
docker network create travel-network

# 2. Redis
docker run -d --name travel-redis \
  -p 6379:6379 \
  redis:7.2-alpine \
  redis-server --requirepass your_redis_password

# 3. Application
docker build -t travel-agency-api .
docker run -d --name travel-app \
  -p 8080:8080 \
  -e DB_URL="jdbc:mysql://host.docker.internal:3306/travel_agency" \
  -e DB_USER=root \
  -e DB_PASSWORD=password \
  -e JWT_SECRET=your_jwt_secret \
  -e REDIS_HOST=travel-redis \
  -e REDIS_PASSWORD=your_redis_password \
  travel-agency-api
```

### ☁️ Déploiement Automatique sur AWS EC2

L'application est automatiquement déployée sur AWS EC2 via GitHub Actions avec Redis conteneur intégré :

#### 🏗️ Architecture de production

```
┌─────────────────────────────────┐
│        AWS EC2 t2.micro         │
│  ┌─────────────┐ ┌─────────────┐ │     ┌─────────────┐
│  │    App      │ │    Redis    │ │────▶│  RDS MySQL  │
│  │ Container   │ │ Container   │ │     │             │
│  │   :8080     │ │   :6379     │ │     │   :3306     │
│  └─────────────┘ └─────────────┘ │     └─────────────┘
└─────────────────────────────────┘
         ↑
    Public Internet
      :8080/travel
```

#### 🔧 Configuration requise

1. **Secrets GitHub à configurer** :
   ```
   AWS_ACCESS_KEY_ID          # Clé d'accès AWS
   AWS_SECRET_ACCESS_KEY      # Clé secrète AWS
   EC2_INSTANCE_ID           # ID de l'instance EC2
   EC2_SSH_PRIVATE_KEY       # Clé SSH privée pour EC2
   REDIS_PASSWORD            # Mot de passe Redis sécurisé
   ```

2. **Infrastructure AWS** :
   - Instance EC2 t2.micro avec Docker installé
   - Base de données RDS MySQL (même VPC)
   - Région AWS : `eu-west-3`
   - Groupes de sécurité : ports 8080, 6379, 3306

#### 🚀 Workflow de déploiement automatique

Le pipeline CI/CD se déroule en plusieurs étapes séquentielles :

1. **Tests & Qualité** → Tests unitaires + analyse Qodana
2. **Build** → Compilation Maven et packaging JAR
3. **🐳 Deploy Redis** → Déploiement conteneur Redis optimisé
4. **🚀 Deploy App** → Déploiement application avec connexion Redis
5. **✅ Health Check** → Vérification cache + endpoints API

#### 🐳 Pipeline Redis Container

Le déploiement Redis est entièrement automatisé avec optimisations EC2 t2.micro :

**Étapes du déploiement Redis :**
```bash
# 1. Création script de déploiement avec mot de passe sécurisé
create-redis-deployment-script.sh ${REDIS_PASSWORD}

# 2. Déploiement sur EC2 via SSH
deploy-redis-to-ec2.sh ${EC2_IP}
  ├── Installation/vérification Docker
  ├── Création configuration Redis optimisée
  ├── Démarrage container avec limites mémoire
  ├── Tests de connectivité et performance
  └── Configuration firewall et logs
```

**Optimisations EC2 t2.micro :**
- **Mémoire limitée** : 300MB max (évite OOM)
- **Persistence** : RDB avec snapshots optimisés
- **Sécurité** : Authentification + commandes dangereuses désactivées
- **Logs** : Rotation automatique (max 10MB × 3 fichiers)
- **Réseau** : Bind localhost + timeout optimisés

**Monitoring Redis :**
```bash
# Status container
sudo docker ps --filter name=travel-redis

# Métriques mémoire temps réel
sudo docker exec travel-redis redis-cli -a [PASSWORD] info memory

# Logs applicatifs
sudo docker logs -f travel-redis
```

#### 🌐 Accès à l'application

Une fois déployée, l'application est accessible sur :
- **URL** : `http://[EC2_IP]:8080/travel`
- **Swagger** : `http://[EC2_IP]:8080/travel/swagger-ui/index.html`
- **Health Check** : `http://[EC2_IP]:8080/travel/api/test/all`
- **Cache Status** : `http://[EC2_IP]:8080/travel/cache/health`

### 🛠️ Variables d'environnement pour la production

```env
# Base de données RDS
DB_URL=jdbc:mysql://your-rds-endpoint:3306/travel_agency?useSSL=false&serverTimezone=UTC
DB_USER=travel_user
DB_PASSWORD=secure_password

# JWT
JWT_SECRET=your_very_long_and_secure_jwt_secret_key_at_least_256_bits

# Redis Local Container
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=your_secure_redis_password_123

```

## 🏗️ Architecture CI/CD

### 📋 Pipeline GitHub Actions

```yaml
Tests + Qodana → Build → Redis Setup → Deploy EC2
  ↓       ↓        ↓         ↓           ↓
JUnit   Quality   JAR   Container   Docker Container
  ↓       ↓        ↓         ↓           ↓
✅      Report    S3     Redis:6379   App:8080 + RDS
```

### 🐳 Infrastructure Docker

- **App Container** :
  - Image de base : `openjdk:21-jdk-slim`
  - Port exposé : `8080`
  - Utilisateur : `appuser` (non-root)
  - Restart policy : `unless-stopped`

- **Redis Container** :
  - Image : `redis:7.2-alpine`
  - Port exposé : `6379`
  - Persistence : Volume Docker
  - Sécurité : Mot de passe requis

### 💰 Optimisation des coûts

- **AVANT** : EC2 + RDS + ElastiCache (~500€/an)
- **APRÈS** : EC2 + RDS + Redis Container (~150€/an)
- **Économies** : ~350€/an (70% de réduction)

## 🔧 Développement

### Structure du projet

```
src/
├── main/
│   ├── java/com/app/travel/
│   │   ├── config/          # Configurations (Redis, Security, CORS)
│   │   ├── controller/      # Contrôleurs REST + Cache endpoints
│   │   ├── service/         # Services avec cache (@Cacheable)
│   │   ├── exception/       # Gestion des exceptions
│   │   ├── model/          # Entités JPA (User, Trip, Option...)
│   │   ├── repos/          # Repositories JPA
│   │   ├── security/       # Configuration sécurité JWT
│   │   └── utils/          # Utilitaires
│   └── resources/
│       └── application.properties   # Config Redis + MySQL
└── test/                   # Tests unitaires et d'intégration
```

### 🧪 Tests de développement

```bash
# Tests unitaires complets
mvn test

# Test cache Redis en local
docker exec -it travel-redis redis-cli -a your_password
> SET test:key "Hello World"
> GET test:key
> TTL test:key

# Monitoring cache pendant développement
curl http://localhost:8080/travel/cache/stats | jq
```

## 📄 Licence

Ce projet est sous licence Apache License 2.0. Voir le fichier `LICENSE` pour plus de détails.
