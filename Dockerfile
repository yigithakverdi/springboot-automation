FROM openjdk:17-jdk-slim
WORKDIR /app
COPY src/src/main/resources/keystore.p12 /app/keystore.p12
COPY src/target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar
ENTRYPOINT ["java","-jar","/app/demo.jar"]
EXPOSE 8443
