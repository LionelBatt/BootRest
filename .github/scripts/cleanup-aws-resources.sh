#!/bin/bash

# Script pour nettoyer les ressources AWS temporaires 
# Usage: ./cleanup-aws-resources.sh <S3_BUCKET_TEMP>

S3_BUCKET_TEMP="$1"

if [[ -z "$S3_BUCKET_TEMP" ]]; then
    echo "Aucun bucket temporaire à nettoyer"
    exit 0
fi

echo "🧹 Suppression du bucket temporaire: $S3_BUCKET_TEMP"

# Supprimer le contenu du bucket temporaire S3
aws s3 rm s3://$S3_BUCKET_TEMP --recursive || {
    echo "⚠️ Erreur lors de la suppression du contenu du bucket temporaire S3"
}

# Supprimer le bucket temporaire S3
aws s3 rb s3://$S3_BUCKET_TEMP || {
    echo "⚠️ Erreur lors de la suppression du bucket temporaire S3"
}

echo "✅ Bucket temporaire supprimé"
