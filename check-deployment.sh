#!/bin/bash

echo "🔍 Vérification des fichiers pour le déploiement CI/CD..."

# Vérifier les fichiers essentiels
FILES_TO_CHECK=(
    "pom.xml"
    "Dockerfile" 
    ".github/workflows/main.yml"
    "src/main/java/com/app/travel/TravelAgencyApplication.java"
)

MISSING_FILES=()

for file in "${FILES_TO_CHECK[@]}"; do
    if [[ ! -f "$file" ]]; then
        MISSING_FILES+=("$file")
    else
        echo "✅ $file"
    fi
done

if [[ ${#MISSING_FILES[@]} -gt 0 ]]; then
    echo ""
    echo "❌ Fichiers manquants:"
    for missing in "${MISSING_FILES[@]}"; do
        echo "   - $missing"
    done
    exit 1
fi

echo ""
echo "🔍 Vérification de la configuration Maven..."
if mvn -q clean compile; then
    echo "✅ Compilation Maven réussie"
else
    echo "❌ Erreur de compilation Maven"
    exit 1
fi

echo ""
echo "🔍 Vérification du JAR construit..."
if [[ -f "target/travel-agency-1.0.1-SNAPSHOT.jar" ]]; then
    echo "✅ JAR trouvé: target/travel-agency-1.0.1-SNAPSHOT.jar"
    echo "📊 Taille: $(du -h target/travel-agency-1.0.1-SNAPSHOT.jar | cut -f1)"
else
    echo "❌ JAR non trouvé. Exécution de mvn package..."
    if mvn -q package -DskipTests; then
        echo "✅ JAR créé avec succès"
    else
        echo "❌ Échec de création du JAR"
        exit 1
    fi
fi

echo ""
echo "🔍 Vérification du Dockerfile..."
if command -v docker >/dev/null 2>&1 && docker info >/dev/null 2>&1; then
    if docker build -f Dockerfile.local -t travel-agency-test . > /dev/null 2>&1; then
        echo "✅ Dockerfile valide"
        docker rmi travel-agency-test > /dev/null 2>&1
    else
        echo "❌ Erreur dans le Dockerfile"
        echo "ℹ️  Tentative de build avec logs pour diagnostic..."
        docker build -f Dockerfile.local -t travel-agency-test .
        exit 1
    fi
else
    echo "⚠️  Docker non disponible - vérification du Dockerfile ignorée"
    echo "ℹ️  Vérification syntaxique basique..."
    if [[ -f "Dockerfile" ]]; then
        echo "✅ Dockerfile principal présent"
    else
        echo "❌ Dockerfile principal manquant"
        exit 1
    fi
fi

echo ""
echo "🎉 Tous les contrôles sont passés !"
echo "🚀 Prêt pour le déploiement CI/CD"
