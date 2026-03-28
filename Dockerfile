# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set the current working directory inside the container
WORKDIR /app

# Copy the pom.xml and download project dependencies
# This is a separate step so Docker can cache dependencies easily
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the actual project source code
COPY src ./src

# Build the application, skipping tests to speed up the container build (Tests run in Jenkins)
RUN mvn clean package -DskipTests

# Stage 2: Create a minimal runtime environment
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the executable JAR from the builder stage
COPY --from=builder /app/target/food-delivery-app-0.0.1-SNAPSHOT.jar app.jar

# Explicitly expose port 8081 as used in application.properties
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
