FROM openjdk:21-jdk-slim

# Installer des outils de diagnostic
RUN apt-get update && apt-get install -y curl procps && rm -rf /var/lib/apt/lists/*

# Créer l'utilisateur avec UID/GID spécifiques
RUN groupadd -g 1000 appuser && useradd -u 1000 -g appuser -m appuser

WORKDIR /app
RUN mkdir -p /app/logs /app/data && chown -R appuser:appuser /app

# Copie du JAR précompilé
COPY *.jar app.jar
RUN chown appuser:appuser app.jar

USER appuser

EXPOSE 8080
VOLUME ["/app/logs", "/app/data"]