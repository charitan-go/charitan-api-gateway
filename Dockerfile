# Stage 1: Build the application using the Gradle image with Alpine
#FROM gradle:8.4.0-jdk21-alpine AS build
#
## Install protoc and other necessary dependencies
#RUN apk add --no-cache protobuf

# Stage 1: Build the application using a Debian-based Gradle image with JDK 21
FROM gradle:8.4.0-jdk21 AS build
WORKDIR /app

# Install protoc via apt-get
RUN apt-get update && apt-get install -y protobuf-compiler


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

# Stage 2: Use JDK 21 with glibc 
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/build/libs/charitan-api-gateway-0.0.1-SNAPSHOT.jar .

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "charitan-api-gateway-0.0.1-SNAPSHOT.jar"]