# --- STAGE 1: Build the Application ---
FROM eclipse-temurin:17-jdk-focal AS builder
WORKDIR /app

# Copy the entire project structure
COPY . /app

# Package the multi-module project
# -DskipTests is CRITICAL for fast builds, tests should run in a separate pipeline stage.
RUN ./mvnw clean install -DskipTests

# Copy the executable JAR from the game-service module build directory
RUN cp game-service/target/*.jar app.jar


# --- STAGE 2: Create the Final Production Image ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Set timezone for consistent logging/auditing (production standard)
ENV TZ=UTC

# Expose the application port
EXPOSE 8080

# Copy the built JAR from the builder stage
COPY --from=builder /app/app.jar .

# Entrypoint to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Production Note: For security, consider running the container as a non-root user.
# USER 1000