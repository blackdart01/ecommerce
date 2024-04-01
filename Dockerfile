# Build stage
#
FROM maven:3.6.3-jdk-17
# Updated to match your Java version

# Adjust the version if needed, but using a base image with Java 17 avoids needing to configure the compiler plugin

COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:17-slim
# Adjusted to match your Java version

COPY target/ecommerce-backend-0.0.1-SNAPSHOT.jar application.jar
# ENV PORT=8080  # Uncomment if you need to set an environment variable for the port
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]

#FROM eclipse-temurin:17-jre-focal as builderbuildnumber
#COPY target/ecommerce-backend-0.0.1-SNAPSHOT.jar application.jar
#RUN java -Djarmode=layertools -jar application.jar extract