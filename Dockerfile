# Build 階段
FROM maven:3.9.3-eclipse-temurin-17 AS build
LABEL authors="silvia chang"
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run 階段
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/BlogWebsiteServer-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
