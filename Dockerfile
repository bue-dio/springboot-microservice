# Step 1: Use Maven image to build the application (WAR)
FROM maven:3.9.5-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn -e -DskipTests clean package

# Step 2: Use lightweight JDK for running the Spring Boot WAR
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.war app.war
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]
