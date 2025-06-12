# Image légère pour déploiement EC2
FROM openjdk:21-jdk-slim

# Créer un utilisateur non-root pour la sécurité
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Répertoire de travail
WORKDIR /app

# Créer les répertoires pour les volumes
RUN mkdir -p /app/logs /app/data && \
    chown -R appuser:appuser /app

# Copier le JAR précompilé (téléchargé depuis S3)
COPY travel-agency-*.jar app.jar

# Changer le propriétaire du fichier
RUN chown appuser:appuser app.jar

# Utiliser l'utilisateur non-root
USER appuser

# Port exposé
EXPOSE 8080

# Volumes pour la persistance
VOLUME ["/app/logs", "/app/data"]

# Variables d'environnement par défaut
ENV SERVER_PORT=8080 \
    LOGGING_LEVEL_ROOT=INFO \
    LOGGING_FILE_PATH=/app/logs/application.log \
    JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# Point d'entrée avec options JVM
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
