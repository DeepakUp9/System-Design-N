# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-focal AS builder
WORKDIR /app

# Copy the Maven build files
COPY pom.xml .
COPY src /app/src

# Build the JAR (skips tests for faster build; run tests in CI/CD pipeline)
RUN ./mvnw clean install -DskipTests

# Stage 2: Create the final, lightweight runtime image
FROM eclipse-temurin:17-jre-focal AS runner
WORKDIR /app

# Expose the application port
EXPOSE 8080

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Define the environment variables for DB connection (runtime config)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/rms_db
ENV SPRING_DATASOURCE_USERNAME=rms_user
ENV SPRING_DATASOURCE_PASSWORD=rms_password

# Entrypoint to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]