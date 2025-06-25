# Start from official OpenJDK image
FROM maven:openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy JAR file (upewnij się, że to ścieżka do twojego pliku JAR)
COPY target/*.jar app.jar

# Expose the port that Spring Boot uses
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]