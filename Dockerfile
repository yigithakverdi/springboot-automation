FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY src/target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/demo.jar"]