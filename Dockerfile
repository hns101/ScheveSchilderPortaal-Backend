# Stage 1: Build the Spring Boot application
# We use a multi-stage build to keep the final image small.
# This stage uses a full JDK for compilation and packaging.
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build # <-- CHANGED THIS LINE!

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files (pom.xml first to leverage Docker cache for dependencies)
COPY pom.xml .

# Copy the entire source code
COPY src ./src

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# Stage 2: Create the final, smaller runtime image
# We use a slim OpenJDK JRE image for the final application,
# which is much smaller than the full JDK image used for building.
FROM openjdk:21-jre-slim-buster

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the 'build' stage
COPY --from=build /app/target/scheveschilderportaal-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot application listens on.
EXPOSE 8080

# Define the command to run your Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
# Optional: Define metadata for the image
LABEL maintainer="De Scheve Schilder <infol@scheveschilder.nl>"
LABEL version="0.0.1-SNAPSHOT"
LABEL description="Backend for Scheveschilder Portaal"