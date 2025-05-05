# Stage 1: Build the Spring Boot application using Maven
FROM maven:3.8.6-openjdk-8 AS build
WORKDIR /app
# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Copy the entire source code and build the package (skip tests for faster build)
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create a minimal runtime image using OpenJDK 8 Alpine
FROM openjdk:8-jdk-alpine
WORKDIR /app
# Copy the built jar file from the build stage. Adjust the path if your jar name is different.
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]