# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the Maven build file and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
CMD ["sh", "-c", "java -jar target/*.jar"]
