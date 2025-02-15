# Stage 1: Build the application using the Gradle image with Alpine
FROM gradle:8.4.0-jdk21-alpine AS build

# Install protoc and other necessary dependencies
RUN apk add --no-cache protobuf


WORKDIR /app

# Copy the Gradle files (including the Kotlin build script)
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .
COPY gradle gradle

# Copy the application source code
COPY src src

# Build the application
RUN ./gradlew build

# Stage 2: Run the application using a lightweight Alpine image with OpenJDK 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/charitan-api-gateway-0.0.1-SNAPSHOT.jar .

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "charitan-api-gateway-0.0.1-SNAPSHOT.jar"]
