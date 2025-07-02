# Use a lightweight OpenJDK image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your target directory into the container
COPY target/erp-core.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Define the startup command
ENTRYPOINT ["java", "-jar", "app.jar"]
