# Base image with OpenJDK
FROM openjdk:17-jdk

# Set working directory (optional, but improves clarity)
WORKDIR /app

# Copy application code
COPY . .

# Install dependencies using Maven
RUN mvn clean install

# Copy the JAR file to a designated location (optional, but avoids building on every start)
COPY target/*.jar app.jar

# Expose the port your Spring Boot application listens on (typically 8080)
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]