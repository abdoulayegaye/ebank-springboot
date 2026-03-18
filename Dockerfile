# ═══════════════════════════════════════════════════════════════
# Stage 1 — Dependency Cache
# Télécharge les dépendances Maven séparément pour profiter
# du cache Docker (rebuild rapide si seul le code change)
#   docker compose build app
#   docker images | grep ebank
#   docker compose up -d app
#   docker compose logs -f app
#   docker login
#   docker push layegaye/ebank:1.0.0
# ═══════════════════════════════════════════════════════════════
FROM maven:3.9-amazoncorretto-21-alpine AS dependencies
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress


# ═══════════════════════════════════════════════════════════════
# Stage 2 — Build
# Compile et package le JAR depuis les sources
# ═══════════════════════════════════════════════════════════════
FROM dependencies AS builder
COPY src/ ./src/
RUN mvn clean package -DskipTests -B --no-transfer-progress


# ═══════════════════════════════════════════════════════════════
# Stage 3 — Runtime
# Image finale légère, sans Maven ni sources
# ═══════════════════════════════════════════════════════════════
FROM amazoncorretto:21-alpine AS runtime

LABEL maintainer="banking-app"
LABEL description="Banking Application - Spring Boot"
LABEL version="1.0.0"

# ── Sécurité : utilisateur non-root ──────────────────────────
RUN addgroup -S bankinggroup && adduser -S bankinguser -G bankinggroup

WORKDIR /app

# ── JAR depuis le stage builder ───────────────────────────────
COPY --from=builder /app/target/*.jar app.jar

# ── Variables d'environnement (surchargeables via docker-compose)
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENV SPRING_PROFILES_ACTIVE=docker

# ── Port exposé ───────────────────────────────────────────────
EXPOSE 8080

# ── Healthcheck via Spring Actuator ───────────────────────────
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# ── Lancement ─────────────────────────────────────────────────
USER bankinguser
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]