# Multi-stage build for optimized container image
# Stage 1: Build
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Copy source code
COPY src src

# Build the application (skip tests and plugins resolution to avoid repository conflicts)
# Only include core Maven Central repositories for plugin resolution
RUN ./mvnw clean package -DskipTests \
    -Dmaven.test.skip=true \
    -Dspotless.check.skip=true

# Stage 2: Runtime
FROM eclipse-temurin:25-jre-alpine

# Install curl for health checks
RUN apk add --no-cache curl

WORKDIR /app

# Copy JAR from builder
COPY --from=builder /app/target/*.jar app.jar

# Create non-root user for security
RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser && \
    chown -R appuser:appuser /app

USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Expose port
EXPOSE 8080

# Run application
# Use /bin/sh to properly expand environment variables
ENTRYPOINT ["/bin/sh", "-c", "java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-production} -jar app.jar"]
