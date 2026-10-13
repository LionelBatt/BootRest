#!/bin/bash

# Script pour créer le script de déploiement avec les secrets GitHub

# Variables à remplacer
DB_URL_VALUE="$1"
DB_USER_VALUE="$2"
DB_PASSWORD_VALUE="$3"
JWT_SECRET_VALUE="$4"
MAIL_HOST_VALUE="$5"
MAIL_USER_VALUE="$6"
MAIL_PASSWORD_VALUE="$7"
REDIS_HOST_VALUE="$8"
REDIS_PASSWORD_VALUE="$9"
S3_BUCKET_TEMP="${10}"
AWS_REGION="${11}"
APP_VERSION="${12}"

echo "🔧 Création du script de déploiement avec les secrets..."

cat > deploy_embedded.sh << 'EOF'
#!/bin/bash
set -e

# Configuration
APP_NAME="travel-agency"
CONTAINER_NAME="travel-agency-app"
IMAGE_NAME="travel-agency:latest"
APP_DIR="/opt/travel-agency"
LOGS_DIR="/var/log/travel-agency"
DATA_DIR="/var/lib/travel-agency"
S3_BUCKET="PLACEHOLDER_S3_BUCKET"
AWS_REGION="PLACEHOLDER_AWS_REGION"
JAR_FILE="travel-agency-PLACEHOLDER_APP_VERSION.jar"
ENV_FILE="/opt/travel-agency/.env"

echo "🚀 Début du déploiement..."

# Créer le fichier .env avec des variables GitHub Secrets
echo "🔐 Création du fichier .env avec des variables sécurisées..."
sudo mkdir -p "/opt/travel-agency"

# Créer le fichier .env avec les valeurs des secrets GitHub
cat > "$ENV_FILE" << 'ENV_CONTENT'
DB_URL=PLACEHOLDER_DB_URL
DB_USER=PLACEHOLDER_DB_USER
DB_PASSWORD=PLACEHOLDER_DB_PASSWORD
JWT_SECRET=PLACEHOLDER_JWT_SECRET
JWT_EXPIRATION=86400000
MAIL_HOST=PLACEHOLDER_MAIL_HOST
MAIL_PORT=587
MAIL_USER=PLACEHOLDER_MAIL_USER
MAIL_PASSWORD=PLACEHOLDER_MAIL_PASSWORD
REDIS_HOST=PLACEHOLDER_REDIS_HOST
REDIS_PASSWORD=PLACEHOLDER_REDIS_PASSWORD
ENV_CONTENT

chmod 600 "$ENV_FILE"
echo "✅ Fichier .env créé"

# Charger les variables d'environnement
set -a
source "$ENV_FILE"
set +a
echo "✅ Variables d'environnement chargées"

# Créer les répertoires avec les bonnes permissions
echo "📁 Création des répertoires..."
sudo mkdir -p "$APP_DIR" "$LOGS_DIR" "$DATA_DIR"
sudo chown -R ubuntu:ubuntu "$APP_DIR"
sudo chown -R 1000:1000 "$LOGS_DIR" "$DATA_DIR"
sudo chmod -R 755 "$LOGS_DIR" "$DATA_DIR"
cd "$APP_DIR"

# Arrêter et supprimer le conteneur existant
if docker ps -q -f name="$CONTAINER_NAME" | grep -q .; then
    echo "🛑 Arrêt du conteneur existant..."
    docker stop "$CONTAINER_NAME" || true
fi

if docker ps -aq -f name="$CONTAINER_NAME" | grep -q .; then
    echo "🗑️ Suppression du conteneur existant..."
    docker rm "$CONTAINER_NAME" || true
fi

# Supprimer l'ancienne image
if docker images -q "$IMAGE_NAME" | grep -q .; then
    echo "🗑️ Suppression de l'ancienne image..."
    docker rmi "$IMAGE_NAME" || true
fi

# Télécharger le JAR depuis S3
echo "📥 Téléchargement du JAR depuis S3..."
JAR_URL="https://s3.$AWS_REGION.amazonaws.com/$S3_BUCKET/app/$JAR_FILE"
echo "🔗 URL: $JAR_URL"

curl -f -L -o "$JAR_FILE" "$JAR_URL"

# Vérifier le téléchargement
if [[ ! -f "./$JAR_FILE" ]]; then
    echo "❌ Erreur: JAR non téléchargé"
    exit 1
fi

echo "✅ JAR téléchargé: $(ls -lh $JAR_FILE)"

# Créer le Dockerfile
echo "📝 Création du Dockerfile..."
cat > Dockerfile << 'DOCKERFILE_END'
FROM eclipse-temurin:25-jre

# Installer des outils de diagnostic
RUN apt-get update && apt-get install -y curl procps && rm -rf /var/lib/apt/lists/*

# Créer l'utilisateur avec UID/GID spécifiques
RUN groupadd -g 1000 appuser && useradd -u 1000 -g appuser -m appuser

WORKDIR /app
RUN mkdir -p /app/logs /app/data && chown -R appuser:appuser /app

# Copier le JAR (nom générique pour éviter les problèmes de version)
COPY *.jar app.jar
RUN chown appuser:appuser app.jar

USER appuser

EXPOSE 8080
VOLUME ["/app/logs", "/app/data"]

# Variables d'environnement par défaut
ENV SERVER_PORT=8080 \
    LOGGING_LEVEL_ROOT=INFO \
    LOGGING_FILE_PATH=/app/logs/application.log \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=120s --retries=3 \
    CMD curl -f http://localhost:$SERVER_PORT/actuator/health || exit 1

# Script d'entrée
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
DOCKERFILE_END

# Construire l'image Docker
echo "🔨 Construction de l'image Docker..."
docker build -t "$IMAGE_NAME" . --no-cache

# Vérifier que l'image a été créée
if ! docker images -q "$IMAGE_NAME" | grep -q .; then
    echo "❌ Erreur: Image Docker non créée"
    exit 1
fi

echo "✅ Image Docker créée"

# Diagnostic simple des variables
echo "🔍 Variables d'environnement:"
echo "DB_URL=$DB_URL"
echo "DB_USER=$DB_USER"
echo "REDIS_HOST=$REDIS_HOST"
echo "JWT_SECRET length: ${#JWT_SECRET}"

# Démarrer le conteneur avec variables explicites
echo "🚀 Démarrage du conteneur..."
docker run -d \
    --name "$CONTAINER_NAME" \
    --restart unless-stopped \
    --network host \
    -v "$LOGS_DIR:/app/logs:rw" \
    -v "$DATA_DIR:/app/data:rw" \
    -e "DB_URL=$DB_URL" \
    -e "DB_USER=$DB_USER" \
    -e "DB_PASSWORD=$DB_PASSWORD" \
    -e "JWT_SECRET=$JWT_SECRET" \
    -e "JWT_EXPIRATION=$JWT_EXPIRATION" \
    -e "MAIL_HOST=$MAIL_HOST" \
    -e "MAIL_PORT=$MAIL_PORT" \
    -e "MAIL_USER=$MAIL_USER" \
    -e "MAIL_PASSWORD=$MAIL_PASSWORD" \
    -e "REDIS_HOST=$REDIS_HOST" \
    -e "REDIS_PASSWORD=$REDIS_PASSWORD" \
    -e "REDIS_PORT=6379" \
    -e "SERVER_PORT=8080" \
    -e "LOGGING_LEVEL_ROOT=INFO" \
    -e "LOGGING_FILE_PATH=/app/logs/application.log" \
    "$IMAGE_NAME"

# Attendre et vérifier le statut
echo "⏳ Attente du démarrage (45 secondes)..."
sleep 45

# Diagnostic du conteneur
echo "🔍 Status du conteneur:"
docker ps -a -f name="$CONTAINER_NAME" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo -e "\n📝 Logs du conteneur (dernières 100 lignes):"
docker logs --tail 100 "$CONTAINER_NAME" 2>&1

echo -e "\n⏸️  Pause de 15 secondes pour examiner les logs..."
sleep 15

echo -e "\n📝 Logs complets du conteneur:"
docker logs "$CONTAINER_NAME" 2>&1

# Vérifier si le conteneur tourne
if docker ps | grep "$CONTAINER_NAME" > /dev/null; then
    echo "✅ Conteneur en cours d'exécution"
    PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4 2>/dev/null || echo "N/A")
    echo "🌐 Application disponible sur : http://YourEc2Ip:8080/travel"
else
    echo "❌ Conteneur ne fonctionne pas correctement"
    docker logs "$CONTAINER_NAME" 2>&1
    exit 1
fi

echo "🎉 Déploiement terminé !"
EOF


sed -i "s|PLACEHOLDER_DB_URL|$DB_URL_VALUE|g" deploy_embedded.sh
sed -i "s|PLACEHOLDER_DB_USER|$DB_USER_VALUE|g" deploy_embedded.sh
sed -i "s|PLACEHOLDER_DB_PASSWORD|$DB_PASSWORD_VALUE|g" deploy_embedded.sh
sed -i "s|PLACEHOLDER_JWT_SECRET|$JWT_SECRET_VALUE|g" deploy_embedded.sh
sed -i "s|PLACEHOLDER_MAIL_HOST|$MAIL_HOST_VALUE|g" deploy_embedded.sh
sed -i "s|PLACEHOLDER_MAIL_USER|$MAIL_USER_VALUE|g" deploy_embedded.sh
sed -i "s|PLACEHOLDER_MAIL_PASSWORD|$MAIL_PASSWORD_VALUE|g" deploy_embedded.sh
sed -i "s|PLACEHOLDER_REDIS_HOST|$REDIS_HOST_VALUE|g" deploy_embedded.sh
sed -i "s|PLACEHOLDER_REDIS_PASSWORD|$REDIS_PASSWORD_VALUE|g" deploy_embedded.sh
sed -i "s|PLACEHOLDER_S3_BUCKET|$S3_BUCKET_TEMP|g" deploy_embedded.sh
sed -i "s|PLACEHOLDER_AWS_REGION|$AWS_REGION|g" deploy_embedded.sh
sed -i "s|PLACEHOLDER_APP_VERSION|$APP_VERSION|g" deploy_embedded.sh

chmod +x deploy_embedded.sh
echo "✅ Script de déploiement créé avec succès"
