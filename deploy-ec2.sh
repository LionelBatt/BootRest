#!/bin/bash

# Script de déploiement EC2 pour l'application Resto avec Docker
# À exécuter sur l'instance EC2 cible

set -e

# Configuration
APP_NAME="travel-agency"
CONTAINER_NAME="travel-agency-app"
IMAGE_NAME="travel-agency:latest"
APP_DIR="/opt/travel-agency"
LOGS_DIR="/var/log/travel-agency"
DATA_DIR="/var/lib/travel-agency"
S3_BUCKET="prod-travel-agency"

echo "🚀 Début du déploiement Docker de l'application Travel Agency"
echo "📅 $(date)"

# Créer les répertoires nécessaires
sudo mkdir -p "$APP_DIR" "$LOGS_DIR" "$DATA_DIR"
sudo chown -R 1000:1000 "$LOGS_DIR" "$DATA_DIR"

# Arrêter et supprimer le conteneur existant
if docker ps -q -f name="$CONTAINER_NAME" | grep -q .; then
    echo "🛑 Arrêt du conteneur existant..."
    docker stop "$CONTAINER_NAME" || true
    docker rm "$CONTAINER_NAME" || true
fi

# Télécharger les fichiers depuis S3
echo "📥 Téléchargement des fichiers..."
cd "$APP_DIR"
sudo aws s3 cp "s3://$S3_BUCKET/app/Dockerfile" ./Dockerfile
sudo aws s3 cp "s3://$S3_BUCKET/app/travel-agency-1.0.1-SNAPSHOT.jar" ./travel-agency-1.0.1-SNAPSHOT.jar

# Construire l'image Docker
echo "🔨 Construction de l'image Docker..."
docker build -t "$IMAGE_NAME" .

# Démarrer le nouveau conteneur avec volumes et variables d'environnement
echo "🚀 Démarrage du conteneur..."
docker run -d \
    --name "$CONTAINER_NAME" \
    --restart unless-stopped \
    -p 8080:8080 \
    -v "$LOGS_DIR:/app/logs" \
    -v "$DATA_DIR:/app/data" \
    -e SERVER_PORT=8080 \
    -e LOGGING_FILE_PATH=/app/logs/application.log \
    -e LOGGING_LEVEL_ROOT=INFO \
    -e DB_URL="${DB_URL}" \
    -e DB_USER="${DB_USER}" \
    -e DB_PASSWORD="${DB_PASSWORD}" \
    -e JWT_SECRET="${JWT_SECRET}" \
    -e MAIL_HOST="${MAIL_HOST}" \
    -e MAIL_PORT="${MAIL_PORT}" \
    -e MAIL_USER="${MAIL_USER}" \
    -e MAIL_PASSWORD="${MAIL_PASSWORD}" \
    "$IMAGE_NAME"

echo "✅ Déploiement terminé avec succès !"
echo "🌐 Application accessible sur http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):8080/travel"
echo "📋 Logs disponibles dans : $LOGS_DIR"
echo "💾 Données persistées dans : $DATA_DIR"

# Vérifier le statut du conteneur
sleep 5
if docker ps | grep "$CONTAINER_NAME" > /dev/null; then
    echo "✅ Conteneur démarré avec succès"
    docker logs --tail 20 "$CONTAINER_NAME"
else
    echo "❌ Erreur lors du démarrage du conteneur"
    docker logs "$CONTAINER_NAME"
    exit 1
fi
