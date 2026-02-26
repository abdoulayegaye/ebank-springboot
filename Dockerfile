# ── Stage 1 : Build ──────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /ebank-app

# Copie pom.xml en premier pour profiter du cache Docker
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copie le source et build
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2 : Run ────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /ebank-app

# Utilisateur non-root pour la sécurité
RUN addgroup -S ebank && adduser -S ebank -G ebank
USER ebank

# Copie uniquement le jar depuis le stage builder
COPY --from=builder /ebank-app/target/ebank-0.0.1-SNAPSHOT.jar ebank-app.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "app.jar"]