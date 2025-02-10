# Use an official Maven image to build the application
FROM maven:3.8.6-jdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml /app
COPY src /app/src

# Build the application
RUN mvn clean install

# Run the application using OpenJDK 17
FROM openjdk:17-jdk

WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/Matrimony_Backend-0.0.1-SNAPSHOT.jar /app/Matrimony_Backend.jar

# Expose the port that the app will run on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "Matrimony_Backend.jar"]
