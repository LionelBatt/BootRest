#!/bin/bash

# Script de création du déploiement Redis pour l'infrastructure
# Ce script génère le script de déploiement Redis à exécuter sur EC2

set -e

REDIS_PASSWORD="$1"

echo "🐳 Création du script de déploiement Redis..."

cat > deploy_redis.sh << 'EOF'
#!/bin/bash

# Script de déploiement Redis Container sur EC2
# Optimisé pour EC2 t2.micro avec contraintes mémoire

set -e

REDIS_PASSWORD="REDIS_PASSWORD_PLACEHOLDER"
REDIS_PORT="6379"
REDIS_CONTAINER_NAME="travel-redis"
REDIS_VERSION="7.2-alpine"
REDIS_CONFIG_DIR="/opt/redis-config"
REDIS_DATA_DIR="/opt/redis-data"

echo "🚀 === DÉPLOIEMENT REDIS CONTAINER SUR EC2 ==="
echo "📅 Date: $(date '+%Y-%m-%d %H:%M:%S')"
echo "🏷️  Version Redis: $REDIS_VERSION"
echo "🔐 Sécurité: Authentification activée"
echo "💾 Mémoire: Limitée à 300MB (optimisé t2.micro)"
echo ""

# Arrêt et suppression de l'ancien container Redis s'il existe
echo "🛑 Arrêt de l'ancien container Redis..."
sudo docker stop $REDIS_CONTAINER_NAME 2>/dev/null || true
sudo docker rm $REDIS_CONTAINER_NAME 2>/dev/null || true

# Création des répertoires pour Redis
echo "📁 Création des répertoires Redis..."
sudo mkdir -p $REDIS_CONFIG_DIR
sudo mkdir -p $REDIS_DATA_DIR
sudo chown 999:999 $REDIS_DATA_DIR  # user redis dans le container

# Création du fichier de configuration Redis optimisé
echo "⚙️  Création de la configuration Redis..."
sudo tee $REDIS_CONFIG_DIR/redis.conf > /dev/null << 'REDIS_CONF'
# Configuration Redis optimisée pour EC2 t2.micro
# Mémoire totale limitée à 300MB pour éviter OOM

# Réseau et sécurité
bind 0.0.0.0
protected-mode yes
port 6379
requirepass REDIS_PASSWORD_PLACEHOLDER

# Optimisations mémoire pour t2.micro (1GB RAM)
maxmemory 300mb
maxmemory-policy allkeys-lru
save 900 1
save 300 10
save 60 10000

# Performance
tcp-keepalive 300
timeout 300
tcp-backlog 511

# Logging
loglevel notice
syslog-enabled yes
syslog-ident redis

# Persistence optimisée
dbfilename dump.rdb
dir /data
rdbcompression yes
rdbchecksum yes

# Désactivation de certaines commandes dangereux
rename-command FLUSHDB ""
rename-command FLUSHALL ""
rename-command SHUTDOWN REDIS_SHUTDOWN
rename-command CONFIG REDIS_CONFIG

# Optimisations supplémentaires
hash-max-ziplist-entries 512
hash-max-ziplist-value 64
list-max-ziplist-size -2
list-compress-depth 0
set-max-intset-entries 512
zset-max-ziplist-entries 128
zset-max-ziplist-value 64
hll-sparse-max-bytes 3000

# Lazy freeing pour éviter les blocages
lazyfree-lazy-eviction yes
lazyfree-lazy-expire yes
lazyfree-lazy-server-del yes

# Clients
maxclients 1000
REDIS_CONF

# Remplacement du mot de passe dans la configuration
sudo sed -i "s/REDIS_PASSWORD_PLACEHOLDER/$REDIS_PASSWORD/g" $REDIS_CONFIG_DIR/redis.conf

# Pull de l'image Redis si nécessaire
echo "📥 Pull de l'image Redis $REDIS_VERSION..."
sudo docker pull redis:$REDIS_VERSION

# Démarrage du container Redis avec la configuration
echo "🚀 Démarrage du container Redis..."
sudo docker run -d \
  --name $REDIS_CONTAINER_NAME \
  --restart unless-stopped \
  -p $REDIS_PORT:6379 \
  -v $REDIS_CONFIG_DIR/redis.conf:/usr/local/etc/redis/redis.conf:ro \
  -v $REDIS_DATA_DIR:/data \
  --memory=350m \
  --memory-swap=350m \
  --oom-kill-disable=false \
  redis:$REDIS_VERSION \
  redis-server /usr/local/etc/redis/redis.conf

# Attente du démarrage
echo "⏳ Attente du démarrage de Redis..."
sleep 5

# Test de connexion Redis
echo "🔍 Test de connexion Redis..."
REDIS_STATUS=$(sudo docker exec $REDIS_CONTAINER_NAME redis-cli -a "$REDIS_PASSWORD" ping 2>/dev/null || echo "FAILED")

if [ "$REDIS_STATUS" = "PONG" ]; then
    echo "✅ Redis démarré avec succès !"
    
    # Affichage des informations Redis
    echo ""
    echo "📊 === INFORMATIONS REDIS ==="
    sudo docker exec $REDIS_CONTAINER_NAME redis-cli -a "$REDIS_PASSWORD" info memory | grep -E "(used_memory_human|maxmemory_human|mem_fragmentation_ratio)"
    sudo docker exec $REDIS_CONTAINER_NAME redis-cli -a "$REDIS_PASSWORD" info server | grep -E "(redis_version|process_id|uptime_in_seconds)"
    
    # Configuration des logs
    echo ""
    echo "📋 === LOGS REDIS ==="
    echo "Logs disponibles avec: sudo docker logs $REDIS_CONTAINER_NAME"
    echo "Monitoring temps réel: sudo docker logs -f $REDIS_CONTAINER_NAME"
    
    # Informations de connexion
    echo ""
    echo "🔗 === INFORMATIONS DE CONNEXION ==="
    echo "Host: localhost"
    echo "Port: $REDIS_PORT"
    echo "Password: [CONFIGURÉ]"
    echo "Container: $REDIS_CONTAINER_NAME"
    echo "Status: $(sudo docker ps --filter name=$REDIS_CONTAINER_NAME --format 'table {{.Status}}')"
    
else
    echo "❌ Échec du démarrage de Redis !"
    echo "📋 Logs du container:"
    sudo docker logs $REDIS_CONTAINER_NAME || true
    exit 1
fi

# Configuration du firewall pour Redis (port 6379)
echo ""
echo "🔒 Configuration du firewall..."
if command -v ufw >/dev/null 2>&1; then
    sudo ufw allow $REDIS_PORT/tcp comment "Redis Container"
    echo "✅ Firewall configuré pour le port $REDIS_PORT"
fi

# Configuration de la rotation des logs Docker
echo ""
echo "📋 Configuration de la rotation des logs..."
if [ ! -f /etc/docker/daemon.json ]; then
    sudo mkdir -p /etc/docker
    sudo tee /etc/docker/daemon.json > /dev/null << 'DOCKER_LOG_CONFIG'
{
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "10m",
    "max-file": "3"
  }
}
DOCKER_LOG_CONFIG
    echo "✅ Configuration des logs Docker créée"
fi

echo ""
echo "🎉 === DÉPLOIEMENT REDIS TERMINÉ ==="
echo "📊 Container Redis: $REDIS_CONTAINER_NAME"
echo "🔗 Endpoint: localhost:$REDIS_PORT"
echo "🔐 Authentification: Activée"
echo "💾 Mémoire max: 300MB"
echo "📁 Données: $REDIS_DATA_DIR"
echo "⚙️  Config: $REDIS_CONFIG_DIR/redis.conf"
echo ""
echo "🔧 Commandes utiles:"
echo "  • Status: sudo docker ps --filter name=$REDIS_CONTAINER_NAME"
echo "  • Logs: sudo docker logs $REDIS_CONTAINER_NAME"
echo "  • Redis CLI: sudo docker exec -it $REDIS_CONTAINER_NAME redis-cli -a [PASSWORD]"
echo "  • Restart: sudo docker restart $REDIS_CONTAINER_NAME"
echo "  • Stop: sudo docker stop $REDIS_CONTAINER_NAME"
echo ""
EOF

# Remplacement du mot de passe dans le script
# Compatible macOS et Linux
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    sed -i '' "s/REDIS_PASSWORD_PLACEHOLDER/$REDIS_PASSWORD/g" deploy_redis.sh
else
    # Linux (GitHub Actions)
    sed -i "s/REDIS_PASSWORD_PLACEHOLDER/$REDIS_PASSWORD/g" deploy_redis.sh
fi

chmod +x deploy_redis.sh

echo "✅ Script de déploiement Redis créé : deploy_redis.sh"
echo "🔐 Mot de passe Redis configuré"
echo "🐳 Prêt pour le déploiement sur EC2"
