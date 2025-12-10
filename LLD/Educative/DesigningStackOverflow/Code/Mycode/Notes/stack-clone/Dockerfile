# Stage 1: Build the application artifact (Build Stage)
FROM eclipse-temurin:17-jdk-focal AS build
# Set working directory inside the container
WORKDIR /app
# Copy the Maven project files
COPY pom.xml .
COPY src ./src
# Build the application, creating a JAR file
RUN --mount=type=cache,target=/root/.m2 ./mvnw clean install -DskipTests
# If using a separate mvnw file, ensure it's copied and executable
# RUN chmod +x mvnw

# Stage 2: Create the final, lightweight runtime image (Runtime Stage)
FROM eclipse-temurin:17-jre-focal AS final
# Set argument for the JAR file path
ARG JAR_FILE=/app/target/stack-clone-0.0.1-SNAPSHOT.jar
# Set argument for the user to run the process (Security/Resilience)
ARG USER=springuser
# Create a dedicated, non-root user for security
RUN groupadd ${USER} && useradd -m -g ${USER} ${USER}

# Copy the application JAR from the build stage
COPY --from=build ${JAR_FILE} /app/app.jar
# Copy the Flyway migration scripts
COPY --from=build /app/src/main/resources/db/migration /app/db/migration
# Copy the application configuration
COPY --from=build /app/src/main/resources/application.yml /app/application.yml

# Change ownership of the application file to the non-root user
RUN chown -R ${USER}:${USER} /app

# Switch to the non-root user (Security)
USER ${USER}

# Set the entrypoint to run the Spring Boot application
# The Spring Profile is set to ensure production configs/environment variables are used
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]