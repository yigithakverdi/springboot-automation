#FROM openjdk:17-jdk-alpine
#WORKDIR /app
#COPY src/target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar
#COPY src/src/main/resources/keystore.p12 /app/keystore.p12
#RUN chmod 644 /app/keystore.p12
#EXPOSE 8443
#ENTRYPOINT ["java", "-jar", "/app/demo.jar"]

# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the keystore file into the container at the desired location
COPY src/src/main/resources/keystore.p12 /app/keystore.p12

# Copy the application JAR file into the container
COPY src/target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java","-jar","/app/demo.jar"]

# Expose the port that the application will run on
EXPOSE 8443
