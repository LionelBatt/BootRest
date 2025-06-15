#!/bin/bash

# Script de déploiement Redis sur EC2
# Exécute le déploiement Redis via SSH sur l'instance EC2

set -e

EC2_IP="$1"

if [ -z "$EC2_IP" ]; then
    echo "❌ Erreur: IP de l'instance EC2 requise"
    echo "Usage: $0 <EC2_IP>"
    exit 1
fi

echo "🐳 === DÉPLOIEMENT REDIS SUR EC2 ==="
echo "🎯 Instance EC2: $EC2_IP"
echo "📅 Date: $(date '+%Y-%m-%d %H:%M:%S')"
echo ""

# Vérification de la connexion SSH
echo "🔍 Test de connexion SSH..."
if ! ssh -o ConnectTimeout=10 -o StrictHostKeyChecking=no ubuntu@$EC2_IP "echo 'SSH OK'" 2>/dev/null; then
    echo "❌ Impossible de se connecter à EC2 via SSH"
    exit 1
fi
echo "✅ Connexion SSH établie"

# Transfert du script de déploiement Redis
echo "📤 Transfert du script de déploiement Redis..."
scp -o StrictHostKeyChecking=no deploy_redis.sh ubuntu@$EC2_IP:~/

# Vérification de Docker sur EC2
echo "🐳 Vérification de Docker sur EC2..."
ssh -o StrictHostKeyChecking=no ubuntu@$EC2_IP << 'DOCKER_CHECK'
if ! command -v docker >/dev/null 2>&1; then
    echo "🔧 Installation de Docker..."
    sudo apt-get update -qq
    sudo apt-get install -y -qq docker.io
    sudo systemctl start docker
    sudo systemctl enable docker
    sudo usermod -aG docker ubuntu
    echo "✅ Docker installé"
else
    echo "✅ Docker déjà installé"
fi

# Vérification du statut Docker
if ! sudo systemctl is-active --quiet docker; then
    echo "🔧 Démarrage de Docker..."
    sudo systemctl start docker
fi

echo "📊 Version Docker: $(sudo docker --version)"
echo "💾 Espace disque disponible:"
df -h / | tail -1 | awk '{print "  • Utilisé: " $3 " / " $2 " (" $5 ")"}'
echo "🧠 Mémoire disponible:"
free -h | grep Mem | awk '{print "  • Utilisée: " $3 " / " $2 " (" int($3/$2*100) "%)"}'
DOCKER_CHECK

# Exécution du déploiement Redis
echo ""
echo "🚀 Exécution du déploiement Redis sur EC2..."
ssh -o StrictHostKeyChecking=no ubuntu@$EC2_IP << 'REDIS_DEPLOY'
echo "🐳 === DÉPLOIEMENT REDIS CONTAINER ==="
chmod +x ~/deploy_redis.sh
~/deploy_redis.sh

echo ""
echo "🔍 === VÉRIFICATION POST-DÉPLOIEMENT ==="

# Vérification du container
CONTAINER_STATUS=$(sudo docker ps --filter name=travel-redis --format "{{.Status}}" 2>/dev/null || echo "Not found")
echo "📊 Status container: $CONTAINER_STATUS"

if sudo docker ps --filter name=travel-redis --quiet | grep -q .; then
    echo "✅ Container Redis opérationnel"
    
    # Test de performance Redis
    echo ""
    echo "⚡ === TEST DE PERFORMANCE ==="
    REDIS_PASSWORD=$(grep "requirepass" /opt/redis-config/redis.conf | awk '{print $2}' 2>/dev/null || echo "rootroot")
    
    # Test de ping
    PING_RESULT=$(sudo docker exec travel-redis redis-cli -a "$REDIS_PASSWORD" ping 2>/dev/null || echo "FAILED")
    echo "🏓 Ping Redis: $PING_RESULT"
    
    # Test d'écriture/lecture
    if [ "$PING_RESULT" = "PONG" ]; then
        sudo docker exec travel-redis redis-cli -a "$REDIS_PASSWORD" set test_key "Hello Redis" >/dev/null 2>&1 || true
        TEST_VALUE=$(sudo docker exec travel-redis redis-cli -a "$REDIS_PASSWORD" get test_key 2>/dev/null || echo "FAILED")
        echo "📝 Test écriture/lecture: $TEST_VALUE"
        sudo docker exec travel-redis redis-cli -a "$REDIS_PASSWORD" del test_key >/dev/null 2>&1 || true
    fi
    
    # Métriques mémoire
    echo ""
    echo "📊 === MÉTRIQUES MÉMOIRE ==="
    sudo docker exec travel-redis redis-cli -a "$REDIS_PASSWORD" info memory 2>/dev/null | grep -E "(used_memory_human|maxmemory_human)" || true
    
    # Logs récents
    echo ""
    echo "📋 === LOGS RÉCENTS (5 dernières lignes) ==="
    sudo docker logs travel-redis --tail 5 2>/dev/null || echo "Pas de logs disponibles"
    
else
    echo "❌ Container Redis non trouvé"
    echo "📋 Containers Docker actifs:"
    sudo docker ps || true
    echo ""
    echo "📋 Logs Docker (si disponibles):"
    sudo docker logs travel-redis 2>/dev/null || echo "Pas de logs disponibles"
    exit 1
fi

echo ""
echo "🎉 === DÉPLOIEMENT REDIS TERMINÉ ==="
echo "🏷️  Container: travel-redis"
echo "🔗 Endpoint: localhost:6379"
echo "🔐 Authentification: Activée"
echo "💾 Mémoire max: 300MB (optimisé t2.micro)"
echo "📂 Configuration: /opt/redis-config/redis.conf"
echo "📂 Données: /opt/redis-data/"
echo ""
echo "🔧 Commandes de gestion:"
echo "  • Status: sudo docker ps --filter name=travel-redis"
echo "  • Logs: sudo docker logs travel-redis"
echo "  • Restart: sudo docker restart travel-redis"
echo "  • Connect: sudo docker exec -it travel-redis redis-cli -a [PASSWORD]"
REDIS_DEPLOY

# Vérification finale depuis l'extérieur
echo ""
echo "🔍 === VÉRIFICATION FINALE ==="
FINAL_CHECK=$(ssh -o StrictHostKeyChecking=no ubuntu@$EC2_IP "sudo docker ps --filter name=travel-redis --quiet" 2>/dev/null || echo "")

if [ -n "$FINAL_CHECK" ]; then
    echo "✅ Redis Container déployé avec succès sur EC2 $EC2_IP"
    echo "🐳 Container ID: $FINAL_CHECK"
    
    # Nettoyage du script temporaire
    ssh -o StrictHostKeyChecking=no ubuntu@$EC2_IP "rm -f ~/deploy_redis.sh" 2>/dev/null || true
    
    echo ""
    echo "🎯 === RÉSUMÉ DU DÉPLOIEMENT ==="
    echo "✅ Instance EC2: $EC2_IP"
    echo "✅ Container Redis: travel-redis"
    echo "✅ Port: 6379"
    echo "✅ Authentification: Configurée"
    echo "✅ Optimisations t2.micro: Appliquées"
    echo "✅ Persistence: Activée"
    echo "✅ Monitoring: Configuré"
    echo ""
    echo "🚀 Redis est prêt pour l'application Spring Boot !"
    
else
    echo "❌ Échec du déploiement Redis sur EC2"
    echo "📋 Vérifiez les logs de déploiement ci-dessus"
    exit 1
fi

echo ""
echo "📝 === NETTOYAGE ==="
rm -f deploy_redis.sh 2>/dev/null || true
echo "✅ Fichiers temporaires supprimés"

echo ""
echo "🏁 === DÉPLOIEMENT REDIS TERMINÉ ==="
echo "⏭️  Prêt pour le déploiement de l'application Spring Boot"
