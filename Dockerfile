FROM openjdk:17-jdk-slim

# Copy the Maven build output to the container
COPY target/urlShortener-0.0.1-SNAPSHOT.jar urlShortener-0.0.1-SNAPSHOT.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "urlShortener-0.0.1-SNAPSHOT.jar"]
