# Multi-stage Docker build for ESPN Cricinfo System
FROM eclipse-temurin:21-jdk-alpine AS builder

# Install Maven and security updates
RUN apk update && \
    apk upgrade && \
    apk add --no-cache maven

WORKDIR /app

# Copy Maven files for dependency caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies (cached layer for faster builds)
RUN ./mvnw dependency:go-offline -B --no-transfer-progress

# Copy source code
COPY src ./src

# Build application with production profile
RUN ./mvnw clean package -DskipTests -Dmaven.test.skip=true -Pproduction --batch-mode --no-transfer-progress

# Runtime stage - Using Distroless for better security
FROM gcr.io/distroless/java21-debian12

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/espn-cricinfo-application/target/*.jar app.jar

# Create log directory (if needed)
# Note: Distroless has limited shell, so logging to stdout/stderr is preferred

# Expose port
EXPOSE 8080

# Health check using Spring Boot actuator
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD ["/usr/bin/java", "-cp", "/app/app.jar", "org.springframework.boot.loader.JarLauncher", \
         "--spring.profiles.active=health-check"] || exit 1

# JVM optimizations for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -XX:+UseCompressedOops \
               -XX:+OptimizeStringConcat \
               -XX:+UseStringDeduplication \
               -Djava.security.egd=file:/dev/./urandom \
               -Dspring.profiles.active=docker \
               -Dlogging.file.path=/tmp/logs"

# Run application with optimized JVM settings
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
