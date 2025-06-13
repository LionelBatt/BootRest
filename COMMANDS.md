# Commandes Utiles - Resto API

## 🛠️ Développement

### Compilation et Construction
```bash
# Nettoyer et compiler
mvn clean compile

# Construire le JAR
mvn clean package

# Construire en ignorant les tests
mvn clean package -DskipTests

# Lancer les tests
mvn test
```

### Démarrage de l'Application
```bash
# Démarrage de l'application spring
mvn spring-boot:run 

# Démarrage avec JAR exécutable
java -jar target/travel-agency-1.0.1-SNAPSHOT.jar
```

### Tests Unitaires
```bash
# Tous les tests
mvn test


# Inscription
curl -X POST http://localhost:8080/resto/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@test.com","password":"password123"}'

# Connexion
curl -X POST http://localhost:8080/resto/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"password123"}'

# Test avec token JWT
curl -X GET http://localhost:8080/app/trips/ \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🔍 Debug et Troubleshooting

### Problèmes Communs
```bash
# Port déjà utilisé
lsof -i :8080
kill -9 PID

# Problèmes de compilation
mvn clean
mvn dependency:purge-local-repository

## 📚 Documentation

### Swagger/OpenAPI
- URL: http://localhost:8080/travel/swagger-ui.html
- JSON: http://localhost:8080/travel/api-docs

### Génération de Documentation
```bash
# Documentation Maven
mvn site

# Documentation JavaDoc
mvn javadoc:javadoc
```