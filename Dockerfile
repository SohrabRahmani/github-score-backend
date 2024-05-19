# Stage 1: Build Stage
FROM maven:3.9.4-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Production Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/githubscore-0.0.1-SNAPSHOT.jar ./

# Environment Variables
ENV DB_URL=postgres \
    DB_NAME=githubscore_db \
    DB_USERNAME=githubscore_user \
    DB_PASSWORD=githubscore_password \
    SPRING_JPA_HIBERNATE_DDL_AUTO=update \
    SPRING_JPA_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect

# Container Startup Command
CMD ["java", "--enable-preview", "-jar", "githubscore-0.0.1-SNAPSHOT.jar"]
