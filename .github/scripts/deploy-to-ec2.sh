#!/bin/bash

# Script pour effectuer le déploiement sur EC2
# Usage: ./deploy-to-ec2.sh <EC2_IP>

EC2_IP="$1"

if [[ -z "$EC2_IP" ]]; then
    echo "❌ Erreur: IP EC2 non fournie"
    echo "Usage: $0 <EC2_IP>"
    exit 1
fi

echo "🌐 IP de l'instance EC2: $EC2_IP"
echo "🔑 Déploiement via SSH..."

# Vérifier que le script de déploiement existe
if [[ ! -f "deploy_embedded.sh" ]]; then
    echo "❌ Erreur: Script deploy_embedded.sh non trouvé"
    exit 1
fi

# Exécuter via SSH
echo "📤 Copie du script de déploiement..."
scp -i ~/.ssh/id_rsa -o StrictHostKeyChecking=no deploy_embedded.sh ubuntu@$EC2_IP:/tmp/

echo "🚀 Exécution du déploiement..."
ssh -i ~/.ssh/id_rsa -o StrictHostKeyChecking=no ubuntu@$EC2_IP "bash /tmp/deploy_embedded.sh"

echo "🎉 Déploiement terminé avec succès !"
