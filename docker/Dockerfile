FROM eclipse-temurin:21-jdk-alpine
WORKDIR app
COPY broker-0.0.1-SNAPSHOT.jar service.jar

CMD java -jar service.jar --server.port=80