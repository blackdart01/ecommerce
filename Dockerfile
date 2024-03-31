# Build stage
#
FROM maven:3.8.6-jdk-17
# Updated to match your Java version

# Adjust the version if needed, but using a base image with Java 17 avoids needing to configure the compiler plugin

COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:17-slim
# Adjusted to match your Java version

COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar
# ENV PORT=8080  # Uncomment if you need to set an environment variable for the port
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]