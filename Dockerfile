# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Resolve dependencies first so this layer is cached until pom.xml changes
COPY pom.xml ./
RUN mvn -B -ntp dependency:go-offline

COPY src ./src
RUN mvn -B -ntp clean package -DskipTests


FROM eclipse-temurin:17-jre-jammy AS runtime
WORKDIR /app

RUN groupadd --system --gid 1001 spring \
    && useradd --system --uid 1001 --gid spring --create-home spring

COPY --from=build --chown=spring:spring /build/target/*.jar /app/app.jar

USER spring:spring

EXPOSE 1005

# Port comes from the PORT env var (Render injects it); application.properties
# falls back to 1005 for local runs. Do not set SERVER_PORT here.
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
