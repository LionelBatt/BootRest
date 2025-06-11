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
java -jar target/resto-1.0.0-SNAPSHOT.jar
```

### Tests Unitaires
```bash
# Tous les tests
mvn test

# Tests d'une classe spécifique
mvn test -Dtest=UserControllerTest

# Tests avec couverture
mvn test jacoco:report
```

### Tests d'API avec curl
```bash
# Test de santé
curl http://localhost:8080/app/actuator/health

# Inscription
curl -X POST http://localhost:8080/resto/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@test.com","password":"password123"}'

# Connexion
curl -X POST http://localhost:8080/resto/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"password123"}'

# Test avec token JWT
curl -X GET http://localhost:8080/app/users/profile \
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
- URL: http://localhost:8080/app/swagger-ui.html
- JSON: http://localhost:8080/app/api-docs

### Génération de Documentation
```bash
# Documentation Maven
mvn site

# Documentation JavaDoc
mvn javadoc:javadoc
```