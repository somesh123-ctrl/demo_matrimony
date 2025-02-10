# Use an official Maven image to build the application
FROM maven:3.8.6-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and dependencies to the container
COPY pom.xml .

# Download the dependencies
RUN mvn dependency:go-offline

# Copy the rest of the application source code
COPY src /app/src

# Build the application
RUN mvn clean install

# Use an official OpenJDK runtime as the base image for the final container
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build container
COPY --from=build /app/target/Matrimony_Backend-0.0.1-SNAPSHOT.jar /app/Matrimony_Backend.jar

# Run the JAR file
CMD ["java", "-jar", "/app/Matrimony_Backend.jar"]

# Expose the port the app will run on
EXPOSE 8080

