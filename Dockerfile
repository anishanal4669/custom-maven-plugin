# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file into the container
# Replace 'your-app.jar' with the actual name of your JAR file
COPY target/your-app.jar /app/your-app.jar

# Expose the port on which your Spring Boot app will run
# Replace 8080 with your actual port if different
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/your-app.jar"]
